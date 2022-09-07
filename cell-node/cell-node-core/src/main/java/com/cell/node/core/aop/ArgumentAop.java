package com.cell.node.core.aop;

import com.cell.base.common.models.Module;
import com.cell.node.core.context.CellContext;
import com.cell.node.core.valid.IValidBasic;
import com.cell.sdk.log.LOG;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ArgumentAop
{
    @Pointcut("@annotation(com.cell.node.core.aop.ArgumentAnnotation)")
    public void argumentCheck() {}


    @Before("argumentCheck()")
    public void doArgumentCheck(JoinPoint joinPoint)
    {
        Object[] args = joinPoint.getArgs();
        if (args == null)
        {
            return;
        }
        CellContext context = null;
        IValidBasic check = null;
        for (Object arg : args)
        {
            if (arg instanceof CellContext)
            {
                context = (CellContext) arg;
                if (context.skipCheckArgument())
                {
                    return;
                }
            } else if (arg instanceof IValidBasic)
            {
                check = (IValidBasic) arg;
            }
        }
        if (context != null && check != null)
        {
            try
            {
                check.validBasic(context);
            } catch (IllegalArgumentException e)
            {
                LOG.error(Module.AOP, e, "context:{},argument is illegal:{}", context, check);
                throw e;
            }
        }
//        if (context != null)
//        {
//            context.or(Context.FLAG_SKIP_CHECK_ARGUMENT);
//        }
    }

}
