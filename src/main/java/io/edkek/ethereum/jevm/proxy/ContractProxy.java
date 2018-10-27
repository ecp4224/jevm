package io.edkek.ethereum.jevm.proxy;

import io.edkek.ethereum.jevm.Contract;
import io.edkek.ethereum.jevm.Web3;
import io.edkek.ethereum.jevm.abi.ABIEncoded;
import io.edkek.ethereum.jevm.abi.ABIEncoder;
import io.edkek.ethereum.jevm.abi.functions.ABIEntry;
import io.edkek.ethereum.jevm.abi.functions.ABIEventFunction;
import io.edkek.ethereum.jevm.abi.functions.ABIFunction;
import io.edkek.ethereum.jevm.types.Address;
import io.edkek.ethereum.jevm.types.Transaction;
import io.edkek.ethereum.jevm.types.TransactionOptions;
import org.apache.commons.codec.binary.Hex;
import org.ethereum.core.CallTransaction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ContractProxy implements InvocationHandler {
    private Class<? extends Contract> contractClass;
    private Web3 web3;
    private String address;
    private ABIEncoded abi;
    private CallTransaction.Contract contractEncoder;

    private ContractProxy(Class<? extends Contract> contractClass, Web3 web3, String address) {
        this.contractClass = contractClass;
        this.web3 = web3;
        this.address = address;

        this.buildEncoder();
    }

    private void buildEncoder() {
        ABIEncoded encodedClass = ABIEncoder.encode(contractClass);

        abi = encodedClass;

        String abi = encodedClass.getABI();

        contractEncoder = new CallTransaction.Contract(abi);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ABIEntry e = abi.getFunction(method);
        if (e instanceof ABIEventFunction)
            throw new IllegalAccessException("You cannot call an event!");

        //Search for transaction parameter
        TransactionOptions options = new TransactionOptions();
        Object[] txArgs = new Object[e.getInputs().length];
        if (args != null) {
            int i = 0;
            for (Object obj : args) {
                if (obj == null) {
                    i++;
                    continue;
                }
                if (obj instanceof TransactionOptions) {
                    options = (TransactionOptions) obj;
                } else {
                    if (obj instanceof Address) {
                        txArgs[i] = ((Address)obj).addressString();
                    } else {
                        txArgs[i] = obj;
                    }
                    i++;
                }
            }
        }

        ABIFunction f = (ABIFunction)e;

        String methodName = abi.getFunction(method).getName(); //Get the name from the abi !!!!
        //This is to ensure we get the proper name, and not the reflection name (which may not match)

        web3.setDefaults(options);

        byte[] encodedCall = contractEncoder.getByName(methodName).encode(txArgs);

        if (f.isConstant()) {
            Transaction tx = new Transaction(new Address("0x6e058b3528502529f112bbcf932bc04f7c66cf7b"), new Address(address), 0, 0, 0, encodedCall);
            tx.setReturnType(method.getReturnType());
            return web3.getProvider().sendCall(tx);
        } else {
            Transaction tx = new Transaction(new Address("0x6e058b3528502529f112bbcf932bc04f7c66cf7b"), new Address(address), 0, 0, 0, encodedCall);

            web3.getProvider().sendRawTransaction(tx);
        }

        System.out.println(Hex.encodeHexString(encodedCall));

        return null;
    }

    public static <T extends Contract> T createConfigProxy(Class<T> contractClass, Web3 web3, String address) {
        ContractProxy proxy = new ContractProxy(contractClass, web3, address);

        return (T) Proxy.newProxyInstance(contractClass.getClassLoader(), new Class[] { contractClass }, proxy);
    }
}
