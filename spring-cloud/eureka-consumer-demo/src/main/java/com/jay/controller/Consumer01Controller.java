package com.jay.controller;

import com.jay.feign.ConsumerFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/11/24
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@RequestMapping("consumer-one")
public class Consumer01Controller {

    @Autowired
    private ConsumerFeign consumerFeign;

    @RequestMapping("restTest")
    public String getRestTest(){
        System.out.println("调用了远程服务");
        return consumerFeign.test();
    }

}
