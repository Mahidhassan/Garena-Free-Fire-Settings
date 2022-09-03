package com.jvmfrog.ffsettings.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jvmfrog.ffsettings.adapter.ManufacturersAdapter;
import com.jvmfrog.ffsettings.utils.CustomTabUtil;
import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.databinding.FragmentManufacturerBinding;
import com.jvmfrog.ffsettings.utils.SharedPreferencesUtils;

import org.json.JSONObject;

import java.util.ArrayList;

public class ManufacturerFragment extends Fragment {

    private FragmentManufacturerBinding binding;
    private ArrayList<String> arrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManufacturerBinding.inflate(inflater, container, false);

        arrayList = new ArrayList<>();

        if (!SharedPreferencesUtils.getBoolean(getActivity(), "isFakeManufacturerNames")) {
            arrayList.add("Samsung");
            arrayList.add("iPhone");
            arrayList.add("Xiaomi");
            arrayList.add("Redmi");
            arrayList.add("Oppo");
            arrayList.add("Huawei");
            arrayList.add("Poco");
            arrayList.add("Honor");
            arrayList.add("LG");
            arrayList.add("ZTE");
            arrayList.add("Vivo");
            arrayList.add("Motorola");
        } else {
            arrayList.add("Sumsang");
            arrayList.add("iRhone");
            arrayList.add("Xioami");
            arrayList.add("Rebmi");
            arrayList.add("Oddo");
            arrayList.add("Huowei");
            arrayList.add("Poso");
            arrayList.add("Hohor");
            arrayList.add("GL");
            arrayList.add("TZE");
            arrayList.add("Wivo");
            arrayList.add("Notorola");
        }

        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        binding.recview.setLayoutManager(layoutManager);
        ManufacturersAdapter adapter = new ManufacturersAdapter(arrayList);
        binding.recview.setAdapter(adapter);

        binding.googleFormText.setText(getString(R.string.dont_have_your_phone_model) + " " + getString(R.string.then_click_here));
        binding.googleFormBtn.setOnClickListener(view -> new CustomTabUtil().OpenCustomTab(getActivity(), getString(R.string.google_form), R.color.md_theme_light_onSecondary));

        return binding.getRoot();
    }
}