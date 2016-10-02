package uk.org.willmott.mediasyncer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.uwetrottmann.trakt5.entities.BaseShow;
import com.uwetrottmann.trakt5.entities.Season;
import com.uwetrottmann.trakt5.enums.Extended;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.org.willmott.mediasyncer.service.TraktService;

public class ActivitySeason extends AppCompatActivity {

    String showId;
    int seasonNumber;
    TraktService traktService;
    BaseShow BaseShow;
    Season season;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SeasonSectionsPagerAdapter mSeasonsSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

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

        // ========================= Set up the tabs ======================
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSeasonsSectionsPagerAdapter = new SeasonSectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.season_container);
        mViewPager.setAdapter(mSeasonsSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.season_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        // ================================================================

        // Download the show information. We have a .get() on the end so that we wait until
        // all the info is loaded before we load the screen.
        try {
            new RetrieveSeasonInfo().execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SeasonSectionsPagerAdapter extends FragmentPagerAdapter {

        public SeasonSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a FragmentPlaceholder (defined as a static inner class below).
            // This is where we define all of the fragments in our activity.
            switch (position) {
                case 0:
                    return FragmentSeasonOverview.newInstance(position);
                case 1:
                    return FragmentEpisode.newInstance(position);
                default: // This will only display if something weird messes up
                    return FragmentPlaceholder.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Overview";
                case 1:
                    return "Episodes";
            }
            return "Error - This should not be a tab";
        }
    }
}
