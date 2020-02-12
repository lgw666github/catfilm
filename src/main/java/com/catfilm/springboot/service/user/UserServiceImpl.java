package com.catfilm.springboot.service.user;

import com.alibaba.druid.util.Utils;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.catfilm.springboot.common.tools.MD5Util;
import com.catfilm.springboot.controller.user.UserNameFromToken;
import com.catfilm.springboot.controller.user.vo.CheckUserVo;
import com.catfilm.springboot.controller.user.vo.UserInfo;
import com.catfilm.springboot.dao.entity.NextUserT;
import com.catfilm.springboot.dao.mapper.NextUserTMapper;
import com.catfilm.springboot.service.exception.CommException;
import com.catfilm.springboot.service.user.bo.RegisterVo;
import jdk.jfr.events.ThrowablesEvent;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService{

    @Autowired
    private NextUserTMapper nextUserTMapper;

    @Override
    public UserInfo queryById(Integer id) throws CommException {
        NextUserT nextUserT = nextUserTMapper.selectById(id);
        if (nextUserT==null)
            throw new CommException(404,"uuid: "+id+" 不存在");
        return nextUserToUserInfo(nextUserT);
    }

    @Override
    public UserInfo queryByUserName() throws CommException {
        AbstractWrapper abstractWrapper=new QueryWrapper();
        abstractWrapper.eq("user_name", UserNameFromToken.getUserName());

        List<NextUserT> list = nextUserTMapper.selectList(abstractWrapper);

        if (null!=list&&!list.isEmpty()) {
            return nextUserToUserInfo(list.get(0));
        }
        throw new CommException(500,"没有userName："+UserNameFromToken.getUserName());
    }

    @Override
    public boolean registerUser(RegisterVo registerVo) throws CommException {
        NextUserT nextUserT=new NextUserT();
        nextUserT.setUserName(registerVo.getUsername());
        nextUserT.setUserPwd(MD5Util.encrypt(registerVo.getPassword()));

        BeanUtils.copyProperties(registerVo,nextUserT);
        int insert = nextUserTMapper.insert(nextUserT);
        if (insert>0)
            return true;
        throw new CommException(500,registerVo.getUsername()+"用户注册失败");

    }

    @Override
    public boolean checkhasUserName(String userName) throws CommException{
        AbstractWrapper abstractWrapper=new QueryWrapper();
        abstractWrapper.eq("user_name",userName);
        Integer num = nextUserTMapper.selectCount(abstractWrapper);

        if (num>0)
            return true;
        return false;

    }

    @Override
    public boolean checkAuth(CheckUserVo checkUserVo) throws CommException{
        if (!checkhasUserName(checkUserVo.getUserName()))
            throw new CommException(404,checkUserVo.getUserName()+"用户名或密码错误，登录失败");
        AbstractWrapper abstractWrapper=new QueryWrapper();
        abstractWrapper.eq("user_name",checkUserVo.getUserName());
        abstractWrapper.eq("user_pwd", MD5Util.encrypt(checkUserVo.getPasswd()));

        List list = nextUserTMapper.selectList(abstractWrapper);
        if (null==list||list.isEmpty())
            return false;
        return true;

    }

    @Override
    public UserInfo updateUserInfo(UserInfo userInfo) throws CommException{
        if (userInfo==null||userInfo.getUuid()==null)
            throw new CommException(500,"用户信息不存在");
        int i = nextUserTMapper.updateById(userInfoToNextUser(userInfo));
        if (i>0)
            return queryById(userInfo.getUuid());

        throw new CommException(500,"修改用户信息失败");
    }

    //nextUser->userInfo
    public UserInfo nextUserToUserInfo(NextUserT nextUserT){
        UserInfo userInfo=new UserInfo();
        userInfo.setUsername(nextUserT.getUserName());
        userInfo.setNickname(nextUserT.getNickName());
        userInfo.setBeginTime(nextUserT.getBeginTime().toEpochSecond(ZoneOffset.of("+8")));
        userInfo.setUpdateTime(nextUserT.getUpdateTime().toEpochSecond(ZoneOffset.of("+8")));
        userInfo.setLifeState(nextUserT.getLifeState()+"");
        if (nextUserT.getSex()==null)
            nextUserT.setSex(0);

        BeanUtils.copyProperties(nextUserT,userInfo);
        return userInfo;
    }

    //userinfo -> nextUser
    public NextUserT userInfoToNextUser(UserInfo userInfo){
        NextUserT nextUserT=new NextUserT();
        nextUserT.setUserName(userInfo.getUsername());
        nextUserT.setNickName(userInfo.getNickname());
        nextUserT.setUpdateTime(LocalDateTime.now());

        nextUserT.setLifeState(Integer.valueOf(userInfo.getLifeState()==null?"0":userInfo.getLifeState()));
        BeanUtils.copyProperties(userInfo,nextUserT);

        return nextUserT;
    }
}
