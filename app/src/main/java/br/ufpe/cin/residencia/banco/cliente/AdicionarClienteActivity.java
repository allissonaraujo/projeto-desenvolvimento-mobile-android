package br.ufpe.cin.residencia.banco.cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import br.ufpe.cin.residencia.banco.R;

public class AdicionarClienteActivity extends AppCompatActivity {

    ClienteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_cliente);
    }
}