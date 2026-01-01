package com.elder.care.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elder.care.common.Result;
import com.elder.care.service.NurseService;
import com.elder.care.vo.NurseDetailVO;
import com.elder.care.vo.NurseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nurse")
public class NurseController {

    @Autowired
    private NurseService nurseService;

    @GetMapping("/available")
    public Result<List<NurseVO>> listAvailableNurses() {
        try {
            List<NurseVO> nurses = nurseService.listAvailableNurses();
            return Result.success(nurses);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<NurseDetailVO> getNurseDetail(@PathVariable Long id) {
        try {
            NurseDetailVO nurse = nurseService.getNurseDetail(id);
            return Result.success(nurse);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/page")
    public Result<IPage<NurseVO>> pageNurses(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {
        try {
            IPage<NurseVO> page = nurseService.pageNurses(new Page<>(current, size), status);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateNurseStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        try {
            nurseService.updateNurseStatus(id, status);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/grade")
    public Result<Void> updateNurseGrade(
            @PathVariable Long id,
            @RequestParam Integer grade) {
        try {
            nurseService.updateNurseGrade(id, grade);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
