package com.aispeech.nativedemo.observer;

import android.content.Intent;
import android.util.Log;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.MessageObserver;
import com.aispeech.nativedemo.DuiApplication;
import com.aispeech.nativedemo.bean.MessageBean;
import com.aispeech.nativedemo.bean.WeatherBean;
import com.aispeech.nativedemo.music.PlayerActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * 客户端MessageObserver, 用于处理客户端动作的消息响应.
 */
public class DuiMessageObserver implements MessageObserver {
    private final String Tag = "DuiMessageObserver";

    public DuiMessageObserver() {
        mGson = new Gson();
    }

    public interface MessageCallback {
        void onMessage();

        void onState(String state);
    }

    private MessageCallback mMessageCallback;
    private LinkedList<MessageBean> mMessageList;
    private boolean mIsFirstVar = true;
    private boolean mHasvar = false;
    private Gson mGson;
    private String[] mSubscribeKeys = new String[]{
            "sys.dialog.state",
            "context.output.text",
            "context.input.text",
            "context.widget.content",
            "context.widget.list",
            "context.widget.web",
            "context.widget.media",
            "context.widget.custom"
    };

    // 注册当前更新消息
    public void regist(MessageCallback messageCallback, LinkedList<MessageBean> msgList) {
        mMessageCallback = messageCallback;
        mMessageList = msgList;
        DDS.getInstance().getAgent().subscribe(mSubscribeKeys, this);
    }

    // 注销当前更新消息
    public void unregist() {
        DDS.getInstance().getAgent().unSubscribe(this);
    }

    private void clearVar() {
        if (mHasvar) {
            mMessageList.pollLast();
        }
    }

    @Override
    public void onMessage(String message, String data) {
        Log.d(Tag, "message : " + message + " data : " + data);
        MessageBean bean = null;
        switch (message) {
            case "context.output.text":
                clearVar();
                bean = new MessageBean();
                String txt = "";
                try {
                    JSONObject jo = new JSONObject(data);
                    txt = jo.optString("text", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bean.setText(txt);
                bean.setType(MessageBean.TYPE_OUTPUT);
                mMessageList.add(bean);
                if (mMessageCallback != null) {
                    mMessageCallback.onMessage();
                }
                break;
            case "context.input.text":
                bean = new MessageBean();
                try {
                    JSONObject jo = new JSONObject(data);
                    if (jo.has("var")) {
                        String var = jo.optString("var", "");
                        if (mIsFirstVar) {
                            mIsFirstVar = false;
                            mHasvar = true;
                            bean.setText(var);
                            bean.setType(MessageBean.TYPE_INPUT);
                            mMessageList.add(bean);
                            if (mMessageCallback != null) {
                                mMessageCallback.onMessage();
                            }
                        } else {
                            mMessageList.pollLast();
                            bean.setText(var);
                            bean.setType(MessageBean.TYPE_INPUT);
                            mMessageList.add(bean);
                            if (mMessageCallback != null) {
                                mMessageCallback.onMessage();
                            }
                        }
                    }
                    if (jo.has("text")) {
                        if (mHasvar) {
                            mMessageList.pollLast();
                            mHasvar = false;
                            mIsFirstVar = true;
                        }
                        String text = jo.optString("text", "");
                        bean.setText(text);
                        bean.setType(MessageBean.TYPE_INPUT);
                        mMessageList.add(bean);
                        if (mMessageCallback != null) {
                            mMessageCallback.onMessage();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "context.widget.content":
                bean = new MessageBean();
                try {
                    JSONObject jo = new JSONObject(data);
                    String title = jo.optString("title", "");
                    String subTitle = jo.optString("subTitle", "");
                    String imgUrl = jo.optString("imageUrl", "");
                    bean.setTitle(title);
                    bean.setSubTitle(subTitle);
                    bean.setImgUrl(imgUrl);
                    bean.setType(MessageBean.TYPE_WIDGET_CONTENT);
                    mMessageList.add(bean);
                    if (mMessageCallback != null) {
                        mMessageCallback.onMessage();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "context.widget.list":
                bean = new MessageBean();
                try {
                    JSONObject jo = new JSONObject(data);
                    JSONArray array = jo.optJSONArray("content");
                    if (array == null || array.length() == 0) {
                        return;
                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.optJSONObject(i);
                        String title = object.optString("title", "");
                        String subTitle = object.optString("subTitle", "");
                        MessageBean b = new MessageBean();
                        b.setTitle(title);
                        b.setSubTitle(subTitle);
                        bean.addMessageBean(b);
                    }
                    int currentPage = jo.optInt("currentPage");
                    bean.setCurrentPage(currentPage);
                    bean.setType(MessageBean.TYPE_WIDGET_LIST);

                    int itemsPerPage = jo.optInt("itemsPerPage");
                    bean.setItemsPerPage(itemsPerPage);

                    mMessageList.add(bean);

                    if (mMessageCallback != null) {
                        mMessageCallback.onMessage();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "context.widget.web":
                bean = new MessageBean();
                try {
                    JSONObject jo = new JSONObject(data);
                    String url = jo.optString("url");
                    bean.setUrl(url);
                    bean.setType(MessageBean.TYPE_WIDGET_WEB);
                    mMessageList.add(bean);
                    if (mMessageCallback != null) {
                        mMessageCallback.onMessage();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "context.widget.custom":
                bean = new MessageBean();
                try {
                    JSONObject jo = new JSONObject(data);
                    String name = jo.optString("name");
                    if (name.equals("weather")) {
                        bean.setWeatherBean(mGson.fromJson(data, WeatherBean.class));
                        bean.setType(MessageBean.TYPE_WIDGET_WEATHER);
                        mMessageList.add(bean);
                        if (mMessageCallback != null) {
                            mMessageCallback.onMessage();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "context.widget.media":
                JSONObject jsonObject;
                int count = 0;
                String name = "";
                try {
                    jsonObject = new JSONObject(data);
                    count = jsonObject.optInt("count");
                    name = jsonObject.optString("widgetName");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (count > 0) {
                    Intent intent = new Intent(DuiApplication.getContext(), PlayerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("data", data);
                    DuiApplication.getContext().startActivity(intent);
                }
                break;
            case "sys.dialog.state":
                if (mMessageCallback != null) {
                    mMessageCallback.onState(data);
                }
                break;
            default:
        }
    }

}
