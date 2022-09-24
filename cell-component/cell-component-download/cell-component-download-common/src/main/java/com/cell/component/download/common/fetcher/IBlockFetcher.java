package com.cell.component.download.common.fetcher;

import com.cell.component.download.common.request.FetchBlockRequest;
import com.cell.component.download.common.response.FetchBlockResponse;

public interface IBlockFetcher
{
    FetchBlockResponse fetchBlocks(FetchBlockRequest req);
}
