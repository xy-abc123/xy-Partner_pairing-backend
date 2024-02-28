package com.xy.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.project.model.dto.TeamQuery;
import com.xy.project.model.domain.Team;
import com.xy.project.model.domain.User;
import com.xy.project.model.request.TeamJoinRequest;
import com.xy.project.model.request.TeamQuitRequest;
import com.xy.project.model.request.TeamUpdateRequest;
import com.xy.project.model.vo.TeamUserVo;

import java.util.List;

/**
 * 用户服务
 *
 * 
 * 
 */
public interface TeamService extends IService<Team> {

    /**
     * 创建队伍
     * @param team
     * @return
     */
    long addTeam(Team team,User loginUser);

    /**
     * 搜索队伍
     * @param teamQuery
     * @param isAdmin
     * @return
     */
    List<TeamUserVo> listTeams(TeamQuery teamQuery, boolean isAdmin);

    boolean updateTeam(TeamUpdateRequest teamUpdateRequest,User loginUser);

    boolean joinTeam(TeamJoinRequest teamJoinRequest,User loginUser);


    /**
     * 退出队伍
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 删除 解散队伍
     * @param id
     * @return
     */
    boolean deleteTeam(Long id,User loginUser);
}
