package com.example.parabirimi.Model


// verileri çektiğimizde bu değişkenlerin değerlerini alacağız
data class ExchangeResponse(
    val base : String,
    val date : String,
    val rates: Map<String, Double>

)


