package com.thetechshrine.mangareader.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetechshrine.mangareader.network.retrofit.ApiClient;
import com.thetechshrine.mangareader.network.retrofit.ApiInterface;
import com.thetechshrine.mangareader.payloads.MangaResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private MutableLiveData<MangaResponse> subjects;
    private MutableLiveData<Boolean> isLoading;

    public LiveData<MangaResponse> getMangas(Context context, int page) {
        // if the list is null
        if (subjects == null) {
            subjects = new MutableLiveData<>();
            loadMangas(context, page);
        }

        // finally we will return the data
        return subjects;
    }

    // we will call this method to get loading state
    public LiveData<Boolean> isLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
            isLoading.setValue(true);
        }

        return isLoading;
    }

    public void loadMangas(final Context context, int page) {
        isLoading.setValue(true);

        ApiInterface api = ApiClient.getClient(context).create(ApiInterface.class);
        Call<MangaResponse> call = api.getMangas(page);

        call.enqueue(new Callback<MangaResponse>() {

            @Override
            public void onResponse(Call<MangaResponse> call, Response<MangaResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    subjects.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MangaResponse> call, Throwable t) {
                isLoading.setValue(false);
            }
        });
    }
}
