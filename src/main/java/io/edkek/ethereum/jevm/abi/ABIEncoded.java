package io.edkek.ethereum.jevm.abi;

import io.edkek.ethereum.jevm.abi.functions.ABIEntry;
import io.edkek.ethereum.jevm.utils.Constants;

import java.lang.reflect.Method;
import java.util.Map;

public class ABIEncoded {
    private Map<Method, ABIEntry> functions;

    ABIEncoded(Map<Method, ABIEntry> functionList) {
        this.functions = functionList;
    }

    public Map<Method, ABIEntry> getFunctions() {
        return functions;
    }

    public ABIEntry getFunction(Method m) {
        return functions.get(m);
    }

    public String getABI() {
        return Constants.GSON.toJson(functions.values());
    }
}
