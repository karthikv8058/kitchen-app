package com.smarttoni.store

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
    }


    fun onClickRelease(v : View){
        var intent = Intent(this, MainActivity::class.java);
        intent.putExtra("MODE",1)
        startActivity(intent)
        finish()
    }

    fun onClickUAT(v : View){
        var intent = Intent(this, MainActivity::class.java);
        intent.putExtra("MODE",2)
        startActivity(intent)
        finish()
    }

    fun onClickDev(v : View){
        var intent = Intent(this, MainActivity::class.java);
        intent.putExtra("MODE",3)
        startActivity(intent)
        finish()
    }
}
