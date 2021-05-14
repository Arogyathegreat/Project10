package kr.pnu.project10.Fragments;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import kr.pnu.project10.Fragments.ViewModels.ProfileViewModel;
import kr.pnu.project10.R;
import kr.pnu.project10.databinding.ProfileFragmentBinding;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private ProfileViewModel mViewModel;
    private ProfileFragmentBinding binding;
    private NavController navController;

    private FirebaseUser authUser;
    private FirebaseAuth mAuth;

    private ImageView ivProfileImage;
    private TextView tvProfileName;
    private ImageButton btnSettings;

    private Uri uriProfileImage;
    private String userName;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = ProfileFragmentBinding.inflate(inflater, container, false);
        tvProfileName = binding.tvProfileName;
        ivProfileImage = binding.ivProfileImage;
        btnSettings = binding.btnSettings;

        mAuth = FirebaseAuth.getInstance();

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        if (mAuth.getCurrentUser() == null)
            navController.navigate(R.id.signup_dest);
        else {
            authUser = mAuth.getCurrentUser();
            uriProfileImage = authUser.getPhotoUrl();
            userName = authUser.getDisplayName();
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvProfileName.setText(String.valueOf(userName));
        Log.d(TAG, "Uri is :" + userName);
        picassoImageSet(uriProfileImage);

        if (authUser != null)
        {
            btnSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signOut();
                }
            });
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    private void picassoImageSet(Uri imgUri) {
        Picasso.get()
                .load(imgUri)
                .fit()
                .centerInside()
                .into(ivProfileImage, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        Log.d(TAG, "Picasso SUCCESS");
                    }
                });
    }

    private void signOut(){
        mAuth.signOut();
        googleSignOut();
        navController.navigate(R.id.prfile_to_signup_action);
    }

    private void googleSignOut(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        mGoogleSignInClient.signOut();
    }

}