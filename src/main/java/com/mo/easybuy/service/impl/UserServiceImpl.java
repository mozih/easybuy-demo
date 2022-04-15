package com.mo.easybuy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mo.easybuy.pojo.User;
import com.mo.easybuy.mapper.UserMapper;
import com.mo.easybuy.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mo
 * @since 2022-02-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<User> queryAllUser() {
        //使用redis缓存
        List<User> userList = (List<User>) this.redisTemplate.opsForValue().get("easybuy:userList");
        if (userList == null || userList.size()==0){
            System.out.println("第一次查询，走数据库");
            userList = this.userMapper.selectList(null);
            //将数据保存到redis缓存中
            this.redisTemplate.opsForValue().set("easybuy:userList",userList);
            return userList;
        }
        return userList;
    }

    @Override
    public User selectById(Integer userId) {
        return this.userMapper.selectById(userId);
    }

    @Override
    public User selectUserByNameAndPass(String userName,String userPassword) {
        //新建一个查询条件
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        //封装查询条件
        userQueryWrapper.eq("userName",userName).eq("userPassword",userPassword);
        return userMapper.selectOne(userQueryWrapper);
    }

    @Override
    public User selectUserByuserName(String userName) {
        //使用redis缓存，避免多次向数据库查询
        User user = (User) this.redisTemplate.opsForValue().get("user:"+userName);
        if (null == user){
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("userName",userName);
            user = this.userMapper.selectOne(userQueryWrapper);
            //存入redis中
            this.redisTemplate.opsForValue().set("user:"+userName, user);
        }
        return user;
    }

    @Override
    public User selectUserByEmail(String email) {
        //使用redis缓存，避免多次向数据库查询
        User user = (User) this.redisTemplate.opsForValue().get("email:"+email);
        if (null == user){
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("email",email);
            user = this.userMapper.selectOne(userQueryWrapper);
            //存入redis中
            this.redisTemplate.opsForValue().set("email:"+email,user);
        }
        return user;
    }

    @Override
    public int registerUser(User user) {
        return this.userMapper.insert(user);
    }

    @Override
    public int updateUserByEmail(User user) {
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("email",user.getEmail()).set("userPassword",user.getUserPassword());
        return this.userMapper.update(null,userUpdateWrapper);
    }
}
