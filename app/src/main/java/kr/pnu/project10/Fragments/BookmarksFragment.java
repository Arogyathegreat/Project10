package kr.pnu.project10.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import kr.pnu.project10.Fragments.ViewModels.BookmarksViewModel;
import kr.pnu.project10.R;
import kr.pnu.project10.Utility.VideoModel;
import kr.pnu.project10.databinding.BookmarksFragmentBinding;

public class BookmarksFragment extends Fragment {

    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    private BookmarksViewModel mViewModel;
    private BookmarksFragmentBinding binding;
    private RecyclerView rvBookmarksContainer;

    private NavController navController;
    private Bundle bundle;

    public static BookmarksFragment newInstance() {
        return new BookmarksFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BookmarksFragmentBinding.inflate(inflater, container, false);
        rvBookmarksContainer = binding.recyclerView;
        rvBookmarksContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BookmarksViewModel.class);
        mViewModel.readDB();
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundle = new Bundle();


        if (mUser != null) {
            setRecyclerView();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setRecyclerView() {
        Query query = rootRef.collection("Users")
                .document(mUser.getUid())
                .collection("Bookmarks")
                .orderBy("Video_Name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<VideoModel> options = new FirestoreRecyclerOptions.Builder<VideoModel>()
                .setQuery(query, VideoModel.class)
                .build();

        FirestoreRecyclerAdapter<VideoModel, VideoViewHolder> adapter =
                new FirestoreRecyclerAdapter<VideoModel, VideoViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull @NotNull VideoViewHolder holder, int position, @NonNull @NotNull VideoModel model) {
                        holder.setVideoName(model.getVideo_Name(), model.getVideo_Link());
                    }

                    @NonNull
                    @NotNull
                    @Override
                    public VideoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_bookmarks_item, parent, false);
                        return new VideoViewHolder(view);
                    }
                };

        rvBookmarksContainer.setAdapter(adapter);
        adapter.startListening();

    }

    private class VideoViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setVideoName(final String videoName, final String videoLink) {
            TextView tvVideoName = mView.findViewById(R.id.tvVideoName);
            ImageButton ibDeleteBookmarks = mView.findViewById(R.id.ibDeleteBookmarks);

            tvVideoName.setText(videoName);

            tvVideoName.setOnClickListener(view -> {
                bundle.putString("videoName", videoName);
                bundle.putString("videoLink", videoLink);
                Log.d("CourseContentsFragment", videoLink);
                Navigation.findNavController(view).navigate(R.id.bookmarks_to_youtubePlayer_action, bundle);
            });

            ibDeleteBookmarks.setOnClickListener(view ->
                    rootRef.collection("Users").document(mUser.getUid())
                    .collection("Bookmarks").document(videoLink)
                    .delete()
                    .addOnSuccessListener(unused -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e)));
        }
    }

    private static final String TAG = "BookmarksFragment";

}