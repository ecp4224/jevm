package io.edkek.ethereum.jevm.utils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class OkHttpResponseFuture<T> implements Callback {
    public final CompletableFuture<T> future = new CompletableFuture<>();

    public OkHttpResponseFuture() {
    }

    @Override
    public void onFailure(Call call, IOException e) {
        future.completeExceptionally(e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        //future.complete(response);
        //TODO Decode response and parse to T
    }
}
