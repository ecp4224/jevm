package io.edkek.ethereum.jevm.abi.functions;

import io.edkek.ethereum.jevm.abi.parameters.ABIParameter;
import io.edkek.ethereum.jevm.annotations.Bind;
import io.edkek.ethereum.jevm.utils.builder.Binder;
import io.edkek.ethereum.jevm.utils.builder.Builder;

public interface ABIFunctionBuilder extends Builder<ABIFunction> {

    @Bind(properties = "type")
    ABIFunctionBuilder setType(String type);

    @Bind(properties = "name")
    ABIFunctionBuilder setName(String name);

    @Bind(properties = "inputs")
    ABIFunctionBuilder setInputs(ABIParameter[] inputs);

    @Bind(properties = "outputs")
    ABIFunctionBuilder setOutputs(ABIParameter[] outputs);

    @Bind(properties = "stateMutability")
    ABIFunctionBuilder setStateMutability(String stateMutability);

    @Bind(properties = "payable")
    @Deprecated
    ABIFunctionBuilder setPayable(boolean payable);

    @Bind(properties = "constant")
    @Deprecated
    ABIFunctionBuilder setConstant(boolean constant);

    @Bind
    String getType();

    @Bind
    String getName();

    @Bind
    ABIParameter[] getInputs();

    @Bind
    ABIParameter[] getOutputs();

    @Bind
    String getStateMutability();

    @Bind
    @Deprecated
    boolean getPayable();

    @Bind
    @Deprecated
    boolean getConstant();

    @Override
    default ABIFunction build() {
        return new ABIFunction(
                getType(), getName(), getInputs(), getOutputs(), getStateMutability()
        );
    }

    default ABIEventFunction buildEvent(boolean anonymous) {
        return new ABIEventFunction(
                getName(), getType(), getInputs(), anonymous
        );
    }

    public static ABIFunctionBuilder create() {
        return Binder.newBinderObject(ABIFunctionBuilder.class);
    }
}
