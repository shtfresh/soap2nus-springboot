package com.example.service;

import com.example.UserMatch.UserMatch;
import com.example.mapper.TPTEnumMapper;
import com.example.mapper.UserMatchMapper;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserMatchService {

    @Autowired
	UserMatchMapper userMatchMapper;

	public Map<String , Object> getUserInfoByUserId(String userId) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		return userMatchMapper.getUserInfoByUserId(map);
	}

	public void updateUser(Integer userId,
										   String age,
										   Integer gender,
										   String height,
										   String weight) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("gender", gender);
		map.put("height", height);
		map.put("weight", weight);

		userMatchMapper.updateUser(map);
	}

	public UserMatch queryReportMatchList(String matchId) {
		Map<String, Object> map = new HashMap<>();
		map.put("matchId", matchId);
		return userMatchMapper.queryReportMatchList(map);
	}

	public List<UserMatch> getMatchList() {
		return userMatchMapper.getMatchList();
	}
}