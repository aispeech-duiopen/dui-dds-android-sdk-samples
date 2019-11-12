package com.aispeech.nativedemo.test;

public class ListItem<T> {
    private String title;
    private Class<T> klass;

    public ListItem(String title, Class<T> klass) {
        this.title = title;
        this.klass = klass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class<T> getKlass() {
        return klass;
    }

    public void setKlass(Class<T> klass) {
        this.klass = klass;
    }
}
