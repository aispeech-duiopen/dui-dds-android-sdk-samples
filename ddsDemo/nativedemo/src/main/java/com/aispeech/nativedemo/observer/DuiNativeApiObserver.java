package com.aispeech.nativedemo.observer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import com.aispeech.dui.dds.DDS;
import com.aispeech.nativedemo.DuiApplication;
import com.aispeech.dui.dsk.duiwidget.ContentWidget;
import com.aispeech.dui.dsk.duiwidget.ListWidget;
import com.aispeech.dui.dsk.duiwidget.NativeApiObserver;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * 注册NativeApiObserver, 用于客户端响应DUI平台技能配置里的资源调用指令, 同一个NativeApiObserver可以处理多个native api.
 * 目前demo中实现了打电话的功能逻辑
 */
public class DuiNativeApiObserver implements NativeApiObserver {
    private String TAG = "DuiNativeApiObserver";
    private static final String NATIVE_API_CONTACT = "sys.query.contacts";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_PHONE = "phone";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_FLAG = "flag";
    private static final String DB_LOCATION = "location.db";
    private Context mContext;

    public DuiNativeApiObserver() {
        mContext = DuiApplication.getContext();
    }

    // 注册当前更新消息
    public void regist() {
        DDS.getInstance().getAgent().subscribe(new String[]{NATIVE_API_CONTACT},
                this);
    }

    // 注销当前更新消息
    public void unregist() {
        DDS.getInstance().getAgent().unSubscribe(this);
    }

    /*
     * onQuery方法执行时，需要调用feedbackNativeApiResult来向DUI平台返回执行结果，表示一个native api执行结束。
     * native api的执行超时时间为10s
     */
    @Override
    public void onQuery(String nativeApi, String data) {
        Log.e(TAG, "nativeApi: " + nativeApi + "  data: " + data);
        if (NATIVE_API_CONTACT.equals(nativeApi)) {
            String searchName = null;
            ListWidget searchNums = null;
            try {
                JSONObject obj = null;
                obj = new JSONObject(data);
                searchName = obj.optString("联系人");
                searchNums = searchContacts(searchName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "query back:" + searchName + "-" + (searchNums != null ? searchNums.toString() : "null"));
            DDS.getInstance().getAgent().feedbackNativeApiResult(nativeApi, searchNums);
        }
    }

    private ListWidget searchContacts(String searchName) {
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (null == cursor) {
            Log.e(TAG, "contacts null");
            return null;
        }
        ListWidget listWidget = new ListWidget();
        SQLiteDatabase db = mContext.openOrCreateDatabase(DB_LOCATION, Context.MODE_PRIVATE, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "").replace("+86", "");
            String type = ContactsContract.CommonDataKinds.Phone.getTypeLabel(mContext.getResources(), cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)
            ), null).toString();
            String flag = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
            if (name.contains(searchName)) {
                Log.e(TAG, name + ":" + number);
                ContentWidget widget = new ContentWidget()
                        .setTitle(name)
                        .setSubTitle(type + ":" + number)
                        .addExtra(PARAM_NAME, name)
                        .addExtra(PARAM_PHONE, number)
                        .addExtra(PARAM_TYPE, type)
                        .addExtra(PARAM_FLAG, flag);
                listWidget.addContentWidget(widget);
            }
        }
        db.close();
        cursor.close();
        return listWidget;
    }

}
