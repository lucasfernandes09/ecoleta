package com.app.ecoleta;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.ecoleta.model.Cidades;
import com.app.ecoleta.model.Estados;
import com.app.ecoleta.model.PontoDeColeta;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PontoDeColetaActivity extends AppCompatActivity {

    private TextInputEditText etNomeEntidade, etEndereco, etNumero;
    private List<Estados> listaDeEstados;
    private List<Cidades> listaDeCidades;
    private ChipGroup chipGroup;
    private List<String> itensDeColeta = new ArrayList<>();
    private Spinner spnEstados;
    private Spinner spnCidades;
    private PontoDeColeta pontoDeColeta = new PontoDeColeta();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ponto_de_coleta);

        //actionBar
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etNomeEntidade = findViewById(R.id.etNomeEntidade);
        etEndereco = findViewById(R.id.etEndereco);
        etNumero = findViewById(R.id.etNumero);
        spnEstados = findViewById(R.id.spnEstados);
        spnCidades = findViewById(R.id.spnCidades);
        chipGroup = findViewById(R.id.chipGroup);

        recuperarEstados();

    }

    private void recuperarEstados() {
        RetrofitService retrofit = RetrofitService.retrofit.create(RetrofitService.class);
        Call<List<Estados>> serviceEstados = retrofit.recuperarEstados();

        serviceEstados.enqueue(new Callback<List<Estados>>() {
            @Override
            public void onResponse(Call<List<Estados>> call, Response<List<Estados>> response) {
                if(response.isSuccessful()) {
                    listaDeEstados = response.body();
                    popularEstados();
                }else {
                    Log.i("infoColeta", "erro na resposta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Estados>> call, Throwable t) {

            }
        });
    }

    public void popularEstados() {
        ArrayAdapter<Estados> adapterEstados = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, listaDeEstados);
        spnEstados.setAdapter(adapterEstados);

        //item selecionado no spinner
        spnEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Estados estado = listaDeEstados.get(position);
                recuperarCidades(estado.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void recuperarCidades(int id) {
        RetrofitService retrofit = RetrofitService.retrofit.create(RetrofitService.class);
        Call<List<Cidades>> serviceCidades = retrofit.recuperarCidades(id);

        serviceCidades.enqueue(new Callback<List<Cidades>>() {
            @Override
            public void onResponse(Call<List<Cidades>> call, Response<List<Cidades>> response) {
                if(response.isSuccessful()) {
                    listaDeCidades = response.body();
                    popularCidades();
                }else {
                    Log.i("infoColeta", "erro na resposta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Cidades>> call, Throwable t) {
            }
        });
    }

    private void popularCidades() {
        ArrayAdapter<Cidades> adapterCidades = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, listaDeCidades);
        spnCidades.setAdapter(adapterCidades);
    }

    public void cadastrarPonto(View view) {
        Boolean valido = validarCampos();

        if(valido) {
            //add cidades e estados
            Estados estado = (Estados) spnEstados.getSelectedItem();
            pontoDeColeta.setEstado(estado.getNome());
            Cidades cidade = (Cidades) spnCidades.getSelectedItem();
            pontoDeColeta.setCidade(cidade.getNome().toUpperCase());

            //add chips checados
            for(int i=0; i<chipGroup.getChildCount(); i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                if(chip.isChecked()) {
                    itensDeColeta.add(chip.getText().toString());
                }
            }
            pontoDeColeta.setItensDeColeta(itensDeColeta);

            //salvar no banco de dados
            pontoDeColeta.salvarPontoDeColeta();

            Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();

        }


    }

    private Boolean validarCampos() {
        String nomeEntidade = etNomeEntidade.getText().toString();
        String endereco = etEndereco.getText().toString();
        String numero = etNumero.getText().toString();

        if(nomeEntidade.equals("")) {
            Toast.makeText(this, "Preencha o nome da entidade", Toast.LENGTH_SHORT).show();
            return false;
        }else if (endereco.equals("")) {
            Toast.makeText(this, "Preencha o endereço", Toast.LENGTH_SHORT).show();
            return false;
        }else if (numero.equals("")) {
            Toast.makeText(this, "Preencha o número", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            //add nome e endereço
            pontoDeColeta.setNome(nomeEntidade); pontoDeColeta.setEndereco(endereco); pontoDeColeta.setNumero(numero);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
