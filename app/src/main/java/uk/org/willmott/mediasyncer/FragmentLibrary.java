package uk.org.willmott.mediasyncer;

/**
 * This fragment represents the users TV Library. It holds shows, and takes the
 * user to a show. The library is a combination of any watched show, collected show,
 * or watch list.
 * TODO later on, add filters to filter what is in the library.
 * Created by tomwi on 05/09/2016.
 */

import android.os.AsyncTask;
import android.os.Bundle;
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

import com.uwetrottmann.trakt5.entities.BaseShow;

import java.util.ArrayList;
import java.util.List;

import uk.org.willmott.mediasyncer.data.access.SeriesAccessor;
import uk.org.willmott.mediasyncer.model.ContentType;
import uk.org.willmott.mediasyncer.model.Series;
import uk.org.willmott.mediasyncer.service.TraktService;
import uk.org.willmott.mediasyncer.ui.LibraryAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentLibrary extends Fragment {
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
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_library);
        libraryAdapter = new LibraryAdapter(getContext(), showsList, getTraktService());
        recyclerView.setAdapter(libraryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
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
            showsList.clear();
            showsList.addAll(new SeriesAccessor(getContext()).getAllSeriesAsModel());
            libraryAdapter.notifyDataSetChanged();

            // Stop the refresh button spinner.
            refresh.setActionView(null);
//            getTraktService().getAllShows(getContext());
//            new RetrieveLibraryInfo().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Retrieve all of the users library info. This will have to be extended to include movies when
     * we add that functionality.
     */
    private class RetrieveLibraryInfo extends AsyncTask<Void, Void, List<BaseShow>> {

        @Override
        protected List<BaseShow> doInBackground(Void... params) {

            TraktService traktService = getTraktService();

            // Get all of the users shows, with the minimum information.
            // Get the users watchlist
            List<BaseShow> watchlistShows = traktService.getShowWatchlist();
            // Get the users watched shows.
            List<BaseShow> watchedShows = traktService.getShowWatched();
            // Get the users collection.
            List<BaseShow> collectedShows = traktService.getShowCollection();

            showsList.clear();
            // Merge all the shows in to one list.
            List<BaseShow> shows = traktService.mergeShows(true, watchlistShows, watchedShows, collectedShows);

            for (BaseShow show : shows) {
                BaseShow showProgress = traktService.getShowWatchedProgress(show.show.ids.trakt.toString());

                // Combine the base show and the show and add to the listing
//                showsList.add(traktService.combineBaseShows(show, showProgress));
            }

            return shows;
        }

        @Override
        protected void onPostExecute(List<BaseShow> shows) {
            super.onPostExecute(shows);
            libraryAdapter.notifyDataSetChanged();
            // Stop the refresh button spinner.
            refresh.setActionView(null);

            // If we returned no shows, say so
            if (shows.size() == 0) {
                Toast.makeText((ActivityMain) FragmentLibrary.this.getActivity(), "Unable to find any shows, check authorisation.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
