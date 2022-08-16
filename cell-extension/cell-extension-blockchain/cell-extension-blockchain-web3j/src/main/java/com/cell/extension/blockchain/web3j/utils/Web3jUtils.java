package com.cell.extension.blockchain.web3j.utils;

import com.cell.base.common.enums.ErrorInterface;
import com.cell.base.core.consumers.IConsumer;
import com.cell.extension.blockchain.web3j.config.Web3JConfig;
import com.cell.extension.blockchain.web3j.config.Web3JWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class Web3jUtils
{
    private static Logger log = LoggerFactory.getLogger(Web3jUtils.class);

    private static Map<String, Web3JWrapper> web3jInstances = new HashMap<>();
    private static List<Web3JWrapper> web3jList = new ArrayList<>();

    public static void seal()
    {
        Web3JConfig instance = Web3JConfig.getInstance();
        List<Web3JConfig.Web3jNode> nodes = instance.getNodes();
        for (Web3JConfig.Web3jNode node : nodes)
        {
            Integer chainId = node.getChainId();
            String address = node.getAddress();
            String privateKey = node.getPrivateKey();
            Credentials credentials = Credentials.create(privateKey);
            Web3j web3j = Web3j.build(new HttpService(address));
            Web3JWrapper wrapper = new Web3JWrapper(web3j, credentials);
            web3jInstances.put(chainId + "", wrapper);
        }
        web3jList.addAll(web3jInstances.values());
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

    public static void doWithWeb3J(IConsumer<Web3JWrapper, ErrorInterface> consumer)
    {
        ErrorInterface result = null;
        for (Web3JWrapper wrapper : web3jList)
        {
            try
            {
                result = handleWeb3j(wrapper, consumer);
            } catch (IOException e)
            {
                continue;
            }
            if (result != null && result.ok())
            {
                break;
            }
        }
    }

    private static ErrorInterface handleWeb3j(Web3JWrapper wrapper, IConsumer<Web3JWrapper, ErrorInterface> consumer) throws IOException
    {
        return consumer.consume(wrapper);
    }
}
