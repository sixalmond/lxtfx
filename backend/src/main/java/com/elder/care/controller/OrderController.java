package com.elder.care.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elder.care.common.Result;
import com.elder.care.dto.CreateOrderDTO;
import com.elder.care.service.OrderService;
import com.elder.care.vo.OrderVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Result<OrderVO> createOrder(
            @RequestBody CreateOrderDTO dto,
            HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            OrderVO order = orderService.createOrder(userId, dto);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id) {
        try {
            OrderVO order = orderService.getOrderDetail(id);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my")
    public Result<IPage<OrderVO>> getMyOrders(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            IPage<OrderVO> page = orderService.pageUserOrders(new Page<>(current, size), userId);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/nurse")
    public Result<IPage<OrderVO>> getNurseOrders(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        try {
            Long nurseId = (Long) request.getAttribute("userId");
            IPage<OrderVO> page = orderService.pageNurseOrders(new Page<>(current, size), nurseId);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/pending")
    public Result<IPage<OrderVO>> getPendingOrders(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        try {
            Long nurseId = (Long) request.getAttribute("userId");
            IPage<OrderVO> page = orderService.pagePendingOrders(new Page<>(current, size), nurseId);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/page")
    public Result<IPage<OrderVO>> pageByStatus(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {
        try {
            IPage<OrderVO> page = orderService.pageByStatus(new Page<>(current, size), status);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/accept")
    public Result<Void> acceptOrder(@PathVariable Long id) {
        try {
            orderService.acceptOrder(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/reject")
    public Result<Void> rejectOrder(@PathVariable Long id) {
        try {
            orderService.rejectOrder(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/start")
    public Result<Void> startService(@PathVariable Long id) {
        try {
            orderService.startService(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/complete")
    public Result<Void> completeOrder(@PathVariable Long id) {
        try {
            orderService.completeOrder(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancel")
    public Result<Void> cancelOrder(@PathVariable Long id) {
        try {
            orderService.cancelOrder(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/complaint")
    public Result<Void> complaintOrder(
            @PathVariable Long id,
            @RequestBody Map<String, String> params) {
        try {
            orderService.complaintOrder(id, params.get("content"));
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/complaints")
    public Result<IPage<OrderVO>> getComplaintOrders(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            IPage<OrderVO> page = orderService.pageComplaintOrders(new Page<>(current, size));
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/handle-complaint")
    public Result<Void> handleComplaint(
            @PathVariable Long id,
            @RequestBody Map<String, String> params) {
        try {
            orderService.handleComplaint(id, params.get("result"));
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
