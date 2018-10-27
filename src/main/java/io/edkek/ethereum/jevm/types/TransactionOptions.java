package io.edkek.ethereum.jevm.types;

public class TransactionOptions {
    private long gasPrice = -1; //-1 shows that provider should estimate best gas price
    private long gasLimit = -1; //-1 shows that provider should estimate gas limit
    private long value;
    private long nonce = -1; //-1 shows that provider will give nonce

    public TransactionOptions(long gasPrice, long gasLimit, long value, long nonce) {
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.value = value;
        this.nonce = nonce;
    }

    public TransactionOptions(long gasPrice, long gasLimit, long value) {
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.value = value;
    }

    public TransactionOptions(long gasPrice, long gasLimit) {
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
    }

    public TransactionOptions(long gasPrice) {
        this.gasPrice = gasPrice;
    }

    public TransactionOptions() {
    }

    public long getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(long gasPrice) {
        this.gasPrice = gasPrice;
    }

    public long getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(long gasLimit) {
        this.gasLimit = gasLimit;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }
}
