package com.metacoders.communityapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.SharedPrefManager;

public class Settings extends AppCompatActivity {
    String link = "";
    TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_new);

        logout = findViewById(R.id.log_out);

        logout.setText("Log Out " + SharedPrefManager.getInstance(getApplicationContext()).getUserModel().getName());

        findViewById(R.id.inviteFriends).setOnClickListener(
                v -> {
                    createAShortLink();
                }
        );

        logout.setOnClickListener(v -> {

            try {
                LoginManager.getInstance().logOut();
            } catch (Exception e) {
                Log.d("TAG", "onCreate: " + e.getMessage());
            }
            Intent o = new Intent(getApplicationContext(), LoginActivity.class);
            SharedPrefManager manager = new SharedPrefManager(getApplicationContext());
            manager.logout();
            o.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(o);
            finish();
        });

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
