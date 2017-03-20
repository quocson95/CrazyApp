package com.example.quocs.crazyapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainControl extends Activity {
    private int STOP = 0;
    private int UP = 1;
    private int DOWN = 2;
    private int LEFT = 3;
    private int RIGHT = 4;
    private int state = 0;
    private VoiceToText voiceToText;
    private String Log_Tag = "Main_Control";
    private Button btn_up,btn_down,btn_left,btn_right,btn_stop;
    private TextView txt_control,txt_voice;
    private ToggleButton tgg_voice;
    private RadioButton radio_voice;
    private BluetoothService bluetoothService;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_control);
        initUI();

        setTouchButton(btn_stop,STOP);
        setTouchButton(btn_up,UP);
        setTouchButton(btn_down,DOWN);
        setTouchButton(btn_left,LEFT);
        setTouchButton(btn_right,RIGHT);
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
                radio_voice.setChecked(state==1?true:false);
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
    private void initUI() {
        //Init Button
        btn_stop = (Button) findViewById(R.id.btn_stop);
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
               } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.i(Log_Tag,"release" +type);
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
                if (bluetoothService.creatConnectBluetooth()) {
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
