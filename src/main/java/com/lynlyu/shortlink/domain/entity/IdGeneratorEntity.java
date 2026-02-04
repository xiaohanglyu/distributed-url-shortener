package com.lynlyu.shortlink.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Entity for ID Segment Generator.
 */
@Data
@TableName("t_id_generator")
public class IdGeneratorEntity {
    @TableId
    private String bizType;
    private Long maxId;
    private Integer step;
}