package com.mycompany.cloud.controller.test.distributedlock.demo2.sequence;

/**
 * Created by sunyujia@aliyun.com on 2016/2/25.
 */
public interface DistributedSequence {

    public Long sequence(String sequenceName);
}
