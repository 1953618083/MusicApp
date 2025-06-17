package com.music.controller;

import com.music.config.JwtUtil;
import com.music.entity.dto.LoginDTO;
import com.music.entity.dto.UserDTO;
import com.music.entity.dto.UserListDTO;
import com.music.entity.vo.UserVO;
import com.music.mapper.UserMapper;
import com.music.response.R;
import com.music.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    public R login(@Valid @RequestBody LoginDTO dto) {
        return userService.doLogin(dto);
    }

    @PostMapping("/admin/login")
    public R adminLogin(@RequestBody LoginDTO dto){
        return userService.adminLogin(dto);
    }
    @PostMapping("/register")
    public R register(@Valid @RequestBody UserDTO dto){
        return userService.register(dto);
    }
    @PostMapping("/list")
    public R userList(@Valid @RequestBody UserListDTO dto){
        return userService.userList(dto);
    }
    @PostMapping("/stop")
    public R stopUser(@RequestParam("userId") Long userId){
        return userService.stopUser(userId);
    }

    @PostMapping("/remove")
    public R removeUser(@RequestParam("userId") Long userId){
        return userService.removeUser(userId);
    }

    @PostMapping("/user_info")
    public R getUserInfo(@RequestParam("account") String account){
        return userService.selectUserInfo(account);
    }

    @GetMapping("/check")
    public R checkToken(HttpServletRequest request) {

        String token = request.getHeader("token");
        boolean checkToken = JwtUtil.checkToken(token);
        if (!checkToken) {
            return R.FAILED("token过期，请登录!");
        }

        return R.SUCCESS("登录正常!");
    }
}
