package com.app.ecoleta.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.app.ecoleta.R
import com.app.ecoleta.model.Estados
import com.app.ecoleta.service.RetrofitService
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class PontoDeColetaActivity1 : AppCompatActivity() {

    private var etNomeEntidade: TextInputEditText? = null
    private var etEndereco: TextInputEditText? = null
    private var etNumero: TextInputEditText? = null
    private var spnEstados: Spinner? = null
    private var spnCidades: Spinner? = null
    private var chipGroup: ChipGroup? = null
    private var listaDeEstados: List<Estados?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ponto_de_coleta)

        //actionBar
        supportActionBar!!.elevation = 0f
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //referenciação
        etNomeEntidade = findViewById(R.id.etNomeEntidade)
        etEndereco = findViewById(R.id.etEndereco)
        etNumero = findViewById(R.id.etNumero)
        spnEstados = findViewById(R.id.spnEstados)
        spnCidades = findViewById(R.id.spnCidades)
        chipGroup = findViewById(R.id.chipGroup)

        recuperarEstados()
    }

    private fun recuperarEstados() {
        val retrofit: RetrofitService = RetrofitService.retrofit.create(RetrofitService::class.java) //referenciar classes em kt(para uma classe java usa ".java")
        val serviceEstados = retrofit.recuperarEstados()

        serviceEstados?.enqueue(object : Callback<List<Estados?>?> {

            override fun onResponse(call: Call<List<Estados?>?>, response: Response<List<Estados?>?>) {
                if(response.isSuccessful) {
                    listaDeEstados = response.body()
                    configEstados()
                }else {
                    Log.i("infoColeta", "onFailure: " + response.message())
                }
            }

            override fun onFailure(call: Call<List<Estados?>?>, t: Throwable) {
                Log.i("infoColeta", "onFailure: " + t.message)
            }
        })
    }

    private fun configEstados() {
        val adapterEstados = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listaDeEstados)

    }
}