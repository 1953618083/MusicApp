package com.music.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.music.entity.dto.UserListDTO;
import com.music.entity.po.UserPO;
import com.music.entity.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    void insert(UserPO userPO);

    UserVO selectUserByAccount(@Param("account") String account);


    void deleteUserById(@Param("id") Long id);

    List<UserVO> selectUserList(UserListDTO dto);

    UserVO selectUserById(@Param("userId") Long userId);

    void modifyUserStatus(@Param("userId") Long userId, @Param("status") Integer status);

    List<UserVO> selectUserInfo(@Param("userIdList") List<Long> userIdList);

    UserVO selectAdminByAccount(@Param("account")String account);
}
