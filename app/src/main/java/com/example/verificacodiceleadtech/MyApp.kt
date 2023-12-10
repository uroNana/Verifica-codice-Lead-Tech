package com.example.verificacodiceleadtech

import android.app.Application
import androidx.room.Room
import com.example.verificacodiceleadtech.repository.CodeDatabase

class MyApp : Application() {

    lateinit var database: CodeDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(applicationContext, CodeDatabase::class.java, "app-database").build()
    }
}
