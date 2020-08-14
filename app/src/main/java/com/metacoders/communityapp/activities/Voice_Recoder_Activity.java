package com.metacoders.communityapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.metacoders.communityapp.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Voice_Recoder_Activity extends AppCompatActivity {
    String mFileName  = null ;

    String name , ph ;

    private ImageButton listBtn;
    private ImageButton recordBtn;
    private TextView filenameText;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    private boolean isRecording = false;
    String uid ;
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE = 21;

    private MediaRecorder mediaRecorder;
    private String recordFile;
    String time  ;

    private Chronometer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice__recoder_);
        listBtn = findViewById(R.id.record_list_btn);
        recordBtn = findViewById(R.id.record_btn);
        timer = findViewById(R.id.record_timer);


        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(isRecording)
                {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null  ;
                    timer.stop();
                    timer.setBase(SystemClock.elapsedRealtime());
                    timer.stop();

                    isRecording = false ;
                    recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
                    uploadAudio();
                }
                else {
                    if(checkPermissions())
                    {

                        timer.setBase(SystemClock.elapsedRealtime());
                        timer.stop();

                        // above two line added just to  reset the clock bug ......
                        //if dont work delete it for own caution
                        startRecording() ;
                    }


                    // startRecording() ;

                }

            }
        });

    }

    private void uploadAudio() {
        final ProgressDialog dialog = new ProgressDialog(Voice_Recoder_Activity.this) ;
        Toast.makeText(getApplicationContext(), "Recordings Saved !!!"  ,Toast.LENGTH_SHORT).show();
        // get the uri ...
        Uri uri = Uri.fromFile(new File(mFileName));

        Intent post = new Intent(getApplicationContext() , PostUploadActivity.class);
        post.putExtra("path", uri.toString()) ;
        post.putExtra("media", "audio") ;
       startActivity(post);





    }

    private void startRecording() {
       String Root =  Environment.getExternalStorageDirectory().getPath() ;
        mFileName =Voice_Recoder_Activity.this.getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        Date now = new Date();
        time = formatter.format(now);
        mFileName = Root + "/recorded_audio"+formatter.format(now) +".aac" ;
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setOutputFile(mFileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);


        try{
            mediaRecorder.prepare();
        }
        catch (IOException ie)
        {
            Log.e("TAGE" , "Fail + "+ ie.getMessage()) ;
        }
        mediaRecorder.start();

        timer.start();
        // Change button image and set Recording state to false
        recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_recording, null));
        isRecording = true;

    }
    private boolean checkPermissions() {
        //Check permission
        if (ActivityCompat.checkSelfPermission(Voice_Recoder_Activity.this, recordPermission) == PackageManager.PERMISSION_GRANTED) {
            //Permission Granted
            return true;
        } else {
            //Permission not granted, ask for permission
            ActivityCompat.requestPermissions(Voice_Recoder_Activity.this, new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }

    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);

            }
            else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {

            //Go ahead with recording audio now

            startRecording();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    startRecording();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isRecording){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null  ;
            timer.stop();
            isRecording = false ;
            timer.setBase(SystemClock.elapsedRealtime());
            timer.stop();
            recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
