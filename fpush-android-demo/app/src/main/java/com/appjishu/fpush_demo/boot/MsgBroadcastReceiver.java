package com.appjishu.fpush_demo.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.appjishu.fpush_demo.constant.SysConstant;

@Deprecated
public class MsgBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String desc = intent.getStringExtra(SysConstant.KEY_MSG_DESC);
        Toast.makeText(context, desc, Toast.LENGTH_SHORT).show();
    }
}
