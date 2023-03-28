package com.wusong.monitoring.metric.micrometer.binder.db.p6spy;

import com.p6spy.engine.common.ConnectionInformation;
import com.p6spy.engine.common.Loggable;
import com.p6spy.engine.common.StatementInformation;
import com.p6spy.engine.event.SimpleJdbcEventListener;
import com.p6spy.engine.logging.Category;
import com.wusong.monitoring.metric.MicrometerUtil;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class MetricJdbcEventListener extends SimpleJdbcEventListener {
    public static class SqlParser {
        /**
         * sql的常见类型
         **/
        public static final String[] sqlTypeList = {"select","update","delete","insert","commit"};

        public static final String SPLIT_CHAR=";";

        public static String getSqlType(Category category, String sql) {
            if (StringUtils.isEmpty(sql)) {
                return category.getName();
            }
            try {
                String sqlType = getSqlString(sql);
                for (String str : sqlTypeList) {
                    if (sqlType.toLowerCase().equals(str)) {
                        return str;
                    }
                }
                return Category.STATEMENT.getName();
            } catch (Exception i) {
                logger.debug("Error parsing SQL {}", sql,i);
                return category.getName();
            }
        }

    public static String getSqlString(String sql) {
        String sqlString = "";
        int len = sql.length();
        for(int i = 0; i < len; ++i) {
            CharSequence cs = sql;
            char ca = cs.charAt(i);
            if (Character.isLowerCase(ca) || Character.isUpperCase(ca)) {
                sqlString = sqlString + cs.charAt(i);
            } else {
                if (sqlString.length() > 0) {
                    return sqlString;
                }
            }
        }
        return sqlString;
    }
}
    public static final Logger logger = LoggerFactory.getLogger(MetricJdbcEventListener.class);
    public static final String SQL_EXECUTE_TIME = "sql_execute_time";

    public static final MetricJdbcEventListener INSTANCE = new MetricJdbcEventListener();

    private static String getHost(String url) {
        if (StringUtils.isEmpty(url)) {
            return "none";
        }
        try {
            return url.split("\\?")[0];
        } catch (Throwable ex) {
            return url;
        }
    }

    private static String getOrDefault(Callable<String> call, String defaultValue) {
        try {
            return call.call();
        } catch (Exception e) {
            return defaultValue;
        }
    }


    public MetricJdbcEventListener() {
    }

    @Override
    public void onAfterAnyAddBatch(StatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
        record(statementInformation, timeElapsedNanos, Category.BATCH, e);
    }

    @Override
    public void onAfterAnyExecute(StatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
        record(statementInformation, timeElapsedNanos, Category.STATEMENT, e);
    }

    @Override
    public void onAfterCommit(ConnectionInformation connectionInformation, long timeElapsedNanos, SQLException e) {
        record(connectionInformation, timeElapsedNanos, Category.COMMIT, e);
    }

    @Override
    public void onAfterExecuteBatch(StatementInformation statementInformation, long timeElapsedNanos,
        int[] updateCounts, SQLException e) {
        record(statementInformation, timeElapsedNanos, Category.BATCH, e);
    }

    @Override
    public void onAfterGetResultSet(StatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
        record(statementInformation, timeElapsedNanos, Category.RESULTSET, e);
    }

    @Override
    public void onAfterRollback(ConnectionInformation connectionInformation, long timeElapsedNanos, SQLException e) {
        record(connectionInformation, timeElapsedNanos, Category.ROLLBACK, e);
    }

    protected void record(Loggable loggable, long timeElapsedNanos, Category category, SQLException e) {
        try {
            Timer timer =
                Metrics
                    .timer(
                        SQL_EXECUTE_TIME, Tags
                            .of(Tag.of("category", category.getName()), Tag.of("readonly", getOrDefault(
                                () -> String.valueOf(loggable.getConnectionInformation().getConnection().isReadOnly()),
                                "error")),
                                Tag.of("autocommit",
                                    getOrDefault(
                                        () -> String.valueOf(
                                            loggable.getConnectionInformation().getConnection().getAutoCommit()),
                                        "error")),
                                Tag.of("statement", SqlParser.getSqlType(category, loggable.getSqlWithValues())),
                                Tag.of("jdbc",
                                    getHost(
                                        loggable.getConnectionInformation().getConnection().getMetaData().getURL())))
                            .and(MicrometerUtil.exceptionAndStatusKey(e)));
            timer.record(timeElapsedNanos, TimeUnit.NANOSECONDS);
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
