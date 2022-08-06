package com.cell.extension.blockchain.web3j.utils;

import com.cell.extension.blockchain.web3j.config.Web3JConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;

public class Web3jUtils
{
    private static Logger log = LoggerFactory.getLogger(Web3jUtils.class);

    private static Web3j web3j = null;

    private static int chainId = 0;
    private static Credentials credentials = null;

    public static void seal()
    {
        Integer chainId = Web3JConfig.getInstance().getChainId();
        String address = Web3JConfig.getInstance().getAddress();
        String privateKey = Web3JConfig.getInstance().getPrivateKey();
        web3j = Web3j.build(new HttpService(address));
        Web3jUtils.chainId = chainId;
        credentials = Credentials.create(privateKey);
    }

    public static Web3j getInstance(){
        return web3j;
    }

    public static boolean isSignatureValid(final String address, final String signature, final String message)
    {
        try
        {
            final String personalMessagePrefix = "\u0019Ethereum Signed Message:\n";
            boolean match = false;

            final String prefix = personalMessagePrefix + message.length();
            final byte[] msgHash = Hash.sha3((prefix + message).getBytes());
            final byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
            byte v = signatureBytes[64];
            if (v < 27)
            {
                v += 27;
            }

            final Sign.SignatureData sd = new Sign.SignatureData(v,
                    Arrays.copyOfRange(signatureBytes, 0, 32),
                    Arrays.copyOfRange(signatureBytes, 32, 64));

            String addressRecovered = null;

            // Iterate for each possible key to recover
            for (int i = 0; i < 4; i++)
            {
                final BigInteger publicKey = Sign.recoverFromSignature((byte) i, new ECDSASignature(
                        new BigInteger(1, sd.getR()),
                        new BigInteger(1, sd.getS())), msgHash);

                if (publicKey != null)
                {
                    addressRecovered = "0x" + Keys.getAddress(publicKey);
                    System.out.println("addressRecovered=" + addressRecovered);
                    if (addressRecovered.equalsIgnoreCase(address))
                    {
                        match = true;
                        break;
                    }
                }
            }
            return match;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 获取交易收据
     *
     * @param txHash
     * @return
     */
    public static TransactionReceipt getTransactionReceipt(String txHash)
    {
        TransactionReceipt receipt = null;
        try
        {
            receipt = web3j.ethGetTransactionReceipt(txHash).send().getResult();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return receipt;
    }

}
