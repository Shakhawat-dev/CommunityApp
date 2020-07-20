package com.metacoders.communityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button  videoBtn , audioBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoBtn = findViewById(R.id.videoBtn);
        audioBtn = findViewById(R.id.audioBtn) ;


        RequestPermission() ;

        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent videoActivity = new Intent(getApplicationContext() , Video_Record_Activity.class);
                startActivity(videoActivity);

            }
        });


        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });



    }

    private void RequestPermission() {

        Dexter.withContext(MainActivity.this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE ,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE ,
                        Manifest.permission.CAMERA ,
                        Manifest.permission.RECORD_AUDIO )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                }).onSameThread().check();
    }
}