package io.edkek.ethereum.jevm.types;

import io.edkek.ethereum.jevm.abi.functions.ABIFunction;
import io.edkek.ethereum.jevm.utils.Constants;
import org.apache.commons.codec.binary.Hex;

public class Transaction {
    private String from;
    private String to;
    private String gas;
    private String gasPrice;
    private String value;
    private String data;

    private transient Class<?> requestedReturnType;

    public Transaction(Address from, Address to, long gas, long gasPrice, long value, byte[] data) {
        this.from = from.addressString();
        this.to = to.addressString();

        this.gas = "0x" + Long.toHexString(gas);
        this.gasPrice = "0x" + Long.toHexString(gasPrice);
        this.value = "0x" + Long.toHexString(value);

        this.data = "0x" + Hex.encodeHexString(data);
    }

    public Class<?> getRequestedType() {
        return requestedReturnType;
    }

    public void setReturnType(Class<?> requestedReturnType) {
        this.requestedReturnType = requestedReturnType;
    }

    public String toJson() {
        return Constants.GSON.toJson(this);
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getGas() {
        return gas;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public String getValue() {
        return value;
    }

    public String getData() {
        return data;
    }
}
