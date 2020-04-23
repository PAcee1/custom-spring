package com.enbuys.service.impl;

import com.enbuys.dao.AccountDao;
import com.enbuys.pojo.Account;
import com.enbuys.service.TransferService;
import com.enbuys.utils.TransactionManager;

import java.sql.SQLException;

/**
 * @author 应癫
 */
public class TransferServiceImpl implements TransferService {

    // private AccountDao accountDao = new JdbcAccountDaoImpl();
    //private AccountDao accountDao = (AccountDao) BeanFactory.getBean("accountDao");
    // 完美形式
    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {
        try {
            // 开启事物
            TransactionManager.getInstance().begin();
            Account from = accountDao.queryAccountByCardNo(fromCardNo);
            Account to = accountDao.queryAccountByCardNo(toCardNo);

            from.setMoney(from.getMoney() - money);
            to.setMoney(to.getMoney() + money);

            accountDao.updateAccountByCardNo(to);

            // 测试异常
            int a = 1/0;

            accountDao.updateAccountByCardNo(from);

            // 提交事务
            TransactionManager.getInstance().commit();
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常后，回滚事物
            TransactionManager.getInstance().rollback();

            throw e; // 将异常抛出，告诉上层
        }
    }
}
