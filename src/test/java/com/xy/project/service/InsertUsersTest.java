package com.xy.project.service;

import com.xy.project.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class InsertUsersTest {
    @Resource
    private UserService userService;

    @Test
    public void doInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<User> userList =new ArrayList<>();
        final int INSERT_NUM = 100000;
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("假用户");
            user.setUserAccount("fakeyupi");
            user.setAvatarUrl("");
            user.setGender(0);
            user.setUserPassword("12345678");
            user.setPhone("112233");
            user.setTags("[]");
            user.setEmail("112233@qq.com");
            user.setUserStatus(0);

            user.setUserRole(0);
            user.setPlanetCode("1111111111");
//            userMapper.insert(user);
            userList.add(user);
        }
        userService.saveBatch(userList,10000);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}

