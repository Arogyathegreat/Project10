package kr.pnu.project10.Fragments;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

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
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();


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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvProfileName.setText(String.valueOf(userName));
        Log.d(TAG, "Uri is :" + userName);
        picassoImageSet(uriProfileImage);

        if (authUser != null) {
            btnSettings.setOnClickListener(view12 -> signOut());
        }

        btnSettings.setOnClickListener(view1 -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), view1);
            popupMenu.getMenuInflater().inflate(R.menu.profile_options_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.signout:
                        signOut();
                        break;
                    case R.id.bookmarks_dest:
                        navController.navigate(R.id.profile_to_bookmarks_action);
                        return true;
                }
                return false;
            });
            popupMenu.setForceShowIcon(true);
            popupMenu.show();
        });

        addUserToDB();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void picassoImageSet(Uri imgUri) {

        String temp = String.valueOf(imgUri).replace("s96-c", "s384-c");

        Picasso.get()
                .load(temp)
                .fit()
                .centerInside()
                .into(ivProfileImage, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        Log.d(TAG, "Picasso SUCCESS. imgUri was" + imgUri);
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        googleSignOut();
        navController.navigate(R.id.profile_to_signup_action);
    }

    private void googleSignOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        mGoogleSignInClient.signOut();
    }

    private void addUserToDB(){
        if (authUser != null)
        {
            Map<String, Object> userData = new HashMap<>();
            userData.put("user_name", authUser.getDisplayName());
            userData.put("user_email", authUser.getEmail());
            userData.put("user_uid", authUser.getUid());
            Log.d(TAG, userData.toString());
            rootRef.collection("Users").document(authUser.getUid())
                    .set(userData)
                    .addOnSuccessListener(unused -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
        }
    }

}