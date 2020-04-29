package com.thetechshrine.mangareader.payloads;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thetechshrine.mangareader.models.Manga;

import java.util.List;

public class MangaResponse extends ListResponse {

    @SerializedName("docs")
    @Expose
    private List<Manga> mangas = null;

    public MangaResponse() {
        super();
    }

    public List<Manga> getMangas() {
        return mangas;
    }

    public void setMangas(List<Manga> mangas) {
        this.mangas = mangas;
    }
}
