package com.elder.care.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elder.care.dto.EvaluationDTO;
import com.elder.care.entity.Evaluation;
import com.elder.care.vo.EvaluationVO;

public interface EvaluationService extends IService<Evaluation> {

    void createEvaluation(EvaluationDTO dto);

    Evaluation getByOrderId(Long orderId);

    IPage<EvaluationVO> pageEvaluationsByNurseId(Page<Evaluation> page, Long nurseId);
}
