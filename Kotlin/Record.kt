package io.github.wilhelmus098.client_app

data class Record(
    var recordid: Int,
    var user: String,
    var slot: Int,
    var parkinglotid: Int,
    var datein: String,
    var dateout: String,
    //var desc: String,
    var cost: Int
    )