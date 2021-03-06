package uk.org.willmott.mediasyncer;

/**
 * This fragment represents the users TV Library. It holds shows, and takes the
 * user to a show. The library is a combination of any watched show, collected show,
 * or watch list.
 * TODO later on, add filters to filter what is in the library.
 * Created by tomwi on 05/09/2016.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

import uk.org.willmott.mediasyncer.data.access.SeriesAccessor;
import uk.org.willmott.mediasyncer.model.ContentType;
import uk.org.willmott.mediasyncer.model.Series;
import uk.org.willmott.mediasyncer.service.RefreshCompleteListener;
import uk.org.willmott.mediasyncer.service.TmdbService;
import uk.org.willmott.mediasyncer.service.TraktService;
import uk.org.willmott.mediasyncer.ui.LibraryAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentLibrary extends Fragment implements RefreshCompleteListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    // The content type that we're displaying in this tab (TV or Movies).
    private ContentType contentType;
    // The list that represents the list item. It contains a hashmap that contains all the data
    // do display in the list fragment.
    private List<Series> showsList = new ArrayList<>();
    // The refresh button in the action bar.
    private MenuItem refresh;
    private MenuItem refresh2;
    // The adapter used to display the listing results.
    LibraryAdapter libraryAdapter;


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentLibrary newInstance(int sectionNumber) {
        FragmentLibrary fragment = new FragmentLibrary();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    public FragmentLibrary() {
    }

    public TraktService getTraktService() { return ((ActivityMain) getActivity()).getTraktService(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // State that we have items that we want to put in the action bar.
        setHasOptionsMenu(true);

        // ========== Get our view =============
        View rootView = inflater.inflate(R.layout.fragment_library, container, false);

        // =================== Now set up the list view. ========================
        // Set up the recycler view to dispaly the list of shows that we just loaded.
        // Extra info will be loaded within the list view
        showsList.addAll(new SeriesAccessor(getContext()).getAllSeriesAsModel());
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_library);
        libraryAdapter = new LibraryAdapter(getContext(), showsList, getTraktService());
        recyclerView.setAdapter(libraryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Update our watch status
        getTraktService().refreshAllEpisodeWatchedStatus(getContext(), this);
        getTraktService().refreshAllEpisodeCollectedStatus(getContext(), this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
//        // Start by finding out what type of content we want to display.
//        contentType = ((ActivityMain) this.getActivity()).getContentType();
//
//        if (contentType == ContentType.TV) {
//        }
    }


    // Set up the action bar buttons / options menu.  For this fragment this is just currently
    // the refresh button.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_library, menu);
        refresh = menu.findItem(R.id.action_refresh);
        refresh = menu.findItem(R.id.refresh2);
    }

    // Set up the handling of the action bar buttons that we've added to this fragment.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            // TODO change the colour of the spinner that's defined in layout/actionbar_indeterminate_spinner.
            // Start the progress spinner spinning.
            refresh.setActionView(R.layout.actionbar_indeterminate_progress);
            // Refresh the data list.
            getTraktService().getAllShows(getContext(), this);
            return true;
        }

        if (id == R.id.refresh2) {
            TmdbService tmdbService = new TmdbService();
            tmdbService.populateBlankShows(getContext(), this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void refreshComplete(String result) {

        if (Strings.isNullOrEmpty(result)) {
            return;
        }
        // Update the results
        showsList.clear();
        showsList.addAll(new SeriesAccessor(getContext()).getAllSeriesAsModel());
        libraryAdapter.notifyDataSetChanged();

        String identifier = result.split(",")[0];
        String message = result.split(",")[1];
        if (identifier.contains("traktrefresh")) {
            // Stop the refresh button spinner.
            refresh.setActionView(null);

            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();

        } else if (result.contains("tmdbupdateall")) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        } else if (result.contains("traktwatchedprogress")) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        } else if (result.contains("trakterror")) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
    }
}