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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.thetechshrine.mangareader.R;
import com.thetechshrine.mangareader.adapters.ChapterAdapter;
import com.thetechshrine.mangareader.models.Chapter;
import com.thetechshrine.mangareader.models.Manga;
import com.thetechshrine.mangareader.payloads.ChapterResponse;
import com.thetechshrine.mangareader.utils.ConnectivityManager;
import com.thetechshrine.mangareader.utils.Constants;
import com.thetechshrine.mangareader.viewmodels.ChaptersViewModel;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChaptersActivity extends AppCompatActivity {

    private ChaptersViewModel viewModel;
    private Manga manga;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refresh;

    private List<Chapter> chapters;
    private ChapterAdapter adapter;

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

        // Get params
        Intent intent = getIntent();
        if (intent != null) {
            manga = intent.getParcelableExtra(Constants.MANGA);
            if (manga == null) {
                onBackPressed();
                return;
            }
        }

        //Init calligraphy
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/open_sans/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_chapters);

        // Init views
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/poppins/Poppins-SemiBold.ttf"));

        assert manga != null;
        toolbarTitle.setText(manga.getTitle().length() > 24 ? manga.getTitle().substring(0, 24) : manga.getTitle());

        recyclerView = findViewById(R.id.recyclerView);
        refresh = findViewById(R.id.refresh);

        // Init components
        initChapters();
        initSwipeRefresh();

        // Init view model
        viewModel = new ViewModelProvider(this).get(ChaptersViewModel.class);

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

        // subscribe to chapters
        viewModel.getChapters(this, manga.getId(), currentPage).observe(this, new Observer<ChapterResponse>() {
            @Override
            public void onChanged(@Nullable ChapterResponse response) {
                if (response != null && response.getChapters() != null) {
                    currentPage = response.getPage();
                    hasNext = response.isHasNextPage();
                    insertChapters(response.getChapters());
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
            chapters.clear();
            adapter.notifyDataSetChanged();
            viewModel.loadChapters(this, manga.getId(), 1);
        } else {
            refresh.setRefreshing(false);
            showToast(getString(R.string.no_internet_connection));
        }
    }


    private void initChapters() {
        chapters = new ArrayList<>();
        adapter = new ChapterAdapter(this, chapters, manga.getId());
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
                    if (linearLayoutManager != null && linearLayoutManager.findLastVisibleItemPosition() == chapters.size() - 1) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void insertChapters(List<Chapter> subjects) {
        this.chapters.addAll(subjects);
        if (this.chapters.size() == 0) {
            showToast(getString(R.string.no_subjects));
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void loadMore() {
        if (hasNext) {
            viewModel.loadChapters(this, manga.getId(), ++currentPage);
        }
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ChaptersActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
