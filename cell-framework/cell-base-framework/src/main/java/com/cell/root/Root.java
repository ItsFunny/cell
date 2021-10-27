package com.cell.root;

import com.cell.annotations.ActivePlugin;
import com.cell.annotations.Command;
import com.cell.annotations.ReactorAnno;
import com.cell.constants.ProtocolConstants;
import com.cell.context.InitCTX;
import com.cell.dispatcher.IDispatcher;
import com.cell.exceptions.ProgramaException;
import com.cell.protocol.ICommand;
import com.cell.reactor.ICommandReactor;
import com.cell.server.IServer;
import com.cell.utils.ClassUtil;
import com.cell.utils.CollectionUtils;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-25 15:05
 */
@Data
@ActivePlugin
public class Root implements ApplicationContextAware
{
    private static final Root instance = new Root();
    private static ApplicationContext context;


    public static Root getInstance()
    {
        return instance;
    }

    private Map<Byte, Set<ICommandReactor>> reactors = new HashMap<>();
    private Map<Class<? extends ICommandReactor>, Set<Class<? extends ICommand>>> reactorCommands = new HashMap<>();

//    private Map<Class<? extends Annotation>, Set<Class<?>>> annotationClasses = new HashMap<>();
//    private Map<Class<? extends Annotation>, Set<Class<?>>> asBeanAnnotation = new HashMap<>();
//    private Map<Class<?>, Object> asBeanInstances = new HashMap<>();

    private Set<IDispatcher> dispatchers = new HashSet<>();

    private Set<IServer> servers = new HashSet<>();

    public void addReactor(ICommandReactor reactor)
    {
        String name = reactor.getClass().getName();
        InitCTX ctx = new InitCTX();
        if (!name.contains("ByteBuddy"))
        {
            Map<String, Object> data = new HashMap<>();
            Set<Class<? extends ICommand>> commds = this.reactorCommands.get(reactor.getClass());
            data.put(ProtocolConstants.INIT_CTX_CMDS, commds);
            ctx.setData(data);
        }
        reactor.initOnce(ctx);
        // TODO
        ReactorAnno anno = (ReactorAnno) ClassUtil.mustGetAnnotation(reactor.getClass(), ReactorAnno.class);
        byte type = anno.type();
        Set<ICommandReactor> iCommandReactors = this.reactors.get(type);
        if (CollectionUtils.isEmpty(iCommandReactors))
        {
            iCommandReactors = new HashSet<>();
            this.reactors.put(type, iCommandReactors);
        }
        boolean contains = iCommandReactors.contains(reactor);
        if (contains && !anno.withForce().forceOverride())
        {
            throw new ProgramaException("duplicate");
        }
        iCommandReactors.add(reactor);
    }


    public void addServer(IServer server)
    {
        this.servers.add(server);
    }

    public void addDispatcher(IDispatcher dispatcher)
    {
        this.dispatchers.add(dispatcher);
    }

    public IDispatcher getDispatcher(Predicate<IDispatcher> predicate)
    {
        for (IDispatcher dispatcher : this.dispatchers)
        {
            if (predicate.test(dispatcher))
            {
                return dispatcher;
            }
        }
        return null;
    }

    public synchronized void addCommands(List<Class<?>> classes)
    {
        classes.stream().filter(p -> !Command.class.isAssignableFrom(p)).forEach(cmd ->
        {
            Command command = ClassUtil.getMergedAnnotation(cmd, Command.class);
            Class<? extends ICommandReactor> reactor = command.reactor();
            Set<Class<? extends ICommand>> classSet = this.reactorCommands.get(reactor);
            if (CollectionUtils.isEmpty(classSet))
            {
                classSet = new HashSet<>();
                this.reactorCommands.put(reactor, classSet);
            }
            boolean contains = classSet.contains(cmd);
            if (contains && !command.forceOverRide())
            {
                throw new ProgramaException("asd");
            }
            classSet.add((Class<? extends ICommand>) cmd);
        });
    }

    public synchronized void addAnnotationClasses(Class<? extends Annotation> an, List<Class<?>> classes)
    {
        if (CollectionUtils.isEmpty(classes)) return;
        Iterator<Class<?>> iterator = classes.iterator();
        Set<Class<?>> asBeanClass = new HashSet<>();
        while (iterator.hasNext())
        {
            Class<?> next = iterator.next();
            if (ClassUtil.isAsBean(next))
            {
                iterator.remove();
                asBeanClass.add(next);
            }
        }
//        Set<Class<?>> classSet = this.annotationClasses.get(an);
//        if (CollectionUtils.isEmpty(classSet))
//        {
//            classSet = new HashSet<>();
//            this.annotationClasses.put(an, classSet);
//        }
//        classSet.addAll(classes);
//
//        if (asBeanClass.isEmpty())
//        {
//            return;
//        }
//        asBeanClass.forEach(p ->
//        {
//            Set<Class<?>> asBeanClz = this.asBeanAnnotation.get(an);
//            if (CollectionUtils.isEmpty(asBeanClz))
//            {
//                asBeanClz = new HashSet<>();
//                this.asBeanAnnotation.put(an, asBeanClz);
//            }
//            asBeanClz.add(p);
//        });
    }

    public Set<ICommandReactor> getReactor(Byte type)
    {
        return this.reactors.get(type);
    }

    public void setCommands(Map<Class<? extends ICommandReactor>, Set<Class<? extends ICommand>>> reactorCommands)
    {
        this.reactorCommands = reactorCommands;
    }

    public void start()
    {
        Set<IServer> servers = this.servers;
        InitCTX ctx = new InitCTX();
        for (IServer server : servers)
        {
            server.initOnce(ctx);
            server.start();
        }
    }

    public void flushAfterStart()
    {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        context = applicationContext;
//        if (asBeanAnnotation.isEmpty())
//        {
//            return;
//        }
//        Set<Class<? extends Annotation>> classes = asBeanAnnotation.keySet();
//        for (Class<? extends Annotation> aClass : classes)
//        {
//            Set<Class<?>> classSet = asBeanAnnotation.get(aClass);
//            classSet.forEach(c ->
//            {
//                Object bean = applicationContext.getBean(c);
//                this.asBeanInstances.put(c, bean);
//            });
//        }
    }


    public static <T> T getBean(Class<T> type)
    {
        return context.getBean(type);
    }

    public static Object getBean(String beanName)
    {
        return context.getBean(beanName);
    }

    public static ApplicationContext getApplicationContext()
    {
        return context;
    }


    public static Collection<String> getBeanByAnnotation(Class<? extends Annotation> a)
    {
        return Arrays.asList(context.getBeanNamesForAnnotation(a));
    }
}

