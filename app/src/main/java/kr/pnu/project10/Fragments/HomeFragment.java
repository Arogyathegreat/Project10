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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import kr.pnu.project10.Fragments.ViewModels.HomeViewModel;
import kr.pnu.project10.R;
import kr.pnu.project10.Utility.CourseModel;
import kr.pnu.project10.databinding.HomeFragmentBinding;


public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private HomeFragmentBinding binding;
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

    private RecyclerView mCourses;

    private NavController navController;

    private Bundle bundle;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = HomeFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mCourses = binding.recyclerView;
        mCourses.setLayoutManager(new LinearLayoutManager(getActivity()));
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        mViewModel.readDB();

        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundle = new Bundle();

        setRecyclerView();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void setRecyclerView() {
        Query query = rootRef.collection("Demo").orderBy("Course_Name", Query.Direction.ASCENDING);
        Log.d(TAG, query.toString());

        FirestoreRecyclerOptions<CourseModel> options = new FirestoreRecyclerOptions.Builder<CourseModel>()
                .setQuery(query, CourseModel.class)
                .build();

        FirestoreRecyclerAdapter<CourseModel, CourseViewHolder> adapter =
                new FirestoreRecyclerAdapter<CourseModel, CourseViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CourseViewHolder holder, int position, @NonNull CourseModel model) {
                        holder.setCourseName(model.getCourse_Name());
                        Log.d(TAG, model.getCourse_Name());
                    }

                    @NonNull
                    @Override
                    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_course_item, parent, false);
                        return new CourseViewHolder(view);
                    }
                };

        mCourses.setAdapter(adapter);
        adapter.startListening();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setCourseName(final String courseName) {
            TextView courseNameView = mView.findViewById(R.id.course_name);
            RelativeLayout courseItem = mView.findViewById(R.id.course_item);
            courseNameView.setText(courseName);

            courseItem.setOnClickListener(view -> {
                    bundle.putString("courseName", courseName);
                    Navigation.findNavController(view).navigate(R.id.home_to_courseContents_action, bundle);
            });
        }

    }

    final String TAG = "HomeFragment";
}