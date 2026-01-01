package com.elder.care.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elder.care.dto.CreateOrderDTO;
import com.elder.care.entity.OrderInfo;
import com.elder.care.vo.OrderVO;

import java.util.List;

public interface OrderService extends IService<OrderInfo> {

    OrderVO createOrder(Long userId, CreateOrderDTO dto);

    void acceptOrder(Long orderId);

    void rejectOrder(Long orderId);

    void startService(Long orderId);

    void completeOrder(Long orderId);

    void cancelOrder(Long orderId);

    void complaintOrder(Long orderId, String content);

    OrderVO getOrderDetail(Long id);

    IPage<OrderVO> pageUserOrders(Page<OrderInfo> page, Long userId);

    IPage<OrderVO> pageNurseOrders(Page<OrderInfo> page, Long nurseId);

    IPage<OrderVO> pageByStatus(Page<OrderInfo> page, Integer status);

    IPage<OrderVO> pagePendingOrders(Page<OrderInfo> page, Long nurseId);

    IPage<OrderVO> pageComplaintOrders(Page<OrderInfo> page);

    void handleComplaint(Long orderId, String result);
}
