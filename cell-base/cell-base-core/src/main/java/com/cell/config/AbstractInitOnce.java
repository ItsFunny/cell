package com.cell.config;

import com.cell.context.InitCTX;
import lombok.Data;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-10-20 23:13
 */
@Data
public abstract class AbstractInitOnce implements IInitOnce
{
    protected AtomicBoolean init = new AtomicBoolean();


    public void initOnce(InitCTX ctx)
    {
        // FIXME
        if (this.init.get())
        {
            return;
        }
        try
        {
            if (this.init.compareAndSet(false, true))
            {
                // FIXME CTX SHOULD HAVE STAGE
                if (null==ctx){
                    ctx=new InitCTX();
                }else{

                }
                this.onInit(ctx);
            }
        } catch (Exception e)
        {
            this.init.compareAndSet(true, false);
            throw e;
        }
    }

    protected abstract void onInit(InitCTX ctx);

    public void refresh()
    {
        this.init.compareAndSet(false, true);
    }

}
