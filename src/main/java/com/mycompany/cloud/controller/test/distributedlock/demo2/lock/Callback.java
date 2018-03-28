package com.mycompany.cloud.controller.test.distributedlock.demo2.lock;

/**
 *  2016/2/23.
 */
public interface Callback {

    public Object onGetLock() throws InterruptedException;

    public Object onTimeout() throws InterruptedException;
}
