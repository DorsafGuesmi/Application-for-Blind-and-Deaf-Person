package com.example.aveuglesourd


class Upload {
    var imgName: String? = null
    var imgUrl: String? = null


    constructor(imgName: String, imgUrl: String?) {
        var imgName = imgName
        if (imgName.trim { it <= ' ' } == "") {
            imgName = "No name"
        }
        this.imgName = imgName
        this.imgUrl = imgUrl
    }

}