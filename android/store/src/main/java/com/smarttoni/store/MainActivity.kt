package com.smarttoni.store

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.AsyncTask
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.smarttoni.store.databinding.ActivityMainBinding
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.net.URL
import android.content.Intent
import android.net.Uri
import android.os.Environment
import java.io.File
import android.os.Build
import androidx.core.content.FileProvider
import android.content.pm.PackageManager
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val MIME_TYPE = "application/vnd.android.package-archive"
private const val PROVIDER_PATH = ".provider"
private const val APP_INSTALL_PATH = "application/vnd.android.package-archive"

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var releaseNotes: ActivityMainBinding? = null

    private var currentVersionCode: Int = 0
    private var currentVersionName: String = "1.0"

    private var url: String? = ""
    private var installing: Boolean = false

    private var devCount: Int = 10

    private var toast: Toast? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.executePendingBindings()
        if(getSharedPreferences("store", Context.MODE_PRIVATE).getBoolean("DevOn",false)){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem):Boolean{
        if(item.itemId == android.R.id.home){
            startActivity(Intent(this,ListActivity::class.java))
            finish()
            return true
        }
        return super.onOptionsItemSelected(item);
//        switch (item.getItemId()) {
//        case android.R.id.home:
//            NavUtils.navigateUpFromSameTask(this);
//            return true;
//        default:
//            return super.onOptionsItemSelected(item);
 //       }
    }

    override fun onResume() {
        super.onResume()
        binding?.installing = installing
        if (!installing) {
            updateCurrentVersionCode()
            binding?.isUpdate = currentVersionCode > 0
            binding?.downloadAvailable = false
            binding?.currentVersion = if (currentVersionCode > 0) "Current version " + currentVersionName else ""


            var httpService : HttpService;
            var dev = intent.getIntExtra("MODE",1);
            if(dev == 3){
                httpService =HttpClient().devHttpClient
            }else  if(dev ==  2){
                httpService =HttpClient().uatHttpClient
            }else{
                httpService =HttpClient().liveHttpClient
            }
            httpService.checkUpdate().enqueue(object : Callback<StoreRequest> {
                override fun onResponse(call: Call<StoreRequest>, response: Response<StoreRequest>) {
                    binding?.releaseNotes = response.body()?.releaseNotes
                    if (response.body()?.latestVersionCode ?: 0 > currentVersionCode) {
                        binding?.currentVersion = (if (currentVersionCode > 0) "Current version " + currentVersionName else "") +
                                " Latest version " +
                                response.body()?.latestVersion
                        url = response.body()?.url
                        binding?.downloadAvailable = true
                    } else {
                        binding?.downloadAvailable = false
                    }
                }

                override fun onFailure(call: Call<StoreRequest>, t: Throwable) {
                    var a =10
                }
            })
        }

    }

    fun install(v: View) {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1)
        } else {
            if (binding?.downloadAvailable ?: false) {
                if (url != null && !url.equals("")) {
                    DownloadFileFromURL().execute(url);
                }
            }
        }
    }


    private fun updateCurrentVersionCode() {
        try {
            var pkg = "com.smarttoni"
            var dev = intent.getIntExtra("MODE",1);
            if(dev == 3){
                pkg= "com.smarttoni.dev"
            }else  if(dev ==  2) {
                pkg = "com.smarttoni.uat"
            }
            val pInfo = getPackageManager().getPackageInfo(pkg, 0)
            currentVersionCode = pInfo.versionCode
            currentVersionName = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            currentVersionCode = 0
            currentVersionName = "0.0.0"
        }
    }

    internal inner class DownloadFileFromURL : AsyncTask<String, Int, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            binding?.downloadAvailable = false;
            binding?.installing = true
            installing = true
        }

        override fun doInBackground(vararg f_url: String): String? {
            val storage = Environment.getExternalStorageDirectory().toString() + "/app.apk"
            try {
                val url = URL(f_url[0])
                val conection = url.openConnection()
                conection.connect()
                val lenghtOfFile = conection.getContentLength()
                val input = BufferedInputStream(url.openStream(), 8192)
                val output = FileOutputStream(storage)
                val data = ByteArray(1024)
                var total: Long = 0
                var count = input.read(data)
                while (count != -1) {
                    total += count.toLong()
                    val pro = (total * 100 / lenghtOfFile);
                    publishProgress(pro.toInt())
                    output.write(data, 0, count)
                    count = input.read(data);
                }
                output.flush()
                output.close()
                input.close()
            } catch (e: Exception) {
                //Log.e("Error: ", e.message)
            }
            return storage
        }

        override fun onProgressUpdate(vararg pro: Int?) {
            binding?.progressVal = pro[0]
            binding?.invalidateAll()
        }

        override fun onPostExecute(file_url: String) {
            installing = false
            binding?.installing = false
            binding?.downloadAvailable = false
            val storage = Environment.getExternalStorageDirectory().toString() + "/app.apk"
            val file = File(storage)
            val uri: Uri
            if (Build.VERSION.SDK_INT < 24) {
                uri = Uri.fromFile(file)
            } else {
                uri = Uri.parse(file.path)
            }
            showInstallOption(storage, uri)
        }
    }

    private fun showInstallOption(
            destination: String,
            uri: Uri
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val contentUri = FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + PROVIDER_PATH,
                    File(destination)
            )
            val install = Intent(Intent.ACTION_VIEW)
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
            install.data = contentUri
            startActivity(install)
        } else {
            val install = Intent(Intent.ACTION_VIEW)
            install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            install.setDataAndType(
                    uri,
                    APP_INSTALL_PATH
            )
            startActivity(install)
        }
    }

    public fun enableDeveloperMode(view:View){
        devCount--;
        if(devCount == 2){
            toast = Toast.makeText(this,"You are 2 steps away from being a developer",Toast.LENGTH_SHORT)
            toast?.show()
        }else if(devCount == 1){
            toast?.cancel()
            toast = Toast.makeText(this,"You are 1 steps away from being a developer",Toast.LENGTH_SHORT)
            toast?.show()
        }else if(devCount == 0){
            toast?.cancel()
            toast =  Toast.makeText(this,"You are developer now",Toast.LENGTH_SHORT)
            toast?.show()
            getSharedPreferences("store", Context.MODE_PRIVATE).edit().putBoolean("DevOn",true).apply();
//            startActivity(Intent(this,ListActivity::class.java))
//            finish()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
}
