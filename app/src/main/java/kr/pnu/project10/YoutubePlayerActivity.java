package kr.pnu.project10;

import static com.google.android.youtube.player.YouTubePlayer.PlayerStyle.DEFAULT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.google.android.youtube.player.YouTubePlayer.PlayerStyle.DEFAULT;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import kr.pnu.project10.Fragments.CourseContentsFragmentArgs;
import kr.pnu.project10.Utility.YoutubeConfig;
import kr.pnu.project10.Utility.YoutubeFailureRecoveryActivity;

public class YoutubePlayerActivity extends YoutubeFailureRecoveryActivity {

    private YouTubePlayerView playerView; // the youtubeplayer video player
    private String mVideoLink;
    private String mVideoName;
    private TextView mVideoNameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);
        playerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        mVideoNameTV = (TextView) findViewById(R.id.video_name);

        playerView.initialize(YoutubeConfig.YOUTUBE_API_KEY, this);

        mVideoName = CourseContentsFragmentArgs.fromBundle(getIntent().getExtras()).getVideoName();
        mVideoLink = CourseContentsFragmentArgs.fromBundle(getIntent().getExtras()).getVideoLink();

        mVideoNameTV.setText(mVideoName);

        Log.d("YoutubePlayerActivity", mVideoLink);

    }


    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return playerView;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        youTubePlayer.setPlayerStyle(DEFAULT);

        Log.d("YoutubePlayerActivity", mVideoLink);

        if(!b)
            youTubePlayer.loadVideo(mVideoLink);

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }


}