package uk.org.willmott.mediasyncer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import uk.org.willmott.mediasyncer.service.TraktService;

public class MainActivity extends AppCompatActivity {

    TraktService traktService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        traktService = new TraktService();
        String token = traktService.getAuthenticationStatus(getIntent());

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                traktService.authenticate(MainActivity.this);
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        Uri uri = intent.getData();

        if (uri != null && uri.toString()
                .startsWith("mediasyncer://oauthresponse")) {
            String code = uri.getQueryParameter("code");
            TextView textView = (TextView) findViewById(R.id.tempTextBox);
            textView.setText(code);
        }
    }
}
