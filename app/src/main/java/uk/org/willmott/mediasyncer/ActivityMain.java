package uk.org.willmott.mediasyncer;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.FloatingActionButton;

import uk.org.willmott.mediasyncer.model.ContentType;
import uk.org.willmott.mediasyncer.service.TraktService;

public class ActivityMain extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private MainSectionsPagerAdapter mMainSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    TraktService traktService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // =============== Tab layout stuff ==========================
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mMainSectionsPagerAdapter = new MainSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.main_container);
        mViewPager.setAdapter(mMainSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        // =============================================================

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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

//        Uncomment me to go straight to family guy
//        // REMOVE ME, JUST FOR TESTING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // Go straight to the family guy show to speed things up.
        Intent intent = new Intent(this, ActivityShow.class);
        // Get the ID of the show we've clicked on
        intent.putExtra("id", "1425");
        startActivity(intent);
//        //""!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    /**
     * Get the trakt service from the main activity. This should be used for any
     * trakt calls.
     */
    public TraktService getTraktService() {
        return traktService;
    }

    /**
     * Return what content type we're loading, TV or movies.
     */
    public ContentType getContentType() { return ContentType.TV; }


    /**
     * Set up the options menu (xml)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handle any options menu (toolbar) button clicks.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, ActivitySettings.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.reauthorise) {
            traktService.reauthorise(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class MainSectionsPagerAdapter extends FragmentPagerAdapter {

        public MainSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a FragmentPlaceholder (defined as a static inner class below).
            // This is where we define all of the fragments in our activity.
            switch (position) {
                case 0: // Left screen will be the alarm clock
                    return FragmentLibrary.newInstance(position);
//                case 1: // Middle screen will be live times
//                    return LiveTubesFragment.newInstance(position);
//                case 2: // Right screen will be a tube planner
//                    return PlannerFragment.newInstance(position);
                default: // This will only display if something weird messes up
                    return FragmentPlaceholder.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_1_name);
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return "Error - This should not be a tab";
        }
    }
}

