package com.elder.care.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elder.care.entity.SysDict;
import com.elder.care.mapper.SysDictMapper;
import com.elder.care.service.SysDictService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    @Override
    public List<SysDict> listByDictType(String dictType) {
        return baseMapper.listByDictType(dictType);
    }

    @Override
    public String getDictName(String dictType, String dictCode) {
        return baseMapper.getDictName(dictType, dictCode);
    }
}
