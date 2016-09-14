package uk.org.willmott.mediasyncer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.uwetrottmann.trakt5.entities.Show;

import java.util.concurrent.ExecutionException;

import uk.org.willmott.mediasyncer.service.TraktService;

public class ShowActivity extends AppCompatActivity {

    String showId;
    TraktService traktService;
    Show show;

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
        // ================================================================

        // ========================= Set up the tabs ======================
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mShowSectionsPagerAdapter = new ShowSectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.show_container);
        mViewPager.setAdapter(mShowSectionsPagerAdapter);

        // For some reason we need to set this so that the view pager will display in the scrollview.
        NestedScrollView nv = (NestedScrollView) findViewById(R.id.show_nest_scrollview);
        nv.setFillViewport(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.show_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        // ================================================================

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
//            TextView textView = (TextView) findViewById(R.id.shows_text);
//            textView.setText(show.title);
            ImageView imageView = (ImageView) findViewById(R.id.shows_banner);
            String url = show.images.thumb.full;
            Picasso.with(ShowActivity.this).load(url).into(imageView);
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
            // Return a PlaceholderFragment (defined as a static inner class below).
            // This is where we define all of the fragments in our activity.
            switch (position) {
                case 0: // Left screen will be the alarm clock
                    return ShowOverviewFragment.newInstance(position);
//                case 1: // Middle screen will be live times
//                    return LiveTubesFragment.newInstance(position);
                default: // This will only display if something weird messes up
                    return PlaceholderFragment.newInstance(position + 1);
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
