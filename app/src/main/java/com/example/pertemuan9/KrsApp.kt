package com.example.pertemuan9

import android.app.Application
import com.example.pertemuan9.dependeciesinjection.ContainerApp

class KrsApp:Application() {
    lateinit var containerApp: ContainerApp //fungsinya untuk menyimpan instance

    override fun onCreate() {
        super.onCreate()
        containerApp = ContainerApp(this) // membuat instance ContainerApp
        //instance adalah object yang dibuat dari class
    }
}