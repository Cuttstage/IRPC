package com.sysu.cluster;

import com.sysu.common.config.Property;
import com.sysu.common.loader.ExtensionLoader;

public class LoadBalanceFactory {

    public static ILoadBalance getLoadBalance() {
        String loadBalance = Property.Cluster.loadbalance;
        ILoadBalance result = ExtensionLoader.getExtensionLoader(ILoadBalance.class).getExtension(loadBalance);
        return result;
    }

}
