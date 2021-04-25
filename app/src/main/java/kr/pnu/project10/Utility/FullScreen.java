package kr.pnu.project10.Utility;

import android.app.Activity;
import android.view.View;

/**
 * Helper class for youtubeplayerview to go fullscreen
 **/

public class FullScreen {

    private final Activity context; // takes the current context
    private final View[] views; // takes all of the views in the context

    /**
     * @param context is current context
     * @param views   to hide/show
     */
    public FullScreen(Activity context, View... views) {
        this.context = context;
        this.views = views;
    }

    /**
     * call to enter fullscreen
     */
    public void enterFullScreen() {
        View decorView = context.getWindow().getDecorView();

        hideSystemUi(decorView); // calls the hideSystemUi function below to set fullscreen

        for (View view : views) {
            view.setVisibility(View.GONE);
            view.invalidate();
        }
    }

    /**
     * call to exit fullscreen
     */
    public void exitFullScreen() {
        View decorView = context.getWindow().getDecorView();

        showSystemUi(decorView);

        for (View view : views) {
            view.setVisibility(View.VISIBLE);
            view.invalidate();
        }
    }

    /**
     * @param mDecorView decor view to hide
     */
    private void hideSystemUi(View mDecorView) {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE  // while in fullscreen set everything to fit the system windows
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION    // View would like its window to be laid out as if it has requested SYSTEM_UI_FLAG_HIDE_NAVIGATION, even if it currently hasn't.
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // View would like its window to be laid out as if it has requested SYSTEM_UI_FLAG_FULLSCREEN, even if it currently hasn't.
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION   // set the system navigation to be temporarily hidden
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // sets the view so that it can take the whole screen
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); // view is interactive while hiding status bar or the navigation bar
    }

    /**
     * @param mDecorView decor view to show
     */
    private void showSystemUi(View mDecorView) {
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
}
