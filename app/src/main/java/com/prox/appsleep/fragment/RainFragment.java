package com.prox.appsleep.fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.prox.appsleep.R;
import com.prox.appsleep.adapter.ItemCategoryAdpater;
import com.prox.appsleep.database.dbCategoryIconModel.CategoryIconDatabase;
import com.prox.appsleep.database.dbCategoryIconModel.CategoryIconViewModel;
import com.prox.appsleep.database.dbFavoriteMusic.FavoriteMusicDatabase;
import com.prox.appsleep.database.dbFavoriteMusic.FavoriteMusicViewModel;
import com.prox.appsleep.itemClick.OnItemClickListener;
import com.prox.appsleep.model.CategoryIconModel;
import com.prox.appsleep.service.PlayMusicService;
import com.prox.appsleep.ui.home.HomeFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RainFragment extends Fragment implements OnItemClickListener {

    public RecyclerView rcvItemRain;
    public ItemCategoryAdpater adpater;
//    public List<CategoryIconModel> categoryIconModelList;
    private CategoryIconModel categoryIconModel;

    public ImageView ivResume;
    public NestedScrollView bottomRemote;
    public BottomSheetBehavior bottomSheetBehavior;

    private PlayMusicService playMusicService;
    private boolean isServiceConnected;

    public LinearLayout bgCategory;

    public boolean isPlayingMedia;
    public CategoryIconModel mCategoryIconModel;
    public List<CategoryIconModel> categoryIconModelListFavorite;
    public CategoryIconViewModel categoryIconViewModel;

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
            mCategoryIconModel = (CategoryIconModel) bundle.get("objectSong");
            categoryIconModelListFavorite = (List<CategoryIconModel>) bundle.get("listMusic");
            isPlayingMedia = bundle.getBoolean("status_player");
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rain, container, false);
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvItemRain = view.findViewById(R.id.rcvItemRain);
        bottomRemote = view.findViewById(R.id.bottom_sheet_remote);
        bgCategory = view.findViewById(R.id.bgCategory);
//        categoryIconModelList = new ArrayList<>();

        adpater = new ItemCategoryAdpater(null, this);
        rcvItemRain.setAdapter(adpater);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rcvItemRain.setLayoutManager(gridLayoutManager);

        categoryIconViewModel = new ViewModelProvider(requireActivity()).get(CategoryIconViewModel.class);
        categoryIconViewModel.getCategoryIconRain().observe(requireActivity(), new Observer<List<CategoryIconModel>>() {
            @Override
            public void onChanged(List<CategoryIconModel> categoryIconModelList) {
                adpater.setCategoryIconModel(categoryIconModelList);
            }
        });
    }

    private void playMusic(CategoryIconModel categoryIconModel) {
        Intent intent = new Intent(requireContext(), PlayMusicService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("listSong", categoryIconModel);
        intent.putExtras(bundle);
        requireActivity().startService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_activity"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void itemClickListener(CategoryIconModel categoryIconModel) {
        playMusic(categoryIconModel);
    }
}
