package com.app.ecoleta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.app.ecoleta.adapter.AdapterPontosDeColeta;
import com.app.ecoleta.model.PontoDeColeta;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PesquisaActivity extends AppCompatActivity {
    private List<PontoDeColeta> listaDePontos = new ArrayList<>();
    private MaterialSearchView svPesquisa;
    private RecyclerView rvPontos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);
        //action bar
        Toolbar toolbarPesquisa = findViewById(R.id.toolbar);
        setSupportActionBar(toolbarPesquisa);

        //referências
        svPesquisa = findViewById(R.id.svPesquisa);

        //recuperar cidade e buscar no db
        String cidade = getIntent().getStringExtra("cidade");
        pesquisarNoDb(cidade);

        //searchView
        svPesquisa.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pesquisarNoDb(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }

    public void pesquisarNoDb(String cidade) {
        DatabaseReference firebaseDb = FirebaseDatabase.getInstance().getReference().child("PontosDeColeta");
        //Query pesquisa = firebaseDb.orderByChild("cidade").equalTo(cidade.toUpperCase());

        firebaseDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    PontoDeColeta pontoDeColeta = ds.getValue(PontoDeColeta.class);
                    listaDePontos.add(pontoDeColeta);
                }
                exibirPontos();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void exibirPontos() {
        rvPontos = findViewById(R.id.rvPontos);
        AdapterPontosDeColeta adapter = new AdapterPontosDeColeta(listaDePontos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        rvPontos.setLayoutManager(layoutManager);
        rvPontos.setHasFixedSize(true);
        rvPontos.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pesquisa, menu);

        MenuItem item = menu.findItem(R.id.menu_pesquisa);
        svPesquisa.setMenuItem(item);

        return true;
    }


}
