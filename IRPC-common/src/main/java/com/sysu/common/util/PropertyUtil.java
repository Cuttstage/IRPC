package com.sysu.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
    private volatile static PropertyUtil instance = null;
    private Properties property;

    public static PropertyUtil getInstance() {
        if (instance == null) {
            synchronized (PropertyUtil.class) {
                if (instance == null) {
                    instance = new PropertyUtil();
                }
            }
        }
        return instance;
    }

    private PropertyUtil() {
        initProperty();
    }

    private void initProperty() {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
            property = new Properties();
            property.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(String.format("call PropertyUtil.initProperty, e.getMessage:[%s]", e.getMessage()), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(String.format("call PropertyUtil.initProperty, e.getMessage:[%s]", e.getMessage()), e);
                }
            }
        }
    }

    public String get(String key) {
        String result = property.getProperty(key);
        return result;
    }
}
