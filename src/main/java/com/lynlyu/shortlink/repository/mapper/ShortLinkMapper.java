package com.lynlyu.shortlink.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lynlyu.shortlink.domain.entity.ShortLinkEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Repository interface for ShortLinkEntity.
 * Extends BaseMapper to inherit standard CRUD operations.
 */
@Mapper
public interface ShortLinkMapper extends BaseMapper<ShortLinkEntity> {

    /**
     * Optimized query to fetch long URL directly.
     * Reduces overhead by selecting only the required column.
     * * @param shortCode The unique short code
     * @return The original long URL string
     */
    @Select("SELECT long_url FROM t_short_link WHERE short_code = #{shortCode} LIMIT 1")
    String selectLongUrlByCode(String shortCode);
}