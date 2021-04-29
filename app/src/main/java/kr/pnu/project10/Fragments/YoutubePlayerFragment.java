package kr.pnu.project10.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.pnu.project10.Fragments.ViewModels.YoutubePlayerViewModel;
import kr.pnu.project10.R;

public class YoutubePlayerFragment extends Fragment {

    private YoutubePlayerViewModel mViewModel;


    public static YoutubePlayerFragment newInstance() {
        return new YoutubePlayerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_youtube_player, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(YoutubePlayerViewModel.class);
        // TODO: Use the ViewModel
    }

}