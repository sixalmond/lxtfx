package com.elder.care.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elder.care.entity.OrderInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderMapper extends BaseMapper<OrderInfo> {

    @Select("SELECT o.*, u.real_name as user_name, n.name as nurse_name, n.price as nurse_price, n.grade as nurse_grade " +
            "FROM order_info o " +
            "LEFT JOIN user u ON o.user_id = u.id " +
            "LEFT JOIN nurse n ON o.nurse_id = n.id " +
            "WHERE o.id = #{id}")
    OrderInfo getOrderDetail(@Param("id") Long id);

    @Select("SELECT o.*, u.real_name as user_name, n.name as nurse_name, n.price as nurse_price, n.grade as nurse_grade " +
            "FROM order_info o " +
            "LEFT JOIN user u ON o.user_id = u.id " +
            "LEFT JOIN nurse n ON o.nurse_id = n.id " +
            "WHERE o.user_id = #{userId} " +
            "ORDER BY o.create_time DESC")
    IPage<OrderInfo> pageByUserId(Page<OrderInfo> page, @Param("userId") Long userId);

    @Select("SELECT o.*, u.real_name as user_name, n.name as nurse_name, n.price as nurse_price, n.grade as nurse_grade " +
            "FROM order_info o " +
            "LEFT JOIN user u ON o.user_id = u.id " +
            "LEFT JOIN nurse n ON o.nurse_id = n.id " +
            "WHERE o.nurse_id = #{nurseId} AND o.order_status != 0 " +
            "ORDER BY o.create_time DESC")
    IPage<OrderInfo> pageByNurseId(Page<OrderInfo> page, @Param("nurseId") Long nurseId);

    @Select("<script>" +
            "SELECT o.*, u.real_name as user_name, n.name as nurse_name, n.price as nurse_price, n.grade as nurse_grade " +
            "FROM order_info o " +
            "LEFT JOIN user u ON o.user_id = u.id " +
            "LEFT JOIN nurse n ON o.nurse_id = n.id " +
            "<where>" +
            "<if test='status != null'>AND o.order_status = #{status}</if>" +
            "</where>" +
            "ORDER BY o.create_time DESC" +
            "</script>")
    IPage<OrderInfo> pageByStatus(Page<OrderInfo> page, @Param("status") Integer status);

    @Select("SELECT o.*, u.real_name as user_name, n.name as nurse_name, n.price as nurse_price, n.grade as nurse_grade " +
            "FROM order_info o " +
            "LEFT JOIN user u ON o.user_id = u.id " +
            "LEFT JOIN nurse n ON o.nurse_id = n.id " +
            "WHERE o.order_status = 0 AND o.nurse_id = #{nurseId} " +
            "ORDER BY o.create_time DESC")
    IPage<OrderInfo> pagePendingOrders(Page<OrderInfo> page, @Param("nurseId") Long nurseId);

    @Select("SELECT o.*, u.real_name as user_name, n.name as nurse_name, n.price as nurse_price, n.grade as nurse_grade " +
            "FROM order_info o " +
            "LEFT JOIN user u ON o.user_id = u.id " +
            "LEFT JOIN nurse n ON o.nurse_id = n.id " +
            "WHERE o.order_status = 4 " +
            "ORDER BY o.create_time DESC")
    IPage<OrderInfo> pageComplaintOrders(Page<OrderInfo> page);
}
