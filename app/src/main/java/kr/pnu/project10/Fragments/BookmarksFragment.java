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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private RelativeLayout rlBookmarksEmpty;

    private NavController navController;
    private Bundle bundle;

    public static BookmarksFragment newInstance() {
        return new BookmarksFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BookmarksFragmentBinding.inflate(inflater, container, false);
        rlBookmarksEmpty = binding.rlBookmarksEmpty;
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
            bookmarksEmpty();
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
                        holder.setVideoName(model.getVideo_Name(), model.getVideo_Link(), model.getVideo_Course());
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

        void setVideoName(final String videoName, final String videoLink, final String videoCourse) {
            TextView tvVideoName = mView.findViewById(R.id.tvVideoName);
            TextView tvVideoCourse = mView.findViewById(R.id.tvVideoCourse);
            ImageButton ibDeleteBookmarks = mView.findViewById(R.id.ibDeleteBookmarks);

            tvVideoName.setText(videoName);
            tvVideoCourse.setText(videoCourse);

            tvVideoName.setOnClickListener(view -> {
                bundle.putString("videoName", videoName);
                bundle.putString("videoLink", videoLink);
                bundle.putString("videoCourse", videoCourse);
                Log.d("CourseContentsFragment", videoLink);
                Navigation.findNavController(view).navigate(R.id.bookmarks_to_youtubePlayer_action, bundle);
            });

            ibDeleteBookmarks.setOnClickListener(view ->
                    rootRef.collection("Users").document(mUser.getUid())
                            .collection("Bookmarks").document(videoLink)
                            .delete()
                            .addOnSuccessListener(unused -> {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                bookmarksEmpty();
                            })
                            .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e)));
        }
    }


    private void bookmarksEmpty() {
        rootRef.collection("Users").document(mUser.getUid()).collection("Bookmarks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Task is successful" + task.getResult().size());
                            setRlBookmarksEmpty(task.getResult().size() == 0);
                        } else
                            Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void setRlBookmarksEmpty(boolean visibility) {
        if (visibility) rlBookmarksEmpty.setVisibility(View.VISIBLE);
        else rlBookmarksEmpty.setVisibility(View.GONE);
    }


    private static final String TAG = "BookmarksFragment";

}