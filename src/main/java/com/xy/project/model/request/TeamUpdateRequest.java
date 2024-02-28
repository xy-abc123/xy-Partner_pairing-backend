package com.xy.project.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TeamUpdateRequest implements Serializable {
    private static final long serialVersionUID = 12312312313123L;

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
     * 过期时间
     */
    private Date expireTime;

    /**
     * 状态 0-公开 1-私有 2-加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;


}
