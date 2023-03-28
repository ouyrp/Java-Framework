package com.wusong.monitoring.metric.micrometer.binder.db.p6spy;

import com.p6spy.engine.common.ConnectionInformation;
import com.p6spy.engine.common.Loggable;
import com.p6spy.engine.common.ResultSetInformation;
import com.p6spy.engine.common.StatementInformation;
import com.p6spy.engine.logging.Category;
import com.p6spy.engine.logging.LoggingEventListener;
import com.p6spy.engine.logging.P6LogLoadableOptions;
import com.p6spy.engine.logging.P6LogOptions;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.P6Logger;
import com.wusong.monitoring.metric.micrometer.binder.db.DBConnectionID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class RecodeLoggingEventListener extends LoggingEventListener {

    public static final RecodeLoggingEventListener INSTANCE = new RecodeLoggingEventListener();

    private static final Logger logger = LoggerFactory.getLogger(RecodeLoggingEventListener.class);
    private static SQLLogger sqlLogger;

    public static void setConnectionCreateThreshold(int connectionCreateThreshold) {
        CONNECTION_CREATE_THRESHOLD = connectionCreateThreshold;
    }

    private static int CONNECTION_CREATE_THRESHOLD=10;

    private static final Set<Category> CATEGORIES_IMPLICITLY_INCLUDED =
        new HashSet<Category>(Arrays.asList(Category.ERROR, Category.OUTAGE));

    protected static void doLog(int logicConnectionId,String underlyingConnection, long elapsedNanos, Category category, String prepared, String sql,
        String url, Loggable loggable) {

        final String format = P6SpyOptions.getActiveInstance().getDateformat();
        final String stringNow;
        if (format == null) {
            stringNow = Long.toString(System.currentTimeMillis());
        } else {
            stringNow = new SimpleDateFormat(format).format(new java.util.Date()).trim();
        }

        sqlLogger.logSQL(logicConnectionId,underlyingConnection, stringNow, TimeUnit.NANOSECONDS.toMillis(elapsedNanos), category, prepared, sql,
            url, loggable);

        final boolean stackTrace = P6SpyOptions.getActiveInstance().getStackTrace();
        if (stackTrace) {
            final String stackTraceClass = P6SpyOptions.getActiveInstance().getStackTraceClass();
            Exception e = new Exception();
            if (stackTraceClass != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String stack = sw.toString();
                if (stack.indexOf(stackTraceClass) == -1) {
                    e = null;
                }
            }
            if (e != null) {
                sqlLogger.logException(e);
            }
        }
    }

    private static synchronized void initialize() {
        if (sqlLogger == null) {
            P6Logger instance = P6SpyOptions.getActiveInstance().getAppenderInstance();
            if (instance instanceof SQLLogger) {
                sqlLogger = (SQLLogger)instance;
            } else {
                sqlLogger = new SQLLogger();
            }
        }
    }

    static boolean isCategoryOk(Category category) {
        final P6LogLoadableOptions opts = P6LogOptions.getActiveInstance();
        if (null == opts) {
            return CATEGORIES_IMPLICITLY_INCLUDED.contains(category);
        }

        final Set<Category> excludeCategories = opts.getExcludeCategoriesSet();

        return sqlLogger != null && sqlLogger.isCategoryEnabled(category)
            && (excludeCategories == null || !excludeCategories.contains(category));
    }

    static boolean isLoggable(String sql) {
        if (null == sql) {
            return false;
        }

        final P6LogLoadableOptions opts = P6LogOptions.getActiveInstance();

        if (!opts.getFilter()) {
            return true;
        }

        final Pattern sqlExpressionPattern = opts.getSQLExpressionPattern();
        final Pattern includeExcludePattern = opts.getIncludeExcludePattern();

        return (sqlExpressionPattern == null
            || sqlExpressionPattern != null && sqlExpressionPattern.matcher(sql).matches())
            && (includeExcludePattern == null
                || includeExcludePattern != null && includeExcludePattern.matcher(sql).matches());
    }

    public static void logElapsed(int logicConnectionId,String underlyingConnection, long timeElapsedNanos, Category category, Loggable loggable) {

        if (sqlLogger == null) {
            initialize();
            if (sqlLogger == null) {
                return;
            }
        }

        final String sql;
        String url = loggable.getConnectionInformation().getUrl();
        if(Objects.nonNull(logger)){
            final boolean reachThreshold = meetsThresholdRequirement(timeElapsedNanos);
            if(reachThreshold) {
                sql = loggable.getSql();
                final boolean canLog = isLoggable(sql);
                final boolean categoryOk = isCategoryOk(category);
                if(categoryOk && canLog){
                    doLog(logicConnectionId,underlyingConnection, timeElapsedNanos, category, sql, loggable.getSqlWithValues(), url == null ? "" : url,
                            loggable);
                    return;
                }
            }else{
                sql = loggable.getSqlWithValues();
            }

            if(logger.isDebugEnabled()){
                logger.debug(
                        "P6Spy intentionally did not log category: {}, statement: {}  Reason: logger= {}, isLoggable={}, isCategoryOk={}, meetsTreshold={}",
                        category, sql, logger, isLoggable(sql), isCategoryOk(category), reachThreshold
                        );
            }
        }
        /*
        if (logger != null && meetsThresholdRequirement(timeElapsedNanos) && isCategoryOk(category)
            && isLoggable(sql = loggable.getSql())) {
            doLog(logicConnectionId,underlyingConnection, timeElapsedNanos, category, sql, loggable.getSqlWithValues(), url == null ? "" : url,
                loggable);
        } else if (logger.isDebugEnabled()) {
            sql = loggable.getSqlWithValues();
            logger.debug("P6Spy intentionally did not log category: " + category + ", statement: " + sql
                + "  Reason: logger=" + logger + ", isLoggable=" + isLoggable(sql) + ", isCategoryOk="
                + isCategoryOk(category) + ", meetsTreshold=" + meetsThresholdRequirement(timeElapsedNanos));
        }*/
    }

    /**
     * javadoc meetsThresholdRequirement
     * @apiNote 判断sql执行时间是否触达慢sql阈值
     *          注: 是否需要放开 <= 0 的判断
     *          即配置 <= 0 的阈值时不检测慢日志
     *
     * @param timeTaken sql执行消耗时长, 纳秒
     * @return boolean
     * @author weng xiaoyong
     * @date 2022/4/11 19:19
     **/
    private static boolean meetsThresholdRequirement(long timeTaken) {
        final P6LogLoadableOptions opts = P6LogOptions.getActiveInstance();
        long executionThreshold = null != opts ? opts.getExecutionThreshold() : 0;

        return executionThreshold <= 0 || TimeUnit.NANOSECONDS.toMillis(timeTaken) > executionThreshold;
    }

    public RecodeLoggingEventListener() {
    }

    @Override
    protected void logElapsed(Loggable loggable, long timeElapsedNanos, Category category, SQLException e) {
        logElapsed(loggable.getConnectionInformation().getConnectionId(),getUnderlyingConnectionId(loggable.getConnectionInformation()), timeElapsedNanos, category, loggable);
    }

    private static DBConnectionID dbConnectionID=(c)->"empty";

    public static DBConnectionID getDbConnectionID() {
        return dbConnectionID;
    }

    public static void setDbConnectionID(DBConnectionID dbConnectionID) {
        RecodeLoggingEventListener.dbConnectionID = dbConnectionID;
    }

    static {
        try{
            Class clazz=RecodeLoggingEventListener.class.getClassLoader().loadClass("com.wusong.monitoring.metric.micrometer.binder.db.MySQLConnectionID");
            if(clazz!=null){
                logger.info("using MySQLConnectionID");
                DBConnectionID o= (DBConnectionID) clazz.newInstance();
                setDbConnectionID(o);
            }
        }catch (Throwable ignore){
        }
    }

    /**
     * 获取 connection id 和 数据库服务器上的connection id 一一关联
     * @param connectionInformation
     * @return
     */
    private String getUnderlyingConnectionId(ConnectionInformation connectionInformation){
        if(dbConnectionID!=null){

        }
        if(connectionInformation!=null){
            if(connectionInformation.getConnection()!=null){
                try {
                    return dbConnectionID.getId(connectionInformation.getConnection().getMetaData().getConnection());
                } catch (Throwable ignored) {
                }
            }
            if(connectionInformation.getPooledConnection()!=null){
                try {
                    return dbConnectionID.getId(connectionInformation.getPooledConnection().getConnection().getMetaData().getConnection());
                } catch (Throwable ignored) {
                }
            }
        }
        return 0+"";
    }

    @Override
    public void onAfterAnyAddBatch(StatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
        logElapsed(statementInformation, timeElapsedNanos, Category.BATCH, e);
    }

    @Override
    public void onAfterAnyExecute(StatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
        logElapsed(statementInformation, timeElapsedNanos, Category.STATEMENT, e);
    }

    @Override
    public void onAfterCommit(ConnectionInformation connectionInformation, long timeElapsedNanos, SQLException e) {
        logElapsed(connectionInformation, timeElapsedNanos, Category.COMMIT, e);
    }

    @Override
    public void onAfterExecuteBatch(StatementInformation statementInformation, long timeElapsedNanos,
        int[] updateCounts, SQLException e) {
        logElapsed(statementInformation, timeElapsedNanos, Category.BATCH, e);
    }

    @Override
    public void onAfterGetResultSet(StatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
        logElapsed(statementInformation, timeElapsedNanos, Category.RESULTSET, e);
    }

    @Override
    public void onAfterResultSetClose(ResultSetInformation resultSetInformation, SQLException e) {
        if (resultSetInformation.getCurrRow() > -1) {

            resultSetInformation.generateLogMessage();
        }
    }

    @Override
    public void onAfterResultSetGet(ResultSetInformation resultSetInformation, int columnIndex, Object value,
        SQLException e) {
        resultSetInformation.setColumnValue(Integer.toString(columnIndex), value);
    }

    @Override
    public void onAfterResultSetGet(ResultSetInformation resultSetInformation, String columnLabel, Object value,
        SQLException e) {
        resultSetInformation.setColumnValue(columnLabel, value);
    }

    @Override
    public void onAfterResultSetNext(ResultSetInformation resultSetInformation, long timeElapsedNanos, boolean hasNext,
        SQLException e) {
        if (hasNext) {
            logElapsed(resultSetInformation, timeElapsedNanos, Category.RESULT, e);
        }
    }

    @Override
    public void onAfterRollback(ConnectionInformation connectionInformation, long timeElapsedNanos, SQLException e) {
        logElapsed(connectionInformation, timeElapsedNanos, Category.ROLLBACK, e);
    }

    @Override
    public void onBeforeResultSetNext(ResultSetInformation resultSetInformation) {
        if (resultSetInformation.getCurrRow() > -1) {

            resultSetInformation.generateLogMessage();
        }
    }

    public static final ThreadLocal<Long> BEFORE_GET_CONNECTION_TIME=new ThreadLocal<>();
    @Override public void onAfterGetConnection(ConnectionInformation connectionInformation, SQLException e) {
        if(e!=null){
            logger.warn("Error on GetConnection: {}",e.getLocalizedMessage());
            return;
        }
        long end=System.currentTimeMillis();
        Long start = BEFORE_GET_CONNECTION_TIME.get();
        if(start!=null){
            long cost=end-start;
            if(cost>CONNECTION_CREATE_THRESHOLD){
                logger.warn("GetConnection connection{} underlying_con_{} used {} ms",connectionInformation.getConnectionId(),getUnderlyingConnectionId(connectionInformation),cost);
            }}
        BEFORE_GET_CONNECTION_TIME.remove();
    }

    @Override public void onBeforeGetConnection(ConnectionInformation connectionInformation) {
        BEFORE_GET_CONNECTION_TIME.set(System.currentTimeMillis());
    }
}
