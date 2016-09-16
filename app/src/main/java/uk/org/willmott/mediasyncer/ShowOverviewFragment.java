package uk.org.willmott.mediasyncer;

/**
 * Fragment for the show overview section on the shows screen tab.
 * Created by tomwi on 05/09/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * The fragment that goes in the over view tab on the shows screen.
 */
public class ShowOverviewFragment extends Fragment {

    /**
     * The fragment that displays the show overview for the show tabs in the show activity.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ShowOverviewFragment newInstance(int sectionNumber) {
        ShowOverviewFragment fragment = new ShowOverviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ShowOverviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show_overview, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.tempTextBox);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }
}
