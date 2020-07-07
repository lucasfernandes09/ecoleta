package com.app.ecoleta.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.app.ecoleta.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //action bar
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

    }

    public void  pesquisarCidade(View view) {
        //dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View viewDialog = inflater.inflate(R.layout.layout_dialog, null, false);
        dialog.setView(viewDialog);
        final AlertDialog show = dialog.show();

        //btn pesquisar
        Button btnPesquisar = viewDialog.findViewById(R.id.btnPesquisar);
        btnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validar campo cidade
                TextInputEditText etCidade = viewDialog.findViewById(R.id.etCidade);
                String cidade = etCidade.getText().toString();
                if(cidade.equals("")) {
                    Toast.makeText(MainActivity.this, "Preencha a cidade", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MainActivity.this, PesquisaActivity.class);
                    intent.putExtra("cidade", cidade);
                    startActivity(intent);
                    show.dismiss();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.btn_cadastre) {
            Intent intent = new Intent(this, PontoDeColetaActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
