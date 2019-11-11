package com.aispeech.h5demo.test.tts;

import android.media.AudioManager;
import android.util.Log;

//import com.aispeech.bean.CustomAudioBean;
import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.tts.TTSEngine;
import com.aispeech.dui.dds.agent.tts.bean.CustomAudioBean;
import com.aispeech.dui.dds.agent.wakeup.word.WakeupWord;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aispeech.h5demo.test.BaseTestActivity;
import com.aispeech.h5demo.test.ListItem;

import java.util.ArrayList;
import java.util.List;

public class TTSTestActivity extends BaseTestActivity {
    private final String TAG = "TTSTestActivity";
    @Override
    protected void initData() {
        mData.add(new ListItem<>("TTS事件回调", null));
        mData.add(new ListItem<>("语音播报-text-优先级1", null));
        mData.add(new ListItem<>("语音播报-text-优先级2", null));
        mData.add(new ListItem<>("语音播报-text-优先级3", null));
        mData.add(new ListItem<>("语音播报-SSML", null));
        mData.add(new ListItem<>("停止播报ttsId-10001", null));
        mData.add(new ListItem<>("停止当前播报", null));
        mData.add(new ListItem<>("停止所有播报", null));
        mData.add(new ListItem<>("设置TTS引擎为本地", null));
        mData.add(new ListItem<>("设置TTS引擎为云端", null));
        mData.add(new ListItem<>("设置发音人-zhilingf", null));
        mData.add(new ListItem<>("设置发音人-gdgm", null));
        mData.add(new ListItem<>("设置发音人-feyinf", null));
        mData.add(new ListItem<>("设置发音人-boy", null));
        mData.add(new ListItem<>("设置发音人-自定义合成类型geyou", null));
        mData.add(new ListItem<>("设置语速-0.8", null));
        mData.add(new ListItem<>("设置音量-10", null));
        mData.add(new ListItem<>("设置TTS播报的通道", null));
        mData.add(new ListItem<>("设置TTS播报自定义录音",null));
        mData.add(new ListItem<>("设备抢焦点", null));
        mData.add(new ListItem<>("设备不抢焦点", null));
    }

    @Override
    protected void onItemClick(ListItem item) {
        switch (item.getTitle()) {
            case "TTS事件回调":
                setTTSListener();
                break;
            case "语音播报-text-优先级1":
                speakTextWithPriorityOne();
                break;
            case "语音播报-text-优先级2":
                speakTextWithPriorityTwo();
                break;
            case "语音播报-text-优先级3":
                speakTextWithPriorityThree();
                break;
            case "语音播报-SSML":
                speakSSML();
                break;
            case "停止播报ttsId-10001":
                shupWithTTSId();
                break;
            case "停止当前播报":
                shupWithZero();
                break;
            case "停止所有播报":
                shupWithNULL();
                break;
            case "设置TTS引擎为本地":
                setLocal();
                break;
            case "设置TTS引擎为云端":
                setCloud();
                break;
            case "设置发音人-zhilingf":
                setSpeaker1();
                break;
            case "设置发音人-gdgm":
                setSpeaker2();
                break;
            case "设置发音人-feyinf":
                setSpeaker3();
                break;
            case "设置发音人-boy":
                setSpeaker4();
                break;
            case "设置发音人-自定义合成类型geyou":
                setCustomSpeaker();
                break;
            case "设置语速-0.8":
                setSpeed();
                break;
            case "设置音量-10":
                setVolume();
                break;
            case "设置TTS播报的通道":
                setStreamType();
                break;
            case "设置TTS播报自定义录音":
                setCustomAudio();
                break;
            case "设备抢焦点":
                enableFocus();
                break;
            case "设备不抢焦点":
                disableFocus();
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
        }
    }

    private void setTTSListener() {
        try {
            DDS.getInstance().getAgent().getTTSEngine().setListener(new TTSEngine.Callback() {
                /**
                 * 开始合成时的回调
                 * @param ttsId 当前TTS的id， 对话过程中的播报ttsid默认为0，通过speak接口调用的播报，ttsid由speak接口指定。
                 */
                @Override
                public void beginning(String ttsId) {
                    Log.d(TAG, "TTS开始播报");
                }
                /**
                 * 合成的音频数据的回调，可能会返回多次，data长度为0表示音频结束
                 * @param data 音频数据
                 */
                @Override
                public void received(byte[] data) {
                    Log.d(TAG, "收到音频，此方法会回调多次，直至data为0，音频结束 data:"+data);
                }
                /**
                 * TTS播报完成的回调
                 * @param status 播报结束的状态。
                 *               正常播报结束为0
                 *               播报中途被打断结束为1
                 */
                @Override
                public void end(String ttsId, int status) {
                    Log.d(TAG, "TTS播报结束");
                }
                /**
                 * 合成过程中出现错误的回调
                 * @param error 错误信息
                 */
                @Override
                public void error(String error) {
                    Log.d(TAG, "出现错误，"+error);
                }
            });
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void speakTextWithPriorityOne() {

        try {
            DDS.getInstance().getAgent().getTTSEngine().speak("苏州思必驰信息科技有限公司 [1]  是一家语音技术公司。2007年，思必驰创立在英国剑桥高新区。思必驰专注于将领先的系列智能语音技术应用于移动互联、智能设备、客户联络中心等行业。公司的中文名字是思必驰，思想必将驰骋，寓意着公司独立自由的思想，以创新为本。同时这个名字还是英文单词Speech的音译，同样代表了公司立足智能语音技术行业的决心。公司的英文名字是AI Speech，其中AI是Artificial Intelligent的缩写，其意思就是“人工智能的”，Speech代表了“语音技术”。", 1, "10001", AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void speakTextWithPriorityTwo() {

        try {
            DDS.getInstance().getAgent().getTTSEngine().speak("人工智能（Artificial Intelligence），英文缩写为AI。它是研究、开发用于模拟、延伸和扩展人的智能的理论、方法、技术及应用系统的一门新的技术科学。", 2, "10002", AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void speakTextWithPriorityThree() {

        try {
            DDS.getInstance().getAgent().getTTSEngine().speak("用来研究人工智能的主要物质基础以及能够实现人工智能技术平台的机器就是计算机，人工智能的发展历史是和计算机科学技术的发展史联系在一起的。除了计算机科学以外，人工智能还涉及信息论、控制论、自动化、仿生学、生物学、心理学、数理逻辑、语言学、医学和哲学等多门学科。人工智能学科研究的主要内容包括：知识表示、自动推理和搜索方法、机器学习和知识获取、知识处理系统、自然语言理解、计算机视觉、智能机器人、自动程序设计等方面。", 3, "10003", AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void speakSSML() {

        //SSML播报
        String test1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<speak>\n" +
                "    思必驰成立于2007年英国剑桥高新区，创始人均来自剑桥，2008年回国落户苏州。\n" +
                "    \n" +
                "    <prosody volume=\"x-loud\"> \n" +
                "        " +
                "是拥有人机对话技术，国际上极少数拥有自主产权、中英文综合语音技术（语音识别、语音合成、自然语言理解、智能交互决策、声纹识别、性别及年龄识别、情绪识别等）的公司之一,\n" +
                "    </prosody> \n" +
                "     \n" +
                "    <prosody rate=\"x-slow\">\n" +
                "    其语音识别、声纹识别、口语对话系统等技术曾经多次在美国国家标准局、美国国防部、国际研究机构评测中夺得冠军，\n" +
                "    </prosody>\n" +
                "    <prosody pitch=\"+5%\">\n" +
                "        代表了技术的国际前沿水平,\n" +
                "    </prosody>\n" +
                "    \n" +
                "    <prosody pitch=\"-10%\">\n" +
                "        被中国和英国政府评为高新技术企业。\n" +
                "    </prosody>\n" +
                "    \n" +
                "</speak>";

        try {
            DDS.getInstance().getAgent().getTTSEngine().speak(test1,1,"ssml");
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void shupWithTTSId() {

        try {
            DDS.getInstance().getAgent().getTTSEngine().shutup("10001");
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }

    }

    private void shupWithZero() {
        try {
            DDS.getInstance().getAgent().getTTSEngine().shutup("0");
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void shupWithNULL() {
        try {
            DDS.getInstance().getAgent().getTTSEngine().shutup("");
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void setLocal() {

        try {
            DDS.getInstance().getAgent().getTTSEngine().setMode(TTSEngine.LOCAL);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void setCloud() {

        try {
            DDS.getInstance().getAgent().getTTSEngine().setMode(TTSEngine.CLOUD);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void setSpeaker1() {

        try {
            DDS.getInstance().getAgent().getTTSEngine().setSpeaker("zhilingf");
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void setSpeaker2() {

        try {
            DDS.getInstance().getAgent().getTTSEngine().setSpeaker("gdgm");
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void setSpeaker3() {

        try {
            DDS.getInstance().getAgent().getTTSEngine().setSpeaker("feyinf");
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void setSpeaker4() {
        try {
            DDS.getInstance().getAgent().getTTSEngine().setSpeaker("boy");
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void setCustomSpeaker() {

        //设置发音人
        try {
            DDS.getInstance().getAgent().getTTSEngine().setSpeaker("geyou"," geyoum_common_back_ce_local.v2.1.0.bin");
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }



    private void setSpeed() {

        try {
            DDS.getInstance().getAgent().getTTSEngine().setSpeed(0.8f);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void setVolume() {
        try {
            DDS.getInstance().getAgent().getTTSEngine().setVolume(5);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void setStreamType() {

        try {
            DDS.getInstance().getAgent().getTTSEngine().setStreamType(AudioManager.STREAM_MUSIC);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void setCustomAudio() {

        CustomAudioBean audioBean = new CustomAudioBean();
        audioBean.setName("我在，有什么可以帮你");
        String filePath = "/sdcard/nhxhua.mp3";
        Log.d("xlg","filePath:"+filePath);
        audioBean.setPath(filePath);
//        audioBean.setPath("nhxhua.mp3");

        CustomAudioBean audioBean1 = new CustomAudioBean();
        audioBean1.setName("开始为您导航");
        String filePath1 = "/sdcard/xiaole.wav";
        Log.d("xlg","filePath:"+filePath);
        audioBean1.setPath(filePath1);

        ArrayList customAudioList = new ArrayList();
        customAudioList.add(audioBean);
        customAudioList.add(audioBean1);

        try {
            DDS.getInstance().getAgent().getTTSEngine().setCustomAudio(customAudioList);
            toMain();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void enableFocus() {
        try {
            DDS.getInstance().getAgent().getTTSEngine().enableFocus(true);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void disableFocus() {
        try {
            DDS.getInstance().getAgent().getTTSEngine().enableFocus(false);
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
//    private void {
//
//    }

}
