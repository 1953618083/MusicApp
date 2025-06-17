package com.music.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.music.config.JwtUtil;
import com.music.entity.dto.LoginDTO;
import com.music.entity.dto.LoginRecordDTO;
import com.music.entity.dto.UserDTO;
import com.music.entity.dto.UserListDTO;
import com.music.entity.po.UserPO;
import com.music.entity.vo.UserVO;
import com.music.mapper.UserMapper;
import com.music.response.R;
import com.power.common.util.CollectionUtil;
import com.power.common.util.DateTimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginRecordService loginRecordService;

    IdentifierGenerator identifierGenerator = new DefaultIdentifierGenerator();


    public R doLogin(LoginDTO dto) {
        String account = dto.getAccount();
        String password = dto.getPassword();

        UserVO userVO = userMapper.selectUserByAccount(account);
        if (userVO == null) {
            return R.FAILED("该账号不存在");
        }
        if (!userVO.getPassword().equals(password)) {
            return R.FAILED("该账号密码不正确");
        }

        LoginRecordDTO loginRecordDTO = new LoginRecordDTO();
        loginRecordDTO.setLoginAccount(account);
        loginRecordDTO.setStatus(1);

        loginRecordService.addLoginRecord(loginRecordDTO);

        String token = JwtUtil.createToken(account);
        Map<String, Object> result = new HashMap<>();
        result.put("userId", userVO.getId());
        result.put("account", account);
        result.put("role", userVO.getStatus());
        result.put("token", token);
        return R.SUCCESS("登录成功!").setData(result);
    }




    public R register(UserDTO dto) {

        if (dto.getId() == null) {
            UserVO userVO = userMapper.selectUserByAccount(dto.getAccount());
            if (userVO != null) {
                return R.FAILED("该账号已存在");
            }
            UserPO po = new UserPO();
            BeanUtils.copyProperties(dto, po);
            po.setId(identifierGenerator.nextId(new Object()).longValue());
            String createDateTime = DateTimeUtil.dateToStr(new Date(), DateTimeUtil.DATE_FORMAT_SECOND);
            String updateDateTime = DateTimeUtil.dateToStr(new Date(), DateTimeUtil.DATE_FORMAT_SECOND);
            po.setCreateTime(createDateTime);
            po.setUpdateTime(updateDateTime);
            userMapper.insert(po);
        } else {
            UserPO po = new UserPO();
            BeanUtils.copyProperties(dto, po);
            String updateDateTime = DateTimeUtil.dateToStr(new Date(), DateTimeUtil.DATE_FORMAT_SECOND);
            UserVO userVO = userMapper.selectUserById(dto.getId());
            po.setCreateTime(userVO.getCreateTime());
            po.setUpdateTime(updateDateTime);
            userMapper.deleteUserById(dto.getId());
            userMapper.insert(po);
        }

        return R.SUCCESS("操作成功!");
    }


    public R userList(UserListDTO dto) {
        List<UserVO> userVOList = userMapper.selectUserList(dto);
        if (CollectionUtil.isEmpty(userVOList)) {
            return R.SUCCESS("获取成功").setData(new ArrayList<>());
        }
        return R.SUCCESS("获取成功").setData(userVOList);
    }

    public R stopUser(Long userId) {
        UserVO userVO = userMapper.selectUserById(userId);
        if (userVO == null) {
            return R.FAILED("该账号不存在");
        }
        Integer status = userVO.getStatus();
        if (status == 1) {
            status = 0;
        } else {
            status = 1;
        }
        userMapper.modifyUserStatus(userId, status);
        return R.SUCCESS("操作成功");
    }


    public R removeUser(Long userId) {
        userMapper.deleteUserById(userId);

        return R.SUCCESS("删除成功");
    }

    public R selectUserInfo(String account) {
        UserVO userVO = userMapper.selectUserByAccount(account);
        if (userVO == null) {
            return R.FAILED("该账号不存在!");
        }

        return R.SUCCESS("操作成功").setData(userVO);
    }

    public R adminLogin(LoginDTO dto) {
        String account = dto.getAccount();
        String password = dto.getPassword();

        UserVO userVO = userMapper.selectAdminByAccount(account);
        if (userVO == null) {
            return R.FAILED("该账号不存在");
        }
        if (!userVO.getPassword().equals(password)) {
            return R.FAILED("该账号密码不正确");
        }

        LoginRecordDTO loginRecordDTO = new LoginRecordDTO();
        loginRecordDTO.setLoginAccount(account);
        loginRecordDTO.setStatus(1);

        loginRecordService.addLoginRecord(loginRecordDTO);

        String token = JwtUtil.createToken(account);
        Map<String, Object> result = new HashMap<>();
        result.put("account", account);
        result.put("role", userVO.getStatus());
        result.put("token", token);
        return R.SUCCESS("登录成功!").setData(result);
    }
}
