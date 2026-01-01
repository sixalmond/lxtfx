package com.elder.care.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.elder.care.entity.SysDict;

import java.util.List;

public interface SysDictService extends IService<SysDict> {

    List<SysDict> listByDictType(String dictType);

    String getDictName(String dictType, String dictCode);
}
