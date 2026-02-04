package com.lynlyu.shortlink.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lynlyu.shortlink.domain.entity.IdGeneratorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Mapper for distributed ID generation using Segment Mode.
 */
@Mapper
public interface IdGeneratorMapper extends BaseMapper<IdGeneratorEntity> {

    /**
     * Atomically increment the max_id by the step value for a specific business type.
     * This uses MySQL's row-level locking to ensure thread safety across multiple instances.
     */
    @Update("UPDATE t_id_generator SET max_id = max_id + step WHERE biz_type = #{bizType}")
    int updateNextSegment(@Param("bizType") String bizType);

    /**
     * Fetch the updated segment info.
     */
    @Select("SELECT biz_type, max_id, step FROM t_id_generator WHERE biz_type = #{bizType}")
    IdGeneratorEntity selectByBizType(@Param("bizType") String bizType);
}