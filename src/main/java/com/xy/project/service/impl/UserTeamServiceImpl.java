package com.xy.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.project.mapper.UserTeamMapper;
import com.xy.project.model.domain.UserTeam;
import com.xy.project.service.UserTeamService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户服务实现类
 *
 * 
 * 
 */
@Service
@Slf4j
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam> implements UserTeamService {

    @Resource
    private UserTeamMapper userTeamMapper;

}

