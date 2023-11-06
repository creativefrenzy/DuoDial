package com.klive.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.klive.app.R;
import com.klive.app.adapter.AlbumAdapterViewProfile;
import com.klive.app.adapter.metend.AlbumAdapterViewProfileMet;
import com.klive.app.response.metend.UserListResponseNew.FemaleImage;

import java.util.List;

public class PictureViewOnViewProfileAlbumMet extends Dialog {
    Context context;
    ViewPager2 viewPager;
    TabLayout tabLayout;
    RelativeLayout close;
    List<FemaleImage> profile_images;
    int selectedPic;

    public PictureViewOnViewProfileAlbumMet(Context context, List<com.klive.app.response.metend.UserListResponseNew.FemaleImage> profile_images, int selectedPic) {
        super(context, R.style.AppTheme);
        this.context = context;
        this.profile_images = profile_images;
        this.selectedPic = selectedPic;

        init();
    }

    void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_pictureview_album);

        close = findViewById(R.id.rl_close);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.indicator_dot);
        viewPager.setAdapter(new AlbumAdapterViewProfileMet(getContext(), profile_images, false));
        viewPager.setCurrentItem(selectedPic);

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        // tab.setText(" " + (position + 1));
                    }
                }
        ).attach();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        show();
    }
}