package id.cuxxie.bakingapp.Fragment;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.cuxxie.bakingapp.Model.Step;
import id.cuxxie.bakingapp.R;

/**
    Playing video with exo taken from this tutorial: https://google.github.io/ExoPlayer/guide.html
 */
public class DetailFragment extends Fragment {
    @BindView(R.id.details_description) TextView textView;
    @BindView(R.id.details_exovideo) SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    Step step;
    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this,v);


        if(savedInstanceState != null && savedInstanceState.containsKey("step"))
        {
            step = savedInstanceState.getParcelable("step");
        }
        if (getActivity().getIntent().hasExtra("step") && step == null)
        {
            step = getActivity().getIntent().getParcelableExtra("step");
        }

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        exoPlayer =
                ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
        exoPlayerView.setPlayer(exoPlayer);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAllFromStep(step);
    }

    public void loadAllFromStep(Step step)
    {
        if(step == null)
            return;
        this.step = step;
        textView.setText(step.getDescription());
        exoPlayerView.setVisibility(View.VISIBLE);
        if( step.getVideoURL() != null&& step.getVideoURL().length() > 0)
            loadUrlToMediaPlayer(step.getVideoURL());
        else if(step.getThubmnailURL() != null && step.getThubmnailURL().endsWith(".mp4"))
            loadUrlToMediaPlayer(step.getThubmnailURL());
        else
            exoPlayerView.setVisibility(View.GONE);
    }

    private void loadUrlToMediaPlayer(String url)
    {
// Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
// Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "yourApplicationName"), bandwidthMeter);
// Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
// This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(url),
                dataSourceFactory, extractorsFactory, null, null);
// Prepare the player with the source.
        exoPlayer.prepare(videoSource);
        exoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        exoPlayer.release();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("step",step);
        super.onSaveInstanceState(outState);
    }
}
