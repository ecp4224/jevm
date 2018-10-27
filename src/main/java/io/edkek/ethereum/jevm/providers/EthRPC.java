package io.edkek.ethereum.jevm.providers;

import com.github.arteam.simplejsonrpc.client.JsonRpcId;
import com.github.arteam.simplejsonrpc.client.JsonRpcParams;
import com.github.arteam.simplejsonrpc.client.ParamsType;
import com.github.arteam.simplejsonrpc.client.generator.AtomicLongIdGenerator;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcMethod;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcParam;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcService;
import io.edkek.ethereum.jevm.types.Transaction;

@JsonRpcService
@JsonRpcId(AtomicLongIdGenerator.class)
@JsonRpcParams(ParamsType.ARRAY)
public interface EthRPC {

    @JsonRpcMethod
    String eth_call(@JsonRpcParam("tx") Transaction tx, @JsonRpcParam("block") String block);

    @JsonRpcMethod
    String eth_call(@JsonRpcParam("tx") Transaction tx, @JsonRpcParam("block") int block);

    @JsonRpcMethod
    String eth_sendRawTransaction(@JsonRpcParam("tx") String signedTx);
}
