package com.example.narratives.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.narratives.R;
import com.example.narratives.activities.BuscarClubActivity;
import com.example.narratives.activities.ChatClubActivity;
import com.example.narratives.activities.InfoClubActivity;
import com.example.narratives.activities.MainActivity;
import com.example.narratives.peticiones.clubes.Club;

import java.util.ArrayList;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ItemViewHolder> implements Filterable {
    private ArrayList<Club> clubList;
    private ArrayList<Club> tempClubList;
    private Context context;
    private int resourceLayout;
    private ClubFilter clubFilter;


    public ClubAdapter(Context context, int resource, ArrayList<Club> itemList) {
        this.clubList = itemList;
        this.tempClubList = itemList;
        this.context = context;
        this.resourceLayout = resource;

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resourceLayout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Club club = clubList.get(position);
        holder.clubName.setText(club.getName());
        holder.clubDesc.setText(club.getDescription());

        // Establecer clic en el elemento
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Context context = v.getContext();
                Intent intent;
                if (context instanceof MainActivity) {
                    intent = new Intent(context, ChatClubActivity.class);
                    intent.putExtra("club_id", club.getId());
                } else if (context instanceof BuscarClubActivity) {
                    intent = new Intent(context, InfoClubActivity.class);
                    intent.putExtra("club", club);
                } else {
                    intent = new Intent();
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clubList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView clubName;
        TextView clubDesc;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            clubName = itemView.findViewById(R.id.textView_clubName);
            clubDesc = itemView.findViewById(R.id.textView_clubDesc);
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if(clubFilter == null){
            clubFilter = new ClubFilter();
        }
        return clubFilter;
    }

    class ClubFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();

            if(charSequence != null && charSequence.length() > 0) {
                charSequence = charSequence.toString().toUpperCase();
                ArrayList<Club> filtros = new ArrayList<>();

                for (int i = 0; i < tempClubList.size(); i++) {
                    if (tempClubList.get(i).getName().toUpperCase().contains(charSequence)) {
                        filtros.add(tempClubList.get(i));
                    }
                }

                filterResults.count = filtros.size();
                filterResults.values = filtros;

            } else {
                filterResults.count = tempClubList.size();
                filterResults.values = tempClubList;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            clubList = (ArrayList<Club>)filterResults.values;
            notifyDataSetChanged();
        }
    }
}

