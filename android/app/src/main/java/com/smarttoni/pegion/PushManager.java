package com.smarttoni.pegion;

import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.User;
import com.smarttoni.http.ChefHttpClient;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PushManager {


    private DaoAdapter daoAdapter;

    public PushManager(DaoAdapter daoAdapter) {
        this.daoAdapter = daoAdapter;
    }

    public void pushAll(PushMessage pushMessage, PushCallback callback) {
        List<User> users = daoAdapter.loadUsers();
        Observable.fromIterable(users).flatMap(new Function<User, ObservableSource<Boolean>>() {
            @Override
            public ObservableSource<Boolean> apply(User user) throws Exception {
                return push(user, pushMessage, callback);
            }
        }).subscribe();
    }


    public void push(String userId, PushMessage pushMessage, PushCallback callback) {
        User user = ServiceLocator.getInstance().getDatabaseAdapter().getUserById(userId);
        if (user == null) {
            if (callback != null) {
                callback.onPushFailed();
            }
            return;
        }
        push(user, pushMessage, callback).subscribe();
    }

    private Observable<Boolean> push(User user, PushMessage message, PushCallback callback) {

        return Observable.create(emitter -> {
            if (user.getIpAddress() != null && !user.getIpAddress().equals("")) {
                new ChefHttpClient().getHttpClient(user.getIpAddress()).push(message).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (callback != null) {
                            callback.onPushSuccess();
                        }
                        emitter.onNext(true);
                        emitter.onComplete();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        if (callback != null) {
                            callback.onPushFailed();
                        }
                        emitter.onNext(false);
                        emitter.onComplete();
                    }
                });
            }
        });
    }
}
