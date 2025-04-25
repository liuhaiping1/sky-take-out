package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    private static  final String WX_LOGIN_URL="https://api.weixin.qq.com/sns/jscode2session";
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        //1.通过HttpClient，构造登录凭证校验请求
        //构造请求参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid",weChatProperties.getAppid());
        paramMap.put("secret",weChatProperties.getSecret());
        paramMap.put("js_code",userLoginDTO.getCode());
        paramMap.put("grant_type","authorization_code");
        //调用HttpClientUtil工具类，发送请求
        String res = HttpClientUtil.doGet(WX_LOGIN_URL, paramMap);
        log.info("res={}",res);

        //2.解析响应结果，获取openid
        //JSON转为object
        JSONObject jsonObject = JSON.parseObject(res);
        String openid =(String) jsonObject.get("openid");
        if (openid==null){
            throw new LoginFailedException(MessageConstant.USER_NOT_LOGIN);
        }
        //3.判断是否为新用户，根据openid查询user表
        User user=userMapper.selectByOpenid(openid);
        //4.新用户，初始化数据到user表中
        if (user == null){
            user = new User();
            user.setOpenid(openid);
            user.setCreateTime(LocalDateTime.now());
            user.setName("用户"+openid.substring(0,5));

            userMapper.insert(user);
        }
        //5.老用户，直接返回user对象数据
        return user;
    }
}
