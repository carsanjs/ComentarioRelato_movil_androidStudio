package com.example.comentariorelato_movil.ui.gps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.comentariorelato_movil.databinding.FragmentGpsBinding;

public class gpsFragment extends Fragment {


    private FragmentGpsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewGps HomeViewGps =
                new ViewModelProvider(this).get(HomeViewGps.class);

        binding = FragmentGpsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
