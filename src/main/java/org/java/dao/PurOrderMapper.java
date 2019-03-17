package org.java.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;


@Mapper
public interface PurOrderMapper {

    public void createOrder(Map m);



    /**
     * 根据业务主键，查询对应的业务数据
     * @param id
     * @return
     */
    public Map<String,Object> findByBusinessKey(@Param("id") String id);

}
