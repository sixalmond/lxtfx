package com.elder.care.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elder.care.dto.EvaluationDTO;
import com.elder.care.entity.Evaluation;
import com.elder.care.entity.OrderInfo;
import com.elder.care.mapper.EvaluationMapper;
import com.elder.care.mapper.OrderMapper;
import com.elder.care.service.EvaluationService;
import com.elder.care.vo.EvaluationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EvaluationServiceImpl extends ServiceImpl<EvaluationMapper, Evaluation> implements EvaluationService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void createEvaluation(EvaluationDTO dto) {
        OrderInfo order = orderMapper.selectById(dto.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (order.getOrderStatus() != 2) {
            throw new RuntimeException("只有已完成的订单才能评价");
        }

        Evaluation exist = baseMapper.getByOrderId(dto.getOrderId());
        if (exist != null) {
            throw new RuntimeException("该订单已评价");
        }

        Evaluation evaluation = new Evaluation();
        evaluation.setOrderId(dto.getOrderId());
        evaluation.setScore(dto.getScore());
        evaluation.setEvaluateContent(dto.getEvaluateContent());
        evaluation.setEvaluateTime(LocalDateTime.now());

        baseMapper.insert(evaluation);
    }

    @Override
    public Evaluation getByOrderId(Long orderId) {
        return baseMapper.getByOrderId(orderId);
    }

    @Override
    public IPage<EvaluationVO> pageEvaluationsByNurseId(Page<Evaluation> page, Long nurseId) {
        IPage<Evaluation> evaluationPage = baseMapper.pageEvaluationsByNurseId(page, nurseId);
        Page<EvaluationVO> voPage = new Page<>(evaluationPage.getCurrent(), evaluationPage.getSize(), evaluationPage.getTotal());
        voPage.setRecords(evaluationPage.getRecords().stream().map(evaluation -> {
            EvaluationVO vo = BeanUtil.copyProperties(evaluation, EvaluationVO.class);
            vo.setUserName(evaluation.getUserName());
            if (evaluation.getEvaluateTime() != null) {
                vo.setEvaluateTime(DateUtil.format(evaluation.getEvaluateTime(), "yyyy-MM-dd HH:mm:ss"));
            }
            return vo;
        }).collect(java.util.stream.Collectors.toList()));
        return voPage;
    }
}
