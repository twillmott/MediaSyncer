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

import com.uwetrottmann.trakt5.entities.Season;

import java.util.ArrayList;
import java.util.List;

import uk.org.willmott.mediasyncer.model.Actor;
import uk.org.willmott.mediasyncer.tvdb.model.Episode;
import uk.org.willmott.mediasyncer.tvdb.model.SeasonActor;
import uk.org.willmott.mediasyncer.tvdb.model.SeasonActorData;
import uk.org.willmott.mediasyncer.tvdb.service.TheTvdbService;
import uk.org.willmott.mediasyncer.ui.ActorAdapter;

/**
 * The fragment that goes in the over view tab on the shows screen.
 */
public class FragmentSeasonOverview extends Fragment {

    /**
     * The fragment that displays the show overview for the show tabs in the show activity.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";


    // List of actors to display in the episode
    List<Actor> actorList = new ArrayList<>();
    // The corresponding adapter
    ActorAdapter actorAdapter;

    TheTvdbService tvdbService;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentSeasonOverview newInstance(int sectionNumber) {
        FragmentSeasonOverview fragment = new FragmentSeasonOverview();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSeasonOverview() {
    }

    public Season getSeason() {
        return ((ActivitySeason) getActivity()).getSeason();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        tvdbService = new TheTvdbService();

        View rootView = inflater.inflate(R.layout.fragment_season_overview, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.season_overview);
        textView.setText("hello");

        // =================== Set up the actor scroller ========================
        // Set up the recycler view to dispaly the list of shows that we just loaded.
        // Extra info will be loaded within the list view
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.season_actors_recyclerview);
        actorAdapter = new ActorAdapter(getActivity(), actorList);
        recyclerView.setAdapter(actorAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // =======================================================
        try {
            new RetrieveTbDbSeries().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }

    private class RetrieveTbDbSeries extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            SeasonActor seasonActor = tvdbService.getSeasonActors("247808");

            for (SeasonActorData seasonActorData : seasonActor.getData()) {
                actorList.add(new Actor("http://thetvdb.com/banners/" + seasonActorData.getImage(), seasonActorData.getName()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            actorAdapter.notifyDataSetChanged();
        }
    }
}
