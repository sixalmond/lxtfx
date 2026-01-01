package com.elder.care.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.elder.care.entity.SysDict;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysDictMapper extends BaseMapper<SysDict> {

    @Select("SELECT * FROM sys_dict WHERE dict_type = #{dictType} ORDER BY sort")
    List<SysDict> listByDictType(@Param("dictType") String dictType);

    @Select("SELECT dict_name FROM sys_dict WHERE dict_type = #{dictType} AND dict_code = #{dictCode}")
    String getDictName(@Param("dictType") String dictType, @Param("dictCode") String dictCode);
}
