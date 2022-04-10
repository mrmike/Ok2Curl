package com.moczul.ok2curl

sealed class CommandComponent {

    object Curl : CommandComponent()
    object Flags : CommandComponent()
    object Method : CommandComponent()
    object Header : CommandComponent()
    object Body : CommandComponent()
    object Url : CommandComponent()

    companion object {
        @JvmField
        val DEFAULT = listOf(Curl, Flags, Method, Header, Body, Url)
    }
}