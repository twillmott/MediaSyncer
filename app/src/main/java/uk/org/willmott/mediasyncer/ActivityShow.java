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

import uk.org.willmott.mediasyncer.service.TraktService;

public class ActivityShow extends AppCompatActivity {

    String showId;
    TraktService traktService;
    Show show;
    BaseShow BaseShow;

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

    public String getShowId() {return showId;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        // Get the ID of the show we're displaying
        showId = getIntent().getStringExtra("id");
        traktService = new TraktService(getIntent().getStringExtra("accessToken"));

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

        // Download the show information. We have a .get() on the end so that we wait until
        // all the info is loaded before we load the screen.
        try {
            new RetrieveShowInfo().execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
     * An async task that will download all the info on the show that we need to display the show
     * activity. i.e. the banner and show name.
     */
    private class RetrieveShowInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            show = getTraktService().getShow(showId);
            return null;
        }

        /**
         * Set all of the UI elements.
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.shows_toolbar);
            toolbar.setTitle(show.title);
            ImageView imageView = (ImageView) findViewById(R.id.shows_banner);
            String url = show.images.thumb.full;
            Picasso.with(ActivityShow.this).load(url).into(imageView);
        }
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
