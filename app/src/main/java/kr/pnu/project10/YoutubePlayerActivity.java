package kr.pnu.project10;

import static com.google.android.youtube.player.YouTubePlayer.PlayerStyle.DEFAULT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.google.android.youtube.player.YouTubePlayer.PlayerStyle.DEFAULT;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import kr.pnu.project10.Fragments.CourseContentsFragmentArgs;
import kr.pnu.project10.Utility.FullScreen;
import kr.pnu.project10.Utility.YoutubeConfig;
import kr.pnu.project10.Utility.YoutubeFailureRecoveryActivity;

public class YoutubePlayerActivity extends YoutubeFailureRecoveryActivity implements
        View.OnClickListener,
        YouTubePlayer.OnFullscreenListener,
        YouTubePlayer.PlayerStateChangeListener,
        YouTubePlayer.PlaybackEventListener {

    private YouTubePlayerView playerView; // the youtubeplayer video player
    private YouTubePlayer youTubePlayer; //calling the YoutubePlayer class for all its methods
    private String mVideoLink;
    private String mVideoName;
    private String mVideoCourse;
    private TextView mVideoNameTV;
    private ImageButton ibBookmarks;
    private boolean fullscreen; //boolean that checks if the youtubeplayerview is in fullscreen mode or not
    private FullScreen fullScreenHelper = new FullScreen(this);

    // To measure time
    private long starttime = 0;
    private long endtime = 0;
    private long timebuf = 0;

    // FIREBASE
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);
        playerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        mVideoNameTV = (TextView) findViewById(R.id.video_name);
        ibBookmarks = (ImageButton) findViewById(R.id.ibBookmarks);

        playerView.initialize(YoutubeConfig.YOUTUBE_API_KEY, this);

        // Why does this work even from BookmarksFragment?
        mVideoName = CourseContentsFragmentArgs.fromBundle(getIntent().getExtras()).getVideoName();
        mVideoLink = CourseContentsFragmentArgs.fromBundle(getIntent().getExtras()).getVideoLink();
        mVideoCourse = CourseContentsFragmentArgs.fromBundle(getIntent().getExtras()).getVideoCourse();

        mVideoNameTV.setText(mVideoName);

        Log.d("YoutubePlayerActivity", mVideoLink);

        doLayout();
        setBookmarks();
    }


    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return playerView;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        this.youTubePlayer = youTubePlayer;
        youTubePlayer.setPlayerStyle(DEFAULT);
        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        youTubePlayer.setOnFullscreenListener(this);
        youTubePlayer.setPlaybackEventListener(this);
        youTubePlayer.setPlayerStateChangeListener(this);

        Log.d("YoutubePlayerActivity", mVideoLink);


        if (!b)
            youTubePlayer.cueVideo(mVideoLink);

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onClick(View view) {
        youTubePlayer.setFullscreen(!fullscreen);
    }

    @Override
    public void onFullscreen(boolean isFullscreen) {
        fullscreen = isFullscreen;
        doLayout();
    }

    @Override
    public void onPlaying() {

    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onBuffering(boolean b) {

    }

    @Override
    public void onSeekTo(int i) {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String videoID) {
        youTubePlayer.play();
        starttime = System.currentTimeMillis();
    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {

    }

    @Override
    public void onVideoEnded() {

    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }


    private void setBookmarks() {

        Map<String, Object> bookmarkData = new HashMap<>();
        bookmarkData.put("Video_Link", mVideoLink);
        bookmarkData.put("Video_Name", mVideoName);
        bookmarkData.put("Video_Course", mVideoCourse);

        ibBookmarks.setOnClickListener(view -> {
            if (mUser != null) {
                rootRef.collection("Users").document(mUser.getUid())
                        .collection("Bookmarks").document(mVideoLink)
                        .set(bookmarkData)
                        .addOnSuccessListener(unused -> {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            Toast.makeText(getApplicationContext(), "Bookmarks successfully added!", Toast.LENGTH_SHORT).show();
                        });
            } else
                Toast.makeText(getApplicationContext(), "Please sign up to use bookmarks", Toast.LENGTH_LONG).show();
        });
    }

    private void doLayout() {
        if (fullscreen) fullScreenHelper.enterFullScreen();
        else fullScreenHelper.exitFullScreen();
    }

    private void updateScoreToDB() {
        endtime = System.currentTimeMillis();
        timebuf = endtime - starttime;
        final long[] score = {0L};

        if ((timebuf >= youTubePlayer.getDurationMillis() * 0.8) && mUser != null) {
            DocumentReference docRef = rootRef.collection("Users").document(mUser.getUid());

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            score[0] = document.getLong("user_score");
                            Log.d(TAG, score[0] + "score is");
                        } else
                            Log.d(TAG, "No such document");
                    }
                }
            });

            docRef.update("user_score", score[0] + 10L)
                    .addOnSuccessListener(unused -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));

        }
    }

    private static final String TAG = "YoutubePlayerActivity";


}