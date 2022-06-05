package com.cell.extension.mybatis.config;

import jdk.jfr.DataAmount;

import java.util.List;

@DataAmount
public class MysqlSummary
{
	private List<String> mysqlDbList;

	public List<String> getMysqlDbList() {
		return mysqlDbList;
	}

	public void setMysqlDbList(List<String> mysqlDbList) {
		this.mysqlDbList = mysqlDbList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MysqlSummary [mysqlDbList=");
		builder.append(mysqlDbList);
		builder.append("]");
		return builder.toString();
	}
}
