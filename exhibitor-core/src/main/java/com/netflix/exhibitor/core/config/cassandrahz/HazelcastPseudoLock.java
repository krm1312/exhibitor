package com.netflix.exhibitor.core.config.cassandrahz;

import com.hazelcast.config.Config;
import com.hazelcast.config.ConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.netflix.exhibitor.core.activity.ActivityLog;
import com.netflix.exhibitor.core.config.PseudoLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Created by kevinm on 3/6/15.
 */
public class HazelcastPseudoLock implements PseudoLock {

    private static final String LOCK_NAME = "ExhibitorPseudoLock";
    private HazelcastInstance hazelcastInstance;

    public HazelcastPseudoLock(String instanceName) {
        Config config = new Config();
        config.setInstanceName(instanceName);
        this.hazelcastInstance = Hazelcast.getOrCreateHazelcastInstance(config);
    }

    @Override
    public boolean lock(ActivityLog log, long maxWait, TimeUnit unit) throws Exception {
        Lock lock = hazelcastInstance.getLock(LOCK_NAME);
        return lock.tryLock(maxWait, unit);
    }

    @Override
    public void unlock() throws Exception {
        Lock lock = hazelcastInstance.getLock(LOCK_NAME);
        lock.unlock();
    }

}
