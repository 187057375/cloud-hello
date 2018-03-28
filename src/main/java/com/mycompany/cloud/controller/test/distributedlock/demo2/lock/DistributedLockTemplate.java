package com.mycompany.cloud.controller.test.distributedlock.demo2.lock;

/**
 * 分布式锁模板类
 *  2016/2/23.
 */
public interface DistributedLockTemplate {

    /**
     *
     * @param lockId 锁id(对应业务唯一ID)
     * @param timeout 单位毫秒
     * @param callback 回调函数
     * @return
     */
    public Object execute(String lockId, int timeout, Callback callback);
}
