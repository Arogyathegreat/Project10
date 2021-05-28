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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import kr.pnu.project10.Fragments.ViewModels.BookmarksViewModel;
import kr.pnu.project10.R;
import kr.pnu.project10.databinding.BookmarksFragmentBinding;

public class BookmarksFragment extends Fragment {

    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    private BookmarksViewModel mViewModel;
    private BookmarksFragmentBinding binding;
    private RecyclerView rvBookmarksContainer;
    private NavController navController;


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

        if(mUser != null){
            setRecyclerView();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setRecyclerView(){

    }
}