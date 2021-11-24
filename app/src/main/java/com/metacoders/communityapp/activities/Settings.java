package com.metacoders.communityapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.utils.AppPreferences;

public class Settings extends AppCompatActivity {
    String link = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_new);

        findViewById(R.id.inviteFriends).setOnClickListener(
                v -> {
                    createAShortLink();
                }
        );
    }

    private void createAShortLink() {

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse("https://newsrmee.page.link/?link=https://newsrme.com/?id%3D" + AppPreferences.getUSerID(getApplicationContext()) + "&apn=com.metacoders.communityapp"))
                .buildShortDynamicLink()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Short link created
                        Uri shortLink = task.getResult().getShortLink();
                        Uri flowchartLink = task.getResult().getPreviewLink();


                        link = shortLink.toString();

                        Log.d("TAG", "createAShortLink: " + shortLink + " -> " + flowchartLink);

                    } else {
                        // Error
                        // ...
                        Log.d("TAG", "createAShortLink: Error : + " + task.getException().toString());
                        //create a link
                        link = "https://newsrmee.page.link/?link=https://newsrme.com/?id%" + AppPreferences.getUSerID(getApplicationContext()) + "D1&apn=com.metacoders.communityapp";

                    }

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            "I'm on NewsRme. Please join with us and get 50k bonus points.\n" +
                                    link);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                });
    }

}
