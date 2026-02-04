package com.lynlyu.shortlink.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entity representing the short link table.
 * Annotations ensured for Lombok Builder and MyBatis-Plus compatibility.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_short_link")
public class ShortLinkEntity {

    @TableId(type = IdType.INPUT)
    private Long id;

    private String shortCode;

    private String longUrl;

    private LocalDateTime createTime;

    private LocalDateTime expireTime;
}