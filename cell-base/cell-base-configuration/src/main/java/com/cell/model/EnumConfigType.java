package com.cell.model;

import com.cell.exceptions.DuplicatedEnumIdException;

import java.util.HashMap;
import java.util.Map;

public enum EnumConfigType {
	JSON((short) 1,"json"),
	XML((short) 2,"mapping"),

	INVALID((short) -1, "Invalid enum log type");
	
	
	private short value;
	private String desc;
	private EnumConfigType(short value, String desc){
		this.value = value;
		this.desc = desc;
	}
	public short getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
	
	private final static Map<Short, EnumConfigType> ENUM_MAP = new HashMap<>();

	static {
		registerErrorEnum(EnumConfigType.values());
	}
	
	public static EnumConfigType getType(short value) {
		
		EnumConfigType enm = ENUM_MAP.get(value);
		if(enm == null){
			enm = EnumConfigType.INVALID;
		}
		return enm;
	}
	
	public static void registerErrorEnum(EnumConfigType[] enums) {
		if (enums != null) {
			for (EnumConfigType enm : enums) {
				short key = enm.getValue();
				EnumConfigType old = ENUM_MAP.put(key, enm);
				if(old != null) {
					throw new DuplicatedEnumIdException("重复的Config type:" + old.name());
				}
			}
		}
	}
}
