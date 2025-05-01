package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

public interface UserMapper {
//    @Select("select * from user where openid = #{openid}")
    User selectByOpenid(String openid);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into user(openid,name,create_time) values(#{openid},#{name},#{createTime})")
    void insert(User user);

    User getById(Long userId);

    /**
     * 根据条件统计用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
