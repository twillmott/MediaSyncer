package uk.org.willmott.mediasyncer;

/**
 * This fragment represents the users TV Library. It holds shows, and takes the
 * user to a show. The library is a combination of any watched show, collected show,
 * or watch list. TODO later on, add filters to filter what is in the library.
 * Created by tomwi on 05/09/2016.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.uwetrottmann.trakt5.entities.BaseShow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.org.willmott.mediasyncer.model.ContentType;
import uk.org.willmott.mediasyncer.service.TraktService;
import uk.org.willmott.mediasyncer.ui.LibrarySimpleAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class LibraryFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    // The below values are used as keys to link information to it's relating list viewfield.
    private static final String LIST_TITLE = "title";
    private static final String LIST_IMAGE = "image_url";
    private static final String LIST_DETAILS = "details";

    // The service that communicates with trakt.
    private TraktService traktService;
    // The content type that we're displaying in this tab (TV or Movies).
    private ContentType contentType;
    // The swipe refresh layout that holds our listing item.
    private SwipeRefreshLayout librarySwipeRefreshLayout;
    // The list that represents the list item. It contains a hashmap that contains all the data
    // do display in the list fragment.
    List<HashMap<String, String>> libraryList = new ArrayList<>();

    SimpleAdapter simpleAdapter;


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static LibraryFragment newInstance(int sectionNumber) {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public LibraryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Don't put any Trakt code in here, it may get run prior to the authorisation
        // in the main activity.
        traktService = ((MainActivity) this.getActivity()).getTraktService();

        // ========== Get our view =============
        View rootView = inflater.inflate(R.layout.fragment_library, container, false);

        //   ========== Set up the swipe refresh layout that holds our list view ==============.
        librarySwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh_library);
        librarySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RetrieveLibraryInfo().execute();
            }
        });
        librarySwipeRefreshLayout.setDistanceToTriggerSync(500);

        // =================== Now set up the list view. ========================
        /*
        How our list view works:
        We have the list_item_library.xml layout that represents a single list item. This item contains
        data such as the title, picture, info, IMDB rating etc..
        We then have the libraryList that contains a hashmap. This hashmap contains a bunch of values
        to populate the list_item_library view.
        Below we have from and to. From is the hashmap key, which maps to the to which is the id of
        each item in the list view. Therefore all items in the hashmap should be in the order stated
        in the from string array.
         */
        // The from array directly maps to the to array.
        String[] from = {LIST_TITLE, LIST_DETAILS, LIST_IMAGE};
        int[] to = {R.id.library_list_title, R.id.library_list_details, R.id.library_list_image};
        // Create an adapter that is full of our list data. The libraryList holds all the data, and the
        // from and to variables map the hashmap keys to the view id's.
        simpleAdapter = new LibrarySimpleAdapter(getActivity(), libraryList, R.layout.list_item_library, from, to);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_library);
        listView.setAdapter(simpleAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);

        // Start by finding out what type of content we want to display.
        contentType = ((MainActivity) this.getActivity()).getContentType();

        if (contentType == ContentType.TV) {
        }
    }


    /**
     * This method updates the library list with the new library information. This is done by populating
     * the libraryList list by putting in all the details to the hashmap. The order is shown by the
     * from variable above.
     */
    private void updateLibraryList() {

    }


    /**
     * Retrieve all of the users library info. This will have to be extended to include movies when
     * we add that functionality.
     */
    private class RetrieveLibraryInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            // Get all of the users shows, with the minimum information.
            // Get the users watchlist
            List<BaseShow> watchlistShows = traktService.getShowWatchlist();
            // Get the users watched shows.
            List<BaseShow> watchedShows = traktService.getShowWatched();
            // Get the users collection.
            List<BaseShow> collectedShows = traktService.getShowCollection();

            // Merge all the shows in to one list.
            List<BaseShow> shows = traktService.mergeShows(true, watchlistShows, watchedShows, collectedShows);

            libraryList.clear();

            for (BaseShow show : shows) {
                HashMap<String, String> hashMap = new HashMap<>();
                // Get more detailed show information.
                String showId = show.show.ids.trakt.toString();
                BaseShow showProgress = traktService.getShowWatchedProgress(showId);

                hashMap.put(LIST_TITLE, show.show.title);

                // Get the next episode filled in
                if (showProgress.next_episode != null) {
                    hashMap.put(LIST_DETAILS, showProgress.next_episode.title);
                } else {
                    hashMap.put(LIST_DETAILS, "Ended");
                }
                // Add the show image to the list view
                String image = show.show.images.poster.full;
                if (image != null) {
                    hashMap.put(LIST_IMAGE, image);
                }

                libraryList.add(hashMap);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            simpleAdapter.notifyDataSetChanged();

            // Stop the refresh action when complete
            librarySwipeRefreshLayout.setRefreshing(false);
            librarySwipeRefreshLayout.destroyDrawingCache();
            librarySwipeRefreshLayout.clearAnimation();
        }
    }
}
