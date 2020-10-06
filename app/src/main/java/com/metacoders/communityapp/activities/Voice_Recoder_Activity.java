package com.metacoders.communityapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
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
    String mFileName = null;
    private static final int IMAGE_PICKER_SELECT = 99;
    String name, ph;
    Boolean isGallery = false;
    private ImageButton listBtn;
    private ImageButton recordBtn;
    private TextView filenameText;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    private boolean isRecording = false;
    String uid;
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE = 21;
    Uri galleryUri = null ;
    private MediaRecorder mediaRecorder;
    private String recordFile;
    String time;

    private Chronometer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_voice__recoder_);
        listBtn = findViewById(R.id.record_list_btn);
        recordBtn = findViewById(R.id.record_btn);
        timer = findViewById(R.id.record_timer);



        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryForAudio();
            }
        });

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isRecording) {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                    timer.stop();
                    timer.setBase(SystemClock.elapsedRealtime());
                    timer.stop();

                    isRecording = false;
                    recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
                    uploadAudio();
                } else {
                    if (checkPermissions()) {

                        timer.setBase(SystemClock.elapsedRealtime());
                        timer.stop();

                        // above two line added just to  reset the clock bug ......
                        //if dont work delete it for own caution
                        startRecording();
                    }


                    // startRecording() ;

                }

            }
        });

    }

    private void openGalleryForAudio() {
        isGallery = true;
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("audio/*");
        //    startActivityForResult(pickIntent, IMAGE_PICKER_SELECT );
        startActivityForResult(Intent.createChooser(pickIntent, "Select Audio"), IMAGE_PICKER_SELECT);
    }

    private void uploadAudio() {

        if (isGallery) {

            isGallery = false;
            Intent post = new Intent(getApplicationContext(), PostUploadActivity.class);
            post.putExtra("path",galleryUri.toString() );
            post.putExtra("media", "audio");
            startActivity(post);


        } else {


            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle("What You Want To Do ? ");
            alert.setMessage("Choose Your Saving Method");
            alert.setButton(Dialog.BUTTON_POSITIVE, "Upload It", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alert.dismiss();

                    Uri uri = Uri.fromFile(new File(mFileName));
                    Intent post = new Intent(getApplicationContext(), PostUploadActivity.class);
                    post.putExtra("path", uri.toString());
                    post.putExtra("media", "audio");
                    startActivity(post);
                }
            });

            alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    alert.dismiss();


                }
            });

            alert.show();


            //  Toast.makeText(getApplicationContext() , "Video Taken  LINK : " + uri.toString() , Toast.LENGTH_SHORT).show();

        }




    }

    private void startRecording() {
        String Root = Environment.getExternalStorageDirectory().getPath();
        mFileName = Voice_Recoder_Activity.this.getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.US);
        Date now = new Date();
        time = formatter.format(now);
        mFileName = Root + "/news_rme_audio" + formatter.format(now) + ".aac";
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setOutputFile(mFileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);


        try {
            mediaRecorder.prepare();
        } catch (IOException ie) {
            Log.e("TAG", "Fail + " + ie.getMessage());
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

            } else {
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
        if (isRecording) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            timer.stop();
            isRecording = false;
            timer.setBase(SystemClock.elapsedRealtime());
            timer.stop();
            recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICKER_SELECT && data != null) {
            Uri selectedMediaUri = data.getData();

             galleryUri = selectedMediaUri;



            //  String TYPE = MimeTypeMap.getFileExtensionFromUrl(selectedMediaUri.getLastPathSegment()).toLowerCase();

            Log.d("TAG", "onActivityResult: TYPE =  " + galleryUri);

            uploadAudio();


        }


    }

    public void showChooseDialogue() {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle("What You Want To Do ? ");
        alert.setMessage("Choose Your Recording Method");
        alert.setButton(Dialog.BUTTON_POSITIVE, "Record Video", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.dismiss();

            }
        });

        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Choose From Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                alert.dismiss();
                openGalleryForAudio();

            }
        });

        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showChooseDialogue();
    }
}

