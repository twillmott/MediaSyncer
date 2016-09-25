package uk.org.willmott.mediasyncer.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jcifs.smb.SmbFile;
import uk.org.willmott.mediasyncer.R;
import uk.org.willmott.mediasyncer.model.StorageType;


/**
 * A preference window that allows the user to pick a sameba directory.
 * Created by tomwi on 24/09/2016.
 */
public class DirectoryPickerPreference extends DialogPreference implements Preference.OnPreferenceClickListener, DirectoryClickListener {

    private static String LOG_TAG = DirectoryPickerPreference.class.getSimpleName();

    // The addapter to display the list of directories.
    private DirectoryAdapter directoryAdapter;
    // The list of directories to go in to the adaptor.
    private List<URL> directoriesList;
    // Keeping track of the current directory that we're in.
    private String currentDirectory = "/";

    Context context;

    public DirectoryPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setPersistent(false);
        setOnPreferenceClickListener(this);
        setDialogLayoutResource(R.layout.directory_pop_up);

    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        directoriesList = new ArrayList<>();

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
                if (previousDirectory.startsWith("smb")) {
                    new LoadSambaDirectory().execute(previousDirectory);
                } else if (!previousDirectory.equals("")) {
                    directoriesList.clear();
                    directoriesList.addAll(getDirectories(previousDirectory));
                    directoryAdapter.notifyDataSetChanged();
                }
                currentDirectory = previousDirectory;
            }
        });

        try {
            directoriesList.add(new URL("file", StorageType.SMB.toString(), ""));
            directoriesList.add(new URL("file", StorageType.INTERNAL.toString(), ""));
            directoriesList.add(new URL("file", StorageType.EXTERNAL.toString(), ""));
        } catch (Exception e) {
            // Don't bother doing anything as there will never be an exception thrown.
        }

        // Set up the recycler view to display the directory results.
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_directory);
        directoryAdapter = new DirectoryAdapter(getContext(), this, directoriesList);
        recyclerView.setAdapter(directoryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setTitle(null);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            setSummary(currentDirectory);
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            sharedPref.edit().putString(getKey(), currentDirectory).apply();
        }
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
        if (directory.startsWith("smb") || directory.equals("file://".concat(StorageType.SMB.toString()))) {
            // Any SMB directory
            currentDirectory = directory;
            new LoadSambaDirectory().execute(directory);
        } else if (directory.equals("file://".concat(StorageType.EXTERNAL.toString()))) {
            // If the user picks external storage .
            directoriesList.clear();
            directoriesList.addAll(getDirectories("/storage"));
            directoryAdapter.notifyDataSetChanged();
        } else if (directory.equals("file://".concat(StorageType.INTERNAL.toString()))) {
            // If the user picks internal storage.
            directoriesList.clear();
            directoriesList.addAll(getDirectories("/sdcard"));
            directoryAdapter.notifyDataSetChanged();
        } else {
            // Any normal file path.
            directoriesList.clear();
            directoriesList.addAll(getDirectories(directory));
            directoryAdapter.notifyDataSetChanged();
        }
    }

    private List<URL> getDirectories(String directoryPath) {
        // Due to using URL to pass around file paths, we end up
        // with files urls being like file:/sdcard/Pictures. So need
        // to remove the preceeding file.
        if (directoryPath.startsWith("file:")) {
            directoryPath = directoryPath.substring(5);
        }
        currentDirectory = directoryPath;
        File directory = new File(directoryPath);
        List<URL> returnedFilePaths = new ArrayList<>();
        File[] subDirectories = directory.listFiles();
        if (subDirectories != null) {
            for (File subDirecotry : subDirectories) {
                if (subDirecotry.isDirectory()) {
                    try {
                        returnedFilePaths.add(new URL("file", "", subDirecotry.getAbsolutePath()));
                    } catch (Exception e) {
                        Log.e(LOG_TAG, e.getMessage());
                    }
                }
            }
        }
        return returnedFilePaths;
    }

    private class LoadSambaDirectory extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            // If we've not defined a directory, we want to find servers (domains)
            if (strings.length == 0 || strings[0].equals("file://SMB")) {
                SmbFile[] domains = null;
                List<URL> servers = new ArrayList<>();
                try {
                    // Get all of the domains (workgroups) on the network.
                    domains = (new SmbFile("smb://")).listFiles();

                    // Go through each domain and get all of it's servers.
                    for (SmbFile domain : domains) {
                        SmbFile[] tempServers = new SmbFile(domain.getURL()).listFiles();
                        for (SmbFile file : tempServers) {
                            servers.add(file.getURL());
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (directoriesList.size() > 0) {
                    directoriesList.clear();
                }
                directoriesList.addAll(servers);

                // Otherwise we can load all the directories within the given directory.
            } else {
                try {
                    List<SmbFile> tempDirectories = Arrays.asList((new SmbFile(strings[0])).listFiles());
                    // Go through the list and remove all files. Files won't end with a /. We only want a directory.
                    List<URL> directories = new ArrayList<>();
                    for (SmbFile smbFile : tempDirectories) {
                        if (smbFile.getURL().getFile().substring(smbFile.getURL().getFile().length() - 1).equals("/")) {
                            directories.add(smbFile.getURL());
                        }
                    }

                    // Update the listing displayed in the UI.
                    if (directoriesList.size() > 0) {
                        directoriesList.clear();
                    }
                    directoriesList.addAll(directories);
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
