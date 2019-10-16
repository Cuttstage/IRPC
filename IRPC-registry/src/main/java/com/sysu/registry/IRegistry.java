package com.sysu.registry;

import com.sysu.common.anno.FarSPI;

@FarSPI("zookeeper")
public interface IRegistry {
    void register(String providerAddress, String service);

    String discover(String service);
}
