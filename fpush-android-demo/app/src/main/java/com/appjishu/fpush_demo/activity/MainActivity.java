package com.appjishu.fpush_demo.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appjishu.fpush_demo.R;
import com.appjishu.fpush_demo.boot.MsgService;
import com.appjishu.fpush_demo.constant.NetConstant;
import com.appjishu.fpush_demo.constant.SysConstant;

public class MainActivity extends AppCompatActivity {
    public static final int MSG_WHAT_MSG = 0;

    private static MainActivity instance = null;
    private MsgHandler msgHandler;
    private EditText hostEditText;
    private EditText httpPortEditText;
    private EditText tcpPortEditText;

    private Button connectBtn;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msgHandler = new MsgHandler();

        hostEditText = findViewById(R.id.host_field);
        httpPortEditText =findViewById(R.id.api_port_field);
        tcpPortEditText = findViewById(R.id.tcp_port_field);
        connectBtn = findViewById(R.id.connect_btn);
        connectBtn.setOnClickListener(new ButtonClickListener());

        instance = this;
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

    public class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String host = hostEditText.getText().toString();
            String httpPort = httpPortEditText.getText().toString();
            String tcpPort = tcpPortEditText.getText().toString();
            if (TextUtils.isEmpty(host)) {
                Toast.makeText(MainActivity.this, "请输入Host", Toast.LENGTH_SHORT).show();
                return;
            } else {
                NetConstant.PUSH_HOST = host;
            }
            if (TextUtils.isEmpty(httpPort)){
                Toast.makeText(MainActivity.this, "请输入HTTP_Port", Toast.LENGTH_SHORT).show();
                return;
            } else {
                NetConstant.API_PORT = Integer.valueOf(httpPort);
            }
            if (TextUtils.isEmpty(tcpPort)){
                Toast.makeText(MainActivity.this, "请输入TCP_Port", Toast.LENGTH_SHORT).show();
                return;
            } else {
                NetConstant.PUSH_PORT = Integer.valueOf(tcpPort);
            }

            Intent intent = new Intent(MainActivity.this, MsgService.class);
            MainActivity.this.startService(intent);
        }
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
