package com.cell.component.mybatis.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


public class CellBaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements ICellBaseService<T>
{
}
