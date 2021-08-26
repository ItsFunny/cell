package com.cell.listener;

import com.cell.postprocessors.extension.SpringExtensionManager;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-25 21:54
 */
public class FacadedListener implements ApplicationListener<SpringApplicationEvent>
{

    @Override
    public void onApplicationEvent(SpringApplicationEvent springApplicationEvent)
    {
        SpringExtensionManager.getInstance().onApplicationEvent(springApplicationEvent);
    }
}
