package io.edkek.ethereum.jevm.providers;

import com.github.arteam.simplejsonrpc.client.JsonRpcClient;
import com.github.arteam.simplejsonrpc.client.Transport;
import io.edkek.ethereum.jevm.Web3Provider;
import io.edkek.ethereum.jevm.abi.ABIEncoder;
import io.edkek.ethereum.jevm.types.Transaction;
import io.edkek.ethereum.jevm.utils.OkHttpResponseFuture;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.Future;

public class HttpProvider implements Web3Provider {

    private String url;
    private OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private JsonRpcClient rpcClient = new JsonRpcClient(new Transport() {

        @NotNull
        @Override
        public String pass(@NotNull String request) throws IOException {
            Request r = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(JSON, request))
                    .build();

            Response response = client.newCall(r).execute();
            return response.body().string();
        }
    });

    private EthRPC rpc;

    public HttpProvider(String url) {
        this.url = url;

        rpc = rpcClient.onDemand(EthRPC.class);
    }

    @Override
    public <T> T sendRawTransaction(Transaction tx) {
        return null;
    }

    @Override
    public <T> T sendCall(Transaction tx) {
        String data = rpc.eth_call(tx, "latest");

        Class<?> returnType = tx.getRequestedType();
        Object o = ABIEncoder.decodeResult(returnType, data);

        return (T)o;
    }

    @Override
    public <T> Future<T> sendRawTransactionAsync(Transaction tx) {
        return null;
    }

    @Override
    public <T> Future<T> sendCallAsync(Transaction tx) {
        return null;
    }

    private <T> Future<T> makeRequestAsync(Request request) {
        Call call = client.newCall(request);

        OkHttpResponseFuture<T> result = new OkHttpResponseFuture<>();

        call.enqueue(result);

        return result.future;
    }
}
