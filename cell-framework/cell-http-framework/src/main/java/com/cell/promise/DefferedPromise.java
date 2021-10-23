package com.cell.promise;

import com.cell.concurrent.base.BasePromise;
import com.cell.concurrent.base.Promise;
import lombok.Data;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-23 15:00
 */
@Data
public class DefferedPromise extends BasePromise
{
    private Promise<Object> promise;
    private DeferredResult<Object> responseResult;

    public DefferedPromise(Promise<Object> promise, DeferredResult<Object> responseResult)
    {
        this.promise = promise;
        this.responseResult = responseResult;
    }


    public void onTimeout(Runnable c)
    {
        this.responseResult.onCompletion(c);
    }
}
