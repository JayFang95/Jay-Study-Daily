package com.jay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jay.bean.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/8
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
