package com.app.ecoleta.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.ecoleta.R
import com.app.ecoleta.model.Cidades
import com.app.ecoleta.model.Estados
import com.app.ecoleta.model.PontoDeColeta
import com.app.ecoleta.service.RetrofitService
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PontoDeColetaActivity : AppCompatActivity() {
    private var etNomeEntidade: TextInputEditText? = null
    private var etEndereco: TextInputEditText? = null
    private var etNumero: TextInputEditText? = null
    private var listaDeEstados: List<Estados>? = null
    private var listaDeCidades: List<Cidades?>? = null
    private var chipGroup: ChipGroup? = null
    private var itensDeColeta: List<String> = ArrayList()
    private var spnEstados: Spinner? = null
    private var spnCidades: Spinner? = null
    private val pontoDeColeta = PontoDeColeta()
    private var nomeEntidade: String? = null
    private var endereco: String? = null
    private var numero: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ponto_de_coleta)

        //actionBar
        Objects.requireNonNull(supportActionBar).elevation = 0f
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
        val retrofit = RetrofitService.retrofit.create(RetrofitService::class.java)
        val serviceEstados = retrofit.recuperarEstados()

        serviceEstados.enqueue(object: Callback<List<Estados?>?> {

            override fun onResponse(call: Call<List<Estados>?>, response: Response<List<Estados>?>) {
                if (response.isSuccessful) {
                    listaDeEstados = response.body()
                    configEstados()
                } else {
                    Log.i("infoColeta", "erro na resposta: " + response.message())
                }
            }

            override fun onFailure(
                call: Call<List<Estados>?>,
                t: Throwable
            ) {
                Log.i("infoColeta", "onFailure: " + t.message)
            }
        })
    }

    fun configEstados() {
        val adapterEstados = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listaDeEstados)
        spnEstados!!.adapter = adapterEstados

        //pegar item selecionado no spinner
        spnEstados!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                val estados = listaDeEstados!![position]
                recuperarCidades(estados.id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun recuperarCidades(id: Int) {
        val retrofit = RetrofitService.retrofit.create(
            RetrofitService::class.java
        )
        val serviceCidades =
            retrofit.recuperarCidades(id)
        serviceCidades.enqueue(object : Callback<List<Cidades?>?> {
            override fun onResponse(
                call: Call<List<Cidades?>?>,
                response: Response<List<Cidades?>?>
            ) {
                if (response.isSuccessful) {
                    listaDeCidades = response.body()
                    configCidades()
                } else {
                    Log.i("infoColeta", "erro na resposta: " + response.message())
                }
            }

            override fun onFailure(
                call: Call<List<Cidades?>?>,
                t: Throwable
            ) {
                Log.i("infoColeta", "onFailure: " + t.message)
            }
        })
    }

    private fun configCidades() {
        val adapterCidades = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            listaDeCidades
        )
        spnCidades!!.adapter = adapterCidades
    }

    fun cadastrarPonto(view: View?) {
        val valido = validarCampos()
        if (valido) {
            //add nome e endereço
            pontoDeColeta.nome = nomeEntidade
            pontoDeColeta.endereco = endereco
            pontoDeColeta.numero = numero

            //add cidades e estados
            val estado = spnEstados!!.selectedItem as Estados
            pontoDeColeta.estado = estado.nome
            val cidade = spnCidades!!.selectedItem as Cidades
            pontoDeColeta.cidade = cidade.nome.toUpperCase()

            //add chips checados
            for (i in 0 until chipGroup!!.childCount) {
                val chip = chipGroup!!.getChildAt(i) as Chip
                if (chip.isChecked) {
                    itensDeColeta.add(chip.text.toString())
                }
            }
            pontoDeColeta.itensDeColeta = itensDeColeta

            //salvar no banco de dados
            pontoDeColeta.salvarPontoDeColeta()
            Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun validarCampos(): Boolean {
        nomeEntidade = etNomeEntidade?.text.toString()
        endereco = etEndereco!!.text.toString()
        numero = etNumero!!.text.toString()
        return if (nomeEntidade == "") {
            Toast.makeText(this, "Preencha o nome da entidade", Toast.LENGTH_SHORT).show()
            false
        } else if (endereco == "") {
            Toast.makeText(this, "Preencha o endereço", Toast.LENGTH_SHORT).show()
            false
        } else if (numero == "") {
            Toast.makeText(this, "Preencha o número", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}