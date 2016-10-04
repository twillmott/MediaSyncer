package uk.org.willmott.mediasyncer.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import uk.org.willmott.mediasyncer.R;
import uk.org.willmott.mediasyncer.model.Actor;

/**
 * The adapter used to display the episode list items (in a RecycleView). It uses picasso to load the
 * pictures for better memory management.
 * Created by tomw on 10/09/2016.
 */
public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.ViewHolder> {

    private List<Actor> mActors;
    private Context mContext;

    public ActorAdapter(Context context, List<Actor> actors) {
        mActors = actors;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public ActorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View actorView = layoutInflater.inflate(R.layout.actor_view, parent, false);

        // Return a new holder instance
        return new ViewHolder(actorView);
    }


    /**
     * Populate the data into the item through the holder
     */
    @Override
    public void onBindViewHolder(ActorAdapter.ViewHolder holder, int position) {
        Actor actor = mActors.get(position);

        // Set up all the item views
        TextView title = holder.name;
        title.setText(actor.getName());
        ImageView imageView = holder.image;
        if (actor.getImageUrl() != null) {
            Picasso.with(mContext).load(actor.getImageUrl()).centerCrop().resize(200, 300).into(imageView);
        } else {
            // TODO Put an unknown person in.
        }
    }

    @Override
    public int getItemCount() {
        return mActors.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView name;
        public ImageView image;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.actor_name);
            image = (ImageView) itemView.findViewById(R.id.actor_image);
        }
    }
}
