package uk.org.willmott.mediasyncer;

/**
 * Fragment for the show overview section on the shows screen tab.
 * Created by tomwi on 05/09/2016.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.uwetrottmann.trakt5.entities.Season;
import com.uwetrottmann.trakt5.enums.Extended;

import java.util.ArrayList;
import java.util.List;

import uk.org.willmott.mediasyncer.service.TraktService;
import uk.org.willmott.mediasyncer.ui.SeasonAdapter;

/**
 * The fragment that goes in the over view tab on the shows screen.
 */
public class SeriesFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    // An instance of the traktService for communicating with trakt
    TraktService traktService;

    // The show ID for the show we've loaded.
    String showId;

    // The list that represents the list item.
    private List<Season> seasonsList = new ArrayList<>();

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

        // =============== Fetch the series info from trakt ========================
        new RetrieveSeries().execute();

        // =================== Now set up the list view. ========================
        // Create the recyclerView listing of all of our seasons.
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_series);
        SeasonAdapter adapter = new SeasonAdapter(getContext(), seasonsList, showId);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    /**
     * An async task that will retrieve all the series information from trakt to
     * display in the listview.
     */
    private class RetrieveSeries extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            // Try filling the list of seasons up from a trakt call.
            List<Season> seasons = new ArrayList<>();
            try {
                seasons = traktService.getTrakt().seasons().summary(showId, Extended.FULLIMAGES).execute().body();
            } catch (Exception e) {
                Log.e("Trakt", e.getMessage());
            }

            // Go through the list of seasons and remove season 0 if it exists.
            for (int i = 0; i < seasons.size(); i++) {
                if (seasons.get(i).number == 0) {
                    seasons.remove(i);
                }
            }
            seasonsList.addAll(seasons);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
