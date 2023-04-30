package br.ufpe.cin.residencia.banco;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.residencia.banco.conta.Conta;
import br.ufpe.cin.residencia.banco.conta.ContaRepository;

//Ver anotações TODO no código
public class BancoViewModel extends AndroidViewModel {
    private ContaRepository repository;
    public LiveData<List<Conta>> contas;
    private MutableLiveData<Conta> _contaAtual = new MutableLiveData<>();
    public LiveData<Conta> contaAtual = _contaAtual;

    private MutableLiveData<List<Conta>> _contasAtualizadas = new MutableLiveData<>();
    public LiveData<List<Conta>> contasAtualizadas = _contasAtualizadas;

    public BancoViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ContaRepository(BancoDB.getDB(application).contaDAO());
        this.contas = this.repository.getContas();
    }

    void transferir(String numeroContaOrigem, String numeroContaDestino, double valor) {
        //TODO implementar transferência entre contas (lembrar de salvar no BD os objetos Conta modificados)
        new Thread(()->{
            List<Conta> contas = this.repository.buscarPeloNumero(numeroContaOrigem);
            Conta conta = contas.get(0);
            conta.debitar(valor);
            this.repository.atualizar(conta);

            List<Conta> contasDestino = this.repository.buscarPeloNumero(numeroContaDestino);
            Conta contaDestino = contasDestino.get(0);
            contaDestino.creditar(valor);
            this.repository.atualizar(contaDestino);
        }).start();


    }

    void creditar(String numeroConta, double valor) {
        //TODO implementar creditar em conta (lembrar de salvar no BD o objeto Conta modificado)
       new Thread(()->{
            List<Conta> contas = this.repository.buscarPeloNumero(numeroConta);
            Conta conta = contas.get(0);
            conta.creditar(valor);
            this.repository.atualizar(conta);
       }).start();
    }

    void debitar(String numeroConta, double valor) {
        //TODO implementar debitar em conta (lembrar de salvar no BD o objeto Conta modificado)
        new Thread(()->{
            List<Conta> contas = this.repository.buscarPeloNumero(numeroConta);
            Conta conta = contas.get(0);
            conta.debitar(valor);
            this.repository.atualizar(conta);
        }).start();
    }

    void buscarPeloNome(String nomeCliente) {

        new Thread(()->{
            List<Conta> listaContas = this.repository.buscarPeloNome(nomeCliente);
            _contasAtualizadas.postValue(listaContas);
        }).start();
        //TODO implementar busca pelo nome do Cliente
    }

    void buscarPeloCPF(String cpfCliente) {

        new Thread(()->{
            List<Conta> listaContas = this.repository.buscarPeloCPF(cpfCliente);
            _contasAtualizadas.postValue(listaContas);
        }).start();
        //TODO implementar busca pelo CPF do Cliente
    }

    void buscarPeloNumero(String numeroConta) {
        new Thread(()->{
            List<Conta> listaContas = this.repository.buscarPeloNumero(numeroConta);
            _contasAtualizadas.postValue(listaContas);
        }).start();
        //TODO implementar busca pelo número da Conta
    }

    double calcularSaldoBanco() {
        double saldoTotal = 0;

        List<Conta> listasContas = contas.getValue();
        if(listasContas != null) {
            for (Conta c :listasContas) {
                saldoTotal += c.saldo;
            }

        }
        return saldoTotal;
    }
}
