package com.example.quocs.crazyapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainControl extends Activity {
    private int state = 0;
    private VoiceToText voiceToText;
    private String Log_Tag = "Main_Control";
    private Button btn_up,btn_down,btn_left,btn_right,btn_stop;
    private TextView txt_control,txt_voice;
    private ToggleButton tgg_voice;
    private RadioButton radio_voice;
    private BluetoothService bluetoothService;
    private Handler handler;

    private  DefineCode defineCode = new DefineCode();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_control);
        initUI();
        //setTouchButton(btn_stop,STOP);
        setTouchButton(btn_up,defineCode.CTRL_GO_UP);
        setTouchButton(btn_down,defineCode.CTRL_GO_DOWN);
        float deg = btn_down.getRotation() + 180F;
        btn_down.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

        setTouchButton(btn_left,defineCode.CTRL_GO_LEFT);
        deg = btn_left.getRotation() + 270F;
        btn_left.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

        setTouchButton(btn_right,defineCode.CTRL_GO_RIGHT);
        deg = btn_right.getRotation() + 90F;
        btn_right.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

        bluetoothService = new BluetoothService();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BluetoothPackage");
        bluetoothService.setMacAddrBL(bundle.getString("MACADDR"));
        voiceToText = new VoiceToText(txt_voice,this);
        radio_voice.setChecked(false);
        radio_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (radio_voice.isChecked()== true) radio_voice.setChecked(false);
//                else radio_voice.setChecked(true);
                state = ((state + 1) % 2 );
                radio_voice.setChecked(state==1);
                if (radio_voice.isChecked()) {
                    voiceToText.startListenVoice();
                }
                else {
                    voiceToText.stopListening();
                }
            }
        });
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String str_msg = msg.obj.toString();
                txt_control.append("\n"+str_msg);
            }

        };
        creatBluetooth();
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.i(Log_Tag,"on Resume");
        if (!bluetoothService.isConnected()) {
            bluetoothService.creatConnectBluetooth(MainControl.this);
            txt_control.append("Trying connected");
        }
    }
    private void initUI() {
        //Init Button
        btn_up = (Button) findViewById(R.id.btn_up);
        btn_down = (Button) findViewById(R.id.btn_down);
        btn_left = (Button) findViewById(R.id.btn_left);
        btn_right = (Button) findViewById(R.id.btn_right);
        //Init TextView
        txt_control = (TextView) findViewById(R.id.txt_view);
        txt_voice = (TextView) findViewById(R.id.txt_voice);
        txt_control.setText("Connected");
        //Init Toggle button
        tgg_voice = (ToggleButton) findViewById(R.id.tgg_voice);
        //Init Radio
        radio_voice = (RadioButton) findViewById(R.id.radio_voice);
    }

    private void setTouchButton(Button btn,final int type) {
       btn.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               if(event.getAction() == MotionEvent.ACTION_DOWN) {
                   Log.i(Log_Tag,"press"+ type );
                   bluetoothService.sendMsg(type);
               } else if (event.getAction() == MotionEvent.ACTION_UP) {
                   if (type == defineCode.CTRL_GO_UP){
                       bluetoothService.sendMsg(defineCode.CTRL_STOP_UP);
                       Log.i(Log_Tag,"release" +defineCode.CTRL_STOP_UP);
                   }
                   else if (type == defineCode.CTRL_GO_DOWN){
                       bluetoothService.sendMsg(defineCode.CTRL_STOP_DOWN);
                       Log.i(Log_Tag,"release" +defineCode.CTRL_STOP_DOWN);
                   }
                   else if (type == defineCode.CTRL_GO_LEFT){
                       bluetoothService.sendMsg(defineCode.CTRL_STOP_LEFT);
                       Log.i(Log_Tag,"release" +defineCode.CTRL_STOP_LEFT);
                   }else if (type == defineCode.CTRL_GO_RIGHT){
                       bluetoothService.sendMsg(defineCode.CTRL_STOP_RIGHT);
                       Log.i(Log_Tag,"release" +defineCode.CTRL_STOP_RIGHT);
                   }


               }
               return false;
           }
       });
    }

    void creatBluetooth(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                if (bluetoothService.creatConnectBluetooth(MainControl.this)) {
                    msg.obj = "Connect success";
                    Log.i(Log_Tag,"Connect sucess");
                }
                else {
                    msg.obj = "Connect fail";
                    Log.i(Log_Tag,"Connect fail");
                }
                handler.sendMessage(msg);
            }
        });
        thread.start();
    }
}
