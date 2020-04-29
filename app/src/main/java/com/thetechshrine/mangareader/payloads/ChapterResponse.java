package com.thetechshrine.mangareader.payloads;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thetechshrine.mangareader.models.Chapter;

import java.util.List;

public class ChapterResponse extends ListResponse {

    @SerializedName("docs")
    @Expose
    private List<Chapter> chapters = null;

    public ChapterResponse() {
        super();
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}
