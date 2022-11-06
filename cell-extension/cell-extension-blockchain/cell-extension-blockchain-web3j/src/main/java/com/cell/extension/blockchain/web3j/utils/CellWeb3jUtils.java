package com.cell.extension.blockchain.web3j.utils;

import com.cell.base.common.enums.ErrorEnums;
import com.cell.base.common.enums.ErrorInterface;
import com.cell.base.common.models.Couple;
import com.cell.base.common.models.Module;
import com.cell.base.common.utils.CollectionUtils;
import com.cell.base.common.utils.RandomUtils;
import com.cell.base.common.utils.StringUtils;
import com.cell.base.core.wrapper.Box;
import com.cell.extension.blockchain.web3j.config.CellWeb3JConfig;
import com.cell.extension.blockchain.web3j.config.Web3JWrapper;
import com.cell.extension.blockchain.web3j.consumer.IWeb3jConsumer;
import com.cell.sdk.log.LOG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class CellWeb3jUtils
{
    private static Logger log = LoggerFactory.getLogger(CellWeb3jUtils.class);
    public static String emptyAddress = "0x0000000000000000000000000000000000000000";
    private static Map<String, Web3JWrapper> web3jInstances = new HashMap<>();
    private static List<Web3JWrapper> web3jList = new ArrayList<>();

    public static void seal()
    {
        CellWeb3JConfig instance = CellWeb3JConfig.getInstance();
        List<CellWeb3JConfig.Web3jNode> nodes = instance.getNodes();
        for (CellWeb3JConfig.Web3jNode node : nodes)
        {
            Integer chainId = node.getChainId();
            String address = node.getAddress();
            String name = node.getName();
            String privateKey = node.getPrivateKey();
            Credentials credentials = null;
            if (StringUtils.isNotEmpty(privateKey))
            {
                credentials = Credentials.create(privateKey);
            }
            Web3j web3j = Web3j.build(new HttpService(address));
            Web3JWrapper wrapper = new Web3JWrapper(web3j, chainId, credentials);
            wrapper.from(node);
            web3jInstances.put(name, wrapper);
            web3jList.add(wrapper);
        }
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

    public static interface Web3jStrategy
    {
        List<Web3JWrapper> handle(List<Web3JWrapper> wrappers);
    }

    public static Web3jStrategy SortStrategy = new Web3jStrategy()
    {
        @Override
        public List<Web3JWrapper> handle(List<Web3JWrapper> wrappers)
        {
            List<Web3JWrapper> ret = new ArrayList<>(web3jList);
            Collections.sort(ret, new Web3jSort());
            return ret;
        }
    };

    public static Web3jStrategy ShuffleStrategy = new Web3jStrategy()
    {
        @Override
        public List<Web3JWrapper> handle(List<Web3JWrapper> wrappers)
        {
            ArrayList<Web3JWrapper> ret = new ArrayList<>(wrappers);
            Collections.shuffle(ret);
            return ret;
        }
    };

    public static Web3jStrategy NoneStrategy = new Web3jStrategy()
    {
        @Override
        public List<Web3JWrapper> handle(List<Web3JWrapper> wrappers)
        {
            return wrappers;
        }
    };

    public static class Web3jSort implements Comparator<Web3JWrapper>
    {
        @Override
        public int compare(Web3JWrapper o1, Web3JWrapper o2)
        {
            return o1.getSuccessCount().get() - o2.getSuccessCount().get();
        }
    }


    public static Optional<EthLog> getEthLog(final EthFilter filter, Web3jStrategy strategy)
    {
        Box<EthLog> box = new Box<>();
        doWeb3j((v) ->
        {
            EthLog log = v.getWeb3j().ethGetLogs(filter).send();
            if (log.hasError())
            {
                LOG.warn(Module.WEB_3J, "获取log失败:wrapper:{},errCode:{},errMsg:{}",
                        v, log.getError().getCode(), log.getError().getMessage());
                return ErrorEnums.CONTINUE;
            }
            box.setData(log);
            return ErrorEnums.NO_ERROR;
        }, strategy);
        return Optional.ofNullable(box.getData());
    }

    public static void doWeb3j(IWeb3jConsumer<Web3JWrapper> consumer, Web3jStrategy strategy)
    {
        handle(consumer, strategy);
    }


    public static void doWithWeb3J(IWeb3jConsumer<Web3JWrapper> consumer)
    {
        handle(consumer, NoneStrategy);
    }

    private static void handle(IWeb3jConsumer<Web3JWrapper> consumer, Web3jStrategy strategy)
    {
        List<Web3JWrapper> wrappers = strategy.handle(web3jList);
        ErrorInterface result = null;
        int failCount = 0;
        Web3JWrapper successWrapper = null;
        for (Web3JWrapper wrapper : wrappers)
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
                successWrapper = wrapper;
                wrapper.inc();
                break;
            }
            failCount++;
        }
        if (failCount > 2)
        {
            LOG.info(Module.HTTP_CLIENT, "execute web3j successfully, failCount:{},successWrapper:{}",
                    failCount, successWrapper);
        }
    }

    public static Web3j getInstance()
    {
        int index = RandomUtils.randomInt(0, web3jList.size() - 1);
        return web3jList.get(index).getWeb3j();
    }

    private static ErrorInterface handleWeb3j(Web3JWrapper wrapper, IWeb3jConsumer<Web3JWrapper> consumer) throws IOException
    {
        return consumer.consume(wrapper);
    }

    public static Optional<Integer> getDecimal(String contractAddress)
    {
        Box<Integer> box = new Box<>();
        CellWeb3jUtils.doWithWeb3J((v) ->
        {
            int ret = getTokenDecimals(v.getWeb3j(), contractAddress);
            box.setData(ret);
            return ErrorEnums.NO_ERROR;
        });
        if (box.getData() == null)
        {
            return Optional.empty();
        }
        return Optional.of(box.getData());
    }


    public static Optional<String> getSupply(String tokenAddress)
    {
        Optional<Integer> decimalOpt = getDecimal(tokenAddress);
        if (decimalOpt.isPresent())
        {
            return Optional.empty();
        }
        Integer decimals = decimalOpt.get();
        double pow = Math.pow(10, Double.parseDouble(String.valueOf(decimals)));
        BigDecimal tokenDecimals = new BigDecimal(pow);
        Optional<BigInteger> tokenTotalSupplyOpt = getTokenTotalSupply(tokenAddress);
        if (!tokenTotalSupplyOpt.isPresent())
        {
            return Optional.empty();
        }
        BigInteger tokenTotalSupply = tokenTotalSupplyOpt.get();
        BigDecimal supply = new BigDecimal(tokenTotalSupply).divide(tokenDecimals, 0, BigDecimal.ROUND_DOWN);
        return Optional.of(supply.toString());
    }

    public static Optional<BigInteger> getTokenTotalSupply(String contractAddress)
    {
        Box<BigInteger> box = new Box<>();
        CellWeb3jUtils.doWithWeb3J((v) ->
        {
            BigInteger ret = doGetTokenTotalSupply(v.getWeb3j(), contractAddress);
            box.setData(ret);
            return ErrorEnums.NO_ERROR;
        });
        if (box.getData() == null)
        {
            return Optional.empty();
        }
        return Optional.of(box.getData());
    }

    private static BigInteger doGetTokenTotalSupply(Web3j web3j, String contractAddress)
    {
        String methodName = "totalSupply";
        String fromAddr = emptyAddress;
        BigInteger totalSupply = BigInteger.ZERO;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>()
        {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try
        {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            totalSupply = (BigInteger) results.get(0).getValue();
        } catch (InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }
        return totalSupply;
    }

    public static int getTokenDecimals(Web3j web3j, String contractAddress)
    {
        String methodName = "decimals";
        String fromAddr = emptyAddress;
        int decimal = 0;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        TypeReference<Uint8> typeReference = new TypeReference<Uint8>()
        {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);
        EthCall ethCall;
        try
        {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            if (CollectionUtils.isEmpty(results))
            {
                return 0;
            }
            decimal = Integer.parseInt(results.get(0).getValue().toString());
        } catch (InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }
        return decimal;
    }


    public static String getSymbol(String contractAddress)
    {
        Box<String> box = new Box<>();
        CellWeb3jUtils.doWithWeb3J((v) ->
        {
            String ret = getTokenSymbol(v.getWeb3j(), contractAddress);
            box.setData(ret);
            return ErrorEnums.NO_ERROR;
        });
        if (box.getData() == null)
        {
            return null;
        }
        return box.getData();
    }

    public static String getTokenName(String contractAddress)
    {
        Box<String> box = new Box<>();
        CellWeb3jUtils.doWithWeb3J((v) ->
        {
            String ret = getTokenName(v.getWeb3j(), contractAddress);
            box.setData(ret);
            return ErrorEnums.NO_ERROR;
        });
        if (box.getData() == null)
        {
            return null;
        }
        return box.getData();
    }

    private static String getTokenSymbol(Web3j web3j, String contractAddress)
    {
        String methodName = "symbol";
        String symbol = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>()
        {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try
        {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            if (Objects.nonNull(results) && results.size() > 0)
            {
                symbol = results.get(0).getValue().toString();
            }
        } catch (InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }
        return symbol;
    }

    private static String getTokenName(Web3j web3j, String contractAddress)
    {
        String methodName = "name";
        String name = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>()
        {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try
        {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            if (Objects.nonNull(results) && results.size() > 0)
            {
                name = results.get(0).getValue().toString();
            }
        } catch (InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }
        return name;
    }
}
