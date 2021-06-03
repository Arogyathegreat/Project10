package kr.pnu.project10.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kr.pnu.project10.Fragments.ViewModels.ExploreViewModel;
import kr.pnu.project10.R;
import kr.pnu.project10.Utility.CreatorModel;
import kr.pnu.project10.databinding.ExploreFragmentBinding;
import kr.pnu.project10.databinding.HomeFragmentBinding;

public class ExploreFragment extends Fragment {

    private ExploreViewModel mViewModel;
    private ExploreFragmentBinding binding;

    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();


    private String strInstagram;
    private String strYoutube;
    private String strTwitter;

    private RecyclerView rvCreatorList;

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ExploreFragmentBinding.inflate(inflater, container, false);
        rvCreatorList = binding.rvCreatorList;
        rvCreatorList.setLayoutManager(new LinearLayoutManager(getActivity()));
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ExploreViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRecyclerView();

        Uri location = Uri.parse("http://instagram.com/_u/its_adamneely");
        Uri ytLocation = Uri.parse("http://www.youtube.com/user/JustinBieberVEVO");
        Intent sendToInstagram = new Intent(Intent.ACTION_VIEW, ytLocation);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setRecyclerView() {
        Query query = rootRef.collection("Creators");

        FirestoreRecyclerOptions<CreatorModel> options = new FirestoreRecyclerOptions.Builder<CreatorModel>()
                .setQuery(query, CreatorModel.class)
                .build();

        FirestoreRecyclerAdapter<CreatorModel, CreatorViewHolder> adapter =
                new FirestoreRecyclerAdapter<CreatorModel, CreatorViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull @NotNull CreatorViewHolder holder, int position, @NonNull @NotNull CreatorModel model) {
                        holder.setCreatorList(model.getCreator_name(), model.getCreator_instagram(),
                                model.getCreator_twitter(), model.getCreator_youtube(), model.getCreator_image());
                    }

                    @NonNull
                    @NotNull
                    @Override
                    public CreatorViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_creator_item, parent, false);
                        return new CreatorViewHolder(view);
                    }
                };

        rvCreatorList.setAdapter(adapter);
        adapter.startListening();
    }

    private class CreatorViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public CreatorViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setCreatorList(final String creatorName, final String creatorInstagram,
                            final String creatorTwitter, final String creatorYoutube,
                            final String creatorImage) {

            TextView tvCreatorName = mView.findViewById(R.id.tvCreatorName);
            ImageView ivCreatorImage = mView.findViewById(R.id.ivCreatorImage);
            ImageButton ibInstagram = mView.findViewById(R.id.ibInstagram);
            ImageButton ibTwitter = mView.findViewById(R.id.ibTwitter);
            ImageButton ibYoutube = mView.findViewById(R.id.ibYoutube);

            tvCreatorName.setText(creatorName);
            setCreatorImage(ivCreatorImage, creatorImage);

            ArrayList<Uri> uriList;

            uriList = createAppUri(creatorInstagram, creatorTwitter, creatorYoutube);

            ArrayList<Uri> finalUriList = uriList;

            ibInstagram.setOnClickListener(view -> {
                Intent sendToInstagram = new Intent(Intent.ACTION_VIEW, finalUriList.get(0));
                try {
                    startActivity(sendToInstagram);
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            });
            ibTwitter.setOnClickListener(view -> {
                Intent sendToTwitter = new Intent(Intent.ACTION_VIEW, finalUriList.get(1));
                try {
                    startActivity(sendToTwitter);
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            });

            ibYoutube.setOnClickListener(view -> {
                Intent sendToYoutube = new Intent(Intent.ACTION_VIEW, finalUriList.get(2));
                try {
                    startActivity(sendToYoutube);
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            });

        }
    }

    private ArrayList<Uri> createAppUri(String ig, String twt, String yt) {
        ArrayList<Uri> uriList = new ArrayList<>();

        uriList.add(Uri.parse("http://instagram.com/_u/" + ig));
        uriList.add(Uri.parse("twitter://user?screen_name=" + twt));
        uriList.add(Uri.parse("http://www.youtube.com/c/" + yt));

        return uriList;
    }

    private void setCreatorImage(ImageView iv, String link){
        Picasso.get()
                .load(link)
                .fit()
                .centerInside()
                .into(iv, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Image loading success");
                    }
                    @Override
                    public void onError(Exception e) {
                        Log.d(TAG, "Image loading failed");
                    }
                });
    }

    private static final String TAG = ExploreFragment.class.getName();
}