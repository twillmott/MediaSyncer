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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.org.willmott.mediasyncer.model.Actor;
import uk.org.willmott.mediasyncer.model.Series;
import uk.org.willmott.mediasyncer.tvdb.model.ShowActors;
import uk.org.willmott.mediasyncer.tvdb.model.ShowActorData;
import uk.org.willmott.mediasyncer.tvdb.service.TheTvdbService;
import uk.org.willmott.mediasyncer.ui.ActorAdapter;

/**
 * The fragment that goes in the over view tab on the shows screen.
 */
public class FragmentShowOverview extends Fragment {

    /**
     * The fragment that displays the show overview for the show tabs in the show activity.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    // List of actors to display in the episode
    List<Actor> actorList = new ArrayList<>();

    // The corresponding adapter
    ActorAdapter actorAdapter;

    // A service to get TVDB data.
    TheTvdbService tvdbService;

    // The show that we're loading
    Series show;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentShowOverview newInstance(int sectionNumber) {
        FragmentShowOverview fragment = new FragmentShowOverview();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentShowOverview() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get the show from the parent activity.
        show = ((ActivityShow) getActivity()).getSeries();

        tvdbService = new TheTvdbService();

        View rootView = inflater.inflate(R.layout.fragment_show_overview, container, false);

        // Set the show overview text
        TextView textView = (TextView) rootView.findViewById(R.id.show_overview);
        textView.setText(show.getOverview());

        // =================== Set up the actor scroller ========================
        // Set up the recycler view to dispaly the list of shows that we just loaded.
        // Extra info will be loaded within the list view
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.show_actors_recyclerview);
        actorAdapter = new ActorAdapter(getActivity(), actorList);
        recyclerView.setAdapter(actorAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        // =======================================================

        try {
            new RetrieveActors().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }


    /**
     * Populate the actors recyvlerview with all of the actors for this show.
     */
    private class RetrieveActors extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            ShowActors showActors = tvdbService.getShowActors(show.getTvdbId().toString());

            for (ShowActorData showActorData : showActors.getData()) {
                actorList.add(new Actor("http://thetvdb.com/banners/" + showActorData.getImage(), showActorData.getName()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            actorAdapter.notifyDataSetChanged();
        }
    }
}
