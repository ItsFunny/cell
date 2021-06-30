package com.cell.refresh;

public interface ILoadConfigListener<T> {

	void load(T obj) throws Exception;
}
