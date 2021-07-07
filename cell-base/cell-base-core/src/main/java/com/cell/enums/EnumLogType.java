package com.cell.enums;


import com.cell.exceptions.DuplicatedEnumIdException;

import java.util.HashMap;
import java.util.Map;


public enum EnumLogType
{

	
	ILLEGAL_USER_ACTION((long) 1,"illegalUser"),
	MAYBE_ILLEGAL_USER_ACTION((long) 2, "有可能是非法的用户操作"),
	ILLEGAL_SERVICE_COMMUNICATION((long) 3,"服务器间交互错误"),
	SERVICE_ALARM_EXCEPTION((long) 4, "服务器内部严重问题"),
	INSIDE_BUG_EXCEPTION((long) 5, "服务内部bug"),
	MONEY((long) 6, "货币相关普通log"),
	MONEY_EXCEPTION((long) 7, "货币相关异常"),
	UNKNOW_EXCEPTION((long) 8, "遗漏的或者未知的异常"),
	RESOURCE_SERVICE_DEBUG((long) 9,"资源服务器debug日志"),
	TRANSACTION((long) 10,"事务日志"),
	NODE_NOTICE((long) 11, "服务器节点 需要关注log"),
	SERVER_EFFICIENCY_DATA_RECORD((long) 22, "服务记录服务效率的数据"),
	EVENT_BOLT((long) 100, "event bolt"),
	EVENT_SPOUT((long) 101, "event spout"),
	TRANSACTION_BOLT((long) 102, "transaction bolt"),
	TRANSACTION_BOLT_EVENT_TRACE((long) 110, "transaction bolt event trace"),
	ALERT_COMPONENT((long) 201, "alarm component"),
	TRANSACTION_SPOUT((long) 103, "transaction spout"),
	ROOM_DB_DUMP((long) 200, "room data engine dump"),
	EVENT_LOGGER((long) 800, "event logger"),
	USER_COMMAND_TRACE((long) 900, "User command"),
	RPC_COMMAND_TRACE((long) 901, "RPC command"),
	EVENT_TRACE((long) 902, "Event trace"),
	NOTIFICATION_TRACE((long) 903, "Notification trace"),
	GC_LOG((long) 998, "gc"),
	GHOST((long) 999, "不会被log系统收集的log"),
	LOG_INTERAL((long) 1000, "log系统内部的log"),
	THIRD_PARTY((long) 1001, "第三方模块"),
	RPC_TEST((long) 1002, "rpc test"),
	NORMAL((long) 0, "普通的log消息"),
	INVALID((long) -1, "Invalid enum log type");
	
	
	private long logType;
	private String desc;
	private EnumLogType(long logType, String desc){
		this.logType = logType;
		this.desc = desc;
	}
	public long getValue() {
		return logType;
	}

	public String getDesc() {
		return desc;
	}
	
	private final static Map<Long, EnumLogType> ENUM_MAP = new HashMap<>();

	static {
		registerErrorEnum(EnumLogType.values());
	}
	
	public static EnumLogType getLogType(short logType) {
		
		EnumLogType enm = ENUM_MAP.get(logType);
		if(enm == null){
			enm = EnumLogType.INVALID;
		}
		return enm;
	}
	
	public static void registerErrorEnum(EnumLogType[] enums) {
		if (enums != null) {
			for (EnumLogType enm : enums) {
				long key = enm.getValue();
				EnumLogType old = ENUM_MAP.put(key, enm);
				if(old != null) {
					throw new DuplicatedEnumIdException("重复的Log type:" + old.name());
				}
			}
		}
	}
}
