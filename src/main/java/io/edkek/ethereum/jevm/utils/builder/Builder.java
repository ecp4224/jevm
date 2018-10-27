package io.edkek.ethereum.jevm.utils.builder;

public interface Builder<T> extends Bindable {

    T build();
}