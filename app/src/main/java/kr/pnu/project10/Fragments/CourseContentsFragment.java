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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.w3c.dom.Text;

import kr.pnu.project10.Fragments.ViewModels.CourseContentsViewModel;
import kr.pnu.project10.R;
import kr.pnu.project10.Utility.CourseModel;
import kr.pnu.project10.Utility.VideoModel;
import kr.pnu.project10.databinding.CourseContentsFragmentBinding;

public class CourseContentsFragment extends Fragment {

    private CourseContentsViewModel mViewModel;
    private String mCourseName; // This is shown on top of the fragment
    private CourseContentsFragmentBinding binding;

    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

    private Bundle bundle;

    private TextView mCourseNameTV;
    private RecyclerView mLectures;

    public static CourseContentsFragment newInstance() {
        return new CourseContentsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = CourseContentsFragmentBinding.inflate(inflater, container, false);
        mCourseNameTV = binding.courseName;
        mLectures = binding.recyclerView;
        mLectures.setLayoutManager(new LinearLayoutManager(getActivity()));
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CourseContentsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCourseName = HomeFragmentArgs.fromBundle(getArguments()).getCourseName();
        mCourseNameTV.setText(mCourseName);
        bundle = new Bundle();
        setRecyclerView();
    }

    private void setRecyclerView() {
        Query query = rootRef.collection("Demo")
                .document(mCourseName).collection("Lectures")
                .orderBy("Video_Name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<VideoModel> options = new FirestoreRecyclerOptions.Builder<VideoModel>()
                .setQuery(query, VideoModel.class)
                .build();

        FirestoreRecyclerAdapter<VideoModel, VideoViewHolder> adapter =
                new FirestoreRecyclerAdapter<VideoModel, VideoViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull VideoViewHolder holder, int position, @NonNull VideoModel model) {
                        holder.setVideoName(model.getVideo_Name(), model.getVideo_Link(), model.getVideo_Course());
                    }

                    @NonNull
                    @Override
                    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_course_item, parent, false);
                        return new VideoViewHolder(view);
                    }
                };

        mLectures.setAdapter(adapter);
        adapter.startListening();
    }

    private class VideoViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setVideoName(final String videoName, final String videoLink, final String videoCourse){
            TextView videoNameView = mView.findViewById(R.id.course_name);
            RelativeLayout videoItem = mView.findViewById(R.id.course_item);
            videoNameView.setText(videoName);

            videoItem.setOnClickListener(view -> {
                bundle.putString("videoName", videoName);
                bundle.putString("videoLink", videoLink);
                bundle.putString("videoCourse", videoCourse);
                Log.d("CourseContentsFragment", videoLink);
                Navigation.findNavController(view).navigate(R.id.courseContents_to_youtubePlayer_action, bundle);
            });
        }
    }

}