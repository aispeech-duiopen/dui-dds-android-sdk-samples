package com.aispeech.nativedemo.test.agent;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.Agent;
import com.aispeech.dui.dds.agent.tts.TTSEngine;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;

import com.aispeech.nativedemo.test.BaseTestActivity;
import com.aispeech.nativedemo.test.ListItem;

public class AgentTestActivity extends BaseTestActivity {

    @Override
    protected void initData() {
        mData.add(new ListItem<>("全双工", null));
        mData.add(new ListItem<>("半双工", null));
        mData.add(new ListItem<>("在线tts", null));
        mData.add(new ListItem<>("文本请求", null));
        mData.add(new ListItem<>("开启对话", null));
        mData.add(new ListItem<>("终止对话", null));
        mData.add(new ListItem<>("开启对话播报文本", null));
        mData.add(new ListItem<>("终止对话播报文本", null));
        mData.add(new ListItem<>("点击唤醒/停止识别/打断播报avatarClick", null));
        mData.add(new ListItem<>("avatarClick附带欢迎语", null));
//        mData.add(new ListItem<>("", null));
//        mData.add(new ListItem<>("", null));
//        mData.add(new ListItem<>("", null));
//        mData.add(new ListItem<>("", null));
//        mData.add(new ListItem<>("", null));
//        mData.add(new ListItem<>("", null));
//        mData.add(new ListItem<>("", null));
//        mData.add(new ListItem<>("", null));
//        mData.add(new ListItem<>("", null));
//        mData.add(new ListItem<>("", null));
//        mData.add(new ListItem<>("", null));
//        mData.add(new ListItem<>("", null));

    }

    @Override
    protected void onItemClick(ListItem item) {
        switch (item.getTitle()) {
            case "全双工":
                try {
                    DDS.getInstance().getAgent().setDuplexMode(Agent.DuplexMode.FULL_DUPLEX);
                } catch (DDSNotInitCompleteException e) {
                    e.printStackTrace();
                }
                break;
            case "半双工":
                try {
                    DDS.getInstance().getAgent().setDuplexMode(Agent.DuplexMode.HALF_DUPLEX);
                } catch (DDSNotInitCompleteException e) {
                    e.printStackTrace();
                }
                break;
            case "在线tts":
                try {
                    DDS.getInstance().getAgent().getTTSEngine().setMode(TTSEngine.CLOUD);
                } catch (DDSNotInitCompleteException e) {
                    e.printStackTrace();
                }
                break;
            case "文本请求":
                sendText();
                break;
            case "开启对话":
                sendText();
                break;
            case "终止对话":
                sendText();
                break;
            case "开启对话播报文本":
                sendText();
                break;
            case "终止对话播报文本":
                sendText();
                break;
            case "点击唤醒/停止识别/打断播报":
                sendText();
                break;
            case "avatarClick附带欢迎语":

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
        }
    }

    private void sendText() {
        try {
            DDS.getInstance().getAgent().sendText("苏州天气");
            toMain();
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
