package com.sysu.registry;

import com.sysu.cluster.ILoadBalance;
import com.sysu.cluster.LoadBalanceFactory;
import com.sysu.common.config.Property;

import java.util.List;

public abstract class AbstractRegistry implements IRegistry {
    protected static final String FOLDER = "/rpcregistrys";
    protected static final String SEPARATOR = "/";

    public AbstractRegistry() {
        String address = Property.Registry.address;
        init(address);
    }


    public String discover(String service) {
        List<String> providers = lookup(service);
        ILoadBalance loadBalance = LoadBalanceFactory.getLoadBalance();
        String result = loadBalance.select(providers);
        return result;
    }

    protected abstract void init(String address);

    protected abstract List<String> lookup(String service);
}
