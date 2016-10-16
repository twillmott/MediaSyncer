package uk.org.willmott.mediasyncer;

/**
 * Fragment for the show overview section on the shows screen tab.
 * Created by tomwi on 05/09/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import uk.org.willmott.mediasyncer.model.Season;
import uk.org.willmott.mediasyncer.service.TraktService;
import uk.org.willmott.mediasyncer.ui.SeasonAdapter;

/**
 * The fragment that goes in the over view tab on the shows screen.
 */
public class FragmentSeason extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    // The list that represents the list item.
    private List<Season> seasonsList = new ArrayList<>();

    // The adapter that holds the list of seasons
    SeasonAdapter adapter;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentSeason newInstance(int sectionNumber) {
        FragmentSeason fragment = new FragmentSeason();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSeason() {
    }

    public TraktService getTraktService() { return ((ActivityShow) getActivity()).getTraktService(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // =================== Get the view ==================
        View rootView = inflater.inflate(R.layout.fragment_season, container, false);

        seasonsList.addAll(((ActivityShow) this.getActivity()).getSeries().getSeasons());

        // =================== Now set up the list view. ========================
        // Create the recyclerView listing of all of our seasons.
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_season);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new SeasonAdapter(
                getContext(),
                seasonsList,
                ((ActivityShow) this.getActivity()).getSeries().getTraktId(),
                getTraktService());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }
}
