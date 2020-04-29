package com.thetechshrine.mangareader.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.thetechshrine.mangareader.R;
import com.thetechshrine.mangareader.activities.ChaptersActivity;
import com.thetechshrine.mangareader.activities.MainActivity;
import com.thetechshrine.mangareader.models.Manga;
import com.thetechshrine.mangareader.utils.Constants;
import com.thetechshrine.mangareader.utils.DateUtils;

import java.util.List;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView container;
        public ImageView picture;
        public TextView title;
        public TextView createdAt;
        public LinearLayout updatedAtContainer;
        public TextView updatedAt;
        public TextView description;
        public TextView genres;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container);
            picture = itemView.findViewById(R.id.picture);
            title = itemView.findViewById(R.id.title);
            createdAt = itemView.findViewById(R.id.createdAt);
            updatedAtContainer = itemView.findViewById(R.id.updatedAtContainer);
            updatedAt = itemView.findViewById(R.id.updatedAt);
            description = itemView.findViewById(R.id.description);
            genres = itemView.findViewById(R.id.genres);

        }
    }

    private Context context;
    private List<Manga> mangas;

    public MangaAdapter(Context context, List<Manga> mangas) {
        this.context = context;
        this.mangas = mangas;
    }

    @NonNull
    @Override
    public MangaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_manga, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaAdapter.ViewHolder holder, int position) {

        // Load fonts
        Typeface poppinsMedium = Typeface.createFromAsset(context.getAssets(), "fonts/poppins/Poppins-Medium.ttf");
        holder.title.setTypeface(poppinsMedium);

        // Load data
        final Manga manga = mangas.get(position);

        Picasso.get().load(manga.getImageUrl()).into(holder.picture);
        holder.title.setText(manga.getTitle());
        holder.createdAt.setText(DateUtils.formatUTCDate(context, manga.getCreatedAt()));
        if (DateUtils.formatUTCDate(context, manga.getCreatedAt()).equals(DateUtils.formatUTCDate(context, manga.getUpdatedAt()))) {
            holder.updatedAtContainer.setVisibility(View.GONE);
        } else {
            holder.updatedAt.setText(DateUtils.formatUTCDate(context, manga.getUpdatedAt()));
        }
        if (manga.getGenres() != null && manga.getGenres().size() > 0) {
            StringBuilder genres = new StringBuilder();
            for (int i=0; i<manga.getGenres().size(); i++) {
                String toAppend = manga.getGenres().get(i);
                if (i != manga.getGenres().size() - 1) toAppend += ", ";
                genres.append(toAppend);
            }
            holder.genres.setText(genres.toString());
        }

        // Init events
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChaptersActivity.class);
                intent.putExtra(Constants.MANGA, manga);
                context.startActivity((intent));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mangas.size();
    }
}
