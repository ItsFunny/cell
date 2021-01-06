package com.cell.services;


/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-10-05 17:33
 */
public interface IDataReadDecorator<T> extends IDataDecorator<byte[]>
{

//    IDataReadDecorator<byte[]> PUBLICKEY_READER = (data) -> PemUtils.replace(PemConstant.PUBLICKEY, data);
//
//    IDataReadDecorator<byte[]> BASE64_READER = (data) -> Base64Utils.decode(new String(data));
}
