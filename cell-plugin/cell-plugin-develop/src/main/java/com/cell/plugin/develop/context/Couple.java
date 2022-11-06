package com.cell.plugin.develop.context;

public class Couple<V1, V2>
{
    private V1 v1;
    private V2 v2;
    private String msg;

    public Couple()
    {
        super();
    }

    public Couple(V1 v1)
    {
        super();
        this.v1 = v1;
    }

    public Couple(V1 v1, V2 v2)
    {
        super();
        this.v1 = v1;
        this.v2 = v2;
    }

    public Couple(V1 v1, V2 v2, String msg)
    {
        super();
        this.v1 = v1;
        this.v2 = v2;
        this.msg = msg;
    }

    public V1 getV1()
    {
        return v1;
    }

    public void setV1(V1 v1)
    {
        this.v1 = v1;
    }

    public V2 getV2()
    {
        return v2;
    }

    public void setV2(V2 v2)
    {
        this.v2 = v2;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Couple{");
        sb.append("v1=").append(v1);
        sb.append(", v2=").append(v2);
        sb.append(", msg='").append(msg).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
