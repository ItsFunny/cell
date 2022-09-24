package com.cell.component.download.common.writer;

import com.cell.component.blockchain.common.block.IBlock;

import java.util.List;

public interface IBlockWriter
{
    void storeBlocks(List<IBlock>blocks);
}
