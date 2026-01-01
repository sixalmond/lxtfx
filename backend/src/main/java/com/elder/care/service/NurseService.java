package com.elder.care.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elder.care.entity.Nurse;
import com.elder.care.vo.NurseDetailVO;
import com.elder.care.vo.NurseVO;

import java.util.List;

public interface NurseService extends IService<Nurse> {

    List<NurseVO> listAvailableNurses();

    NurseDetailVO getNurseDetail(Long id);

    IPage<NurseVO> pageNurses(Page<Nurse> page, Integer status);

    void updateNurseStatus(Long id, Integer status);

    void updateNurseGrade(Long id, Integer grade);
}
