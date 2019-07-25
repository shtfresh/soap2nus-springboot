package com.example.mapper;

import java.util.List;
import java.util.Map;

public interface UserMatchEnrollMapper {
    void enrolledMatch(Map map);

    List<Map<String, Object>> queryEnrolledMatchList(Map map);

    List<Map<String, Object>> getAllEnrolledMatchList();
}