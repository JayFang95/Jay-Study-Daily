package com.jay.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/11/30
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "zk-demo2-service", path = "demo2")
public interface ZkDemo2Feign {

    /**
     * demo2 本地方法
     * @return s
     */
    @RequestMapping("zk/local")
    public String zkLocal();

}
