package com.app.ecoleta.service

import com.app.ecoleta.model.Cidades
import com.app.ecoleta.model.Estados
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitService {

    companion object {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicodados.ibge.gov.br/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @GET("api/v1/localidades/estados")
    fun recuperarEstados(): Call<List<Estados?>?>?

    @GET("api/v1/localidades/estados/{UF}/municipios")
    fun recuperarCidades(@Path("UF") id_uf: Int): Call<List<Cidades?>?>?

}