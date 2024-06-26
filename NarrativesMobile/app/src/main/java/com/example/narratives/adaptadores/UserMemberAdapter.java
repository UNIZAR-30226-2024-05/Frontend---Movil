package com.example.narratives.adaptadores;

import android.content.Context;
import android.util.Log;
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
import com.example.narratives.peticiones.clubes.Club.UserMember;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class UserMemberAdapter extends ArrayAdapter<UserMember> implements Filterable {


    private List<UserMember> userList;
    private List<UserMember> tempUserList;

    private Context context;
    private int resourceLayout;

    private UserFilter userFilter;

    public UserMemberAdapter(@NonNull Context _context, int _resource, @NonNull List <UserMember> _objects) {
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

        UserMember user = userList.get(position);

        TextView nombre = view.findViewById(R.id.textViewMemberNombre);
        nombre.setText(user.getUsername());

        ShapeableImageView foto = view.findViewById(R.id.imageViewMemberFoto);
        foto.setImageResource(InfoAmigos.getImageResourceFromImgCode(user.getImg()));

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
    public UserMember getItem(int position) {
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
                ArrayList<UserMember> filtros = new ArrayList<>();

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
            userList = (ArrayList<UserMember>)filterResults.values;
            notifyDataSetChanged();
        }
    }
}
