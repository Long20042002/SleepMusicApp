package com.prox.appsleep.ui.favorite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.prox.appsleep.R;
import com.prox.appsleep.adapter.FavoriteMusicAdapter;
import com.prox.appsleep.adapter.MusicPlayAdapter;
import com.prox.appsleep.database.dbAlarm.AlarmDatabase;
import com.prox.appsleep.database.dbCategoryIconModel.CategoryIconDatabase;
import com.prox.appsleep.database.dbFavoriteMusic.FavoriteMusicDatabase;
import com.prox.appsleep.database.dbFavoriteMusic.FavoriteMusicViewModel;
import com.prox.appsleep.databinding.FragmentFavoriteBinding;
import com.prox.appsleep.itemClick.OnItemClickFavoriteMusic;
import com.prox.appsleep.itemClick.OnItemClickMusicPlay;
import com.prox.appsleep.model.CategoryIconModel;
import com.prox.appsleep.model.FavoriteMusic;
import com.prox.appsleep.service.PlayMusicService;
import com.prox.appsleep.sharedpreferences.DataLocalManger;
import com.proxglobal.proxads.adsv2.ads.ProxAds;
import com.proxglobal.proxads.adsv2.callback.AdsCallback;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    public FavoriteViewModel favoriteViewModel;
    private FragmentFavoriteBinding binding;
    private FavoriteMusicAdapter adapter;
    public FavoriteMusicViewModel favoriteMusicViewModel;

    public List<CategoryIconModel> categoryIconModelListFavorite;
    public boolean isPlayingMedia;
    public CategoryIconModel mCategoryIconModel;

    List<CategoryIconModel> categoryIconModelList = new ArrayList<>();

    public String nameFavoriteMusicList;

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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ProxAds.getInstance().showSmallNativeWithShimmer(requireActivity(), getResources().getString(R.string.id_native_favorite), binding.adContainer, new AdsCallback() {
            @Override
            public void onShow() {
                super.onShow();
            }

            @Override
            public void onClosed() {
                super.onClosed();
            }

            @Override
            public void onError() {
                super.onError();
            }
        });

        adapter = new FavoriteMusicAdapter(requireContext(), new OnItemClickFavoriteMusic() {
            @Override
            public void ItemClickPlayFavoriteMusicList(FavoriteMusic favoriteMusic) {
                playMusicFavorite(favoriteMusic);
            }

            @Override
            public void ItemClickDeleteFavoriteMusicList(FavoriteMusic favoriteMusic) {
                deleteFavorite(favoriteMusic);
            }

            @Override
            public void ItemClickUpdateFavoriteMusicList(FavoriteMusic favoriteMusic) {
                if (favoriteMusic.isPlaying()){
                    playMusicFavoriteUpdate(favoriteMusic);
                }
                updateFavorite(favoriteMusic);
            }
        });

        binding.rcvListFavoriteMusic.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        binding.rcvListFavoriteMusic.setLayoutManager(linearLayoutManager);

        favoriteMusicViewModel = new ViewModelProvider(requireActivity()).get(FavoriteMusicViewModel.class);
        favoriteMusicViewModel.getAllFavoriteMusic().observe(getViewLifecycleOwner(), new Observer<List<FavoriteMusic>>() {
            @Override
            public void onChanged(List<FavoriteMusic> favoriteMusics) {
                adapter.setFavoriteMusics(favoriteMusics);
            }
        });


        favoriteViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    private void updateFavorite(FavoriteMusic favoriteMusic) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View viewD = LayoutInflater.from(builder.getContext()).inflate(R.layout.dialog_update_favorite_list_music, null);
        builder.setView(viewD);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

        categoryIconModelList = favoriteMusic.getListFavoriteMusic();

        TextView tvNameOfListFavorite = viewD.findViewById(R.id.tvNameOfListFavorite);
        ImageView ivEditFavoriteMusic = viewD.findViewById(R.id.ivEditFavoriteMusic);
        ImageView ivCloseUpdateClock = viewD.findViewById(R.id.ivCloseUpdateFavoriteDialog);
        EditText edNameUpdateFavoriteMusicList = viewD.findViewById(R.id.edNameUpdateFavoriteMusicList);
        ImageView ivAcpEditNameFavorite = viewD.findViewById(R.id.ivAcpEditNameFavorite);
        RecyclerView rvListMusicFavorite = viewD.findViewById(R.id.rcvListMusicOfFavorite);
        TextView tvDeleteFavoriteMusic = viewD.findViewById(R.id.tvDeleteFavoriteMusic);
        TextView tvUpdateFavoriteMusic = viewD.findViewById(R.id.tvUpdateFavoriteMusic);
        RelativeLayout rltNameFavoriteMusic = viewD.findViewById(R.id.rltNameFavoriteMusic);
        ConstraintLayout cstrEditNameFavoriteMusic = viewD.findViewById(R.id.cstrEditNameFavoriteMusic);
        tvNameOfListFavorite.setText(favoriteMusic.getNameListFavoriteMusic());

        nameFavoriteMusicList = favoriteMusic.getNameListFavoriteMusic();

        ivCloseUpdateClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (favoriteMusic.isPlaying()) {
//
//                    for (int i = 0; i < favoriteMusic.getListFavoriteMusic().size(); i++) {
//                        if (favoriteMusic.get(i).getId() == favoriteMusic.getId()) {
//                            playMusicFavoriteUpdate(favoriteMusics.get(i));
//                        }
//                    }
//                }
                dialog.dismiss();
            }
        });

        ivEditFavoriteMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rltNameFavoriteMusic.setVisibility(View.GONE);
                cstrEditNameFavoriteMusic.setVisibility(View.VISIBLE);
            }
        });

        MusicPlayAdapter musicAdapter = new MusicPlayAdapter(null, new OnItemClickMusicPlay() {
            @Override
            public void itemMusicPlayDelete(CategoryIconModel categoryIconModel) {
                playMusic(categoryIconModel);
                categoryIconModel.setPlaying(false);
                CategoryIconDatabase.getInstance(builder.getContext()).categoryIconDao().update(categoryIconModel);
                favoriteMusic.setPlaying(false);
                FavoriteMusicDatabase.getInstance(requireActivity()).favoriteMusicDao().update(favoriteMusic);
                categoryIconModelList.remove(categoryIconModel);
//                favoriteMusic.setListFavoriteMusic(arrayList);
//                FavoriteMusicDatabase.getInstance(builder.getContext()).favoriteMusicDao()
//                        .update(favoriteMusic);

            }

            @Override
            public void seekBarCtrlMusicPlay(CategoryIconModel categoryIconModel, int Progress) {
                Intent intent = new Intent(requireContext(), PlayMusicService.class);
                intent.setAction("volume");
                intent.putExtra("ctrlVolume", Progress);
                Bundle bundle = new Bundle();
                bundle.putSerializable("musicPlaying", categoryIconModel);
                intent.putExtras(bundle);
                requireContext().startService(intent);
            }
        });
        musicAdapter.setDataMusicPlay(favoriteMusic.getListFavoriteMusic());
        rvListMusicFavorite.setAdapter(musicAdapter);
        rvListMusicFavorite.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        tvUpdateFavoriteMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteMusic.setPlaying(true);
                favoriteMusic.setNameListFavoriteMusic(nameFavoriteMusicList);
                favoriteMusic.setListFavoriteMusic(categoryIconModelList);
                FavoriteMusicDatabase.getInstance(requireContext()).favoriteMusicDao()
                        .update(favoriteMusic);
                dialog.dismiss();
            }
        });

        ivAcpEditNameFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edNameUpdateFavoriteMusicList.getText().toString().equals("")) {
                    edNameUpdateFavoriteMusicList.setError("Please enter playlist name!");
                    return;
                }

                if (edNameUpdateFavoriteMusicList.getText().toString().length() > 15) {
                    edNameUpdateFavoriteMusicList.setError("Enter the name of your favorites list under 15 characters!");
                    return;
                }
                nameFavoriteMusicList = String.valueOf(edNameUpdateFavoriteMusicList.getText());
                cstrEditNameFavoriteMusic.setVisibility(View.GONE);
                rltNameFavoriteMusic.setVisibility(View.VISIBLE);
                tvNameOfListFavorite.setText(nameFavoriteMusicList);
            }
        });

        tvDeleteFavoriteMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteFavorite(favoriteMusic);
                dialog.dismiss();
            }
        });

    }

    private void playMusic(CategoryIconModel categoryIconModel) {
        Intent intent = new Intent(getActivity(), PlayMusicService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("listSong", categoryIconModel);
        intent.putExtras(bundle);
        requireContext().startService(intent);
    }

    private void deleteFavorite(FavoriteMusic favoriteMusic) {
        FavoriteMusicDatabase.getInstance(requireContext()).favoriteMusicDao().delete(favoriteMusic);
        AlarmDatabase.getInstance(requireActivity()).alarmDao().updateNameMixAlarm(favoriteMusic.getNameListFavoriteMusic());
        sendActionToService(PlayMusicService.ACTION_CLEAR);
    }

    private void playMusicFavorite(FavoriteMusic favoriteMusic) {
        if (favoriteMusic.isPlaying()) {
            CategoryIconDatabase.getInstance(requireContext()).categoryIconDao().setIsPlayingFalse();
            clickStopService();
            DataLocalManger.setCountMusicPlaying(favoriteMusic.getListFavoriteMusic().size());
            for (int i = 0; i < favoriteMusic.getListFavoriteMusic().size(); i++) {
                playMusic(favoriteMusic.getListFavoriteMusic().get(i));
                favoriteMusic.getListFavoriteMusic().get(i).setPlaying(true);
                CategoryIconDatabase.getInstance(requireContext()).categoryIconDao()
                        .update(favoriteMusic.getListFavoriteMusic().get(i));
                DataLocalManger.setCountMusicPlaying(favoriteMusic.getListFavoriteMusic().size());
            }
        }else {
            sendActionToService(PlayMusicService.ACTION_CLEAR);
            CategoryIconDatabase.getInstance(requireContext()).categoryIconDao().setIsPlayingFalse();
        }
    }

    private void playMusicFavoriteUpdate(FavoriteMusic favoriteMusic) {
        if (favoriteMusic.isPlaying()) {
            CategoryIconDatabase.getInstance(requireContext()).categoryIconDao().setIsPlayingFalse();
            clickStopService();
            DataLocalManger.setCountMusicPlaying(favoriteMusic.getListFavoriteMusic().size());
            for (int i = 0; i < favoriteMusic.getListFavoriteMusic().size(); i++) {
                playMusic(favoriteMusic.getListFavoriteMusic().get(i));
                favoriteMusic.getListFavoriteMusic().get(i).setPlaying(true);
                CategoryIconDatabase.getInstance(requireContext()).categoryIconDao()
                        .update(favoriteMusic.getListFavoriteMusic().get(i));
            }
        }else {
            sendActionToService(PlayMusicService.ACTION_CLEAR);
            CategoryIconDatabase.getInstance(requireContext()).categoryIconDao().setIsPlayingFalse();
        }
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void sendActionToService(int action) {
        Intent intent = new Intent(requireContext(), PlayMusicService.class);
        intent.putExtra("action_music_service", action);

        requireContext().startService(intent);
    }

    private void clickStopService(){
        Intent intent = new Intent(requireContext(), PlayMusicService.class);
        requireContext().stopService(intent);
    }
}