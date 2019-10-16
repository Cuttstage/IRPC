package com.sysu.cluster;

import com.sysu.common.anno.FarSPI;

import java.util.List;

@FarSPI("random")
public interface ILoadBalance {

    String select(List<String> providers);
}
