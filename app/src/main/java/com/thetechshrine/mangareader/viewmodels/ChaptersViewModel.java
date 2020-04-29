package com.thetechshrine.mangareader.viewmodels;


import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetechshrine.mangareader.network.retrofit.ApiClient;
import com.thetechshrine.mangareader.network.retrofit.ApiInterface;
import com.thetechshrine.mangareader.payloads.ChapterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChaptersViewModel extends ViewModel {

    private MutableLiveData<ChapterResponse> chapters;
    private MutableLiveData<Boolean> isLoading;

    // we will call this method to get the data
    public LiveData<ChapterResponse> getChapters(Context context, String mangaId, int page) {
        // if the list is null
        if (chapters == null) {
            chapters = new MutableLiveData<>();
            loadChapters(context, mangaId, page);
        }

        // finally we will return the data
        return chapters;
    }

    // we will call this method to get loading state
    public LiveData<Boolean> isLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
            isLoading.setValue(true);
        }

        return isLoading;
    }

    // This method is using Retrofit to get the JSON data from URL
    public void loadChapters(Context context, String mangaId, int page) {
        isLoading.setValue(true);

        ApiInterface api = ApiClient.getClient(context).create(ApiInterface.class);
        Call<ChapterResponse> call = api.getChapters(mangaId, page);

        call.enqueue(new Callback<ChapterResponse>() {

            @Override
            public void onResponse(Call<ChapterResponse> call, Response<ChapterResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    chapters.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ChapterResponse> call, Throwable t) {
                isLoading.setValue(false);
            }
        });
    }
}
