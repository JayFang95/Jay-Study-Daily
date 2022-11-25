package com.jay.feign;

import org.springframework.stereotype.Service;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/11/24
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
public class ConsumerFeignImpl implements ConsumerFeign{
    @Override
    public String test() {
        return "调用异常";
    }
}
