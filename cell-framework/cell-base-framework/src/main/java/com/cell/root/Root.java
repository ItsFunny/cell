package com.cell.root;

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
public class Root
{
    private static final Root instance = new Root();


    public static Root getInstance()
    {
        return instance;
    }

    private Map<Byte, Set<ICommandReactor>> reactors = new HashMap<>();
    private Map<Class<? extends ICommandReactor>, Set<Class<? extends ICommand>>> reactorCommands = new HashMap<>();

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
        for (IServer server : servers)
        {
            server.start();
        }
    }
}

