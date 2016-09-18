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
import android.view.View;
import android.view.ViewGroup;

import com.uwetrottmann.trakt5.entities.Episode;
import com.uwetrottmann.trakt5.entities.Season;
import com.uwetrottmann.trakt5.enums.Extended;

import java.util.ArrayList;
import java.util.List;

import uk.org.willmott.mediasyncer.service.TraktService;
import uk.org.willmott.mediasyncer.ui.EpisodeAdapter;
import uk.org.willmott.mediasyncer.ui.SeasonAdapter;

/**
 * The fragment that goes in the over view tab on the shows screen.
 */
public class EpisodesFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";


    // An instance of the traktService for communicating with trakt
    TraktService traktService;

    // The show ID for the show we've loaded.
    String showId;

    // The season number we've loaded
    int seasonNumber;

    // The list that represents the list item.
    private List<Episode> episodesList = new ArrayList<>();

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static EpisodesFragment newInstance(int sectionNumber) {
        EpisodesFragment fragment = new EpisodesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public EpisodesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // =================== Get the view ==================
        View rootView = inflater.inflate(R.layout.fragment_series, container, false);

        // ================ Get the parents instance of trakt ===========
        traktService = ((SeasonActivity) this.getActivity()).getTraktService();
        // Get the ID of the show that we're loading form the parent activity
        showId = ((SeasonActivity) this.getActivity()).getShowId();
        seasonNumber = ((SeasonActivity) this.getActivity()).getSeason();

        // =============== Fetch the series info from trakt ========================
        new RetrieveEpisodes().execute();

        // =================== Now set up the list view. ========================
        // Create the recyclerView listing of all of our seasons.
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_series);
        EpisodeAdapter adapter = new EpisodeAdapter(getContext(), episodesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    /**
     * An async task that will retrieve all the series information from trakt to
     * display in the listview.
     */
    private class RetrieveEpisodes extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            // Try filling the list of seasons up from a trakt call.
            List<Episode> episodes = new ArrayList<>();
            try {
                episodes = traktService.getTrakt().seasons().season(showId, seasonNumber, Extended.FULLIMAGES).execute().body();
            } catch (Exception e) {
                Log.e("Trakt", e.getMessage());
            }

            episodesList.addAll(episodes);
            return null;
        }
    }
}