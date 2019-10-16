package com.sysu.cluster.loadbalance;

import com.sysu.cluster.AbstractLoadBalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundLoadBalance extends AbstractLoadBalance {
    private AtomicInteger previous = new AtomicInteger();

    @Override
    public String doSelect(List<String> providers) {
        int size = providers.size();
        if (previous.get() >= size) {
            previous.set(0);
        }
        String result = providers.get(previous.get());
        previous.set(previous.get() + 1);
        return result;
    }
}
