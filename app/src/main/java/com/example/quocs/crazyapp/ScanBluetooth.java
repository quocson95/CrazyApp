package com.example.quocs.crazyapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;

public class ScanBluetooth extends Activity {
    private String Log_Tag = "Scan_Bluetooth";
    private int  REQUEST_ENABLE_BT = 1;
    private int BL_CMD_YES = -1;
    private int BL_CMD_NO = 0;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> pairedDevicesArrayAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ListView listPairDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bluetooth);
        listPairDevice = (ListView) findViewById(R.id.list_item);
        pairedDevicesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listPairDevice.setAdapter(pairedDevicesArrayAdapter);
        showListPair();

        listPairDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String info = pairedDevicesArrayAdapter.getItem(position).toString();
                String address = info.substring(info.length() - 17);
                Log.i(Log_Tag,"You select ");
                Log.i(Log_Tag,address);
                Intent intent = new Intent(ScanBluetooth.this,MainControl.class);
                Bundle bundle = new Bundle();
                bundle.putString("MACADDR",address);
                intent.putExtra("BluetoothPackage",bundle);
                startActivity(intent);
            }
        });
    }

    private void showListPair(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null){
            Log.i(Log_Tag,"Your device does not support Bluetooth");
            Toast.makeText(getApplicationContext(),"Your device does not support Bluetooth",
                                                    Toast.LENGTH_LONG).show();
        }
        else if (!bluetoothAdapter.isEnabled()){
            Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);
        }
        else {
            displayPairDevice();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == REQUEST_ENABLE_BT){
           if (resultCode == BL_CMD_YES){
               Toast.makeText(getApplicationContext(),"Bluetooth turned on" ,
                       Toast.LENGTH_LONG).show();
               displayPairDevice();
           }
           else  if (resultCode == BL_CMD_NO){
               Toast.makeText(getApplicationContext(),"Bluetooth turned off" ,
                       Toast.LENGTH_LONG).show();
               pairedDevicesArrayAdapter.add("Bluetooth turned off");
               pairedDevicesArrayAdapter.add("Please turn on Bluetooth");
           }
       }
    }
    private void displayPairDevice() {
        pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0){
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            pairedDevicesArrayAdapter.add("No Device has pair");
        }
    }
}
