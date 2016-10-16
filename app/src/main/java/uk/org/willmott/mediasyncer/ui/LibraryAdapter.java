package uk.org.willmott.mediasyncer.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.uwetrottmann.trakt5.entities.BaseShow;

import org.parceler.Parcels;

import java.util.List;

import uk.org.willmott.mediasyncer.ActivityShow;
import uk.org.willmott.mediasyncer.R;
import uk.org.willmott.mediasyncer.model.Series;
import uk.org.willmott.mediasyncer.service.TraktService;

/**
 * The simple adapter used to display the library listing components. It uses picasso to load the
 * pictures for better memory management.
 * Created by tomw on 10/09/2016.
 */
public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {

    List<Series> mShows;
    Context mContext;
    TraktService traktService;

    public LibraryAdapter(Context context, List<Series> shows, TraktService traktService) {
        mShows = shows;
        mContext = context;
        this.traktService = traktService;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public LibraryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View libraryView = layoutInflater.inflate(R.layout.list_item_library, parent, false);

        // Return a new holder instance
        return new ViewHolder(libraryView);
    }

    /**
     * Populate the data into the item through the holder
     */
    @Override
    public void onBindViewHolder(LibraryAdapter.ViewHolder holder, int position) {
        Series series = mShows.get(position);

        // Set up all the item views
        TextView title = holder.title;
        ImageView imageView = holder.image;
        TextView infoTextView = holder.info;

        title.setText(series.getTitle());

        if (series.getNextEpisode() == null) {
            infoTextView.setText("Ended");
        } else {
            infoTextView.setText(series.getNextEpisode().getTitle());
        }

        // Add the show image to the list view
        String image = series.getThumbnailUrl();
        if (image != null) {
            Picasso.with(mContext).load(image).resize(54,80).into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mShows.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView title;
        public TextView info;
        public ImageView image;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.library_list_title);
            info = (TextView) itemView.findViewById(R.id.library_list_details);
            image = (ImageView) itemView.findViewById(R.id.library_list_image);

            // Set up the on click listeners.
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ActivityShow.class);
                    // Get the ID of the show we've clicked on
                    String showId = LibraryAdapter.this.mShows.get(getAdapterPosition()).getTraktId();
                    // Put the id in to the intent
                    intent.putExtra("accessToken", traktService.getAccessToken());
                    intent.putExtra("show", Parcels.wrap(mShows.get(getAdapterPosition())));
                    view.getContext().startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(view.getContext(), "Need to implement a long click action", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }
}
