package com.app.ecoleta.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PontoDeColeta {

    private String nome;
    private String endereco;
    private String numero;
    private String estado;
    private String cidade;
    private List<String> itensDeColeta;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public List<String> getItensDeColeta() {
        return itensDeColeta;
    }

    public void setItensDeColeta(List<String> itensDeColeta) {
        this.itensDeColeta = itensDeColeta;
    }

    public void salvarPontoDeColeta () {
        DatabaseReference firebaseDb = FirebaseDatabase.getInstance().getReference();
        firebaseDb.child("PontosDeColeta").child(this.getNome()).setValue(this);

    }
}
