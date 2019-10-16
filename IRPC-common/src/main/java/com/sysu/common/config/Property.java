package com.sysu.common.config;

import com.sysu.common.util.PropertyUtil;

public class Property {
    private static String REGISTRY_PROTOCOL_KEY = "IRPC.registry.protocol";
    private static String REGISTRY_ADDRESS_KEY = "IRPC.registry.address";
    private static String CLUSTER_LOADBALANCE_KEY = "IRPC.cluster.loadbalance";

    public static class Registry {
        public static String protocol = PropertyUtil.getInstance().get(REGISTRY_PROTOCOL_KEY);
        public static String address = PropertyUtil.getInstance().get(REGISTRY_ADDRESS_KEY);
    }

    public static class Cluster {
        public static String loadbalance = PropertyUtil.getInstance().get(CLUSTER_LOADBALANCE_KEY);
    }
}
