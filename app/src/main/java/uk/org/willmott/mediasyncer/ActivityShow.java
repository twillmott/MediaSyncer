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
import com.uwetrottmann.trakt5.entities.Show;

import org.parceler.Parcels;

import uk.org.willmott.mediasyncer.model.Season;
import uk.org.willmott.mediasyncer.model.Series;
import uk.org.willmott.mediasyncer.service.TraktService;

public class ActivityShow extends AppCompatActivity {

    TraktService traktService;
    Series series;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private ShowSectionsPagerAdapter mShowSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public TraktService getTraktService() { return traktService; }

    public Series getSeries() {
        return series;
    }

    public String getShowId() {
        return series.getTraktId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        // Get the ID of the show we're displaying
        traktService = new TraktService(getIntent().getStringExtra("accessToken"));
        series = (Series) Parcels.unwrap(getIntent().getParcelableExtra("show"));

        // ================== Set up the toolbar ==========================
        Toolbar toolbar = (Toolbar) findViewById(R.id.shows_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // ================================================================
        // ================== Set up the activity vies=====================
        toolbar.setTitle(series.getTitle());

        ImageView imageView = (ImageView) findViewById(R.id.shows_banner);
        // Set the banner container to be 16:9
        Double max_height = imageView.getWidth() * 0.5625;
        imageView.setMaxHeight(max_height.intValue());
        Picasso.with(ActivityShow.this).load(series.getBannerUrl()).into(imageView);
        // ================================================================

        // ========================= Set up the tabs ======================
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mShowSectionsPagerAdapter = new ShowSectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.show_container);
        mViewPager.setAdapter(mShowSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.show_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        // ================================================================

        // Placeholder code in case we want to add a floating action button.
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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
    public class ShowSectionsPagerAdapter extends FragmentPagerAdapter {

        public ShowSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a FragmentPlaceholder (defined as a static inner class below).
            // This is where we define all of the fragments in our activity.
            switch (position) {
                case 0:
                    return FragmentShowOverview.newInstance(position);
                case 1:
                    return FragmentSeason.newInstance(position);
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
                    return "Seasons";
            }
            return "Error - This should not be a tab";
        }
    }
}
