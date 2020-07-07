package com.app.ecoleta.model

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PontoDeColeta {

    var nome: String? = null       //? significa que pode ser null. Se inicializar é not null por padrão
    var endereco: String? = null
    var numero: String? = null
    var estado: String? = null
    var cidade: String? = null
    var itensDeColeta: List<String>? = null

    fun salvarPontoDeColeta() {
        val firebaseDb: DatabaseReference = FirebaseDatabase.getInstance().reference
        firebaseDb.child("PontosDeColeta").child(nome!!).setValue(this)  //!! converte qualquer valor para not null e lança uma NPE ser for null
    }
}