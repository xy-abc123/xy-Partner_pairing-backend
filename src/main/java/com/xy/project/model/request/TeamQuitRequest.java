package com.xy.project.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户退出队伍
 */

@Data
public class TeamQuitRequest implements Serializable {
    private static final long serialVersionUID = 12312312313123L;

    private Long teamId;



}
