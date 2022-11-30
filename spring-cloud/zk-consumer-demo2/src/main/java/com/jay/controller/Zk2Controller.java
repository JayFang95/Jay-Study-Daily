package com.jay.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("demo2")
public class Zk2Controller {

    @RequestMapping("zk/local")
    public String zkLocal(){
        return "hello, 我是来自 zk demo2 的方法";
    }

}
