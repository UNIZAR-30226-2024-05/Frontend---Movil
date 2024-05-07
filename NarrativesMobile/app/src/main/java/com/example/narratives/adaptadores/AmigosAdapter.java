package com.example.narratives.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.narratives.R;
import com.example.narratives.informacion.InfoAmigos;
import com.example.narratives.peticiones.amigos.AmigoSimple;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class AmigosAdapter extends ArrayAdapter<AmigoSimple> implements Filterable {


    private List<AmigoSimple> userList;
    private List<AmigoSimple> tempUserList;

    private Context context;
    private int resourceLayout;

    private UserFilter userFilter;

    public AmigosAdapter(@NonNull Context _context, int _resource, @NonNull List <AmigoSimple> _objects) {
        super(_context, _resource, _objects);
        this.userList = _objects;
        this.tempUserList = _objects;
        this.context = _context;
        this.resourceLayout = _resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resourceLayout, null);
        }

        AmigoSimple amigo = userList.get(position);

        TextView nombre = view.findViewById(R.id.textViewAmigoNombre);
        nombre.setText(amigo.getUsername());

        ShapeableImageView foto = view.findViewById(R.id.imageViewAmigoFoto);
        foto.setImageResource(InfoAmigos.getImageResourceFromImgCode(amigo.getImg()));

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if(userFilter == null){
            userFilter = new UserFilter();
        }
        return userFilter;
    }

    @Nullable
    @Override
    public AmigoSimple getItem(int position) {
        return userList.get(position);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    class UserFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();

            if(charSequence != null && charSequence.length() > 0) {
                charSequence = charSequence.toString().toUpperCase();
                ArrayList<AmigoSimple> filtros = new ArrayList<>();

                for (int i = 0; i < tempUserList.size(); i++) {
                    if (tempUserList.get(i).getUsername().toUpperCase().contains(charSequence)) {
                        filtros.add(tempUserList.get(i));
                    }
                }

                filterResults.count = filtros.size();
                filterResults.values = filtros;

            } else {
                filterResults.count = tempUserList.size();
                filterResults.values = tempUserList;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            userList = (ArrayList<AmigoSimple>)filterResults.values;
            notifyDataSetChanged();
        }
    }
}
