package com.prox.appsleep.ui.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.prox.appsleep.databinding.FragmentSettingBinding;
import com.proxglobal.rate.ProxRateDialog;

public class SettingFragment extends Fragment {

    private SettingViewModel settingViewModel;
    private FragmentSettingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingViewModel =
                new ViewModelProvider(this).get(SettingViewModel.class);

        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.rltLanguageSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireActivity(), "Coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.rltFadeOutDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireActivity(), "Coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.tvTermsOfServiceST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProxRateDialog.showAlways(requireActivity(), requireActivity().getSupportFragmentManager());
            }
        });

        binding.tvPrivacyPolicyST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://exquisiteapp.github.io/sleepmuic/privacy.html"));
                startActivity(browserIntent);
            }
        });


        binding.tvContactUsST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("mailto:"+"vdelicatestudio@gmail.com"));
                startActivity(intent);
            }
        });

        binding.rltModeDarkOrLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireActivity(), "Coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.tvSettingUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireActivity(), "Coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        settingViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}