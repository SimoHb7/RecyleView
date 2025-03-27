package com.example.starsrating.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.starsrating.R;
import com.example.starsrating.Beans.Star;
import com.example.starsrating.Service.StarService;

import java.util.ArrayList;
import java.util.List;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.StarViewHolder> implements Filterable {
    private static final String TAG = "StarAdapter";
    private List<Star> stars;
    private List<Star> starsFilter;
    private Context context;
    private NewFilter mfilter;

    public StarAdapter(Context context, List<Star> stars) {
        this.stars = stars;
        this.context = context;
        starsFilter = new ArrayList<>(stars);
        mfilter = new NewFilter(this);
    }

    @NonNull
    @Override
    public StarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.star_item, viewGroup, false);
        final StarViewHolder holder = new StarViewHolder(v);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popup = LayoutInflater.from(context).inflate(R.layout.star_edit_item, null, false);
                final ImageView img = popup.findViewById(R.id.img);
                final RatingBar bar = popup.findViewById(R.id.ratingBar);
                final TextView idss = popup.findViewById(R.id.idss);

                Glide.with(context)
                        .asBitmap()
                        .load(starsFilter.get(holder.getAdapterPosition()).getImg())
                        .apply(new RequestOptions().override(100, 100))
                        .into(img);

                bar.setRating(starsFilter.get(holder.getAdapterPosition()).getStar());
                idss.setText(String.valueOf(starsFilter.get(holder.getAdapterPosition()).getId()));

                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Notez : ")
                        .setMessage("Donner une note entre 1 et 5 :")
                        .setView(popup)
                        .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                float s = bar.getRating();
                                int ids = Integer.parseInt(idss.getText().toString());

                                Star star = StarService.getInstance().findById(ids);
                                if (star != null) {
                                    star.setStar(s);
                                    StarService.getInstance().update(star);
                                    notifyItemChanged(holder.getAdapterPosition());
                                }
                            }
                        })
                        .setNegativeButton("Annuler", null)
                        .create();
                dialog.show();
            }

        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    new AlertDialog.Builder(context)
                            .setTitle("Supprimer")
                            .setMessage("Voulez-vous vraiment supprimer cet élément ?")
                            .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Star toRemove = starsFilter.get(position);
                                    stars.remove(toRemove);
                                    starsFilter.remove(position);
                                    StarService.getInstance().delete(toRemove); // if implemented
                                    notifyItemRemoved(position);
                                }
                            })
                            .setNegativeButton("Annuler", null)
                            .show();
                }
                return true;
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StarViewHolder starViewHolder, int i) {
        Log.d(TAG, "onBindView call ! " + i);
        Glide.with(context)
                .asBitmap()
                .load(starsFilter.get(i).getImg())
                .apply(new RequestOptions().override(100, 100))
                .into(starViewHolder.img);
        starViewHolder.name.setText(starsFilter.get(i).getName().toUpperCase());
        starViewHolder.stars.setRating(starsFilter.get(i).getStar());
        starViewHolder.idss.setText(String.valueOf(starsFilter.get(i).getId()));
    }

    @Override
    public int getItemCount() {
        return starsFilter.size();
    }

    @Override
    public Filter getFilter() {
        return mfilter;
    }

    public static class StarViewHolder extends RecyclerView.ViewHolder {
        TextView idss;
        ImageView img;
        TextView name;
        RatingBar stars;
        RelativeLayout parent;

        public StarViewHolder(@NonNull View itemView) {
            super(itemView);
            idss = itemView.findViewById(R.id.ids);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            stars = itemView.findViewById(R.id.stars);
            parent = itemView.findViewById(R.id.parent);
        }
    }

    public class NewFilter extends Filter {
        private final RecyclerView.Adapter mAdapter;

        public NewFilter(RecyclerView.Adapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Star> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(stars);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Star star : stars) {
                    if (star.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(star);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            starsFilter.clear();
            starsFilter.addAll((List) filterResults.values);
            mAdapter.notifyDataSetChanged();
        }
    }
}