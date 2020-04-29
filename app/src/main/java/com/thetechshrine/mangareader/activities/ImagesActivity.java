package com.thetechshrine.mangareader.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.thetechshrine.mangareader.R;
import com.thetechshrine.mangareader.fragments.ImageFragment;
import com.thetechshrine.mangareader.helpers.ZoomOutTransformation;
import com.thetechshrine.mangareader.models.Chapter;
import com.thetechshrine.mangareader.utils.ConnectivityManager;
import com.thetechshrine.mangareader.utils.Constants;
import com.thetechshrine.mangareader.utils.Event;
import com.thetechshrine.mangareader.utils.EventBus;
import com.thetechshrine.mangareader.viewmodels.ImagesViewModel;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ImagesActivity extends FragmentActivity {

    private ImagesViewModel viewModel;

    private String mangaId;
    private String chapterId;

    private ViewPager2 viewPager;
    private SwipeRefreshLayout refresh;
    private ImageButton back;
    private ImageButton settings;
    private TextView currentPage;
    private TextView totalPages;

    private PageAdapter pageAdapter;
    private List<Fragment> fragments;

    private boolean isLoading = false;

    private String scaleType = "FIT_CENTER";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            mangaId = intent.getStringExtra(Constants.MANGA_ID);
            chapterId = intent.getStringExtra(Constants.CHAPTER_ID);
            if (mangaId == null || chapterId == null) {
                onBackPressed();
                return;
            }
        }

        //Init calligraphy
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/open_sans/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_images);

        // Init views
        viewPager = findViewById(R.id.viewPager);
        refresh = findViewById(R.id.refresh);
        back = findViewById(R.id.back);
        settings = findViewById(R.id.settings);
        currentPage = findViewById(R.id.currentPage);
        totalPages = findViewById(R.id.totalPages);

        // Setup actions
        setupActions();

        // Init view model
        viewModel = new ViewModelProvider(this).get(ImagesViewModel.class);

        if (!ConnectivityManager.checkInternetConnection(this)) {
            showToast(getString(R.string.no_internet_connection));
            return;
        }

        // subscribe to loading state
        viewModel.isLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                isLoading = aBoolean;
                refresh.setRefreshing(isLoading);
            }
        });

        // subscribe to mangas
        viewModel.getChapter(this, mangaId, chapterId).observe(this, response -> {
            if (response != null && response.getImages() != null) {
                initViewPager(response.getImages());
            }
        });
    }

    private void setupActions() {
        back.setOnClickListener(v -> onBackPressed());

        settings.setOnClickListener(v -> showPopupMenu(v));
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu_change_scale_type);
        popupMenu.setOnMenuItemClickListener(item -> {
            Event event;
            switch (item.getItemId()) {
                case R.id.scaleFitCenter:
                    event = new Event(Event.SUBJECT_IMAGE_FRAGMENT_SCALE_TYPE_FIT_CENTER, "Fit Center");
                    EventBus.publish(EventBus.SUBJECT_IMAGE_FRAGMENT, event);
                    scaleType = "FIT_CENTER";
                    return true;
                case R.id.scaleCenterCrop:
                    event = new Event(Event.SUBJECT_IMAGE_FRAGMENT_SCALE_TYPE_CENTER_CROP, "Center Crop");
                    EventBus.publish(EventBus.SUBJECT_IMAGE_FRAGMENT, event);
                    scaleType = "CENTER_CROP";
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    private void initViewPager(List<String> images) {
        fragments = new ArrayList<>();
        for (int i=0; i<images.size(); i++) {
            fragments.add(ImageFragment.newInstance(images.get(i)));
        }

        pageAdapter = new PageAdapter(this, fragments);
        viewPager.setAdapter(pageAdapter);

        ZoomOutTransformation transformation = new ZoomOutTransformation();
        viewPager.setPageTransformer(transformation);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(final int position) {
                super.onPageSelected(position);

                runOnUiThread(() -> {
                    String page = (position+1) + " / ";
                    currentPage.setText(page);
                });
            }
        });

        totalPages.setText(String.valueOf(fragments.size()));
    }

    private class PageAdapter extends FragmentStateAdapter {

        private List<Fragment> fragments;

        public PageAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragments) {
            super(fragmentActivity);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ImagesActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
