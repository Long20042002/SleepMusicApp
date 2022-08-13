package com.prox.appsleep.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.prox.appsleep.R;
import com.prox.appsleep.adapter.CategoryAdapter;
import com.prox.appsleep.adapter.MusicPlayAdapter;
import com.prox.appsleep.database.dbCategoryIconModel.CategoryIconDatabase;
import com.prox.appsleep.database.dbCategoryIconModel.CategoryIconViewModel;
import com.prox.appsleep.database.dbFavoriteMusic.FavoriteMusicDatabase;
import com.prox.appsleep.database.dbFavoriteMusic.FavoriteMusicViewModel;
import com.prox.appsleep.databinding.FragmentHomeBinding;
import com.prox.appsleep.itemClick.OnItemClickMusicPlay;
import com.prox.appsleep.model.CategoryIconModel;
import com.prox.appsleep.model.FavoriteMusic;
import com.prox.appsleep.service.CountDownTimerService;
import com.prox.appsleep.service.PlayMusicService;
import com.prox.appsleep.sharedpreferences.DataLocalManger;
import com.proxglobal.proxads.adsv2.ads.ProxAds;
import com.proxglobal.proxads.adsv2.callback.AdsCallback;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment{

    public HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public CategoryAdapter categoryAdapter;

    public LinearLayout bottomRemote;
    public BottomSheetBehavior bottomSheetBehavior;
    public ImageView ivFavorite, ivPlayOrPause, ivExtend;

    public MusicPlayAdapter adapter;
    public RecyclerView rcvItemMusicPlay;
    public List<CategoryIconModel> categoryIconModelList;
    public List<CategoryIconModel> categoryIconModelListFavorite;
    private boolean isPlayingMedia;
    public boolean isPlayingMediaPre;
    private CategoryIconModel mCategoryIconModel;
    public CategoryIconViewModel categoryIconViewModel;

    public FavoriteMusicViewModel favoriteMusicViewModel;
    public TextView tvNameMix, tvTimer;

    public CardView cvTimer;
    public boolean checkHaveMusicPlaying = false;


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
            int actionMusic = bundle.getInt("action_music");
            handleLayoutMusic(actionMusic);
        }
    };

    public BroadcastReceiver broadcastReceiverTimer = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent);
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_activity"));
        requireActivity().registerReceiver(broadcastReceiverTimer, new IntentFilter(CountDownTimerService.COUNTDOWN_BR));

        categoryAdapter = new CategoryAdapter(requireActivity());
        binding.viewpager2.setAdapter(categoryAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewpager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setCustomView(R.layout.item_rain);
                    break;
                case 1:
                    tab.setCustomView(R.layout.item_forest);
                    break;
                case 2:
                    tab.setCustomView(R.layout.item_city);
                    break;
                case 3:
                    tab.setCustomView(R.layout.item_relax);
                    break;
                case 4:
                    tab.setCustomView(R.layout.item_asmr);
                    break;
            }
        }).attach();

        return root;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_activity"));


        bottomRemote = view.findViewById(R.id.bottom_sheet_remote);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomRemote);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        ivPlayOrPause = view.findViewById(R.id.ivRemoteMusicFromBottomSheet);
        rcvItemMusicPlay = bottomRemote.findViewById(R.id.rcvMusicPlay);
        ivFavorite = bottomRemote.findViewById(R.id.ivFavorite);
        ivExtend = bottomRemote.findViewById(R.id.ivExtend);
        tvNameMix = bottomRemote.findViewById(R.id.tvNameMix);
        cvTimer = bottomRemote.findViewById(R.id.cvTimer);
        tvTimer = bottomRemote.findViewById(R.id.tvTimer);

        categoryIconModelList = new ArrayList<>();
        categoryIconViewModel = new ViewModelProvider(requireActivity()).get(CategoryIconViewModel.class);

        cvTimer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                setTimeOffMusic();
            }
        });
        ivExtend.setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        ivExtend.setImageResource(R.drawable.ic_collapsed);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        ivExtend.setImageResource(R.drawable.ic_extend);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {

            }
        });

        isPlayingMediaPre = DataLocalManger.getStatusMedia();
        if (isPlayingMediaPre) {
            ivPlayOrPause.setImageResource(R.drawable.ic_pause);
            sendActionToService(PlayMusicService.ACTION_RESUME);
        } else {
            ivPlayOrPause.setImageResource(R.drawable.ic_play);
            sendActionToService(PlayMusicService.ACTION_PAUSE);
        }

        if (mCategoryIconModel != null) {
            categoryIconModelList.add(mCategoryIconModel);
        }

        adapter = new MusicPlayAdapter(null, new OnItemClickMusicPlay() {
            @Override
            public void itemMusicPlayDelete(CategoryIconModel categoryIconModel) {
                playMusic(categoryIconModel);
                categoryIconModel.setPlaying(false);
                CategoryIconDatabase.getInstance(requireContext()).categoryIconDao().update(categoryIconModel);
            }

            @Override
            public void seekBarCtrlMusicPlay(CategoryIconModel categoryIconModel, int Progress) {
                Intent intent = new Intent(requireActivity(), PlayMusicService.class);
                intent.setAction("volume");
                intent.putExtra("ctrlVolume", Progress);
                Bundle bundle = new Bundle();
                bundle.putSerializable("musicPlaying", categoryIconModel);
                intent.putExtras(bundle);
                requireContext().startService(intent);
            }
        });

        rcvItemMusicPlay.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rcvItemMusicPlay.setAdapter(adapter);

        categoryIconViewModel = new ViewModelProvider(requireActivity()).get(CategoryIconViewModel.class);
        categoryIconViewModel.getAllCategoryIconTrue().observe(requireActivity(), new Observer<List<CategoryIconModel>>() {
            @Override
            public void onChanged(List<CategoryIconModel> categoryIconModelList) {
                if (categoryIconModelList.size() > 8) {

                } else {
                    adapter.setDataMusicPlay(categoryIconModelList);
                }

                if (categoryIconModelList.size() <= 0) {
                    checkHaveMusicPlaying = false;
                }else {
                    checkHaveMusicPlaying = true;
                }
            }
        });

        favoriteMusicViewModel = new ViewModelProvider(requireActivity()).get(FavoriteMusicViewModel.class);
        favoriteMusicViewModel.getSongFavoritePlaying().observe(requireActivity(), favoriteMusics -> {
            if (favoriteMusics.size() != 0) {
                tvNameMix.setText(favoriteMusics.get(0).getNameListFavoriteMusic());
                ivFavorite.setImageResource(R.drawable.ic_favorite_active);
                ivFavorite.setClickable(false);
            } else {
                tvNameMix.setText("My new mix");
                ivFavorite.setImageResource(R.drawable.ic_favorite);
                ivFavorite.setClickable(true);
            }
        });

        ivFavorite.setOnClickListener(v -> addListFavoriteMusic());

        ivPlayOrPause.setOnClickListener(view1 -> {
            if (categoryIconModelListFavorite != null) {
                if (categoryIconModelListFavorite.size() == 0) {
                    ivPlayOrPause.setImageResource(R.drawable.ic_play);
                    return;
                }
            }

            if (isPlayingMedia) {
                sendActionToService(PlayMusicService.ACTION_PAUSE);
                DataLocalManger.setStatusMedia(false);
            } else {
                sendActionToService(PlayMusicService.ACTION_RESUME);
                DataLocalManger.setStatusMedia(true);
            }
        });
    }

    private void addListFavoriteMusic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View viewD = LayoutInflater.from(builder.getContext()).inflate(R.layout.dialog_add_favorite_list_music, null);
        builder.setView(viewD);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextInputEditText edNameFavoriteMusicList = viewD.findViewById(R.id.edNameFavoriteMusicList);
        TextView tvAddFavoriteMusic = viewD.findViewById(R.id.tvAddFavoriteMusic);

        List<CategoryIconModel> categoryIconModelListFavoriteAdd = CategoryIconDatabase
                .getInstance(requireActivity()).categoryIconDao().getCategoryIconModelPlaying();

        tvAddFavoriteMusic.setOnClickListener(v -> {
            String nameFavoriteMusicList = String.valueOf(edNameFavoriteMusicList.getText());
            if (nameFavoriteMusicList.equalsIgnoreCase("")) {
                edNameFavoriteMusicList.setError("Please enter playlist name!");
                return;
            }
            FavoriteMusicDatabase.getInstance(requireContext()).favoriteMusicDao()
                    .insert(new FavoriteMusic(nameFavoriteMusicList, categoryIconModelListFavoriteAdd, true));
            dialog.dismiss();
        });
    }

    private void handleLayoutMusic(int actionMusic) {
        switch (actionMusic) {
            case PlayMusicService.ACTION_START:
            case PlayMusicService.ACTION_RESUME:
                DataLocalManger.setStatusMedia(true);
                ivPlayOrPause.setImageResource(R.drawable.ic_pause);
                break;

            case PlayMusicService.ACTION_PAUSE:
            case PlayMusicService.ACTION_CLEAR:
                ivPlayOrPause.setImageResource(R.drawable.ic_play);
                DataLocalManger.setStatusMedia(false);
                break;
        }
    }

    private void sendActionToService(int action) {
        Intent intent = new Intent(getActivity(), PlayMusicService.class);
        intent.putExtra("action_music_service", action);

        requireContext().startService(intent);
    }

    private void playMusic(CategoryIconModel categoryIconModel) {
        Intent intent = new Intent(requireContext(), PlayMusicService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("listSong", categoryIconModel);
        intent.putExtras(bundle);
        requireActivity().startService(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void setTimeOffMusic() {
        if (!checkHaveMusicPlaying){
            Toast.makeText(requireActivity(),"Please add sound first", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View viewP = LayoutInflater.from(builder.getContext()).inflate(R.layout.dialog_set_time_off_music, null);
        builder.setView(viewP);
        Dialog dialogP = builder.create();
        dialogP.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogP.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialogP.show();

        ImageView ivCloseDialog = viewP.findViewById(R.id.ivCloseDialog);
        NumberPicker npHour = viewP.findViewById(R.id.npHourOffMusic);
        NumberPicker npMinutes = viewP.findViewById(R.id.npMinutesOffMusic);
        TextView tvTimerCustomOffMusic = viewP.findViewById(R.id.tvTimerCustomOffMusic);
        TextView tvRemoveTimer = viewP.findViewById(R.id.tvRemoveTimer);

        npHour.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        npMinutes.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        npHour.setTextColor(Color.WHITE);
        npMinutes.setTextColor(Color.WHITE);

        npHour.setMinValue(0);
        npHour.setMaxValue(12);
        npMinutes.setMinValue(0);
        npMinutes.setMaxValue(59);

        ivCloseDialog.setOnClickListener(v -> dialogP.dismiss());

        tvRemoveTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), CountDownTimerService.class);
                requireActivity().stopService(intent);
                tvTimer.setText("00:00");
                dialogP.dismiss();
            }
        });

        tvTimerCustomOffMusic.setOnClickListener(v -> {
            int setTimer = (npHour.getValue()*60*60000)+(npMinutes.getValue()*60000);
            sendTimerToService(setTimer);
            dialogP.dismiss();
        });

    }

    private void sendTimerToService(int setTimer){
        Intent intent = new Intent(requireContext(), CountDownTimerService.class);
        requireActivity().stopService(intent);
        intent.setAction("setTimeOffMusic");
        intent.putExtra("timer", setTimer);
        requireContext().startService(intent);
    }

    @SuppressLint("SetTextI18n")
    private void updateGUI(Intent intent){
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 0);
            String timeFormatted;
            int hours = (int) ((millisUntilFinished/1000)/3600);
            int minutes = (int) ((millisUntilFinished/1000)%3600) / 60;
            int seconds = (int) (( millisUntilFinished / 1000 ) % 60);
            if (hours > 0){
                timeFormatted = String.format(Locale.getDefault(),"%d:%02d:%02d",hours, minutes, seconds);
            }else {
                timeFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
            }

            if (millisUntilFinished >= 1000) {
                tvTimer.setText(timeFormatted);
            }else {
                tvTimer.setText("00:00");
                sendActionToService(PlayMusicService.ACTION_PAUSE);
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver);
        try {
            requireActivity().unregisterReceiver(broadcastReceiverTimer);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}