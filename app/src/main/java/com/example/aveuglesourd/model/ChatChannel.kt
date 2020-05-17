package com.example.aveuglesourd.model

data class ChatChannel(val userIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}