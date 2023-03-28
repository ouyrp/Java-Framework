package com.wusong.monitoring.metric.micrometer.binder.db;

import com.mysql.cj.jdbc.ConnectionImpl;
import com.wusong.monitoring.metric.micrometer.binder.db.p6spy.RecodeLoggingEventListener;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author p14
 */
public class MySQLConnectionID implements DBConnectionID{

    static {
        try{
            Class clazz= RecodeLoggingEventListener.class.getClassLoader().loadClass("com.mysql.cj.jdbc.ConnectionImpl");
            if(clazz!=null){
               RecodeLoggingEventListener.setDbConnectionID(new MySQLConnectionID());
            }
        }catch (Throwable ignore){
        }
    }
    @Override
    public String getId(Connection connection) {
        if(connection instanceof  ConnectionImpl){
            return ""+((ConnectionImpl) connection).getId();
        }
        try {
            return ""+connection.unwrap(ConnectionImpl.class).getId();
        } catch (SQLException ignore) {
        }
        return "";
    }
}
