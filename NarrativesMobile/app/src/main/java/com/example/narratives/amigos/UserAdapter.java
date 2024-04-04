package com.example.narratives.amigos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.narratives.R;
import com.example.narratives.peticiones.User;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {


    private List<User> list;
    private Context context;
    private int resourceLayout;



    public UserAdapter(@NonNull Context _context, int _resource, @NonNull List <User> _objects) {
        super(_context, _resource, _objects);
        this.list = _objects;
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

        User user = list.get(position);

        TextView nombre = view.findViewById(R.id.textViewUsuarioNombrePrueba);
        nombre.setText(user.getUsername());

        TextView rol = view.findViewById(R.id.textViewUsuarioRolPrueba);
        rol.setText(user.getRole());

        ShapeableImageView foto = view.findViewById(R.id.imageViewUsuarioFotoPrueba);
        if (position % 2 == 0) {
            foto.setImageResource(R.drawable.pfp_gato);
        } else {
            foto.setImageResource(R.drawable.pfp_rana);
        }


        return view;
    }
}
