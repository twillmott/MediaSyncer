package uk.org.willmott.mediasyncer.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.uwetrottmann.trakt5.entities.Episode;
import com.uwetrottmann.trakt5.entities.Season;

import java.util.List;

import uk.org.willmott.mediasyncer.R;
import uk.org.willmott.mediasyncer.service.TraktService;

/**
 * The adapter used to display the episode list items (in a RecycleView). It uses picasso to load the
 * pictures for better memory management.
 * Created by tomw on 10/09/2016.
 */
public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {

    private List<Episode> mEpisodes;
    private Context mContext;
    private TraktService traktService;

    public EpisodeAdapter(Context context, List<Episode> episodes, TraktService traktService) {
        mEpisodes = episodes;
        mContext = context;
        this.traktService = traktService;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public EpisodeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View epidodeView = layoutInflater.inflate(R.layout.list_item_episode, parent, false);

        // Return a new holder instance
        return new ViewHolder(epidodeView);
    }


    /**
     * Populate the data into the item through the holder
     */
    @Override
    public void onBindViewHolder(EpisodeAdapter.ViewHolder holder, int position) {
        Episode episode = mEpisodes.get(position);

        // Set up all the item views
        TextView title = holder.title;
        title.setText("Episode ".concat(episode.number.toString()));
        TextView info = holder.info;
        String episodeInfoString = "First aired: " + episode.first_aired.getDayOfMonth() + "/" +
                episode.first_aired.getMonthOfYear() + "/" + episode.first_aired.getYear();
        info.setText(episodeInfoString);
        ImageView imageView = holder.image;

        if (episode.images.screenshot.thumb != null) {
            Picasso.with(mContext).load(episode.images.screenshot.thumb).centerCrop().resize(54,80).into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mEpisodes.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
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

            title = (TextView) itemView.findViewById(R.id.episode_title);
            info = (TextView) itemView.findViewById(R.id.episode_details);
            image = (ImageView) itemView.findViewById(R.id.episode_image);

            // Set up the on click listeners.
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Need to implement a click action", Toast.LENGTH_SHORT).show();
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
