package com.music.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_play_history")
@Data
@TableName(value = "m_play_history")
public class PlayHistoryPO {

    @Id
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField(value = "music_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long musicId;

    @TableField(value = "user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;


    @TableField(value = "create_time")
    private String createTime;

    @TableField(value = "play_duration")
    private Integer playDuration;


}
