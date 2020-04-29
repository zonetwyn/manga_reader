package com.thetechshrine.mangareader.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.thetechshrine.mangareader.R;
import com.thetechshrine.mangareader.adapters.MangaAdapter;
import com.thetechshrine.mangareader.models.Chapter;
import com.thetechshrine.mangareader.models.Manga;
import com.thetechshrine.mangareader.payloads.MangaResponse;
import com.thetechshrine.mangareader.utils.ConnectivityManager;
import com.thetechshrine.mangareader.utils.RecyclerItemClickListener;
import com.thetechshrine.mangareader.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refresh;

    private List<Manga> mangas;
    private MangaAdapter adapter;

    private boolean isLoading = false;
    private int currentPage = 1;
    private boolean hasNext = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Init calligraphy
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/open_sans/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_main);

        // Init views
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/poppins/Poppins-SemiBold.ttf"));

        recyclerView = findViewById(R.id.recyclerView);
        refresh = findViewById(R.id.refresh);

        // Init components
        initMangas();
        initSwipeRefresh();

        // Init view model
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        if (!ConnectivityManager.checkInternetConnection(this)) {
            showToast(getString(R.string.no_internet_connection));
            return;
        }

        // subscribe to loading state
        viewModel.isLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null) {
                    isLoading = aBoolean;
                    refresh.setRefreshing(isLoading);
                }
            }
        });

        // subscribe to mangas
        viewModel.getMangas(this, currentPage).observe(this, new Observer<MangaResponse>() {
            @Override
            public void onChanged(@Nullable MangaResponse response) {
                if (response != null && response.getMangas() != null) {
                    currentPage = response.getPage();
                    hasNext = response.isHasNextPage();
                    insertMangas(response.getMangas());
                }
            }
        });
    }

    private void initSwipeRefresh() {
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        if (ConnectivityManager.checkInternetConnection(this)) {
            mangas.clear();
            adapter.notifyDataSetChanged();
            viewModel.loadMangas(this, 1);
        } else {
            refresh.setRefreshing(false);
            showToast(getString(R.string.no_internet_connection));
        }
    }


    private void initMangas() {
        mangas = new ArrayList<>();
        adapter = new MangaAdapter(this, mangas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastVisibleItemPosition() == mangas.size() - 1) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void insertMangas(List<Manga> subjects) {
        this.mangas.addAll(subjects);
        if (this.mangas.size() == 0) {
            showToast(getString(R.string.no_subjects));
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void loadMore() {
        if (hasNext) {
            viewModel.loadMangas(this, ++currentPage);
        }
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
