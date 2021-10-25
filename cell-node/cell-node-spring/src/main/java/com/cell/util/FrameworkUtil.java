package com.cell.util;

import com.cell.bridge.ISpringNodeExtension;
import com.cell.utils.ClassUtil;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-26 05:09
 */
public class FrameworkUtil
{
    public static boolean checkIsExtension(Class c){
        return ISpringNodeExtension.class.isAssignableFrom(c) && !ClassUtil.checkIsAbstract(c);
    }

}
