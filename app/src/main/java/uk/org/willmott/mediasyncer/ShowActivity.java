package uk.org.willmott.mediasyncer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uwetrottmann.trakt5.entities.Show;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import uk.org.willmott.mediasyncer.service.TraktService;

public class ShowActivity extends AppCompatActivity {

    String showId;
    TraktService traktService;
    Show show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        // Get the ID of the show we're displaying
        showId = getIntent().getStringExtra("id");

        // Set up a trakt service
        traktService = new TraktService(ShowActivity.this, null);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.shows_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Download the show information.
        try {
            new RetriveShowInfo().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private class RetriveShowInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            show = traktService.getShow(showId);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.shows_toolbar);
            toolbar.setTitle(show.title);
            TextView textView = (TextView) findViewById(R.id.shows_text);
            textView.setText(show.title);
            ImageView imageView = (ImageView) findViewById(R.id.shows_banner);
            String url = show.images.thumb.full;
            Picasso.with(ShowActivity.this).load(url).into(imageView);
        }
    }
}
