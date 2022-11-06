package com.cell.plugin.develop.context;

import com.google.common.base.Stopwatch;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
public class ElapsedTimeInfos implements IElapsedTimeInfo
{
    @Data
    public static class TraceNode
    {
        private boolean done;
        private String key;
        private Stopwatch stopwatch;
        private TraceNode parent;
        private TraceNode child;
        private List<TraceNode> doneNodes;

        public TraceNode(String key)
        {
            this.key = key;
            this.stopwatch = Stopwatch.createStarted();
        }

        void addDoneChild()
        {
            if (this.doneNodes == null)
            {
                this.doneNodes = new ArrayList<>();
            }
            this.doneNodes.add(this.child);
            this.child = null;
        }

        void linkChild(TraceNode child)
        {
            if (this.child == null)
            {
                child.parent = this;
                this.child = child;
            } else
            {
                this.child.linkChild(child);
            }
        }

        void end()
        {
            if (this.done)
            {
                return;
            }
            this.done = true;
            this.stopwatch.stop();
            if (this.parent != null)
            {
                this.parent.addDoneChild();
            }
            if (null != this.child)
            {
                this.child.end();
            }
        }

        void doDump(boolean fist, StringBuilder sb)
        {
            sb.append(this.key);
            if (fist)
            {
                sb.append("[");
            } else
            {
                sb.append("<");
            }
            sb.append(this.stopwatch.elapsed(TimeUnit.MILLISECONDS)).append("ms");
            if (this.doneNodes != null)
            {
                sb.append(" ");
                for (int i = 0; i < doneNodes.size(); i++)
                {
                    doneNodes.get(i).doDump(false, sb);
                    if (i != doneNodes.size() - 1)
                    {
                        sb.append(",");
                    }
                }
            }
            if (null != this.child)
            {
                this.child.doDump(false, sb);
            }
            if (!fist)
            {
                sb.append(">");
            } else
            {
                sb.append("]");
            }
        }
    }

    private LinkedHashMap<String, TraceNode> traces = new LinkedHashMap<>();
    private TraceNode lastTrace;
    private List<String> traceKeys = new ArrayList<>(0);

    @Override
    public void traceInfo(String key, String info)
    {
        TraceNode node = new TraceNode(key);
        if (this.lastTrace != null)
        {
            this.lastTrace.linkChild(node);
        } else
        {
            this.lastTrace = node;
        }
        this.traces.put(key, node);
        this.traceKeys.add(key);
    }

    @Override
    public void trace(String key)
    {
        this.traceInfo(key, null);
    }


    @Override
    public void traceEnd()
    {
        this.traceEndWithInfo(null);
    }

    @Override
    public void traceEndWithInfo(String info)
    {
        if (this.traces.size() == 0)
        {
            return;
        }
        String key = this.traceKeys.remove(this.traceKeys.size() - 1);
        TraceNode node = this.traces.get(key);
        node.end();
        if (node.parent != null)
        {
            if (node.child == null)
            {
                this.traces.remove(key);
            }
        }
        this.lastTrace = node.parent;
    }


    @Override
    public String dump()
    {
        StringBuilder sb = new StringBuilder();
        int size = traces.size();
        int index = 0;
        for (String s : traces.keySet())
        {
            TraceNode node = traces.get(s);
            if (node.parent!=null){
                index++;
                continue;
            }
            node.end();
            node.doDump(true, sb);
            index++;
            if (index != size - 1)
            {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception
    {
        ElapsedTimeInfos timeInfos = new ElapsedTimeInfos();
        // [p1 ms <p1-2 ms,<c2 ms>,<c3 ms>> ]
        timeInfos.trace("p1");
        timeInfos.trace("p1-2");
        timeInfos.trace("c2");
        TimeUnit.SECONDS.sleep(1);
        timeInfos.traceEnd();
        timeInfos.trace("c3");
        TimeUnit.MILLISECONDS.sleep(200);
        timeInfos.traceEnd();
        timeInfos.traceEnd();
        timeInfos.traceEnd();
        TimeUnit.SECONDS.sleep(1);

        timeInfos.trace("pppp");
        timeInfos.trace("pppp2");
        timeInfos.trace("pppp3");
        TimeUnit.SECONDS.sleep(1);
        timeInfos.traceEnd();
        System.out.println(timeInfos.dump());
    }
}
