package kr.pnu.project10;

import static com.google.android.youtube.player.YouTubePlayer.PlayerStyle.DEFAULT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.google.android.youtube.player.YouTubePlayer.PlayerStyle.DEFAULT;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
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

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import kr.pnu.project10.Fragments.CourseContentsFragmentArgs;
import kr.pnu.project10.Utility.YoutubeConfig;
import kr.pnu.project10.Utility.YoutubeFailureRecoveryActivity;

public class YoutubePlayerActivity extends YoutubeFailureRecoveryActivity {

    private YouTubePlayerView playerView; // the youtubeplayer video player
    private String mVideoLink;
    private String mVideoName;
    private TextView mVideoNameTV;
    private ImageButton ibBookmarks;

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

        mVideoNameTV.setText(mVideoName);

        Log.d("YoutubePlayerActivity", mVideoLink);

        setBookmarks();
    }


    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return playerView;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        youTubePlayer.setPlayerStyle(DEFAULT);
        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);

        Log.d("YoutubePlayerActivity", mVideoLink);

        if (!b)
            youTubePlayer.loadVideo(mVideoLink);

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    private void setBookmarks() {

        Map<String, Object> bookmarkData = new HashMap<>();
        bookmarkData.put("Video_Link", mVideoLink);
        bookmarkData.put("Video_Name", mVideoName);

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

    private static final String TAG = "YoutubePlayerActivity";


}