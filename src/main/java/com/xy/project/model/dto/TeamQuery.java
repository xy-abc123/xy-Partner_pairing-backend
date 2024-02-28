package com.xy.project.model.dto;

import com.xy.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class TeamQuery extends PageRequest {

    private long id;

    private List<Long> idList;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 队伍名称
     */
    private String searchText;


    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 状态 0-公开 1-私有 2-加密
     */
    private Integer status;
}
