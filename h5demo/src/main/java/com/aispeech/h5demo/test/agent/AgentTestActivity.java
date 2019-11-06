package com.aispeech.h5demo.test.agent;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aispeech.h5demo.test.BaseTestActivity;
import com.aispeech.h5demo.test.ListItem;

public class AgentTestActivity extends BaseTestActivity {

    @Override
    protected void initData() {
        mData.add(new ListItem<>("文本请求", null));
    }

    @Override
    protected void onItemClick(ListItem item) {
        switch (item.getTitle()) {
            case "文本请求":
                sendText();
                break;
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
}
