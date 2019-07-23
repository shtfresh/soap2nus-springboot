package com.example.mapper;

import java.util.List;
import java.util.Map;

public interface UserMatchEnrollMapper {

    List<Map<String, Object>> queryEnrolledMatchList(Map map);
}