package kr.pnu.project10.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import kr.pnu.project10.Fragments.ViewModels.VoidViewModel;
import kr.pnu.project10.R;


/**
 * Fragment that is visited when <tt>Profile</tt> is clicked in the bottom nav bar.
 * Will check current user login status. If user exists then sent to ProfileFragment else
 * SignupFragment.
 */
public class VoidFragment extends Fragment {

    private VoidViewModel mViewModel;

    private FirebaseAuth mAuth;

    private NavController navController;

    public static VoidFragment newInstance() {
        return new VoidFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null)
            navController.navigate(R.id.signup_dest);
        else
            navController.navigate(R.id.profile_dest);

        return inflater.inflate(R.layout.void_fragment, container, false);
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(VoidViewModel.class);
//        // TODO: Use the ViewModel
//    }

}