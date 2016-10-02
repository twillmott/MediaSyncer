package uk.org.willmott.mediasyncer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uwetrottmann.trakt5.entities.Episode;
import com.uwetrottmann.trakt5.enums.Extended;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.org.willmott.mediasyncer.imdb.service.ImdbApiEndpoint;
import uk.org.willmott.mediasyncer.imdb.service.ImdbService;
import uk.org.willmott.mediasyncer.model.Actor;
import uk.org.willmott.mediasyncer.service.TraktService;
import uk.org.willmott.mediasyncer.tvdb.service.TheTvdbService;
import uk.org.willmott.mediasyncer.ui.ActorAdapter;

public class ActivityEpisode extends AppCompatActivity {

    // The show that we want to load
    String showId;

    // The season number that we want to load
    int seasonNumber;

    // The episode number that we want to load
    int episodeNumber;

    TraktService traktService;

    // The episode that we're displaying
    Episode episode;

    // List of actors to display in the episode
    List<Actor> actorList = new ArrayList<>();
    // The corresponding adapter
    ActorAdapter actorAdapter;

    TheTvdbService tvdbService = new TheTvdbService();
    ImdbService imdbService = new ImdbService();

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public TraktService getTraktService() {return traktService;}

    public String getShowId() {return showId;}

    public int getSeason() {return seasonNumber;}

    public int getEpisode() {return episodeNumber;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);

        // Get the ID of the show we're displaying
        showId = getIntent().getStringExtra("id");
        seasonNumber = getIntent().getIntExtra("season", 0);
        episodeNumber = getIntent().getIntExtra("episode", 0);
        traktService = new TraktService(getIntent().getStringExtra("accessToken"));

        // ============= Set up the toolbar =====================
        Toolbar toolbar = (Toolbar) findViewById(R.id.episode_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // ======================================================

        TextView textView = (TextView) findViewById(R.id.episode_info_text);
        textView.setText("Hello");

        // =================== Set up the actor scroller ========================
        // Set up the recycler view to dispaly the list of shows that we just loaded.
        // Extra info will be loaded within the list view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.episode_actors_recyclerview);
        actorAdapter = new ActorAdapter(this, actorList);
        recyclerView.setAdapter(actorAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Download the show information. We have a .get() on the end so that we wait until
        // all the info is loaded before we load the screen.
        try {
            // Get all the episode info that's required on page load
            new RetrieveEpisodeInfo().execute().get();

            // Start off the tasks to populate all the non esential stuff
            new PopulateActors().execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * An async task that will download all the info on the episode that we need to display the episode
     * activity. i.e. the banner and episode name.
     */
    private class RetrieveEpisodeInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                episode = getTraktService().getTrakt().episodes().summary(showId, seasonNumber, episodeNumber, Extended.FULLIMAGES).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Set all of the UI elements.
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.episode_toolbar);
            toolbar.setTitle("Episode " + episode.number.toString() + ": " + episode.title);
            ImageView imageView = (ImageView) findViewById(R.id.episode_banner);
            // Set the banner container to be 16:9
            Double max_height = imageView.getWidth() * 0.5625;
            imageView.setMaxHeight(max_height.intValue());
            String url = episode.images.screenshot.full;
            Picasso.with(ActivityEpisode.this).load(url).into(imageView);
        }
    }

    // Define the animation on the back button press.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private class PopulateActors extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            uk.org.willmott.mediasyncer.tvdb.model.Episode tvdbEpisode = tvdbService.getEpisode(episode.ids.tvdb.toString());

            if (actorList.size() > 0) {
                actorList.clear();
            }

            for (String actor : tvdbEpisode.getData().getGuestStars()) {
                actorList.add(new Actor(imdbService.getActorImage(actor), actor));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            actorAdapter.notifyDataSetChanged();
        }
    }
}