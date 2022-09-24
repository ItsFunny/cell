package com.cell.component.download.common.response;

import com.cell.component.blockchain.common.block.IBlock;
import lombok.Data;

import java.util.List;

@Data
public class FetchBlockResponse
{
    private List<IBlock>catchupBlocks;
}
