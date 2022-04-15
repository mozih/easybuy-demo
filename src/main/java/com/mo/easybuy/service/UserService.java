package com.mo.easybuy.service;

import com.mo.easybuy.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mo
 * @since 2022-02-28
 */
public interface UserService extends IService<User> {

    //查询所有用户信息
    List<User> queryAllUser();
    //通过用户Id查询用户信息
    User selectById(Integer userId);
    //通过用户名查询用户
    User selectUserByuserName(String userName);
    //通过邮箱查询用户
    User selectUserByEmail(String email);
    //通过用户名和密码查询用户信息
    User selectUserByNameAndPass(String userName,String userPassword);
    //注册用户
    int registerUser(User user);
    //修改用户信息
    int updateUserByEmail(User user);

}
