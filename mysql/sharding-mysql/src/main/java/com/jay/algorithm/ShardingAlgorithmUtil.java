package com.jay.algorithm;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/8
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 分片算法工具类
 */
public class ShardingAlgorithmUtil {

    /**
     * 获取年份
     */
    public static String getYearByMillisecond(long millisecond) {
        return new SimpleDateFormat("yyyy").format(new Date(millisecond));
    }

    /**
     * 获取年月
     */
    public static String getYearJoinMonthByMillisecond(long millisecond) {
        return new SimpleDateFormat("yyyyMM").format(new Date(millisecond));
    }
}
