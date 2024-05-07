package com.example.narratives.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;

import java.util.ArrayList;
import java.util.List;

public class AudiolibroAdapter extends BaseAdapter implements Filterable {

    private List<AudiolibroItem> audiolibroList;
    private List<AudiolibroItem> tempAudiolibroList;
    private Context context;

    private AudiolibroFilter audiolibroFilter;

    public AudiolibroAdapter(Context context, List<AudiolibroItem> audiolibroList) {
        this.context = context;
        this.audiolibroList = audiolibroList;
        this.tempAudiolibroList = new ArrayList<>(audiolibroList);
    }

    @Override
    public int getCount() {
        return audiolibroList.size();
    }

    @Override
    public Object getItem(int position) {
        return audiolibroList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(audiolibroList.get(position).getTitulo());

        return view;
    }

    @Override
    public Filter getFilter() {
        if (audiolibroFilter == null) {
            audiolibroFilter = new AudiolibroFilter();
        }
        return audiolibroFilter;
    }

    class AudiolibroFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();

            if (charSequence != null && charSequence.length() > 0) {
                charSequence = charSequence.toString().toUpperCase();
                ArrayList<AudiolibroItem> filtros = new ArrayList<>();

                for (int i = 0; i < tempAudiolibroList.size(); i++) {
                    if (tempAudiolibroList.get(i).getTitulo().toUpperCase().contains(charSequence)) {
                        filtros.add(tempAudiolibroList.get(i));
                    }
                }

                filterResults.count = filtros.size();
                filterResults.values = filtros;

            } else {
                filterResults.count = tempAudiolibroList.size();
                filterResults.values = tempAudiolibroList;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (filterResults != null && filterResults.values != null) {
                audiolibroList = (ArrayList<AudiolibroItem>) filterResults.values;
                notifyDataSetChanged();
            }
        }
    }
}
