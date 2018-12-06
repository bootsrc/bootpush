package com.appjishu.fpush_demo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.appjishu.fpush_demo.R;
import com.appjishu.fpush_demo.boot.MsgService;
import com.appjishu.fpush_demo.boot.MyApp;
import com.appjishu.fpush_demo.constant.SysConstant;

public class MainActivity extends AppCompatActivity {
    public static final int MSG_WHAT_MSG = 0;

    private static MainActivity instance = null;
    private MsgHandler msgHandler;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msgHandler = new MsgHandler();
        instance = this;

        Intent intent = new Intent(this, MsgService.class);
        startService(intent);

//        Intent intent1 = new Intent("msgBroadcastReceiver");
//        intent.putExtra(SysConstant.KEY_MSG_DESC, "XXXXXXXXXXXX");
//        sendBroadcast(intent1);
    }

//    public class InnerReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String desc = intent.getStringExtra(SysConstant.KEY_MSG_DESC);
//            Toast.makeText(context, desc, Toast.LENGTH_SHORT).show();
//        }
//    }

    public void sendMessage(Message message) {
        msgHandler.sendMessage(message);
    }

    public class MsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_MSG:
                    String title = msg.getData().getString(SysConstant.KEY_MSG_TITLE);
                    String desc = msg.getData().getString(SysConstant.KEY_MSG_DESC);
                    String text = "title:" + title + "\n" + "desc:" + desc;
                    Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
