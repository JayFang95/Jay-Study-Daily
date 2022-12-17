package com.jay.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2021/8/2
 * @description 人员bean
 * history
 * <author>    <time>    <version>    <desc>
 *  作者姓名     修改时间     版本号        描述
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 所属公司id
     */
    private Long companyId;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 职务
     */
    private String post;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 公众号id
     */
    private String appId;

    /**
     * 备注
     */
    private String note;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否可以删除
     */
    private Boolean deleteFlg;

    /**
     * 属于编组
     */
    private Long groupId;


}
