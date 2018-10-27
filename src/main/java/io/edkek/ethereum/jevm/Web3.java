package io.edkek.ethereum.jevm;

import io.edkek.ethereum.jevm.proxy.ContractProxy;
import io.edkek.ethereum.jevm.types.TransactionOptions;

public class Web3 {

    private Web3Provider provider;
    private long nonce;

    public Web3(Web3Provider provider) {
        this.provider = provider;
    }

    public Web3Provider getProvider() {
        return provider;
    }

    public <T extends Contract> T getContract(Class<T> contractInterface, String address) {
        return ContractProxy.createConfigProxy(contractInterface, this, address);
    }

    public boolean disconnect() {
        return false;
    }

    public void setDefaults(TransactionOptions options) {

    }
}
