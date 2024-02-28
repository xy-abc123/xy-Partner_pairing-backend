package com.xy.project.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TeamJoinRequest implements Serializable {
    private static final long serialVersionUID = 12312312313123L;

    private Long teamId;


    /**
     * 密码
     */
    private String password;


}
