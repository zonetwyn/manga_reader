package com.thetechshrine.mangareader.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Chapter implements Parcelable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("number")
    @Expose
    private double number;
    @SerializedName("releaseDate")
    @Expose
    private String releaseDate;
    @SerializedName("images")
    @Expose
    private List<String> images;

    public Chapter() {
    }

    protected Chapter(Parcel in) {
        id = in.readString();
        createdAt = in.readString();
        url = in.readString();
        number = in.readDouble();
        releaseDate = in.readString();
        images = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(createdAt);
        dest.writeString(url);
        dest.writeDouble(number);
        dest.writeString(releaseDate);
        dest.writeStringList(images);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Chapter> CREATOR = new Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel in) {
            return new Chapter(in);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
