package com.sysu.rpc;

import java.io.Serializable;
import java.util.Arrays;

public class RequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String className;
    private String methodName;
    private Class[] types;
    private Object[] params;

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Class[] getTypes() {
        return types;
    }

    public void setTypes(Class[] types) {
        this.types = types;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "RequestDTO{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", types=" + Arrays.toString(types) +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
