package com.mycompany.cloud.service.test;

import com.mycompany.cloud.domain.test.NewsMb;
import org.apache.ibatis.annotations.*;
import org.springframework.dao.DataAccessException;


/**
 * @author peter
 * @version V1.0 创建时间：17/12/6
 *          Copyright 2017 by PreTang
 */
@Mapper
public interface TestNewsMybatisDao {

    @Select("select id,content from t_pro_news where id=#{id}")
    public NewsMb queryNewsMb(Integer id)  throws DataAccessException;


    /**
     * 根据楼盘名称，查询消息信息
     * @param buildingName 楼盘名称名
     */
    @Select("SELECT * FROM t_pro_news")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "content", column = "content"),
            @Result(property = "buildingName", column = "buildingName"),
    })
    public NewsMb findByName(@Param("buildingName") String buildingName);


}
