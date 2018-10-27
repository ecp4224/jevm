package io.edkek.ethereum.jevm.abi.parameters;

public class ABIEventParameter extends ABIParameter {
    private boolean indexed;

    public ABIEventParameter(String name, String type, boolean indexed) {
        super(name, type);

        this.indexed = indexed;
    }

    public boolean isIndexed() {
        return indexed;
    }
}
