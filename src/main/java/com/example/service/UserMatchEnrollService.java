package com.example.service;

import com.example.UserMatch.UserMatch;
import com.example.mapper.UserMatchEnrollMapper;
import com.example.mapper.UserMatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserMatchEnrollService {

    @Autowired
	UserMatchEnrollMapper userMatchEnrollMapper;

	public List<Map<String, Object>> queryEnrolledMatchList(String userId) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		return userMatchEnrollMapper.queryEnrolledMatchList(map);
	}

	public void enrolledMatch(Map map) {
		userMatchEnrollMapper.enrolledMatch(map);
	}

	public List<Map<String, Object>> getAllEnrolledMatchList() {
		return userMatchEnrollMapper.getAllEnrolledMatchList();
	}
}