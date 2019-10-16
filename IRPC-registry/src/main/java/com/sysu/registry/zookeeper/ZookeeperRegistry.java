package com.sysu.registry.zookeeper;

import com.sysu.registry.AbstractRegistry;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZookeeperRegistry extends AbstractRegistry {

    private static final int SESSION_TIMEOUT_MS = 5000;
    private static final int SLEEP_TIME_MS = 1000;
    private static final int MAX_RETRIES = 2;

    private Map<String, List<String>> serviceProviderMap = new HashMap<String, List<String>>();
    private CuratorFramework  curatorFramework;

    @Override
    protected void init(String address) {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(address)
                .sessionTimeoutMs(SESSION_TIMEOUT_MS)
                .retryPolicy(new ExponentialBackoffRetry(SLEEP_TIME_MS, MAX_RETRIES))
                .build();

        curatorFramework.start();
    }

    public void register(String providerAddress, String service) {
        try {
            String servicePath = FOLDER + SEPARATOR + service;
            Stat stat = curatorFramework.checkExists().forPath(servicePath);

            if (stat == null) {
                curatorFramework.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT).forPath(servicePath);
            }
            String provider = servicePath + SEPARATOR + providerAddress;
            curatorFramework.create().withMode(CreateMode.EPHEMERAL)
                    .forPath(provider);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected List<String> lookup(String service) {
        try {
            String servicePath = FOLDER + SEPARATOR + service;
            List<String> providers = curatorFramework.getChildren().forPath(servicePath);

            serviceProviderMap.put(service, providers);
            watchProvider(servicePath);

            return serviceProviderMap.get(service);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void watchProvider(final String path) {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, path, true);
        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                serviceProviderMap.put(path, curatorFramework.getChildren().forPath(path));
            }
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            pathChildrenCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
