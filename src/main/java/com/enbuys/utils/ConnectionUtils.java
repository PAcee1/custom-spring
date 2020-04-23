package com.enbuys.utils;

import com.alibaba.druid.pool.DruidPooledConnection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Created by pace
 * @Date 2020/4/23 16:39
 * @Classname ConnectionUtils
 */
public class ConnectionUtils {

    // 使用ThreadLoacl保存连接
    private ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    /*private static ConnectionUtils connectionUtils = new ConnectionUtils();

    private ConnectionUtils(){}

    // 单例模式，提供获取方法
    public static ConnectionUtils getInstance(){
        return connectionUtils;
    }*/

    /**
     * 获取连接
     * @return
     */
    public Connection getConnection(){
        // 判断此线程是否为空，为空创建，不为空直接返回
        Connection connection = threadLocal.get();
        if(connection == null){
            try {
                connection = DruidUtils.getInstance().getConnection();
                threadLocal.set(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
