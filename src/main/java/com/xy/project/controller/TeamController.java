package com.xy.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xy.project.common.BaseResponse;
import com.xy.project.common.DeleteRequest;
import com.xy.project.common.ErrorCode;
import com.xy.project.common.ResultUtils;
import com.xy.project.exception.BusinessException;
import com.xy.project.model.domain.Team;
import com.xy.project.model.domain.User;
import com.xy.project.model.domain.UserTeam;
import com.xy.project.model.dto.TeamQuery;
import com.xy.project.model.request.TeamAddRequest;
import com.xy.project.model.request.TeamJoinRequest;
import com.xy.project.model.request.TeamQuitRequest;
import com.xy.project.model.request.TeamUpdateRequest;
import com.xy.project.model.vo.TeamUserVo;
import com.xy.project.service.TeamService;
import com.xy.project.service.UserService;
import com.xy.project.service.UserTeamService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 队伍接口
 *
 *
 */
@RestController
@RequestMapping("/team")
@CrossOrigin(origins = {"http://localhost:5173","http://localhost:3000"},allowCredentials = "true")
@Slf4j
public class TeamController {

    @Resource
    private TeamService teamService;

    @Resource
    private UserService userService;
    @Resource
    private UserTeamService userTeamService;


    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request){
        if (teamAddRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team=new Team();
        User loginUser = userService.getLoginUser(request);
        BeanUtils.copyProperties(teamAddRequest,team);
        long teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);

    }



    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest,HttpServletRequest request){
        if (teamUpdateRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean save = teamService.updateTeam(teamUpdateRequest,loginUser);
        if (!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新失败 ");
        }
        return ResultUtils.success(true);

    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(Long id){
        if (id<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team==null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(team);
    }

    @GetMapping("/list")
    public BaseResponse<List<TeamUserVo>> ListTeams(TeamQuery teamQuery,HttpServletRequest request){
        if (teamQuery==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        boolean isAdmin = userService.isAdmin(request);
        List<TeamUserVo> teamList = teamService.listTeams(teamQuery,isAdmin);

        //判断当前用户是否加入队伍
        List<Long> teamIdList=teamList.stream().map(TeamUserVo::getId).collect(Collectors.toList());
        QueryWrapper<UserTeam> userTeamQueryWrapper =new QueryWrapper<>();

        try{
            User loginUser = userService.getLoginUser(request);
            userTeamQueryWrapper.eq("userId",loginUser.getId());
            userTeamQueryWrapper.in("teamId",teamIdList);
            final List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
            Set<Long> hasJoinTeamIdSet = userTeamList.stream().map(UserTeam::getTeamId).collect(Collectors.toSet());
            teamList.forEach(team->{
                boolean hasJoin=hasJoinTeamIdSet.contains(team.getId());
                team.setHasJoin(hasJoin);
            });
        }catch (Exception e){
        }
        //查询加入队伍的用户信息
        QueryWrapper<UserTeam> userTeamJoinQueryWrapper=new QueryWrapper<>();
        userTeamJoinQueryWrapper.in("teamId",teamIdList);
        List<UserTeam> userTeamList = userTeamService.list(userTeamJoinQueryWrapper);
        Map<Long, List<UserTeam>> teamIdUserTeamList = userTeamList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));
        teamList.forEach(team->{
            team.setHasJoinNum(teamIdUserTeamList.getOrDefault(team.getId(),new ArrayList<>( )).size()) ;
        });
        return ResultUtils.success(teamList);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page <Team>> ListTeamsByPage(TeamQuery teamQuery){
        if (teamQuery==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team=new Team();
        BeanUtils.copyProperties(teamQuery,team);
        Page<Team> page=new Page<>(teamQuery.getPageNum(),teamQuery.getPageSize());
        QueryWrapper<Team> queryWrapper=new QueryWrapper<>();
        Page<Team> teamPage = teamService.page(page,queryWrapper);
        return ResultUtils.success(teamPage);
    }

    @PostMapping("/join")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest,HttpServletRequest request){
        if (teamJoinRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result=teamService.joinTeam(teamJoinRequest,loginUser);
        return ResultUtils.success(result);
    }

    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest,HttpServletRequest request){
        if (teamQuitRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser=userService.getLoginUser(request);
        boolean result=teamService.quitTeam(teamQuitRequest,loginUser);
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request){
        if (deleteRequest==null||deleteRequest.getId()<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id=deleteRequest.getId();
        User loginUser=userService.getLoginUser(request);
        boolean save = teamService.deleteTeam(id,loginUser);
        if (!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"删除失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 获取当前用户已创建的队伍
     */
    @GetMapping("/list/my/create")
    public BaseResponse<List<TeamUserVo>> ListMyCreateTeams(TeamQuery teamQuery,HttpServletRequest request){
        if (teamQuery==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        teamQuery.setUserId(loginUser.getId());
        List<TeamUserVo> teamList = teamService.listTeams(teamQuery,true);
        return ResultUtils.success(teamList);
    }

    /**
     * 获取当前用户已加入的队伍
     */
    @GetMapping("/list/my/join")
    public BaseResponse<List<TeamUserVo>> ListMyJoinTeams(TeamQuery teamQuery,HttpServletRequest request){
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getId());
        List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
        Map<Long, List<UserTeam>> listMap = userTeamList.stream()
                .collect(Collectors.groupingBy(UserTeam::getTeamId));
        List<Long> idList = new ArrayList<>(listMap.keySet());
        teamQuery.setIdList(idList);
        List<TeamUserVo> teamList = teamService.listTeams(teamQuery, true);
        return ResultUtils.success(teamList);
    }
}
