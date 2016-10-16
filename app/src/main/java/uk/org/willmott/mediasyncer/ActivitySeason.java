package uk.org.willmott.mediasyncer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.org.willmott.mediasyncer.model.Episode;
import uk.org.willmott.mediasyncer.model.Season;
import uk.org.willmott.mediasyncer.ui.EpisodeAdapter;

public class ActivitySeason extends AppCompatActivity {

    Season season;

    // The list that represents the list item.
    private List<Episode> episodesList = new ArrayList<>();

    // The corresponding adapter for the episode list
    EpisodeAdapter adapter;

    public Season getSeason() {
        return season;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);

        season = Parcels.unwrap(getIntent().getParcelableExtra("season"));
        episodesList.addAll(season.getEpisodes());

        // =================== Set up the toolbar =========================
        Toolbar toolbar = (Toolbar) findViewById(R.id.season_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // ================================================================

        toolbar.setTitle("Season " + Integer.toString(season.getSeasonNumber()));

        try {
            new LoadBanner().execute(season.getBannerUrl()).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =================== Now set up the list view. ========================
        // Create the recyclerView listing of all of our seasons.
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_episodes);
        adapter = new EpisodeAdapter(this, episodesList);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Define the animation on the back button press.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    /**
     * Picasso won't load the image in the UI thread, so having to create this asynctask to do it.
     */
    private class LoadBanner extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... voids) {
            return voids[0];
        }

        /**
         * Load the banner.
         */
        @Override
        protected void onPostExecute(String aVoid) {
            ImageView imageView = (ImageView) findViewById(R.id.season_banner);
            // Set the banner container to be 16:9
            Double max_height = imageView.getWidth() * 0.5625;
            imageView.setMaxHeight(max_height.intValue());
            Picasso.with(ActivitySeason.this).load(aVoid).into(imageView);
        }
    }
}
