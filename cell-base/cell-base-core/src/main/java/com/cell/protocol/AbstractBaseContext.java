package com.cell.protocol;

import com.cell.constants.ContextConstants;
import com.cell.reactor.ICommandReactor;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 11:33
 */
@Data
public abstract class AbstractBaseContext implements IBuzzContext
{
    protected String sequenceId;
    protected long requestTimestamp;
    protected ICommandReactor reactor;

    private String ip;


    public AbstractBaseContext()
    {
        this.requestTimestamp = System.currentTimeMillis();
//        promise.addListener((BaseFutureListener) future ->
//        {
//            if (!future.isSuccess())
//            {
//                LOG.error(Module.HTTP_FRAMEWORK, future.cause(), "sequenceId = {}, send response  fail", getSequenceId());
//                discard();
//            }
//        });
    }
    protected boolean timeout(long status)
    {
        return (status & ContextConstants.TIMEOUT) >= ContextConstants.TIMEOUT;
    }

    protected boolean programError(long status)
    {
        return (status & ContextConstants.PROGRAMA_ERROR) >= ContextConstants.PROGRAMA_ERROR;
    }

}
