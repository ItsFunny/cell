package com.cell.comparators;

import cn.bidsun.common.annotation.Order;
import cn.bidsun.common.model.Constants;
import com.cell.annotations.CellOrder;
import com.cell.constants.Constants;

import java.util.Comparator;

public class OrderComparator implements Comparator<Class<?>>
{

	@Override
	public int compare(Class<?> o1, Class<?> o2) {
		CellOrder anno1 = o1.getAnnotation(CellOrder.class);
		CellOrder anno2 = o2.getAnnotation(CellOrder.class);
		int value1 = anno1 != null ? anno1.value() : Constants.DEFAULT_ORDER;
		int value2 = anno2 != null ? anno2.value() : Constants.DEFAULT_ORDER;
		return value1 > value2 ? 1 : (value1 < value2 ? -1 : 0);
	}
	
}
