package com.cell.base.common.validators;


import com.cell.base.common.exceptions.ValidateException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-07-09 21:10
 */
public interface IValidator
{
    void valid() throws ValidateException;

}
