package com.app.ecoleta.model

class Estados {

    var nome: String = ""
    var id = 0

    constructor(nome: String, id: Int) {
        this.nome = nome
        this.id = id
    }

    override fun toString(): String {
        return this.nome
    }
}