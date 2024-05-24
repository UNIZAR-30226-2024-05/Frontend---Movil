package com.example.narratives.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.narratives.R;
import com.example.narratives.activities.clubes.BuscarClubActivity;
import com.example.narratives.activities.clubes.ChatClubActivity;
import com.example.narratives.activities.clubes.InfoClubActivity;
import com.example.narratives.activities.main.MainActivity;
import com.example.narratives.fragments.FragmentClubs;
import com.example.narratives.peticiones.clubes.Club;

import java.util.ArrayList;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ItemViewHolder> implements Filterable {
    private ArrayList<Club> clubList;
    private ArrayList<Club> tempClubList;
    private Context context;
    private Fragment fragment;
    private int resourceLayout;
    private ClubFilter clubFilter;


    public ClubAdapter(Context context, @Nullable Fragment fragment, int resource, ArrayList<Club> itemList) {
        this.clubList = itemList;
        this.tempClubList = itemList;
        this.context = context;
        this.fragment = fragment;
        this.resourceLayout = resource;

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from((Context) context).inflate(resourceLayout, parent, false);
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
                    intent = new Intent((Context) context, ChatClubActivity.class);
                    intent.putExtra("club_id", club.getId());
                    fragment.startActivityForResult(intent, FragmentClubs.CHAT_CLUB);
                } else if (context instanceof BuscarClubActivity) {
                    intent = new Intent((Context) context, InfoClubActivity.class);
                    intent.putExtra("club_id", club.getId());
                    ((Activity) context).startActivityForResult(intent, FragmentClubs.INFO_CLUB);
                } else {
                    intent = new Intent();
                }
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

