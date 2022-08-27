package com.cell.base.common.decorators;

import com.cell.base.common.enums.ErrorEnums;
import com.cell.base.common.enums.ErrorInterface;

public interface IDecoratorV2<T>
{
     ErrorInterface decorate(T data);
}
