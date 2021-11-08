package com.cell.base.common.decorators;

import com.cell.base.common.enums.TypeEnums;
import com.cell.base.common.models.Couple;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-07 17:49
 */
public abstract class DefaultTypeStateful implements TypeStateful<TypeEnums>
{
    private long status;

    @Override
    public Long getState()
    {
        return this.status;
    }

    public void setStatus(long status)
    {
        this.status = status;
    }


    // Thread Safe, 读暂时没用到这个,不用atomicInteg因为  & |  ,方便我做以后的分类,如NODE_DOWN和timeout是2种状态
    public Couple<Long, Boolean> doubleSyncCheckSetStatus(long excepted, long minCompare, long newValue)
    {
//        DebugUtil.info("当前状态为:" + this.status + ",excepted=" + excepted + ",minCompare=" + minCompare + "newValue=" + newValue);
        if ((this.status & excepted) >= minCompare)
        {
            synchronized (this)
            {
                if ((this.status & excepted) >= minCompare)
                {
                    Long prevStatus = this.status;
                    this.status = newValue;
                    return new Couple<>(prevStatus, true);
                }
            }
        }
        return new Couple<>(this.status, false);
    }
}
