package com.aispeech.nativedemo.test.wakeup;

import android.util.Log;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.wakeup.word.WakeupWord;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aispeech.nativedemo.test.BaseTestActivity;
import com.aispeech.nativedemo.test.ListItem;

import java.util.ArrayList;
import java.util.List;

public class WakeupTestActivity extends BaseTestActivity {
    private final String TAG = "WakeupTestActivity";
    @Override
    protected void initData() {
        mData.add(new ListItem<>("开启语音唤醒", null));
        mData.add(new ListItem<>("关闭语音唤醒", null));
        mData.add(new ListItem<>("添加一条主唤醒词-小明", null));
        mData.add(new ListItem<>("删除一条主唤醒词-小明", null));
        mData.add(new ListItem<>("添加多条主唤醒词-小红/天天", null));
        mData.add(new ListItem<>("删除多条主唤醒词-小红/天天", null));
        mData.add(new ListItem<>("更新多条主唤醒词", null));
        mData.add(new ListItem<>("获取主唤醒词", null));
        mData.add(new ListItem<>("更新副唤醒词-小乐", null));
        mData.add(new ListItem<>("获取副唤醒词", null));
        mData.add(new ListItem<>("实时添加一条命令唤醒词", null));
        mData.add(new ListItem<>("实时添加多条命令唤醒词", null));
        mData.add(new ListItem<>("实时移除一条命令唤醒词", null));
        mData.add(new ListItem<>("实时移除多条命令唤醒词", null));
        mData.add(new ListItem<>("实时更新一条命令唤醒词", null));
        mData.add(new ListItem<>("实时更新多条命令唤醒词", null));
        mData.add(new ListItem<>("清空当前设置的命令唤醒词", null));
        mData.add(new ListItem<>("实时更新一条打断唤醒词", null));
        mData.add(new ListItem<>("实时更新多条打断唤醒词", null));
        mData.add(new ListItem<>("清空当前设置的打断唤醒词", null));
        mData.add(new ListItem<>("唤醒词", null));
        mData.add(new ListItem<>("唤醒词", null));
        mData.add(new ListItem<>("唤醒词", null));
        mData.add(new ListItem<>("唤醒词", null));
        mData.add(new ListItem<>("唤醒词", null));
        mData.add(new ListItem<>("唤醒词", null));
        mData.add(new ListItem<>("唤醒词", null));
    }

    @Override
    protected void onItemClick(ListItem item) {
        switch (item.getTitle()) {
            case "开启语音唤醒":
                enableWakeup();
                break;
            case "关闭语音唤醒":
                disableWakeup();
                break;
            case "添加一条主唤醒词-小明":
                addMain();
                break;
            case "删除一条主唤醒词-小明":
                removeMain();
                break;
            case "添加多条主唤醒词-小红/天天":
                addMainWakeupWords();
                break;
            case "删除多条主唤醒词-小红/天天":
                removeMainWakeupWord();
                break;
            case "更新多条主唤醒词":
                updateMain();
                break;
            case "获取主唤醒词":
                getMain();
                break;
            case "更新副唤醒词-小乐":
                updateMinorWakeupWord();
                break;
            case "获取副唤醒词":
                getMinorWord();
                break;
            case "实时更新一条命令唤醒词":
                updateCommandWakeupWord();
                break;
            case "实时更新多条命令唤醒词":
                updateCommandWakeupWords();
                break;
            case "清空当前设置的命令唤醒词":
                clearCommandWakeupWord();
                break;
            case "实时更新一条打断唤醒词":

                break;
            case "实时更新多条打断唤醒词":

                break;
            case "清空当前设置的打断唤醒词":

                break;
//            case "":
//
//                break;
//            case "":
//
//                break;
//            case "":
//
//                break;
//            case "":
//
//                break;
//            case "":
//
//                break;
//            case "":
//
//                break;
//            case "":
//
//                break;
//            case "":
//
//                break;
//            case "":
//
//                break;
//            case "":
//
//                break;
        }
    }

    private void enableWakeup() {
        // 开启语音唤醒
        try {
            DDS.getInstance().getAgent().getWakeupEngine().enableWakeup();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void disableWakeup() {
        // 关闭语音唤醒
        try {
            DDS.getInstance().getAgent().getWakeupEngine().disableWakeup();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void addMain() {
        try {
            WakeupWord minorWord = new WakeupWord()
                    .setPinyin("hai xin xiao ju")
                    .setWord("海信小聚")
                    .setThreshold("0.15")
                    .addGreeting("小聚又回来了");
            DDS.getInstance().getAgent().getWakeupEngine().addMainWakeupWord(minorWord);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void removeMainWakeupWord() {
        try {
            WakeupWord minorWord = new WakeupWord()
                    .setPinyin("ni hao xiao ming")
                    .setWord("你好小明");
            DDS.getInstance().getAgent().getWakeupEngine().removeMainWakeupWord(minorWord);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void addMainWakeupWords() {
        try {
            //实时添加主唤醒词-添加多条主唤醒词(最新支持)
            WakeupWord mainWord = new WakeupWord()
                    .setPinyin("ni hao xiao hong")
                    .setWord("你好小红")
                    .addGreeting("小红在")
                    .setThreshold("0.15");
            WakeupWord mainWord2 = new WakeupWord()
                    .setPinyin("ni hao tian tian")
                    .setWord("你好天天")
                    .addGreeting("天天在")
                    .setThreshold("0.15");
            List<WakeupWord> mainWordList = new ArrayList<>();
            mainWordList.add(mainWord);
            mainWordList.add(mainWord2);
            DDS.getInstance().getAgent().getWakeupEngine().addMainWakeupWords(mainWordList);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void updateMain() {
        WakeupWord mainWord = new WakeupWord()
                .setPinyin("ni hao xiao le")
                .setWord("你好小乐") .setThreshold("0.15")
                .addGreeting("我在");
        WakeupWord mainWord2 = new WakeupWord()
                .setPinyin("ni hao xiao chi")
                .setWord("你好小迟")
                .setThreshold("0.15")
                .addGreeting("我在");
        List<WakeupWord> mainWordList = new ArrayList<>();
        mainWordList.add(mainWord); mainWordList.add(mainWord2);
        try {
            DDS.getInstance().getAgent().getWakeupEngine().updateMainWakeupWords(mainWordList);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void removeMain() {
        try {
            WakeupWord mainWord = new WakeupWord().setWord("你好小迟");
            WakeupWord mainWord2 = new WakeupWord().setWord("你好小乐");
            List<WakeupWord> mainWordList = new ArrayList<>();
            mainWordList.add(mainWord);
            mainWordList.add(mainWord2);
            DDS.getInstance().getAgent().getWakeupEngine().removeMainWakeupWords(mainWordList);
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

    private void updateMinorWakeupWord() {
        // 实时更新副唤醒词(最新支持)
        WakeupWord minorWord = new WakeupWord()
                .setPinyin("ni hao xiao le")
                .setWord("你好小乐")
                .setThreshold("0.15")
                .addGreeting("小乐在");
        try {
            DDS.getInstance().getAgent().getWakeupEngine().updateMinorWakeupWord(minorWord);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void getMinorWord() {
        //获取当前的副唤醒词
        try {
            String minorWakeupWord = DDS.getInstance().getAgent().getWakeupEngine().getMinorWakeupWord();
            Log.d(TAG,"minorWakeupWord: "+minorWakeupWord);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void updateCommandWakeupWord() {

        // 实时更新命令唤醒词-更新一条命令唤醒词(最新支持)
        WakeupWord commandWord = new WakeupWord()
                .setPinyin("xia yi shou")
                .setWord("下一首")
                .setThreshold("0.18")
                .addGreeting("我在")
                .setAction("sys.next");
        try {
            DDS.getInstance().getAgent().getWakeupEngine().updateCommandWakeupWord(commandWord);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }


    private void updateCommandWakeupWords() {

        // 实时更新命令唤醒词-更新多条命令唤醒词(最新支持)
        WakeupWord commandWord = new WakeupWord()
                .setPinyin("xia yi shou")
                .setWord("下一首")
                .setThreshold("0.18")
                .addGreeting("我在")
                .setAction("sys.next");
        WakeupWord commandWord1 = new WakeupWord()
                .setPinyin("shang yi shou")
                .setWord("上一首")
                .addGreeting("我在")
                .setThreshold("0.20")
                .setAction("sys.play");
        List<WakeupWord> commandList = new ArrayList<>();
        commandList.add(commandWord);
        commandList.add(commandWord1);
        try {
            DDS.getInstance().getAgent().getWakeupEngine().updateCommandWakeupWords(commandList);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }

    }

    private void clearCommandWakeupWord() {
        //清空当前设置的命令唤醒词
        try {
            DDS.getInstance().getAgent().getWakeupEngine().clearCommandWakeupWord();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

//    private void {
//
//    }
//
//    private void {
//
//    }
//
//    private void {
//
//    }
//
//    private void {
//
//    }
//
//    private void {
//
//    }
//
//    private void {
//
//    }
//
//    private void {
//
//    }
//
//    private void {
//
//    }



}
