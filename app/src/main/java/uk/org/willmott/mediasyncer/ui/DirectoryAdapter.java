package uk.org.willmott.mediasyncer.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.List;

import jcifs.smb.SmbFile;
import uk.org.willmott.mediasyncer.R;

/**
 * The adapter used to display directories in the file pickers.
 * Created by tomw on 10/09/2016.
 */
public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.ViewHolder> {

    List<URL> mDirectories;
    Context mContext;
    DirectoryClickListener directoryClickListener;

    public DirectoryAdapter(Context context, DirectoryClickListener directoryClickListener, List<URL> directories) {
        mDirectories = directories;
        this.directoryClickListener = directoryClickListener;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public DirectoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View libraryView = layoutInflater.inflate(R.layout.list_item_directory, parent, false);

        // Return a new holder instance
        return new ViewHolder(libraryView);
    }

    /**
     * Populate the data into the item through the holder
     */
    @Override
    public void onBindViewHolder(DirectoryAdapter.ViewHolder holder, int position) {
        URL url = mDirectories.get(position);

        // Set up all the item views
        TextView title = holder.title;
        ImageView imageView = holder.image;

        String file = url.getFile();

        // Split the file path up by the forward slashes
        String[] fileParts = file.split("/");

        if (fileParts.length == 0 || (fileParts.length == 1 && fileParts[0].equals(""))) {
            title.setText(url.getHost());
            imageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_computer_black_24px));
        } else {
            title.setText(fileParts[fileParts.length - 1]);
            imageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_folder_black_24px));
        }
    }

    @Override
    public int getItemCount() {
        return mDirectories.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView title;
        public ImageView image;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.directory_list_title);
            image = (ImageView) itemView.findViewById(R.id.directory_library_list_image);

            // Set up the on click listeners.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    directoryClickListener.directorySelected(DirectoryAdapter.this.mDirectories.get(getAdapterPosition()).toString());
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
