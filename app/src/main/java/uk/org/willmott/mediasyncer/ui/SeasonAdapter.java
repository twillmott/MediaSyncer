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

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.List;

import uk.org.willmott.mediasyncer.R;
import uk.org.willmott.mediasyncer.ActivitySeason;
import uk.org.willmott.mediasyncer.ActivityShow;
import uk.org.willmott.mediasyncer.model.Season;
import uk.org.willmott.mediasyncer.service.TraktService;

/**
 * The adapter used to display the season list items (in a RecycleView). It uses picasso to load the
 * pictures for better memory management.
 * Created by tomw on 10/09/2016.
 */
public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.ViewHolder> {

    private List<Season> mSeasons;
    private Context mContext;
    private String showId;
    private TraktService traktService;

    public SeasonAdapter(Context context, List<Season> seasons, String showId, TraktService traktService) {
        mSeasons = seasons;
        mContext = context;
        this.showId = showId;
        this.traktService = traktService;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public SeasonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View seasonView = layoutInflater.inflate(R.layout.list_item_season, parent, false);

        // Return a new holder instance
        return new ViewHolder(seasonView);
    }


    /**
     * Populate the data into the item through the holder
     */
    @Override
    public void onBindViewHolder(SeasonAdapter.ViewHolder holder, int position) {
        Season season = mSeasons.get(position);

        // Set up all the item views
        TextView title = holder.title;
        title.setText(mContext.getString(R.string.season).concat(" ").concat(Integer.toString(season.getSeasonNumber())));
        TextView info = holder.info;
//        info.setText(season.episode_count.toString() + " Episodes (" + season.aired_episodes + " Aired)");
        info.setText("Add functionality to inform number of aired episodes");

        ImageView imageView = holder.image;
        Picasso.with(mContext).load(season.getThumbnailUrl()).resize(54, 80).into(imageView);
    }

    @Override
    public int getItemCount() {
        return mSeasons.size();
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

            title = (TextView) itemView.findViewById(R.id.season_list_title);
            info = (TextView) itemView.findViewById(R.id.season_list_details);
            image = (ImageView) itemView.findViewById(R.id.season_list_image);

            // Set up the on click listeners.
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), ActivitySeason.class);
                    // Put the id in to the intent
                    intent.putExtra("id", showId);
                    intent.putExtra("season", Parcels.wrap(SeasonAdapter.this.mSeasons.get(getAdapterPosition())));
                    intent.putExtra("accessToken", traktService.getAccessToken());

                    view.getContext().startActivity(intent);
                    ((ActivityShow)view.getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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