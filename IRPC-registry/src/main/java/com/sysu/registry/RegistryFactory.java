package com.sysu.registry;

import com.sysu.common.config.Property;
import com.sysu.common.loader.ExtensionLoader;

public class RegistryFactory {

    public static IRegistry getRegistry() {
        String protocol = Property.Registry.protocol;
        IRegistry registry = ExtensionLoader.getExtensionLoader(IRegistry.class).getExtension(protocol);
        return registry;
    }
}