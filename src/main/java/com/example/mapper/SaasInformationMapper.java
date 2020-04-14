package com.example.mapper;



import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.example.model.ChangePassWordBean;


//@Mapper
public interface SaasInformationMapper {


	@Delete("delete from T_SAASINFORMATION")
	 void deleteSaasInfomation();
	
	@Insert("insert into T_SAASINFORMATION(DOMAIN,USER_NAME,PASS_WORD) values(#{domain}, #{userName}, #{passWord})")
	int insertSysRole(ChangePassWordBean changePassWordBean);
	
	@Select("select * from T_SAASINFORMATION where rownum <=1")
	 @Results(id = "infomationMap",value = {
	    		@Result(id = true,property = "domain",column = "DOMAIN"),
	    		@Result(property = "userName",column = "USER_NAME"),
	    		@Result(property = "passWord",column = "PASS_WORD")
	    })
	 public ChangePassWordBean getSaasInfomation();
}
