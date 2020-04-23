package com.enbuys.factory;

import com.enbuys.pojo.Account;
import com.enbuys.utils.TransactionManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author pace
 * @version v1.0
 * @Type ProxyFactory.java
 * @Desc
 * @date 2020/4/23 21:56
 */
public class ProxyFactory {

    private TransactionManager transactionManager;

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    // 使用JDK动态代理，实现AOP增强
    public Object getProxy(Object obj){
        Object o = Proxy.newProxyInstance(obj.getClass().getClassLoader(),
            obj.getClass().getInterfaces(),
            new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Object result = null;
                    try {
                        // 开启事物
                        transactionManager.begin();
                        System.out.println("开启事务");

                        // 执行方法
                        result = method.invoke(obj, args);

                        // 提交事务
                        transactionManager.commit();
                        System.out.println("事务提交");
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 出现异常后，回滚事物
                        transactionManager.rollback();
                        System.out.println("事务回滚");
                        throw e; // 将异常抛出，告诉上层
                    }
                    return result;
                }
            });
        return o;
    }

}
