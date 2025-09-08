package com.example.securitypassword.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securitypassword.DetalleContrasenaActivity;
import com.example.securitypassword.R;
import com.example.securitypassword.model.Contrasena;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ContrasenaAdapter extends RecyclerView.Adapter<ContrasenaAdapter.ContrasenaViewHolder> {

    private List<Contrasena> listaContrasenas;
    private Context context;

    public ContrasenaAdapter(Context context) {
        this.context = context;
        this.listaContrasenas = new ArrayList<>();
    }

    public void setContrasenas(List<Contrasena> contrasenas) {
        this.listaContrasenas = contrasenas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContrasenaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contrasena, parent, false);
        return new ContrasenaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContrasenaViewHolder holder, int position) {
        Contrasena contrasena = listaContrasenas.get(position);
        holder.bind(contrasena);
    }

    @Override
    public int getItemCount() {
        return listaContrasenas.size();
    }

    class ContrasenaViewHolder extends RecyclerView.ViewHolder {
        private TextView textoServicio;
        private TextView textoUsuario;
        private TextView textoFecha;
        private TextView textoUrl;

        public ContrasenaViewHolder(@NonNull View itemView) {
            super(itemView);
            textoServicio = itemView.findViewById(R.id.textoServicio);
            textoUsuario = itemView.findViewById(R.id.textoUsuario);
            textoFecha = itemView.findViewById(R.id.textoFecha);
            textoUrl = itemView.findViewById(R.id.textoUrl);

            // Click listener para abrir detalles
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Contrasena contrasena = listaContrasenas.get(position);
                    Intent intent = new Intent(context, DetalleContrasenaActivity.class);
                    intent.putExtra("contrasena_id", contrasena.getId());
                    intent.putExtra("servicio", contrasena.getServicio());
                    intent.putExtra("url", contrasena.getUrl());
                    intent.putExtra("usuario", contrasena.getUsuario());
                    intent.putExtra("contrasena", contrasena.getContrasena());
                    intent.putExtra("notas", contrasena.getNotas());
                    context.startActivity(intent);
                }
            });
        }

        public void bind(Contrasena contrasena) {
            textoServicio.setText(contrasena.getServicio());
            textoUsuario.setText(contrasena.getUsuario());

            // Formatear fecha
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String fecha = sdf.format(new Date(contrasena.getFechaCreacion()));
            textoFecha.setText(fecha);

            // Mostrar URL si existe
            if (contrasena.getUrl() != null && !contrasena.getUrl().isEmpty()) {
                textoUrl.setText(contrasena.getUrl());
                textoUrl.setVisibility(View.VISIBLE);
            } else {
                textoUrl.setVisibility(View.GONE);
            }
        }
    }
}
