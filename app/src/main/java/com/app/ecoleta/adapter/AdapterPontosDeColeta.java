package com.app.ecoleta.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ecoleta.R;
import com.app.ecoleta.model.PontoDeColeta;

import java.util.List;

public class AdapterPontosDeColeta extends RecyclerView.Adapter<AdapterPontosDeColeta.MyViewHolder>{

    private List<PontoDeColeta> listaDePontos;

    public AdapterPontosDeColeta(List<PontoDeColeta> listaDePontos) {
        this.listaDePontos = listaDePontos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_pontos, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PontoDeColeta ponto = listaDePontos.get(position);

        holder.tvNome.setText(ponto.getNome());
        holder.tvItens.setText(ponto.getItensDeColeta().toString().replace("[", "").replace("]", ""));
        String endereco = ponto.getCidade() + ", " +  ponto.getEstado().toUpperCase() + "\n" +
                ponto.getEndereco() + ", " + ponto.getNumero();
        holder.tvEndereco.setText(endereco);

    }

    @Override
    public int getItemCount() {
        return listaDePontos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvItens, tvEndereco;
        ImageView ivImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNomeEntidade);
            tvItens = itemView.findViewById(R.id.tvItens);
            tvEndereco = itemView.findViewById(R.id.tvEndereço);
            ivImg = itemView.findViewById(R.id.ivImg);
        }
    }
}
