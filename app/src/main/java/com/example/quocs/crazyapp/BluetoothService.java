package com.example.quocs.crazyapp;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.UUID;

public class BluetoothService {

    private String Log_Tag = "Bluetooth_Service";
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private BluetoothSocket socket;
    private String MAC_ADDR_BL = "";
    private final IBinder binder = new LocalBluetoothService();

    public class LocalBluetoothService extends Binder {
        public BluetoothService getService()  {
            return BluetoothService.this;
        }
    }
    public BluetoothService() {
    }
    public void setMacAddrBL(String macAddr){
        this.MAC_ADDR_BL = macAddr;
    }
    public boolean creatConnectBluetooth(Context context){
        BluetoothAdapter bluetoothAdapter;
        BluetoothDevice device;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()){
            //TODO
        }
        device = bluetoothAdapter.getRemoteDevice(this.MAC_ADDR_BL);
        try {
            //socket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID_INSECURE);
            socket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        try {
            socket.connect();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean sendMsg(int msg){
        try {
            socket.getOutputStream().write(msg);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean isConnected(){
        if (socket != null)return socket.isConnected();
        else return true;
    }
}
