package com.thetechshrine.mangareader.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thetechshrine.mangareader.R;
import com.thetechshrine.mangareader.activities.ImagesActivity;
import com.thetechshrine.mangareader.models.Chapter;
import com.thetechshrine.mangareader.utils.Constants;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView releaseDate;
        public ImageButton view;
        public ImageButton download;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            releaseDate = itemView.findViewById(R.id.releaseDate);
            view = itemView.findViewById(R.id.view);
            download = itemView.findViewById(R.id.download);
        }
    }

    private Context context;
    private List<Chapter> chapters;

    private String mangaId;

    public ChapterAdapter(Context context, List<Chapter> chapters, String mangaId) {
        this.context = context;
        this.chapters = chapters;
        this.mangaId = mangaId;
    }

    @NonNull
    @Override
    public ChapterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_chapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterAdapter.ViewHolder holder, int position) {

        // Load fonts
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/open_sans/OpenSans-SemiBold.ttf");
        holder.name.setTypeface(typeface);

        // Load data
        final Chapter chapter = chapters.get(position);

        String name = "Chapter " + chapter.getNumber();
        holder.name.setText(name);
        holder.releaseDate.setText(chapter.getReleaseDate());

        // Init events
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImagesActivity.class);
                intent.putExtra(Constants.MANGA_ID, mangaId);
                intent.putExtra(Constants.CHAPTER_ID, chapter.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }
}
