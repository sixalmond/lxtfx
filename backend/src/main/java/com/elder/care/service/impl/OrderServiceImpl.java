package com.elder.care.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elder.care.dto.CreateOrderDTO;
import com.elder.care.entity.Evaluation;
import com.elder.care.entity.Nurse;
import com.elder.care.entity.OrderInfo;
import com.elder.care.entity.SysDict;
import com.elder.care.mapper.EvaluationMapper;
import com.elder.care.mapper.NurseMapper;
import com.elder.care.mapper.OrderMapper;
import com.elder.care.service.NurseService;
import com.elder.care.service.OrderService;
import com.elder.care.service.SysDictService;
import com.elder.care.vo.EvaluationVO;
import com.elder.care.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderInfo> implements OrderService {

    @Autowired
    private NurseMapper nurseMapper;

    @Autowired
    private NurseService nurseService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private EvaluationMapper evaluationMapper;

    @Override
    @Transactional
    public OrderVO createOrder(Long userId, CreateOrderDTO dto) {
        Nurse nurse = nurseMapper.selectById(dto.getNurseId());
        if (nurse == null) {
            throw new RuntimeException("护工不存在");
        }

        if (nurse.getStatus() != 0) {
            throw new RuntimeException("护工当前不可接单");
        }

        OrderInfo order = new OrderInfo();
        order.setUserId(userId);
        order.setNurseId(dto.getNurseId());
        order.setOrderStatus(0);
        order.setOrderTime(LocalDateTime.now());

        baseMapper.insert(order);
        return convertToVO(order);
    }

    @Override
    @Transactional
    public void acceptOrder(Long orderId) {
        OrderInfo order = baseMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (order.getOrderStatus() != 0) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setOrderStatus(1);
        order.setConfirmTime(LocalDateTime.now());
        baseMapper.updateById(order);

        nurseService.updateNurseStatus(order.getNurseId(), 1);
    }

    @Override
    @Transactional
    public void rejectOrder(Long orderId) {
        OrderInfo order = baseMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (order.getOrderStatus() != 0) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setOrderStatus(3);
        baseMapper.updateById(order);
    }

    @Override
    @Transactional
    public void startService(Long orderId) {
        OrderInfo order = baseMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (order.getOrderStatus() != 1) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setOrderStatus(5);
        order.setServiceStartTime(LocalDateTime.now());
        baseMapper.updateById(order);
    }

    @Override
    @Transactional
    public void completeOrder(Long orderId) {
        OrderInfo order = baseMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (order.getOrderStatus() != 5) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setOrderStatus(2);
        order.setServiceEndTime(LocalDateTime.now());
        baseMapper.updateById(order);

        nurseService.updateNurseStatus(order.getNurseId(), 0);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        OrderInfo order = baseMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (order.getOrderStatus() == 2 || order.getOrderStatus() == 5) {
            throw new RuntimeException("已完成或进行中的订单不能取消");
        }

        Integer oldStatus = order.getOrderStatus();
        order.setOrderStatus(3);
        baseMapper.updateById(order);

        if (oldStatus == 1) {
            nurseService.updateNurseStatus(order.getNurseId(), 0);
        }
    }

    @Override
    @Transactional
    public void complaintOrder(Long orderId, String content) {
        OrderInfo order = baseMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (order.getOrderStatus() != 2) {
            throw new RuntimeException("只有已完成的订单才能投诉");
        }

        order.setOrderStatus(4);
        order.setComplaintContent(content);
        baseMapper.updateById(order);
    }

    @Override
    public OrderVO getOrderDetail(Long id) {
        OrderInfo order = baseMapper.getOrderDetail(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        return convertToVO(order);
    }

    @Override
    public IPage<OrderVO> pageUserOrders(Page<OrderInfo> page, Long userId) {
        IPage<OrderInfo> orderPage = baseMapper.pageByUserId(page, userId);
        Page<OrderVO> voPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        voPage.setRecords(convertToVOList(orderPage.getRecords()));
        return voPage;
    }

    @Override
    public IPage<OrderVO> pageNurseOrders(Page<OrderInfo> page, Long nurseId) {
        IPage<OrderInfo> orderPage = baseMapper.pageByNurseId(page, nurseId);
        Page<OrderVO> voPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        voPage.setRecords(convertToVOList(orderPage.getRecords()));
        return voPage;
    }

    @Override
    public IPage<OrderVO> pageByStatus(Page<OrderInfo> page, Integer status) {
        IPage<OrderInfo> orderPage = baseMapper.pageByStatus(page, status);
        Page<OrderVO> voPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        voPage.setRecords(convertToVOList(orderPage.getRecords()));
        return voPage;
    }

    @Override
    public IPage<OrderVO> pagePendingOrders(Page<OrderInfo> page, Long nurseId) {
        IPage<OrderInfo> orderPage = baseMapper.pagePendingOrders(page, nurseId);
        Page<OrderVO> voPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        voPage.setRecords(convertToVOList(orderPage.getRecords()));
        return voPage;
    }

    @Override
    public IPage<OrderVO> pageComplaintOrders(Page<OrderInfo> page) {
        IPage<OrderInfo> orderPage = baseMapper.pageComplaintOrders(page);
        Page<OrderVO> voPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        voPage.setRecords(convertToVOList(orderPage.getRecords()));
        return voPage;
    }

    @Override
    @Transactional
    public void handleComplaint(Long orderId, String result) {
        OrderInfo order = baseMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (order.getOrderStatus() != 4) {
            throw new RuntimeException("只有投诉中的订单才能处理");
        }

        order.setHandleResult(result);
        order.setOrderStatus(2);
        baseMapper.updateById(order);
    }

    private OrderVO convertToVO(OrderInfo order) {
        OrderVO vo = BeanUtil.copyProperties(order, OrderVO.class);
        vo.setOrderStatusName(sysDictService.getDictName("order_status", String.valueOf(order.getOrderStatus())));

        if (order.getOrderTime() != null) {
            vo.setOrderTime(DateUtil.format(order.getOrderTime(), "yyyy-MM-dd HH:mm:ss"));
        }
        if (order.getConfirmTime() != null) {
            vo.setConfirmTime(DateUtil.format(order.getConfirmTime(), "yyyy-MM-dd HH:mm:ss"));
        }
        if (order.getServiceStartTime() != null) {
            vo.setServiceStartTime(DateUtil.format(order.getServiceStartTime(), "yyyy-MM-dd HH:mm:ss"));
        }
        if (order.getServiceEndTime() != null) {
            vo.setServiceEndTime(DateUtil.format(order.getServiceEndTime(), "yyyy-MM-dd HH:mm:ss"));
        }

        Evaluation evaluation = evaluationMapper.getByOrderId(order.getId());
        vo.setEvaluated(evaluation != null);

        return vo;
    }

    private List<OrderVO> convertToVOList(List<OrderInfo> orders) {
        Map<String, String> statusMap = sysDictService.listByDictType("order_status").stream()
                .collect(Collectors.toMap(d -> d.getDictCode(), SysDict::getDictName));

        Map<Long, Boolean> evaluatedMap;
        if (orders != null && !orders.isEmpty()) {
            List<Long> orderIds = orders.stream().map(OrderInfo::getId).collect(Collectors.toList());
            evaluatedMap = evaluationMapper.getByOrderIds(orderIds).stream()
                    .collect(Collectors.toMap(Evaluation::getOrderId, e -> true, (e1, e2) -> e1));
        } else {
            evaluatedMap = new java.util.HashMap<>();
        }

        return orders.stream().map(order -> {
            OrderVO vo = BeanUtil.copyProperties(order, OrderVO.class);
            vo.setOrderStatusName(statusMap.getOrDefault(String.valueOf(order.getOrderStatus()), ""));

            if (order.getOrderTime() != null) {
                vo.setOrderTime(DateUtil.format(order.getOrderTime(), "yyyy-MM-dd HH:mm:ss"));
            }
            if (order.getConfirmTime() != null) {
                vo.setConfirmTime(DateUtil.format(order.getConfirmTime(), "yyyy-MM-dd HH:mm:ss"));
            }
            if (order.getServiceStartTime() != null) {
                vo.setServiceStartTime(DateUtil.format(order.getServiceStartTime(), "yyyy-MM-dd HH:mm:ss"));
            }
            if (order.getServiceEndTime() != null) {
                vo.setServiceEndTime(DateUtil.format(order.getServiceEndTime(), "yyyy-MM-dd HH:mm:ss"));
            }

            vo.setEvaluated(evaluatedMap.containsKey(order.getId()));

            return vo;
        }).collect(Collectors.toList());
    }
}
