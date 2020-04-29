package com.thetechshrine.mangareader.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Manga implements Parcelable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("baseUrl")
    @Expose
    private String baseUrl;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("genres")
    @Expose
    private List<String> genres;
    @SerializedName("description")
    @Expose
    private String description;

    private List<Chapter> chapters;

    public Manga() {
    }

    protected Manga(Parcel in) {
        id = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        title = in.readString();
        baseUrl = in.readString();
        imageUrl = in.readString();
        genres = in.createStringArrayList();
        description = in.readString();
        chapters = in.createTypedArrayList(Chapter.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(title);
        dest.writeString(baseUrl);
        dest.writeString(imageUrl);
        dest.writeStringList(genres);
        dest.writeString(description);
        dest.writeTypedList(chapters);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Manga> CREATOR = new Creator<Manga>() {
        @Override
        public Manga createFromParcel(Parcel in) {
            return new Manga(in);
        }

        @Override
        public Manga[] newArray(int size) {
            return new Manga[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}
