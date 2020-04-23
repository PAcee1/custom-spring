package com.enbuys.utils;

import java.sql.SQLException;

/**
 * @Created by pace
 * @Date 2020/4/23 17:32
 * @Classname TransactionManager
 */
public class TransactionManager {

    private ConnectionUtils connectionUtils;

    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    /*private TransactionManager(){}

    private static TransactionManager transactionManager = new TransactionManager();

    public static TransactionManager getInstance(){
        return transactionManager;
    }*/

    // 设置手工提交事务
    public void begin() throws SQLException {
        connectionUtils.getConnection().setAutoCommit(false);
    }

    // 提交事务
    public void commit() throws SQLException {
        connectionUtils.getConnection().commit();
    }

    // 回滚事务
    public void rollback() throws SQLException {
        connectionUtils.getConnection().rollback();
    }
}
