package com.aispeech.nativedemo.bean;

import java.util.ArrayList;

/**
 * Created by yrl on 17-9-1.
 */

public class MessageBean {
    public static final int TYPE_OUTPUT = 0;
    public static final int TYPE_INPUT = 1;
    public static final int TYPE_WIDGET_CONTENT = 2;
    public static final int TYPE_WIDGET_LIST = 3;
    public static final int TYPE_WIDGET_WEB = 4;
    public static final int TYPE_WIDGET_MEDIA = 5;
    public static final int TYPE_WIDGET_WEATHER = 6;

    private int type;
    private String text;
    private String title;
    private String subTitle;
    private String imgUrl;
    private String url;
    private WeatherBean weatherBean;

    public WeatherBean getWeatherBean() {
        return weatherBean;
    }

    public void setWeatherBean(WeatherBean weatherBean) {
        this.weatherBean = weatherBean;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private ArrayList<MessageBean> mWidgetListItem = new ArrayList<>();
    private int mCurrentPage = 1;
    private boolean mFirstLayout = true;

    private int itemsPerPage;// 每页的条数

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void addMessageBean(MessageBean bean) {
        mWidgetListItem.add(bean);
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int mCurrentPage) {
        this.mCurrentPage = mCurrentPage;
    }

    public boolean isFirstLayout() {
        return mFirstLayout;
    }

    public void setFirstLayout(boolean mFirstLayout) {
        this.mFirstLayout = mFirstLayout;
    }

    public ArrayList<MessageBean> getMessageBeanList() {
        return mWidgetListItem;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }
}
