package uk.org.willmott.mediasyncer;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import uk.org.willmott.mediasyncer.model.Episode;

public class ActivityEpisode extends AppCompatActivity {

    // The episode that we're displaying.
    Episode episode;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public Episode getEpisode() {
        return episode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);

        episode = Parcels.unwrap(getIntent().getParcelableExtra("episode"));

        // ============= Set up the toolbar =====================
        Toolbar toolbar = (Toolbar) findViewById(R.id.episode_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // ======================================================

        toolbar.setTitle("Episode " + Integer.toString(episode.getEpisodeNumber()));
        toolbar.setSubtitle(episode.getTitle());

        ImageView imageView = (ImageView) findViewById(R.id.episode_banner);
        // Set the banner container to be 16:9
        Double max_height = imageView.getWidth() * 0.5625;
        imageView.setMaxHeight(max_height.intValue());
        Picasso.with(ActivityEpisode.this).load(episode.getBannerUrl()).into(imageView);

        // Episode information
        TextView textView = (TextView) findViewById(R.id.episode_info_text);
        textView.setText(episode.getOverview());
    }

    // Define the animation on the back button press.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}