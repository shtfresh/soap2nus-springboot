package com.example.client;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "readygo")
public interface ReadyGoClient {


	   
    /**
     * @param userId
     * @return
     * 根据用户id获取内容
     */
    @RequestMapping(value = "/trainer/getUserInfoByUserId", method = RequestMethod.GET)
    Map<String , Object> getUserInfoByUserId(@RequestParam(value="userId")String userId);
    
    
    
    
    /**
     * @param userId
     * @param age
     * @param gender
     * @param height
     * @param weight
     * @return
     * 更新个人信息
     */
    @RequestMapping(value = "/trainer/updateUser" , method = RequestMethod.POST)
    Map<String , Object> updateUser(@RequestParam(value = "userId" , required = false) Integer userId ,
                                           @RequestParam(value = "age" , required = false) String age ,
                                           @RequestParam(value = "gender" , required = false) Integer gender ,
                                           @RequestParam(value = "height" , required = false) String height ,
                                           @RequestParam(value = "weight" , required = false) String weight
                                           );
    
    
    /**
     * @param userId
     * @return
     * 获取报名赛事
     */
    @RequestMapping(value = "/trainer/queryReportMatchList" , method = RequestMethod.GET)
    Map<String , Object> queryReportMatchList(@RequestParam(value = "userId") String userId);

    

    /**
     * @param userId
     * @param matchId
     * @return
     * 获取赛事是数据
     */
    @RequestMapping(value = "/trainer/queryReportMatchDetail" , method = RequestMethod.GET)
    public Map<String , Object> queryReportMatchDetail(@RequestParam(value = "userId") String userId ,
                                                       @RequestParam(value = "matchId") String matchId
                                                       );
}
