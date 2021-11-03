package com.cell.internal;

import com.cell.annotations.CellOrder;
import com.cell.constants.OrderConstants;
import com.cell.context.INodeContext;
import com.cell.extension.AbstractSpringNodeExtension;
import com.cell.log.LOG;
import com.cell.log.LogTypeEnums;
import com.cell.models.Module;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryUsage;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-05 07:29
 */
// 这部分逻辑抄的 gc监控
@CellOrder(OrderConstants.MIN_ORDER - 999)
public class GCLoggingExtension extends AbstractSpringNodeExtension
{
    public static final String GC_FAIL = "Allocation Failure";

    @Override
    public void onInit(INodeContext ctx) throws Exception
    {
        this.install();
    }

    @Override
    public void onStart(INodeContext ctx) throws Exception
    {

    }

    @Override
    public void onReady(INodeContext ctx) throws Exception
    {

    }

    @Override
    public void onClose(INodeContext ctx) throws Exception
    {

    }

    public void install()
    {
        installGCMonitoring();
    }

    public void installGCMonitoring()
    {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans)
        {
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = new NotificationListener()
            {
                @SuppressWarnings("restriction")
                @Override
                public void handleNotification(Notification notification, Object handback)
                {
                    if (notification.getType().equals(
                            com.sun.management.GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION))
                    {
                        com.sun.management.GarbageCollectionNotificationInfo info = com.sun.management.GarbageCollectionNotificationInfo
                                .from((CompositeData) notification.getUserData());
                        long duration = info.getGcInfo().getDuration();
                        String gctype = info.getGcAction();
                        if ("end of minor GC".equals(gctype))
                        {
                            gctype = "Minor GC";
                        } else if ("end of major GC".equals(gctype))
                        {
                            gctype = "Major GC";
                        }

                        LOG.info(Module.COMMON, "[{}], id: [{}], algo: [{}], cause: [{}], paused: [{} ms]", LogTypeEnums.GC, gctype,
                                info.getGcInfo().getId(), info.getGcName(), info.getGcCause(), duration);

                        Map<String, MemoryUsage> membefore = info.getGcInfo().getMemoryUsageBeforeGc();
                        Map<String, MemoryUsage> mem = info.getGcInfo().getMemoryUsageAfterGc();
                        for (Map.Entry<String, MemoryUsage> entry : mem.entrySet())
                        {

                            String name = entry.getKey();
                            if (name.equals("Code Cache") || name.equals("Compressed Class Space")
                                    || name.equals("Metaspace"))
                            {
                                continue;
                            }
                            MemoryUsage after = entry.getValue();
                            MemoryUsage before = membefore.get(name);

                            LOG.info(Module.COMMON, "[{}] [{}]: {}/{}/{} -> {}/{}/{}", LogTypeEnums.GC,
                                    info.getGcInfo().getId(), entry.getKey(), formatByte(before.getUsed()),
                                    formatByte(before.getCommitted()), formatByte(before.getMax()),
                                    formatByte(after.getUsed()), formatByte(after.getCommitted()),
                                    formatByte(after.getMax()));
                        }
                    }
                }
            };
            // Add the listener
            emitter.addNotificationListener(listener, null, null);
        }
    }

    public String formatByte(double b)
    {
        return formatNumber(b, "B");
    }

    public String formatNumber(double b, String tailFix)
    {
        final String UNITS[] = new String[]{"", "K", "M", "G", "T", "P", "E", "Z"};
        for (String u : UNITS)
        {
            if (Math.abs(b) < 1024)
            {
                return String.format("%.2f%s%s", b, u, tailFix);
            }

            b /= 1024;
        }

        return String.format("%.2f%s%s", b, "Y", tailFix);
    }

    public static void main(String[] args)
    {
        new GCLoggingExtension().install();
        System.gc();

        try
        {
            System.gc();
            System.gc();
            System.gc();
            Thread.sleep(100000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
