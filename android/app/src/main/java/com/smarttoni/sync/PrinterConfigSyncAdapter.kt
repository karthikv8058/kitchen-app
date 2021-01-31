package com.smarttoni.sync

import android.content.Context
import com.smarttoni.database.DaoAdapter
import com.smarttoni.database.GreenDaoAdapter
import com.smarttoni.entities.Printer
import com.smarttoni.pos.interceptor.TCPInterceptor
import com.smarttoni.http.HttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PrinterConfigSyncAdapter : AbstractSyncAdapter {
    override fun onSync(context: Context, daoAdapter: DaoAdapter, restaurantId: String, successListener: AbstractSyncAdapter.SyncSuccessListener, failListener: AbstractSyncAdapter.SyncFailListener) {
        val daoAdapter: GreenDaoAdapter = GreenDaoAdapter(context)
        HttpClient(context).httpClient.getPrinterConfig(restaurantId).enqueue(object : Callback<List<Printer>> {
            override fun onResponse(call: Call<List<Printer>>, response: Response<List<Printer>>) {
                Thread(Runnable {
                    if (response != null) {
                        val printerList = response.body()
                        if (printerList != null && printerList.isNotEmpty()) {
                            daoAdapter.deletePrinterData()

                            for (printer in printerList) {
                                if (printer.uuId == null) break
                                printer.format = printer.printerProtocol?.format
                                daoAdapter.savePrinter(printer)
                                startTcpConnectionForPrinter(context, printer.port!!.toInt(), printer.ipAddress)
                            }
                            val printers = daoAdapter.printerData
                            for (printer in printers) {
                                startTcpConnectionForPrinter(context, printer.port.toInt(), printer.ipAddress)
                            }
                            if (printers.isEmpty()) {
                                startTcpConnectionForPrinter(context, 0, null)
                            }
                        }
                    } else {
                        startTcpConnectionForPrinter(context, 0, null)
                    }
                    successListener.onSuccess()
                }).start()
            }

            override fun onFailure(call: Call<List<Printer>>, t: Throwable) {
                failListener.onFail()
            }
        })
    }

    private fun startTcpConnectionForPrinter(context: Context, port: Int, printerIp: String?) {

        val t = Thread(Runnable {
            val tcpinterceptor = TCPInterceptor()
            tcpinterceptor.start(context, printerIp, port)
        })
        t.start()

    }

}
