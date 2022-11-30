package com.jay.controller;

import com.jay.feign.ZkDemo2Feign;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/11/30
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@RequestMapping("demo1")
public class ZkController {

    @Autowired
    private ZkDemo2Feign zkDemo2Feign;

    @RequestMapping("zk/rest")
    public String testRestZk(){
        return zkDemo2Feign.zkLocal();
    }

}
