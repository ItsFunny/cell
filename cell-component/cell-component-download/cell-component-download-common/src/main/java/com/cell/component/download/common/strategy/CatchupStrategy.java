package com.cell.component.download.common.strategy;

import com.cell.component.download.common.request.CatchUpRequest;
import com.cell.component.download.common.response.CatchUpResponse;


public interface CatchupStrategy
{
    CatchUpResponse catchUp(CatchUpRequest req);
    int strategyType();
}
