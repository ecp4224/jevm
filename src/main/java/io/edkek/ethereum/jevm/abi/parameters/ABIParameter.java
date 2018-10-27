package io.edkek.ethereum.jevm.abi.parameters;

public class ABIParameter {
    private String name;
    private String type;

    public ABIParameter(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
