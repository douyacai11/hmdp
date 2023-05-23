package com.hmdp.utils;

//基于redis实现 分布式锁
public interface ILock {

    /**1.获取锁
     *   其中的timeoutSec代表的是expire对应的时间。给锁加上过期时间*/
    boolean tryLock(Long timeoutSec);


    //2.释放锁
    void unlock();
}
