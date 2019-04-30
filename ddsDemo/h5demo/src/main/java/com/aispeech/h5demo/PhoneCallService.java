package com.aispeech.h5demo;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.aispeech.dui.dds.DDS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by djk
 * 上传通讯录到云端,只有云端获取到了本地上传的通讯录,云端在识别的时候才会能识别到联系人
 */
public class PhoneCallService extends Service {
    public static final String TAG = "PhoneCallService";
    private static final String TOPIC_UPLOAD_CONTACTS = "upload.contacts";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_PHONE = "phone";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_FLAG = "flag";
    private static final String DB_LOCATION = "location.db";
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = DuiApplication.getContext();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initCfg();
        return super.onStartCommand(intent, flags, startId);
    }

    private void tryGetDataBase() {
        File dbPath = mContext.getDatabasePath(DB_LOCATION);
        if (dbPath.exists()) { //skip if db exists
            return;
        }
        File dbDir = dbPath.getParentFile();
        if (!dbDir.exists()) { //mkdir for db
            dbDir.mkdir();
        }
        Log.e(TAG, "Copy " + DB_LOCATION + " to " + dbPath);
        InputStream istream = null;
        OutputStream ostream = null;
        try {
            istream = mContext.getAssets().open(DB_LOCATION);
            ostream = new FileOutputStream(dbPath);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = istream.read(buffer)) > 0) {
                ostream.write(buffer, 0, length);
            }
            ostream.close();
            istream.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (istream != null) {
                    istream.close();
                }
                if (ostream != null) {
                    ostream.close();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    // 监听当前电话状态
    private PhoneStateListener mListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.e(TAG, "CALL IN RINGING :" + incomingNumber);
                    DDS.getInstance().getAgent().publishSticky("dialog.ctrl", "close");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.e(TAG, "CALL IN ACCEPT :" + incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.e(TAG, "CALL IDLE");
                    break;
            }
        }
    };

    /**
     * 初始化observer
     */
    private void initCfg() {
        Log.e(TAG, "Start PhoneCall");
        tryGetDataBase();
        mContext.getContentResolver().registerContentObserver(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, true, mObserver);
        //listen phone call state
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
        updateContacts();
    }

    // 监听本地通讯录的改变
    private ContentObserver mObserver = new ContentObserver(
            new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            Log.e(TAG, "Contacts onChanged.");
            updateContacts();
        }
    };

    // 上传通讯录
    private void updateContacts() {
        //select contacts and publish
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (null == cursor) {
            Log.e(TAG, "contacts null");
            return;
        }
        SQLiteDatabase db = mContext.openOrCreateDatabase(DB_LOCATION, Context.MODE_PRIVATE, null);
        JSONArray contacts = new JSONArray();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "").replace("+86", "");
            String type = ContactsContract.CommonDataKinds.Phone.getTypeLabel(mContext.getResources(), cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)
            ), null).toString();
            String flag = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
            try {
                JSONObject obj = new JSONObject()
                        .put(PARAM_NAME, name)
                        .put(PARAM_PHONE, number)
                        .put(PARAM_TYPE, type)
                        .put(PARAM_FLAG, flag);
                Log.e(TAG, obj.toString(1));
                contacts.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        db.close();
        cursor.close();
        if (contacts.length() > 0) {
            Log.e(TAG, "upload.contacts:" + contacts.toString());
            // 将本地通讯录上传给云端
            DDS.getInstance().getAgent().publishSticky(TOPIC_UPLOAD_CONTACTS, contacts.toString(), UUID.randomUUID().toString());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
