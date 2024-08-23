package com.example.todoapp.di

interface ResourceProvider {
    fun getString(resId: Int): String
}