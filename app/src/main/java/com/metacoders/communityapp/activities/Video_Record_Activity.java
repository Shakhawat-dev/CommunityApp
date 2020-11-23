package com.metacoders.communityapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.metacoders.communityapp.R;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.VideoResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.controls.Mode;
import com.otaliastudios.cameraview.overlay.OverlayLayout;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class Video_Record_Activity extends AppCompatActivity {
    private static final String TAG = "VIDEO_RECORD";

    private static final int IMAGE_PICKER_SELECT = 99;
    private static final int VIDEOMAXTIME = 420000;
    SurfaceView sampleGLView;
    ImageView closeBtn, flashBTn;
    Boolean isFlashOn = false;
    Uri uri;
    TextView timer;
    Context context;
    ImageButton imageButton, changeCameraBtn, VideoBTn;
    CircleImageView videoButton;
    CircleImageView circleImageView;
    String category = "all";
    Boolean isprressed = false, isRecording = false;
    File file;
    Boolean isGallery = false;

    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_video__record_);

        context = Video_Record_Activity.this;


        Dexter.withContext(context)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                }).onSameThread().check();
// Cast to OverlayLayout.LayoutParams
        View overlay = findViewById(R.id.watermark);
        OverlayLayout.LayoutParams params = (OverlayLayout.LayoutParams) overlay.getLayoutParams();

// Perform changes
        //   params.drawOnPreview = true; // draw on preview
        params.drawOnPreview = false; // do not draw on preview
        params.drawOnPictureSnapshot = true; // draw on picture snapshots
        // params.drawOnPictureSnapshot = false; // do not draw on picture snapshots
        params.drawOnVideoSnapshot = true; // draw on video snapshots
        //   params.drawOnVideoSnapshot = false; // do not draw on video snapshots

// When done, apply


        CameraView camera = findViewById(R.id.camera);
        imageButton = findViewById(R.id.captureBtn);
        changeCameraBtn = findViewById(R.id.changeCameraBtn);
        VideoBTn = findViewById(R.id.videoBTN);
        videoButton = findViewById(R.id.component);
        circleImageView = findViewById(R.id.lastImage);
        flashBTn = findViewById(R.id.flashBtn);
        closeBtn = findViewById(R.id.closeBtn);

        timer = findViewById(R.id.timer);


        //   VideoBTn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_camera_black_24dp));


        overlay.setLayoutParams(params);
        camera.setMode(Mode.VIDEO);

        camera.setLifecycleOwner(this);
        flashBTn.setBackground(Video_Record_Activity.this.getResources().getDrawable(R.drawable.ic_flash_off_black_24dp));


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        flashBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFlashOn) {
                    // flash is on
                    camera.setFlash(Flash.OFF);
                    flashBTn.setBackground(Video_Record_Activity.this.getResources().getDrawable(R.drawable.ic_flash_off_black_24dp));
                    isFlashOn = false;
                } else {

                    // flash is off

                    camera.setFlash(Flash.TORCH);
                    flashBTn.setBackground(Video_Record_Activity.this.getResources().getDrawable(R.drawable.ic_flash_on_black_24dp));
                    isFlashOn = true;
                }

            }
        });

//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isprressed) {
//                    camera.setVideoMaxDuration(20000);
//
//                    long time = System.currentTimeMillis() / 100000;
//                    String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
//                    File file = new File(ROOT_DIR + "/DCIM/camera" + "/test" + time + ".mp4");
//                    camera.takeVideoSnapshot(file);
//
//
//
//                } else {
//                    camera.takePictureSnapshot();
//                }
//
//            }
//        });

        changeCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera.getFacing() == Facing.BACK) {
                    camera.setFacing(Facing.FRONT);
                } else {
                    camera.setFacing(Facing.BACK);

                }


            }
        });



        camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(PictureResult result) {
                // A Picture was taken!

                Toast.makeText(context, "Press And Hold", Toast.LENGTH_LONG).show();

//                long time = System.currentTimeMillis() / 100000;
//
//                String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
//                File file = new File(ROOT_DIR  + "/test" + time + ".jpeg");
//
//                result.toFile(file, new FileCallback() {
//                    @Override
//                    public void onFileReady(@Nullable File file) {
//
//
//
//                    }
//                });
//
//                Uri uri21 = Uri.fromFile(file) ;
//                getToCreatePostPage("image", uri21);


            }

            @Override
            public void onVideoTaken(VideoResult result) {

                // A Video was taken!
//                Toast.makeText(context, "Video  Was  Taken ", Toast.LENGTH_LONG).show();
                long time = System.currentTimeMillis() / 100000;

                String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
                // File file = new File(ROOT_DIR + "/DCIM/camera" + "/test" + time + ".jpeg");
                Uri uri1 = Uri.fromFile(result.getFile());
                getToCreatePostPage("video", uri1);


            }

            @Override
            public void onOrientationChanged(int orientation) {
                super.onOrientationChanged(orientation);
                Log.d(TAG, "onOrientationChanged: " + orientation);



            }

            // And much more
        });




        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                camera.setMode(Mode.VIDEO);
                camera.setVideoMaxDuration(VIDEOMAXTIME + 100);

                long time = System.currentTimeMillis() / 100000;
                String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
                file = new File(ROOT_DIR + "/test" + time + ".mp4");

                if (!isRecording) {
                    // stop the camera
                    videoButton.setImageResource(R.color.red);
                    isRecording = true;

                    camera.takeVideo(file);


                    countDownTimer = new CountDownTimer(VIDEOMAXTIME, 1000) {

                        public void onTick(long millisUntilFinished) {
                            // timer.setText("seconds remaining: " + millisUntilFinished / 1000);

//                            long  min =  TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)  ;
//                            long ss =TimeUnit.MINUTES.toMillis(min);
//                            String sec = TimeUnit.MILLISECONDS.toSeconds(ss) + "" ;

                            timer.setText(getIntervalTime(millisUntilFinished));


                        }

                        public void onFinish() {
                            timer.setText("done!");

                            camera.stopVideo();
                            videoButton.setImageResource(R.color.white);
                            isRecording = false;
                        }
                    }.start();


                } else {


                    camera.stopVideo();
                    timer.setText("done!");
                    countDownTimer.cancel();
                    videoButton.setImageResource(R.color.white);
                    isRecording = false;


//                    isRecording = true ;
//                    camera.setMode(Mode.VIDEO);
//                    camera.setVideoMaxDuration(310000);
//                    long time = System.currentTimeMillis() / 100000;
//                    String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
//                    file = new File(ROOT_DIR + "/test" + time + ".mp4");
//                    camera.takeVideo(file);


                }

            }
        });


//        videoButton.setActionListener(new CameraVideoButton.ActionListener() {
//            @Override
//            public void onStartRecord() {
//
//
//
//            }
//
//            @Override
//            public void onEndRecord() {
//
//
//
//
////                Uri uri11 = Uri.fromFile(file) ;
////
////
////                getToCreatePostPage("video" , uri11);
//
//            }
//
//            @Override
//            public void onDurationTooShortError() {
//
//
//            }
//
//            @Override
//            public void onSingleTap() {
//
//                Toast.makeText(getApplicationContext(), "Press And Hold" , Toast.LENGTH_SHORT).show();
//
//               // camera.takePictureSnapshot();
//            }
//        });


        circleImageView.setImageResource(R.drawable.placeholder);

        try {
            setTheLastImage();
        } catch (Exception e) {

            circleImageView.setImageResource(R.drawable.placeholder);

        }


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGalleryForVideo();

            }
        });


    }

    private void openGalleryForVideo() {
        isGallery = true;
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("video/*");
        //    startActivityForResult(pickIntent, IMAGE_PICKER_SELECT );
        startActivityForResult(Intent.createChooser(pickIntent, "Select Videos"), IMAGE_PICKER_SELECT);
    }

    private void setTheLastImage() {
        // Find the last picture
        String[] projection = new String[]{
                MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Video.VideoColumns.DATE_TAKEN,
                MediaStore.Video.VideoColumns.MIME_TYPE
        };
        final Cursor cursor = context.getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                        null, MediaStore.Video.VideoColumns.DATE_TAKEN + " DESC");

// Put it in the image view
        if (cursor.moveToFirst()) {

            String imageLocation = cursor.getString(1);
            File imageFile = new File(imageLocation);
            if (imageFile.exists()) {   // TODO: is there a better way to do this?
                Bitmap bm = BitmapFactory.decodeFile(imageLocation);
                Glide.with(context)
                        .load(bm)
                        .error(R.drawable.placeholder)
                        .into(circleImageView);


            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICKER_SELECT && data != null) {
            Uri selectedMediaUri = data.getData();

            uri = selectedMediaUri;

            String TYPE = MimeTypeMap.getFileExtensionFromUrl(selectedMediaUri.getLastPathSegment()).toLowerCase();

            Log.d("TAG", "onActivityResult: TYPE =  " + TYPE);

            if (TYPE.length() > 0) {
                if (TYPE.contains("jpg") || TYPE.contains("png") || TYPE.contains("jpeg") || TYPE.contains("gif")
                        || TYPE.contains("tiff")) {
                    getToCreatePostPage("image", selectedMediaUri);
                    //handle image
                } else {


                    getToCreatePostPage("video", selectedMediaUri);
                }

            } else {
                String path = selectedMediaUri.getPath();
                //  File filee = new File(selectedMediaUri.toString());
                //   String file = selectedMediaUri.getPath();

                try {
                    path = getPath(Video_Record_Activity.this, selectedMediaUri);
                    file = new File(path);
                } catch (Exception e) {

                    path = selectedMediaUri.getPath();
                    file = new File(path);
                }

                String exten = null;

                int i = path.lastIndexOf('.');
                if (i > 0) {
                    exten = path.substring(i + 1);
                }


                Log.d("TAG", "onActivityResult: Extension  " + exten);

                if (exten != null) {
                    if (exten.contains("jpg") || exten.contains("png") || exten.contains("jpeg") || exten.contains("gif")
                            || exten.contains("tiff")) {
                        //  getToCreatePostPage("image", selectedMediaUri);
                        //handle image
                    } else {
                        getToCreatePostPage("video", selectedMediaUri);
                    }
                } else {

                    AlertDialog dialogue = new AlertDialog.Builder(Video_Record_Activity.this).create();

                    dialogue.setTitle("SomeThing Went Wrong !!!");

                    dialogue.show();

                }

            }


        }
    }

    public void getToCreatePostPage(String FileMime, Uri uri) { // type == o means came form camera

        if (isGallery) {

            isGallery = false;
            Intent post = new Intent(context, PostUploadActivity.class);
            post.putExtra("path", uri.toString());
            post.putExtra("media", "video");
            startActivity(post);


        } else {


            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle("What You Want To Do ? ");
            alert.setMessage("Choose Your Saving Method");
            alert.setButton(Dialog.BUTTON_POSITIVE, "Upload It", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alert.dismiss();
                    Intent post = new Intent(context, PostUploadActivity.class);
                    post.putExtra("path", uri.toString());
                    post.putExtra("media", "video");
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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
////        Intent i = new Intent(Share_Activity.this, MainActivity.class);
////        Bundle bndlAnimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.enter_from_left, R.anim.exit_to_right).toBundle();
////        startActivity(i,bndlAnimation);
////        finish();
//    }

    public static String getPath(Context ctx, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = ctx.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(columnIndex);
        cursor.close();
        return s;
    }

    public static String getIntervalTime(long longInterval) {

        long intMillis = longInterval;
        long dd = TimeUnit.MILLISECONDS.toDays(intMillis);
        long daysMillis = TimeUnit.DAYS.toMillis(dd);
        intMillis -= daysMillis;
        long hh = TimeUnit.MILLISECONDS.toHours(intMillis);
        long hoursMillis = TimeUnit.HOURS.toMillis(hh);
        intMillis -= hoursMillis;
        long mm = TimeUnit.MILLISECONDS.toMinutes(intMillis);
        long minutesMillis = TimeUnit.MINUTES.toMillis(mm);
        intMillis -= minutesMillis;
        long ss = TimeUnit.MILLISECONDS.toSeconds(intMillis);
        long secondsMillis = TimeUnit.SECONDS.toMillis(ss);
        intMillis -= secondsMillis;

        String stringInterval = "%02d:%02d";
        return String.format(stringInterval, mm, ss);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}






