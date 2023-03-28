package com.wusong.monitoring.metric.micrometer.binder.db;

import java.sql.Connection;

/**
 * @author p14
 */
public interface DBConnectionID {
    /**
     * 获取 数据库连接 的物理 ID，改id和服务端一一对应
     * @param connection
     * @return
     */
    String getId(Connection connection);
}
