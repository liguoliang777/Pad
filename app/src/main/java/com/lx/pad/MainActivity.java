package com.lx.pad;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lx.pad.component.MyGameActivity;
import com.lx.pad.util.LLog;
import com.ngame.Utils.KeyMgrUtils;
import com.ngame.app.KeyboardEditActivity;
import com.ngame.app.PadToolGuideActivity;
import com.ngds.pad.Msg.LooperEventManager;
import com.ngds.pad.Msg.LooperThread;
import com.ngds.pad.PadServiceBinder;
import com.ngds.pad.server.DeviceManager;
import com.ngds.pad.server.PadService;
import com.ngds.pad.utils.ActivityMgrUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int MSG_CMD_START_KEYBOARD_EDIT_ACTIVITY = 1;   //启动按键设置activity的消息号
    public static final int MSG_CMD_BLE_CONNECT_STATE = 2;              //通知蓝牙连接状态
    public static final int MSG_CMD_REMOTE_SOCKET_STATE = 3;            //通知晨风射socket连接状态

    public static final int MSG_VAL_BLE_NONE = 0;
    public static final int MSG_VAL_BLE_CONNECTED = 1;

    public static final int MSG_VAL_SOCKET_CLOSE = 0;
    public static final int MSG_VAL_SOCKET_CONNECTED = 1;


    TextView tv = null;
    Button btnStartGame = null;
    Button btnStartService = null;
    Button btnTestKeyEditActivity = null;
    Button btnTestPromptView = null;
    Button btnTestMsgQueue = null;

    boolean bleConnect = false;
    boolean socketConnect = false;

    private static MainActivity activity = null;
    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {    //这个handler只是测试代码的，不
//            super.handleMessage(msg);
            if (activity != null) {
                if(msg.what == MSG_CMD_START_KEYBOARD_EDIT_ACTIVITY){
                    final KeyboardEditActivity ac = KeyboardEditActivity.getInstance();
                    if(ac == null || ac.isFinishing()) {
                        Intent intent = new Intent(activity, KeyboardEditActivity.class);
                        activity.startActivity(intent);
                    }else{
                        ac.finish();
                    }
                }else if(msg.what == MSG_CMD_BLE_CONNECT_STATE){
                    //ble state connected
    //                Toast.makeText(MyApplication.getContextObj(), "蓝牙连接成功!", Toast.LENGTH_LONG);
                    LLog.d("ble connect success! <<===========================");
                    if(msg.arg1 == MSG_VAL_BLE_CONNECTED) {
                        activity.bleConnect = true;
                    }else if(msg.arg1 == MSG_VAL_BLE_NONE){
                        activity.bleConnect = false;
                    }
                    activity.updateStatePrompt();
                }else if(msg.what == MSG_CMD_REMOTE_SOCKET_STATE){
                    if(msg.arg1 == MSG_VAL_SOCKET_CONNECTED){
                        activity.socketConnect = true;
                    }else if(msg.arg1 == MSG_VAL_SOCKET_CLOSE){
                        activity.socketConnect = false;
                    }
                    activity.updateStatePrompt();
                }
            }
        }
    };

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public static void sendHandleMsg(int cmd, int val){
        Message msg = new Message();
        msg.what = cmd;
        msg.arg1 = val;
        handler.sendMessage(msg);
    }

    public void updateStatePrompt(){
        String sBle = "蓝牙未连接";
        String sSocket = "InjectServer未运行";
        if(bleConnect){
            sBle = "蓝牙连接成功";
        }
        if(socketConnect){
            sSocket = "InjectServer开启成功";
        }

        tv.setText(sBle + " | " + sSocket);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   //设置全屏
        setContentView(R.layout.activity_main);

        activity = this;

        // Example of a call to a native method
        tv = (TextView) findViewById(R.id.sample_text);
        btnStartGame = (Button)findViewById(R.id.btn_startGame);
        btnStartService = (Button)findViewById(R.id.btn_startService);
        btnTestKeyEditActivity = (Button)findViewById(R.id.btn_testKeyEditActivity);
        btnTestPromptView = (Button)findViewById(R.id.btn_testPromptView);
        btnTestMsgQueue = findViewById(R.id.btn_testMsgQueue);

//        looperThread = new LooperThread();
//        looperThread.start();
        LooperEventManager.init();

        updateStatePrompt();
//        tv.setText(stringFromJNI());
        btnStartGame.setOnClickListener(this);
        btnStartService.setOnClickListener(this);
        btnTestKeyEditActivity.setOnClickListener(this);
        btnTestPromptView.setOnClickListener(this);
        btnTestMsgQueue.setOnClickListener(this);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_startGame:{
                //测试代码，打开游戏选择界面
                startGame();
                break;
            }
            case R.id.btn_startService:{
                //启动服务，服务中会启动对应的蓝牙连接和映射socket的连接，初始华服务的代码
                PadServiceBinder.getInstance(this).initArgs("");
                break;
            }
            case R.id.btn_testKeyEditActivity:{
                //测试代码，打开按键编辑界面activity   ---------  此功能己实现通过按键来呼出和关闭，这里的仅是测试代码
                Intent intent = new Intent(this, KeyboardEditActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_testPromptView:{
                //测试代码，打开按键提示界面   ---------  此功能己实现通过按键来呼出和关闭，这里的仅是测试代码
                KeyMgrUtils.promptFrameLayoutView(this);
                break;
            }
            case R.id.btn_testMsgQueue:{
//                for(int i = 0; i < 100; i++) {
//                    LooperEventManager.sendEventMsg(i, nCount++);
//                    LLog.d("MainActivity->onClick for send i:" + i);
//                }
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
//        PadServiceBinder.getInstance(this).unRegisterAndUnbindService();
        super.onDestroy();
    }

    public void startGame(){
        //首先需要判断后台映射代码是否己启动

        //判断手柄是否己连接

        //开始游戏
        ComponentName componentName = new ComponentName(this, MyGameActivity.class);
        Intent intent = new Intent();
        intent.setComponent(componentName);
        startActivity(intent);
    }

}
