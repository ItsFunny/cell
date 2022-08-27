package com.cell.extension.blockchain.web3j.consumer;

import com.cell.base.common.enums.ErrorInterface;

import java.io.IOException;

public interface IWeb3jConsumer<T>
{
    ErrorInterface consume(T t) throws IOException;
}
