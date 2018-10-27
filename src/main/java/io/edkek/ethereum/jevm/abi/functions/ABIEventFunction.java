package io.edkek.ethereum.jevm.abi.functions;

import io.edkek.ethereum.jevm.abi.parameters.ABIParameter;

public class ABIEventFunction extends ABIEntry {
    private boolean anonymous;

    ABIEventFunction(String name, String type, ABIParameter[] inputs, boolean anonymous) {
        super(name, type, inputs);

        this.anonymous = anonymous;
    }

    public boolean isAnonymous() {
        return anonymous;
    }


}
