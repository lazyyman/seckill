package org.seckill.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillExecption;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by lxw on 2017/10/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SeckillServiceImplTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list = {}", list);
    }

    @Test
    public void getById() throws Exception {
        long seckillId = 1000;
        Seckill seckill = seckillService.getById(seckillId);
        logger.info("seckill = {}", seckill);
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long seckillId = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        logger.info("exposer = {}", exposer);
        //Exposer{
        // exposed=true,
        // md5='75049ec45714c32e62cea70cf0d56a76',
        // seckillId=1000, now=0, start=0, end=0}
    }

    //集成测试代码完整逻辑, 注意可重复执行.
    @Test
    public void testSeckillLogic() throws Exception {
        long seckillId = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if(exposer.isExposed()){
            logger.info("exposer = {}", exposer);
            long userPhone = 18770918893L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
                logger.info("execution = {}", execution);
            } catch (RepeatKillExecption e) {
                logger.error(e.getMessage());
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            }
        }else {
            //秒杀未开始
            logger.warn("execution = {}", exposer);
        }
        /**
         * 18:03:01.814 [main] INFO  c.alibaba.druid.pool.DruidDataSource - {dataSource-1} inited
         18:03:01.838 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Creating a new SqlSession
         18:03:01.843 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Registering transaction synchronization for SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@648c94da]
         18:03:01.848 [main] DEBUG o.m.s.t.SpringManagedTransaction - JDBC Connection [com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl@6138e79a] will be managed by Spring
         18:03:01.852 [main] DEBUG o.s.dao.SeckillDao.reduceNumber - ==>  Preparing: update seckill set number = number - 1 where seckill_id = ? and start_time <= ? and end_time >= ? and number > 0;
         18:03:01.883 [main] DEBUG o.s.dao.SeckillDao.reduceNumber - ==> Parameters: 1000(Long), 2017-10-05 18:03:01.833(Timestamp), 2017-10-05 18:03:01.833(Timestamp)
         18:03:01.906 [main] DEBUG o.s.dao.SeckillDao.reduceNumber - <==    Updates: 1
         18:03:01.906 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@648c94da]
         18:03:01.907 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Fetched SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@648c94da] from current transaction
         18:03:01.907 [main] DEBUG o.s.d.S.insertSuccessKilled - ==>  Preparing: insert ignore into success_killed(seckill_id, user_phone, state) values (?, ?, 1)
         18:03:01.909 [main] DEBUG o.s.d.S.insertSuccessKilled - ==> Parameters: 1000(Long), 18770918893(Long)
         18:03:01.934 [main] DEBUG o.s.d.S.insertSuccessKilled - <==    Updates: 1
         18:03:01.943 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@648c94da]
         18:03:01.945 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Fetched SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@648c94da] from current transaction
         18:03:01.950 [main] DEBUG o.s.d.S.queryByIdWithSeckill - ==>  Preparing: select sk.seckill_id, sk.user_phone, sk.create_time, sk.state, s.seckill_id "seckill.seckillId", s.name "seckill.name", s.number "seckill.number", s.start_time "seckill.start_time", s.end_time "seckill.end_time", s.create_time "seckill.create_time" from success_killed sk inner join seckill s on sk.seckill_id = s.seckill_id where sk.seckill_id = ? and sk.user_phone = ?
         18:03:01.957 [main] DEBUG o.s.d.S.queryByIdWithSeckill - ==> Parameters: 1000(Long), 18770918893(Long)
         18:03:01.999 [main] DEBUG o.s.d.S.queryByIdWithSeckill - <==      Total: 1
         18:03:02.000 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@648c94da]
         18:03:02.001 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Transaction synchronization committing SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@648c94da]
         18:03:02.001 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Transaction synchronization deregistering SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@648c94da]
         18:03:02.001 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Transaction synchronization closing SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@648c94da]
         18:03:02.037 [main] INFO  o.s.s.impl.SeckillServiceImplTest - execution = org.seckill.dto.SeckillExecution@8dbfffb
         18:03:02.045 [Thread-1] INFO  c.alibaba.druid.pool.DruidDataSource - {dataSource-1} closed
         */
    }

}