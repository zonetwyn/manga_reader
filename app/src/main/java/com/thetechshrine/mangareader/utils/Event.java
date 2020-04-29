package com.thetechshrine.mangareader.utils;

public class Event {

    public static final int SUBJECT_IMAGE_FRAGMENT_SCALE_TYPE_FIT_CENTER = 100;
    public static final int SUBJECT_IMAGE_FRAGMENT_SCALE_TYPE_CENTER_CROP = 200;

    private int subject;
    private Object data;

    public Event() {
    }

    public Event(int subject, Object data) {
        this.subject = subject;
        this.data = data;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
