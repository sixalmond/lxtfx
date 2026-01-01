package com.elder.care.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elder.care.common.Result;
import com.elder.care.dto.EvaluationDTO;
import com.elder.care.entity.Evaluation;
import com.elder.care.service.EvaluationService;
import com.elder.care.vo.EvaluationVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evaluation")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @PostMapping
    public Result<Void> createEvaluation(@RequestBody EvaluationDTO dto) {
        try {
            evaluationService.createEvaluation(dto);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/order/{orderId}")
    public Result<Evaluation> getByOrderId(@PathVariable Long orderId) {
        try {
            Evaluation evaluation = evaluationService.getByOrderId(orderId);
            return Result.success(evaluation);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my")
    public Result<IPage<EvaluationVO>> getMyEvaluations(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        try {
            Long nurseId = (Long) request.getAttribute("userId");
            IPage<EvaluationVO> page = evaluationService.pageEvaluationsByNurseId(new Page<>(current, size), nurseId);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
