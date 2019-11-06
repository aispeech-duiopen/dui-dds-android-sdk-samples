package com.aispeech.h5demo.test.wakeup;

import android.util.Log;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.wakeup.word.WakeupWord;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aispeech.h5demo.test.BaseTestActivity;
import com.aispeech.h5demo.test.ListItem;

import java.util.List;

public class WakeupTestActivity extends BaseTestActivity {

    @Override
    protected void initData() {
        mData.add(new ListItem<>("添加主唤醒词", null));
        mData.add(new ListItem<>("删除主唤醒词", null));
        mData.add(new ListItem<>("获取主唤醒词", null));
    }

    @Override
    protected void onItemClick(ListItem item) {
        switch (item.getTitle()) {
            case "添加主唤醒词":
                addMain();
                break;
            case "删除主唤醒词":
                removeMain();
                break;
            case "获取主唤醒词":
                getMain();
                break;
        }
    }

    private void addMain() {
        try {
            WakeupWord minorWord = new WakeupWord()
                    .setPinyin("ni hao xiao ming")
                    .setWord("你好小明")
                    .setThreshold("0.15")
                    .addGreeting("小明又回来了");
            DDS.getInstance().getAgent().getWakeupEngine().addMainWakeupWord(minorWord);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void removeMain() {
        try {
            WakeupWord minorWord = new WakeupWord()
                    .setPinyin("ni hao xiao ming")
                    .setWord("你好小明");
            DDS.getInstance().getAgent().getWakeupEngine().removeMainWakeupWord(minorWord);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void getMain() {
        try {
            List<WakeupWord> wordList = DDS.getInstance().getAgent().getWakeupEngine().getMainWakeupWords();
            Log.d(TAG, "wordList = " + wordList.toString());
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }
}
