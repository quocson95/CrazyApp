package com.example.quocs.crazyapp;

/**
 * Created by quocs on 3/26/2017.
 */

public final class DefineCode {
    public DefineCode(){};
    public final int MAX_SPEED = 255;;
    //define directory
    public final int CTRL_GO_UP = 1;
    public final int CTRL_GO_DOWN = 2;
    public final int CTRL_GO_LEFT = 3;
    public final int CTRL_GO_RIGHT = 4;

    public final int CTRL_STOP_UP = 5;
    public final int CTRL_STOP_DOWN = 6;
    public final int CTRL_STOP_LEFT = 7;
    public final int CTRL_STOP_RIGHT = 8;

    public final int STBY = 10; //standby

    //Motor A
    public final int PWMA = 3; //Speed control
    public final int AIN1 = 8; //Direction
    public final int AIN2 = 9; //Direction

    //Motor B
    public final int PWMB = 5 ;//Speed control
    public final int BIN1 = 11 ;//Direction
    public final int BIN2 = 12 ;//Direction

    //Define Type Stop Code
    public final int CODE_STOP_UP = 1;
    public final int CODE_STOP_DOWN = 2;
    public final int CODE_STOP_TURN = 3;

    //define State
    public final int STATE_IDLE = 0;
    public final int STATE_UP = 1;
    public final int STATE_DOWN = 2;
    public final int STATE_LEFT_CIRCLE = 3;
    public final int STATE_RIGHT_CIRCLE = 4;
    public final int STATE_LEFT_FWD = 5;
    public final int STATE_RIGHT_FWD = 6;
    public final int STATE_LEFT_REV = 7;
    public final int STATE_RIGHT_REV = 8;
    public final int STATE_STOP_HANDLE = 9;
}