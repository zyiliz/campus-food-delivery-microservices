package com.example.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("user_preference")
public class Preference {

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "preference_summary")
    private String preferenceSummary;

    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;
}
