package com.cell.decorators;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-10-05 14:35
 */
public interface IDecorator<T>
{
    T decorate(T data);

//    IDataDecorator<byte[]> CERTIFICATE_DECORATOR = (data) ->
//    {
//        String str = new String(data);
//        data = Base64Utils.decode(str);
//
//        StringWriter stringWriter = new StringWriter();
//        JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter);
//        pemWriter.writeObject(new PemObject(PemConstant.CERTIFICATE, data));
//        pemWriter.close();
//        return stringWriter.toString().getBytes();
//    };
//    IDataDecorator<byte[]> PRIVATEKEY_DECORATOR = (data) ->
//    {
////        String str = new String(data);
////        data = Base64Util.decode(str);
//        StringWriter stringWriter = new StringWriter();
//        PemWriter pemWriter = new PemWriter(stringWriter);
//        pemWriter.writeObject(new PemObject(PemConstant.PRIVATEKEY, data));
//        pemWriter.close();
//        return stringWriter.toString().getBytes();
//    };
//
//    IDataDecorator<byte[]> PUBLICKEY_DECORATOR = (data) ->
//    {
////        String str = new String(data);
////        data = Base64Util.decode(str);
//
//        StringWriter stringWriter = new StringWriter();
//        PemWriter pemWriter = new PemWriter(stringWriter);
//        pemWriter.writeObject(new PemObject(PemConstant.PUBLICKEY, data));
//        pemWriter.close();
//        return stringWriter.toString().getBytes();
//    };
//
//    IDataDecorator<byte[]> BASE64_DECORATOR = (data) -> Base64Utils.encode(data).getBytes();
//
//    IDataDecorator<byte[]> ORIGIN_BYTES = (data) ->
//    {
//        StringWriter stringWriter = new StringWriter();
//        PemWriter pemWriter = new PemWriter(stringWriter);
//        pemWriter.writeObject(new PemObject(PemConstant.PUBLICKEY, data));
//        pemWriter.close();
//        return stringWriter.toString().getBytes();
//    };


}
