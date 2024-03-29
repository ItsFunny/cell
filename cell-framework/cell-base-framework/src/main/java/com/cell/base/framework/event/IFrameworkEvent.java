package com.cell.base.framework.event;

import com.cell.base.core.protocol.IServerRequest;
import com.cell.base.core.protocol.IServerResponse;
import com.cell.event.IProcessEvent;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 19:00
 */
public interface IFrameworkEvent extends IProcessEvent
{
    IServerRequest getRequest();

    IServerResponse getResponse();


    class DefaultRequestResponseEvent implements IFrameworkEvent
    {
        private IServerRequest request;
        private IServerResponse response;

        public DefaultRequestResponseEvent(IServerRequest request, IServerResponse response)
        {
            this.request = request;
            this.response = response;
        }

        @Override
        public IServerRequest getRequest()
        {
            return this.request;
        }

        @Override
        public IServerResponse getResponse()
        {
            return this.response;
        }
    }
}
