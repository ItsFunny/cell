//package com.cell.hooks;
//
//import com.cell.config.AbstractInitOnce;
//import com.cell.context.InitCTX;
//import com.cell.events.IEvent;
//import com.cell.utils.CollectionUtils;
//import org.omg.CORBA.PRIVATE_MEMBER;
//import reactor.core.publisher.Flux;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author Charlie
// * @When
// * @Description
// * @Detail
// * @Attention:
// * @Date 创建时间：2021-09-13 22:29
// */
//public abstract class AbstractReactHookExchangeFactory<REQ, RESP> extends AbstractInitOnce implements IReactorHookExchangeFactory<REQ, RESP>
//{
//    private List<IReactorHook<REQ, RESP>> hooks;
//
//    @Override
//    public Flux<IReactorHook<REQ, RESP>> create()
//    {
//        Flux<IReactorHook<REQ, RESP>> ret = Flux.fromIterable(this.hooks);
//        return ret;
//    }
//
//    @Override
//    public void register(IReactorHook<REQ, RESP> hook)
//    {
//        if (CollectionUtils.isEmpty(this.hooks))
//        {
//            this.hooks = new ArrayList<>();
//        }
//        this.hooks.add(hook);
//    }
//
//    @Override
//    protected void onInit(InitCTX ctx)
//    {
//    }
//}
