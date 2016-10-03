package uk.org.willmott.mediasyncer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.uwetrottmann.trakt5.entities.BaseShow;
import com.uwetrottmann.trakt5.entities.Episode;
import com.uwetrottmann.trakt5.entities.Season;
import com.uwetrottmann.trakt5.enums.Extended;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.org.willmott.mediasyncer.service.TraktService;
import uk.org.willmott.mediasyncer.ui.EpisodeAdapter;

public class ActivitySeason extends AppCompatActivity {

    String showId;

    int seasonNumber;

    TraktService traktService;

    BaseShow BaseShow;

    Season season;

    // The list that represents the list item.
    private List<Episode> episodesList = new ArrayList<>();

    // The corresponding adapter for the episode list
    EpisodeAdapter adapter;

    public TraktService getTraktService() {return traktService;}

    public String getShowId() {return showId;}

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public Season getSeason() {
        return season;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);

        // Get the ID of the show we're displaying
        showId = getIntent().getStringExtra("id");
        seasonNumber = getIntent().getIntExtra("season", 0);
        traktService = new TraktService(getIntent().getStringExtra("accessToken"));

        // Set up the toolbar
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


        // Download the show information. We have a .get() on the end so that we wait until
        // all the info is loaded before we load the screen.
        try {
            new RetrieveSeasonInfo().execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =============== Fetch the series info from trakt ========================
        new RetrieveEpisodes().execute();

        // =================== Now set up the list view. ========================
        // Create the recyclerView listing of all of our seasons.
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_episodes);
        adapter = new EpisodeAdapter(this, episodesList, getTraktService(), showId, seasonNumber);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * An async task that will download all the info on the season that we need to display the season
     * activity. i.e. the banner and season name.
     */
    private class RetrieveSeasonInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //show = traktService.getShow(showId);
            List<Season> seasons = new ArrayList<>();
            try {
                seasons = getTraktService().getTrakt().seasons().summary(showId, Extended.FULLIMAGES).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (Season season1 : seasons) {
                if (season1.number == seasonNumber) {
                    season = season1;
                    break;
                }
            }
            return null;
        }

        /**
         * Set all of the UI elements.
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.season_toolbar);
            toolbar.setTitle("Season " + season.number.toString());
            ImageView imageView = (ImageView) findViewById(R.id.season_banner);
            // Set the banner container to be 16:9
            Double max_height = imageView.getWidth() * 0.5625;
            imageView.setMaxHeight(max_height.intValue());
            String url = season.images.thumb.full;
            Picasso.with(ActivitySeason.this).load(url).into(imageView);
        }
    }

    // Define the animation on the back button press.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * An async task that will retrieve all the season information from trakt to
     * display in the listview.
     */
    private class RetrieveEpisodes extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            // Try filling the list of seasons up from a trakt call.
            List<Episode> episodes = new ArrayList<>();
            try {
                episodes = getTraktService().getTrakt().seasons().season(showId, seasonNumber, Extended.FULLIMAGES).execute().body();
            } catch (Exception e) {
                Log.e("Trakt", e.getMessage());
            }

            episodesList.addAll(episodes);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
        }
    }
}
