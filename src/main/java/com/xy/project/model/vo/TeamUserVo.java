package com.xy.project.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 队伍和用户信息封装类
 */
@Data
public class TeamUserVo implements Serializable {
    private static final long serialVersionUID =12312313131312L;
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 状态 0-公开 1-私有 2-加密
     */
    private Integer status;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    /**
     * 创建人信息
     */
    private UserVo createUser;

    /**
     * 用户是否已加入的队伍信息
     */
    private boolean hasJoin=false;

    /**
     * 加入用户数量
     */
    private Integer hasJoinNum;
}

