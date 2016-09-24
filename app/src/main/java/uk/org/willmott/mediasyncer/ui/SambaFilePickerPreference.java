package uk.org.willmott.mediasyncer.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jcifs.smb.SmbFile;
import uk.org.willmott.mediasyncer.R;


/**
 * A preference window that allows the user to pick a sameba directory.
 * Created by tomwi on 24/09/2016.
 */
public class SambaFilePickerPreference extends DialogPreference implements Preference.OnPreferenceClickListener, DirectoryClickListener {

    private static String LOG_TAG = SambaFilePickerPreference.class.getSimpleName();

    // The addapter to display the list of directories.
    private DirectoryAdapter directoryAdapter;
    // The list of directories to go in to the adaptor.
    private List<SmbFile> smbFiles = new ArrayList<>();
    // Keeping track of the current directory that we're in.
    private String currentDirectory = "/";

    public SambaFilePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(false);
        setOnPreferenceClickListener(this);
        setDialogLayoutResource(R.layout.directory_pop_up);

    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.directory_pop_up_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24px);
        toolbar.setTitle("Select Directory");
        // Set the back button to go to the previous directory (found by taking the last item
        // off of the current directory.
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String previousDirectory = "";
                String[] directoryComponents = currentDirectory.split("/");
                for (int i = 0; i < directoryComponents.length - 1; i++) {
                    previousDirectory = previousDirectory.concat(directoryComponents[i]).concat("/");
                }
                new LoadSambaDirectory().execute(previousDirectory);
                currentDirectory = previousDirectory;
            }
        });

        // Get samba shares on the first load.
        try {
            new LoadSambaDirectory().execute().get();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        // Set up the recycler view to display the directory results.
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_directory);
        directoryAdapter = new DirectoryAdapter(getContext(), this, smbFiles);
        recyclerView.setAdapter(directoryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

    /**
     * Handle the user clicking a directory (go inside it)
     */
    @Override
    public void directorySelected(String directory) {
        currentDirectory = directory;
        new LoadSambaDirectory().execute(directory);
    }

    private class LoadSambaDirectory extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            // If we've not defined a directory, we want to find servers (domains)
            if (strings.length == 0) {
                SmbFile[] domains = null;
                List<SmbFile> servers = new ArrayList<>();
                try {
                    // Get all of the domains (workgroups) on the network.
                    domains = (new SmbFile("smb://")).listFiles();

                    // Go through each domain and get all of it's servers.
                    for (SmbFile domain : domains) {
                        servers.addAll(Arrays.asList(new SmbFile(domain.getURL()).listFiles()));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (smbFiles.size() > 0) {
                    smbFiles.clear();
                }
                smbFiles.addAll(servers);

                // Otherwise we can load all the directories within the given directory.
            } else {
                try {
                    List<SmbFile> tempDirectories = Arrays.asList((new SmbFile(strings[0])).listFiles());
                    // Go through the list and remove all files. Files won't end with a /. We only want a directory.
                    List<SmbFile> directories = new ArrayList<>();
                    for (SmbFile smbFile : tempDirectories) {
                        if (smbFile.getURL().getFile().substring(smbFile.getURL().getFile().length() - 1).equals("/")) {
                            directories.add(smbFile);
                        }
                    }

                    // Update the listing displayed in the UI.
                    if (smbFiles.size() > 0) {
                        smbFiles.clear();
                    }
                    smbFiles.addAll(directories);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void mVoid) {
            super.onPostExecute(mVoid);
            directoryAdapter.notifyDataSetChanged();
        }
    }
}
