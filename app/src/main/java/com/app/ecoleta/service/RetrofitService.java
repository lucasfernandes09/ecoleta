package com.app.ecoleta.service;

import com.app.ecoleta.model.Cidades;
import com.app.ecoleta.model.Estados;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://servicodados.ibge.gov.br/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("api/v1/localidades/estados")
    Call<List<Estados>> recuperarEstados();

    @GET("api/v1/localidades/estados/{UF}/municipios")
    Call<List<Cidades>> recuperarCidades(@Path("UF") int id_uf);
}
