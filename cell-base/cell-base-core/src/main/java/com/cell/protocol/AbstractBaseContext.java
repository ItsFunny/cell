package com.cell.protocol;

import com.cell.concurrent.DummyExecutor;
import com.cell.concurrent.promise.BaseDefaultPromise;
import com.cell.concurrent.promise.BaseFutureListener;
import com.cell.concurrent.promise.BasePromise;
import com.cell.log.LOG;
import com.cell.models.Module;
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
    private BasePromise promise;
    protected ICommandReactor reactor;

    private String ip;


    public AbstractBaseContext()
    {
        this.requestTimestamp = System.currentTimeMillis();
        this.promise = new BaseDefaultPromise(DummyExecutor.getInstance());
        promise.addListener((BaseFutureListener) future ->
        {
            if (!future.isSuccess())
            {
                LOG.error(Module.HTTP_FRAMEWORK, future.cause(), "sequenceId = {}, send response  fail", getSequenceId());
                discard();
            }
        });
    }
}
