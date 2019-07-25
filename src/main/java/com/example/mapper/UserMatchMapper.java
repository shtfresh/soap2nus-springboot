package com.example.mapper;

import com.example.UserMatch.UserMatch;

import java.util.List;
import java.util.Map;

public interface UserMatchMapper {
    /**
     * @param userId
     * @return
     * 根据用户id获取内容
     */
    Map<String , Object> getUserInfoByUserId(Map map);

    /**
     * @param userId
     * @param age
     * @param gender
     * @param height
     * @param weight
     * @return
     * 更新个人信息
     */
    Map<String , Object> updateUser(Map map);

    /**
     * @param userId
     * @return
     * 获取报名赛事
     */
    UserMatch queryReportMatchList(Map map);

    /**
     * @param userId
     * @param matchId
     * @return
     * 获取赛事数据
     */
    List<UserMatch> getMatchList();
}
