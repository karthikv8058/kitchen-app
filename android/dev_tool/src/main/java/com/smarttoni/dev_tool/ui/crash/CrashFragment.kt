package com.smarttoni.devtools.ui.crash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.smarttoni.dev_tool.R

class CrashFragment : Fragment() {

    private lateinit var tv: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_crash, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view!!.findViewById<Button>(R.id.crash).setOnClickListener(View.OnClickListener {
            tv?.setText("crash")
        })
        view!!.findViewById<Button>(R.id.exitApp).setOnClickListener(View.OnClickListener {
           System.exit(0)
        })
    }
}