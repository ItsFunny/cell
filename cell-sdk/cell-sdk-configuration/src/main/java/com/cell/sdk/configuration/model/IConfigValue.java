package com.cell.sdk.configuration.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

public interface IConfigValue
{
    public IConfigValue getArrayObject(int index);

    public Set<String> getObjectKeys();

    public IConfigValue getObject(String key);

    public String getModuleName();

    public List<IConfigValue> asValueList();

    public <T> T asObject(Class<T> clazz);

    public boolean asBoolean();

    public byte asByte();

    public short asShort();

    public int asInt();

    public long asLong();

    public float asFloat();

    public double asDouble();

    public BigDecimal asBigDecimal();

    public BigInteger asBigInteger();

    public byte[] asBytes();

    public char asChar();

    public String asString();
}
