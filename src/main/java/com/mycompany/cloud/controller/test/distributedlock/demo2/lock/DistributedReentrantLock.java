package com.mycompany.cloud.controller.test.distributedlock.demo2.lock;

import java.util.concurrent.TimeUnit;

/**
 *  2016/2/26.
 */
public interface DistributedReentrantLock {
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException;

    public void unlock();
}
