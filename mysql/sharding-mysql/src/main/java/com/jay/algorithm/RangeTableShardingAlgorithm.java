package com.jay.algorithm;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/8
 * @description 表范围分片算法
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class RangeTableShardingAlgorithm implements RangeShardingAlgorithm<Long> {

    /**
     * 表范围分片算法
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames,
                                         RangeShardingValue<Long> rangeShardingValue) {
        Range<Long> range = rangeShardingValue.getValueRange();
        long startMillisecond = range.lowerEndpoint();
        long endMillisecond = range.upperEndpoint();
        return getMonthBetween(startMillisecond, endMillisecond, availableTargetNames);
    }

    /**
     * 计算有效的库表名
     */
    public static Collection<String> getMonthBetween(long minTime, long maxTime,
                                                     Collection<String> availableTargetNames) {
        Collection<String> result = new ArrayList<>();
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(new Date(minTime));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
        max.setTime(new Date(maxTime));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (min.before(max)) {
            String yyyyMM = sdf.format(min.getTime());
            availableTargetNames.forEach(availableTargetName -> {
                if (availableTargetName.endsWith(yyyyMM)) {
                    result.add(availableTargetName);
                }
            });
            min.add(Calendar.MONTH, 1);
        }
        return result;
    }

}
