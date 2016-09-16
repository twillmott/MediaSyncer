package uk.org.willmott.mediasyncer;

/**
 * Fragment for the show overview section on the shows screen tab.
 * Created by tomwi on 05/09/2016.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.uwetrottmann.trakt5.entities.Season;
import com.uwetrottmann.trakt5.enums.Extended;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.org.willmott.mediasyncer.service.TraktService;
import uk.org.willmott.mediasyncer.ui.SeriesSimpleAdapter;

/**
 * The fragment that goes in the over view tab on the shows screen.
 */
public class SeriesFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    // The below values are used as keys to link information to it's relating list viewfield.
    private static final String LIST_SERIES = "series";
    private static final String LIST_IMAGE = "image_url";
    private static final String LIST_DETAILS = "details";

    // An instance of the traktService for communicating with trakt
    TraktService traktService;

    // The show ID for the show we've loaded.
    String showId;

    // The list that represents the list item. It contains a hashmap that contains all the data
    // do display in the list fragment.
    private List<HashMap<String, String>> seasonsList = new ArrayList<>();

    // The simple adapter that holds all of the list results.
    private SimpleAdapter simpleAdapter;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SeriesFragment newInstance(int sectionNumber) {
        SeriesFragment fragment = new SeriesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SeriesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // =================== Get the view ==================
        View rootView = inflater.inflate(R.layout.fragment_series, container, false);

        // ================ Get the parents instance of trakt ===========
        traktService = ((ShowActivity) this.getActivity()).getTraktService();
        // Get the ID of the show that we're loading form the parent activity
        showId = ((ShowActivity) this.getActivity()).getShowId();

        // =================== Now set up the list view. ========================
        /*
        See main activity for how this kind of list view works.
         */
        // The from array directly maps to the to array.
        String[] from = {LIST_SERIES, LIST_DETAILS, LIST_IMAGE};
        int[] to = {R.id.library_list_title, R.id.library_list_details, R.id.library_list_image};
        // Create an adapter that is full of our list data. The seasonsList holds all the data, and the
        // from and to variables map the hashmap keys to the view id's.
        simpleAdapter = new SeriesSimpleAdapter(getActivity(), seasonsList, R.layout.list_item_series, from, to);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_series);
        listView.setAdapter(simpleAdapter);
        // Go to the episodes screen when we have clicked the listing.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), ShowActivity.class);
                // Get the ID of the show we've clicked on
//                String showId = ((HashMap<String, String>) parent.getItemAtPosition(position)).get("id");
//                // Put the id in to the intent
//                intent.putExtra("id", showId);
//                startActivity(intent);
            }
        });

        // =============== Fetch the series info from trakt ========================
        new RetrieveSeries().execute();

        return rootView;
    }

    /**
     * An async task that will retrieve all the series information from trakt to
     * display in the listview.
     */
    private class RetrieveSeries extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            List<Season> seasons = new ArrayList<>();
            // Try filling the list of seasons up from a trakt call.
            try {
                seasons = traktService.getTrakt().seasons().summary(showId, Extended.FULLIMAGES).execute().body();
            } catch (Exception e) {
                Log.e("Trakt", e.getMessage());
            }

            // Go through each season and add the information to the hashmap that holds all the info.
            for ( Season season : seasons ) {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put(LIST_SERIES, "Season " + season.number.toString());

                // Get the next episode filled in
                if (season.overview != null) {
                    hashMap.put(LIST_DETAILS, season.overview);
                } else {
                    hashMap.put(LIST_DETAILS, "Ended");
                }
                // Add the show image to the list view
                if (season.images.logo != null) {
                    hashMap.put(LIST_IMAGE, season.images.logo.full);
                }

                // Add the id in last for the next screen to find it. This won't be displayed on the
                // listview.
                hashMap.put("id", showId);

                seasonsList.add(hashMap);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            simpleAdapter.notifyDataSetChanged();
        }
    }
}
