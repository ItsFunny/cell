package com.cell.component.download.common.subscriber;


import com.cell.component.download.common.response.CatchUpResponse;
import com.cell.component.download.common.response.FetchBlockResponse;

import java.util.Set;
import java.util.function.Consumer;

public interface ContractSubscriber
{
    Set<String> contracts();

    void catchUp(CatchUpResponse response);

}
