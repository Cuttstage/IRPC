package com.sysu.cluster.loadbalance;

import com.sysu.cluster.AbstractLoadBalance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    public String doSelect(List<String> providers) {
        int len = providers.size();
        Random random = new Random();
        int result = random.nextInt(len);
        return providers.get(result);
    }
}
