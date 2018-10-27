package io.edkek.ethereum.jevm.abi.functions;

import io.edkek.ethereum.jevm.abi.parameters.ABIParameter;
import io.edkek.ethereum.jevm.utils.Constants;

public class ABIFunction extends ABIEntry {
    private ABIParameter[] outputs;
    private String stateMutability;
    private boolean payable;
    private boolean constant;

    ABIFunction(String type, String name, ABIParameter[] inputs, ABIParameter[] outputs, String stateMutability, boolean payable, boolean constant) {
        super(name, type, inputs);

        this.outputs = outputs;
        this.stateMutability = stateMutability;
        this.payable = payable;
        this.constant = constant;
    }

    ABIFunction(String type, String name, ABIParameter[] inputs, ABIParameter[] outputs, String stateMutability) {
        super(name, type, inputs);

        this.outputs = outputs;
        this.stateMutability = stateMutability;

        this.payable = this.stateMutability.equals("payable");
        this.constant = this.stateMutability.equals("pure") || this.stateMutability.equals("view");
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public ABIParameter[] getInputs() {
        return inputs;
    }

    public ABIParameter[] getOutputs() {
        return outputs;
    }

    public String getStateMutability() {
        return stateMutability;
    }

    public boolean isPayable() {
        return payable;
    }

    public boolean isConstant() {
        return constant;
    }

    public String toJson() {
        return Constants.GSON.toJson(this);
    }


}
