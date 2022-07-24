package com.cell.node.core.aop;

import com.cell.node.core.context.CellContext;
import com.cell.node.core.valid.IValidBasic;
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
            check.validBasic(context);
        }
//        if (context != null)
//        {
//            context.or(Context.FLAG_SKIP_CHECK_ARGUMENT);
//        }
    }

}
