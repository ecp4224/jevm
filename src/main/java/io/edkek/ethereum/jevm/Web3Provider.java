package io.edkek.ethereum.jevm;


import io.edkek.ethereum.jevm.types.Transaction;

import java.util.concurrent.Future;

public interface Web3Provider {

    <T> T sendRawTransaction(Transaction tx);

    <T> T sendCall(Transaction tx);

    <T> Future<T> sendRawTransactionAsync(Transaction tx);

    <T> Future<T> sendCallAsync(Transaction tx);
}
