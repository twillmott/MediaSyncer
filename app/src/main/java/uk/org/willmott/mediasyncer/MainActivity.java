package uk.org.willmott.mediasyncer;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.uwetrottmann.trakt5.entities.BaseShow;

import java.util.List;

import uk.org.willmott.mediasyncer.service.TraktService;

public class MainActivity extends AppCompatActivity {

    TraktService traktService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ==== Trakt Authorisation =====
        // On start up we need to check that we have the required trakt authorisation. If we don't,
        // we direct the user to a browser, then return to this main screen, meaning the intent will,
        // have the redirect url attached. We need to check that if this redirect URL exists, if it
        // does, we need to continue with the authorisation. Otherwise we need to check if we have
        // authorisation.
        Uri intentUri = getIntent().getData();
        if (intentUri != null && intentUri.toString().startsWith(TraktService.REDIRECT_URL) && intentUri.getQueryParameter("code") != null) {
            // Continue the authorisation process
            traktService = new TraktService(this, intentUri.getQueryParameter("code"));
        } else {
            // Check for authorisation
            traktService = new TraktService(this, null);
        }
        // ==============================

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Everything in this class is just testing (messing around).

                List<BaseShow> shows;
                List<BaseShow> showsProgress;
                shows = traktService.getWatchedShows();
                showsProgress = traktService.getShowWatchedProgress(shows);

                TextView text = (TextView) findViewById(R.id.tempTextBox);
                int i = 0;
                for (BaseShow show : shows) {
                    text.append("\n" + show.show.title);
                    if (showsProgress.get(i).next_episode != null) {
                        text.append(" " + showsProgress.get(i).next_episode.title);
                    }
                    i++;
                }
            }
        });
    }
}

