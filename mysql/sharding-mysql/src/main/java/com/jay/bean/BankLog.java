package com.jay.bean;

import lombok.Data;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/8
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
public class BankLog {
    private int id;
    private String name;
    private String note;
    private Long createTime;
}
