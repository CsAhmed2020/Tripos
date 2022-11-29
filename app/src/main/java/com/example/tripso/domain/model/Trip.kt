package com.example.tripso.domain.model

data class Trip(
    var tripName:String ? = "",
    var tripFrom:String ?="",
    var tripTo: String? = "",
    var startDate:String ?="0000",
    var endDate:String ?="0000"
)
