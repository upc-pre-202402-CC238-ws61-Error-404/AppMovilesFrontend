package com.example.chaquitaclla_appmovil_android.crops_details.beans

class Crop(
    var id: Int,
    var name: String,
    var imageUrl: String,
    var diseases: List<Int>,
    var pests: List<Int>,
    var cares: List<Int>
) {

}