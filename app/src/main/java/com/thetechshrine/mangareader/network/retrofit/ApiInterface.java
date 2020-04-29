package com.thetechshrine.mangareader.network.retrofit;

import com.thetechshrine.mangareader.models.Chapter;
import com.thetechshrine.mangareader.payloads.ChapterResponse;
import com.thetechshrine.mangareader.payloads.MangaResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    String BASE_URL = "https://manga-reader-api.herokuapp.com/api/v1/";

    @GET("mangas")
    Call<MangaResponse> getMangas(@Query("page") int page);

    @GET("mangas/{mangaId}/chapters")
    Call<ChapterResponse> getChapters(@Path("mangaId") String mangaId, @Query("page") int page);

    @GET("mangas/{mangaId}/chapters/{chapterId}")
    Call<Chapter> getChapter(@Path("mangaId") String mangaId, @Path("chapterId") String chapterId);
}
