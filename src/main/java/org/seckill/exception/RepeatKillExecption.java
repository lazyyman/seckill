package org.seckill.exception;

/**
 * 重复秒杀异常(运行时异常)
 * Created by lxw on 2017/10/5.
 */
public class RepeatKillExecption extends SeckillException{

    public RepeatKillExecption(String message) {
        super(message);
    }

    public RepeatKillExecption(String message, Throwable cause) {
        super(message, cause);
    }
}
