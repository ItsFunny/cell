package com.cell.manager;

import com.cell.services.GroupFul;
import com.cell.services.TypeFul;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-14 05:41
 */
public interface IManager<TYPE, GROUP> extends TypeFul<TYPE>, GroupFul<GROUP>
{

}
