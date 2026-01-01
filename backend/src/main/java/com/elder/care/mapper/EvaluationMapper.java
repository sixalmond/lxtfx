package com.elder.care.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elder.care.entity.Evaluation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EvaluationMapper extends BaseMapper<Evaluation> {

    @Select("SELECT * FROM evaluation WHERE order_id = #{orderId}")
    Evaluation getByOrderId(@Param("orderId") Long orderId);

    @Select("<script>" +
            "SELECT * FROM evaluation WHERE order_id IN " +
            "<foreach collection='orderIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<Evaluation> getByOrderIds(@Param("orderIds") List<Long> orderIds);

    @Select("SELECT e.*, u.real_name as user_name, o.user_id " +
            "FROM evaluation e " +
            "LEFT JOIN order_info o ON e.order_id = o.id " +
            "LEFT JOIN user u ON o.user_id = u.id " +
            "WHERE o.nurse_id = #{nurseId} " +
            "ORDER BY e.create_time DESC")
    List<Evaluation> getEvaluationsByNurseId(@Param("nurseId") Long nurseId);

    @Select("SELECT e.*, u.real_name as user_name, o.user_id " +
            "FROM evaluation e " +
            "LEFT JOIN order_info o ON e.order_id = o.id " +
            "LEFT JOIN user u ON o.user_id = u.id " +
            "WHERE o.nurse_id = #{nurseId} " +
            "ORDER BY e.create_time DESC")
    IPage<Evaluation> pageEvaluationsByNurseId(Page<Evaluation> page, @Param("nurseId") Long nurseId);
}
