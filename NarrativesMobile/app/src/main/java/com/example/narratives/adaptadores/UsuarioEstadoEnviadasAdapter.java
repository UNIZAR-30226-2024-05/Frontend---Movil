package com.example.narratives.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.fragments.FragmentAmigos;
import com.example.narratives.informacion.InfoAmigos;
import com.example.narratives.peticiones.GenericMessageResult;
import com.example.narratives.peticiones.GenericOtherIdRequest;
import com.example.narratives.peticiones.amistad.lista.UsuarioEstado;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioEstadoEnviadasAdapter extends ArrayAdapter<UsuarioEstado> implements Filterable {


    private List<UsuarioEstado> userList;
    private List<UsuarioEstado> tempUserList;

    private Context context;
    private int resourceLayout;

    private UsuarioEstadoFilter userFilter;

    RetrofitInterface retrofitInterface = ApiClient.getRetrofitInterface();

    public UsuarioEstadoEnviadasAdapter(@NonNull Context _context, int _resource, @NonNull List <UsuarioEstado> _objects) {
        super(_context, _resource, _objects);
        Collections.sort(_objects, new Comparator<UsuarioEstado>() {
            @Override
            public int compare(UsuarioEstado a1, UsuarioEstado a2) {
                return a1.getUsername().compareToIgnoreCase(a2.getUsername());
            }
        });
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

        UsuarioEstado usuario = userList.get(position);

        TextView nombre = view.findViewById(R.id.textViewNombreAnadirUsuario);
        nombre.setText(usuario.getUsername());

        ShapeableImageView foto = view.findViewById(R.id.imageViewFotoAnadirAmigo);
        foto.setImageResource(InfoAmigos.getImageResourceFromImgCode(usuario.getImg()));

        FloatingActionButton boton = (FloatingActionButton) view.findViewById(R.id.botonCancelarSoli);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                peticionAmistadCancel(usuario.getId(), boton);
            }
        });

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if(userFilter == null){
            userFilter = new UsuarioEstadoFilter();
        }
        return userFilter;
    }

    @Nullable
    @Override
    public UsuarioEstado getItem(int position) {
        return userList.get(position);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    class UsuarioEstadoFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();

            if(charSequence != null && charSequence.length() > 0) {
                charSequence = charSequence.toString().toUpperCase();
                ArrayList<UsuarioEstado> filtros = new ArrayList<>();

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
            userList = (ArrayList<UsuarioEstado>)filterResults.values;
            notifyDataSetChanged();
        }
    }

    private void peticionAmistadCancel(int id, FloatingActionButton boton){
        GenericOtherIdRequest request = new GenericOtherIdRequest(id);

        Call<GenericMessageResult> llamada = retrofitInterface.ejecutarAmistadCancel(ApiClient.getUserCookie(), request);
        llamada.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(Call<GenericMessageResult> call, Response<GenericMessageResult> response) {

                if(response.code() == 200) {
                    boton.setVisibility(View.GONE);
                    FragmentAmigos.actualizarLista = true;

                }  else if (response.code() == 409){

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");

                        if(error.equals("No sent request")){
                            Toast.makeText(getContext(), "No puedes cancelar una petición no enviada", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Algo ha fallado obteniendo el error (amigosCancel)", Toast.LENGTH_LONG).show();
                    }
                }  else if (response.code() == 500){
                    Toast.makeText(getContext(), "Error del server (amigosCancel)", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(), "Código de error (amigosCancel): " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GenericMessageResult> call, Throwable t) {
                Toast.makeText(getContext(), "No se ha conectado con el servidor (amigosCancel)",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
