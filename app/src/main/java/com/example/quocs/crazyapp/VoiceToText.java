package com.example.quocs.crazyapp;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by quocs on 3/19/2017.
 */

public class VoiceToText implements RecognitionListener{
    private static String Log_Tag = "Voice_Service";
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private TextView txt_voice;
    private Context context;
    private boolean isListen = false;
    public VoiceToText(TextView txt_voice,Context context) {
        this.txt_voice = txt_voice;
        this.context = context;
    }

    public void startListenVoice(){
        speech = SpeechRecognizer.createSpeechRecognizer(this.context);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.context.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        speech.startListening(recognizerIntent);
        isListen = true;
    }
    public void stopListening(){
        if (isListen) {
            speech.stopListening();
            speech.destroy();
        }
        isListen = false;
    }
    @Override
    public void onBeginningOfSpeech() {
        Log.i(Log_Tag, "onBeginningOfSpeech");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(Log_Tag, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(Log_Tag, "onEndOfSpeech");
        new CountDownTimer(300, 1000) {

            public void onTick(long millisUntilFinished) {
                speech.stopListening();
                Log.i(Log_Tag,"countdown starting ...");
            }

            public void onFinish() {
                speech.startListening(recognizerIntent);
            }
        }.start();
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(Log_Tag, "FAILED " + errorMessage);
        txt_voice.setText(errorMessage);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(Log_Tag, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(Log_Tag, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(Log_Tag, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(Log_Tag, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches)
            text += result + "\n";

        txt_voice.setText(text);

    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(Log_Tag, "onRmsChanged: " + rmsdB);
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}
