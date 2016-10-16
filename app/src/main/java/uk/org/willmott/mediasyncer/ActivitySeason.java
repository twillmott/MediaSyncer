package uk.org.willmott.mediasyncer;

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

import uk.org.willmott.mediasyncer.model.Episode;
import uk.org.willmott.mediasyncer.model.Season;
import uk.org.willmott.mediasyncer.service.TraktService;
import uk.org.willmott.mediasyncer.ui.EpisodeAdapter;

public class ActivitySeason extends AppCompatActivity {

    String showId;

    Season season;

    TraktService traktService;

    // The list that represents the list item.
    private List<Episode> episodesList = new ArrayList<>();

    // The corresponding adapter for the episode list
    EpisodeAdapter adapter;

    public TraktService getTraktService() {return traktService;}

    public String getShowId() {return showId;}

    public Season getSeason() {
        return season;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);

        // Get the ID of the show we're displaying
        showId = getIntent().getStringExtra("id");
        season = (Season) Parcels.unwrap(getIntent().getParcelableExtra("season"));
        traktService = new TraktService(getIntent().getStringExtra("accessToken"));

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
        ImageView imageView = (ImageView) findViewById(R.id.season_banner);
        // Set the banner container to be 16:9
        Double max_height = imageView.getWidth() * 0.5625;
        imageView.setMaxHeight(max_height.intValue());
        Picasso.with(ActivitySeason.this).load(season.getBannerUrl()).into(imageView);

        // =================== Now set up the list view. ========================
        // Create the recyclerView listing of all of our seasons.
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_episodes);
        adapter = new EpisodeAdapter(this, episodesList, getTraktService(), showId, season);
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
}
