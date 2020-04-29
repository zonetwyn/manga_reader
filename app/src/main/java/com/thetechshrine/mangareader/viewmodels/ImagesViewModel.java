package com.thetechshrine.mangareader.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetechshrine.mangareader.models.Chapter;
import com.thetechshrine.mangareader.network.retrofit.ApiClient;
import com.thetechshrine.mangareader.network.retrofit.ApiInterface;
import com.thetechshrine.mangareader.payloads.ChapterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagesViewModel extends ViewModel {

    private MutableLiveData<Chapter> chapter;
    private MutableLiveData<Boolean> isLoading;

    // we will call this method to get the data
    public LiveData<Chapter> getChapter(Context context, String mangaId, String chapterId) {
        // if the list is null
        if (chapter == null) {
            chapter = new MutableLiveData<>();
            loadChapter(context, mangaId, chapterId);
        }

        // finally we will return the data
        return chapter;
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
    public void loadChapter(Context context, String mangaId, String chapterId) {
        isLoading.setValue(true);

        ApiInterface api = ApiClient.getClient(context).create(ApiInterface.class);
        Call<Chapter> call = api.getChapter(mangaId, chapterId);

        call.enqueue(new Callback<Chapter>() {

            @Override
            public void onResponse(Call<Chapter> call, Response<Chapter> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    chapter.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Chapter> call, Throwable t) {
                isLoading.setValue(false);
            }
        });
    }
}
