package io.edkek.ethereum.jevm.abi.functions;

import io.edkek.ethereum.jevm.abi.parameters.ABIParameter;

public abstract class ABIEntry {
    protected String name;
    protected String type;
    protected ABIParameter[] inputs;

    public ABIEntry(String name, String type, ABIParameter[] inputs) {
        this.name = name;
        this.type = type;
        this.inputs = inputs;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public ABIParameter[] getInputs() {
        return inputs;
    }
}
