package com.mycompany.cloud.controller.test.distributedlock.demo2.sequence;

/**
 *  2016/2/25.
 */
public interface DistributedSequence {

    public Long sequence(String sequenceName);
}
