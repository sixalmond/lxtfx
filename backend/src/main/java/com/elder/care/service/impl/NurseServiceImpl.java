package com.elder.care.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elder.care.entity.Evaluation;
import com.elder.care.entity.Nurse;
import com.elder.care.entity.SysDict;
import com.elder.care.mapper.EvaluationMapper;
import com.elder.care.mapper.NurseMapper;
import com.elder.care.service.NurseService;
import com.elder.care.service.SysDictService;
import com.elder.care.vo.EvaluationVO;
import com.elder.care.vo.NurseDetailVO;
import com.elder.care.vo.NurseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NurseServiceImpl extends ServiceImpl<NurseMapper, Nurse> implements NurseService {

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private EvaluationMapper evaluationMapper;

    @Override
    public List<NurseVO> listAvailableNurses() {
        List<Nurse> nurses = baseMapper.listAvailableNurses();
        return nurses.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public NurseDetailVO getNurseDetail(Long id) {
        Nurse nurse = baseMapper.getNurseById(id);
        if (nurse == null) {
            throw new RuntimeException("护工不存在");
        }
        NurseDetailVO vo = BeanUtil.copyProperties(nurse, NurseDetailVO.class);
        vo.setGradeName(sysDictService.getDictName("nurse_grade", String.valueOf(nurse.getGrade())));
        vo.setStatusName(sysDictService.getDictName("nurse_status", String.valueOf(nurse.getStatus())));
        
        List<Evaluation> evaluations = evaluationMapper.getEvaluationsByNurseId(id);
        vo.setEvaluations(evaluations.stream().map(evaluation -> {
            EvaluationVO evaluationVO = new EvaluationVO();
            evaluationVO.setId(evaluation.getId());
            evaluationVO.setOrderId(evaluation.getOrderId());
            evaluationVO.setScore(evaluation.getScore());
            evaluationVO.setEvaluateContent(evaluation.getEvaluateContent());
            evaluationVO.setEvaluateTime(evaluation.getCreateTime() != null ? evaluation.getCreateTime().toString() : null);
            evaluationVO.setUserName(evaluation.getUserName());
            return evaluationVO;
        }).collect(Collectors.toList()));
        
        return vo;
    }

    @Override
    public IPage<NurseVO> pageNurses(Page<Nurse> page, Integer status) {
        IPage<Nurse> nursePage = baseMapper.pageNurses(page, status);
        Page<NurseVO> voPage = new Page<>(nursePage.getCurrent(), nursePage.getSize(), nursePage.getTotal());
        voPage.setRecords(nursePage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public void updateNurseStatus(Long id, Integer status) {
        Nurse nurse = baseMapper.selectById(id);
        if (nurse == null) {
            throw new RuntimeException("护工不存在");
        }
        nurse.setStatus(status);
        baseMapper.updateById(nurse);
    }

    @Override
    public void updateNurseGrade(Long id, Integer grade) {
        Nurse nurse = baseMapper.selectById(id);
        if (nurse == null) {
            throw new RuntimeException("护工不存在");
        }
        nurse.setGrade(grade);
        baseMapper.updateById(nurse);
    }

    private NurseVO convertToVO(Nurse nurse) {
        NurseVO vo = BeanUtil.copyProperties(nurse, NurseVO.class);
        vo.setGradeName(sysDictService.getDictName("nurse_grade", String.valueOf(nurse.getGrade())));
        vo.setStatusName(sysDictService.getDictName("nurse_status", String.valueOf(nurse.getStatus())));
        return vo;
    }
}
