package com.capstone.objdetection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Locale;

public class LauncherActivity extends AppCompatActivity {

    private Button btnEmergency, btnCamera;
    public static final Integer RecordAudioRequestCode = 1;
    private EditText etNumber;
    private ConstraintLayout layout;

    private SpeechRecognizer speechRecognizer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        btnEmergency = findViewById(R.id.btnEmergency);
        btnCamera = findViewById(R.id.btnCamera);
        etNumber = findViewById(R.id.etNumber);
        layout = findViewById(R.id.layout);

        init();

    }

    private void init() {
        etNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable data) {
                SessionClass.saveMobileNumber(getApplicationContext(), data.toString());
            }
        });

        btnEmergency.setOnClickListener(v -> {
            goToCallFunction();
        });

        btnCamera.setOnClickListener(v -> {
            Intent myIntent = new Intent(LauncherActivity.this, DetectorActivity.class);
            LauncherActivity.this.startActivity(myIntent);
        });

        layout.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                sendVoiceCommand();
        }
    }

    private void goToCallFunction() {
        if(validate()){
            //----------------------when the call button is pressed-----------------------------
            final int REQUEST_PHONE_CALL = 1;
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            String mobileNo = SessionClass.getMobileNumber(getApplicationContext());
            callIntent.setData(Uri.parse("tel:" + mobileNo));
            //-----------------checks for permission before placing the call--------------------
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ContextCompat.checkSelfPermission(LauncherActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(LauncherActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                }else{
                    //-------------------------------places the call-----------------------------------
                    startActivity(callIntent);
                }
            }
        }else{
            Toast.makeText(getApplicationContext(), "Enter a mobile number", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendVoiceCommand() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizer.startListening(speechRecognizerIntent);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(data.contains("call")){
                    goToCallFunction();
                }else if(data.contains("camera")){
                    Intent myIntent = new Intent(LauncherActivity.this, DetectorActivity.class);
                    LauncherActivity.this.startActivity(myIntent);
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    private boolean validate() {
        if (SessionClass.getMobileNumber(getApplicationContext()) == null) {
            return false;
        }
        else if (SessionClass.getMobileNumber(getApplicationContext()).isEmpty()) {
            return false;
        }
        else if (SessionClass.getMobileNumber(getApplicationContext()).length() < 10) {
            Toast.makeText(getApplicationContext(), "Mobile number count is less than 10", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /*@Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if(data.contains("call")){
            goToCallFunction();
        }else if(data.contains("camera")){
            Intent myIntent = new Intent(LauncherActivity.this, DetectorActivity.class);
            LauncherActivity.this.startActivity(myIntent);
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }*/
}