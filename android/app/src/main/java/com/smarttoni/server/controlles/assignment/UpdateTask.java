package com.smarttoni.server.controlles.assignment;

import android.content.Context;
import android.util.DisplayMetrics;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.tcp.TcpConnection;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.R;
import com.smarttoni.assignment.Queue;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.assignment.task.TaskManger;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.Printer;
import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.Station;
import com.smarttoni.entities.Task;
import com.smarttoni.entities.Work;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;
import com.smarttoni.utils.PrinterManager;
import com.smarttoni.utils.Strings;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.InetAddress;


public class UpdateTask extends HttpSecurityRequest {

    private Context context;

    public UpdateTask(Context context) {
        this.context = context;

    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        try {
            String queueId = jsonObject.getString("queueId");
            String status = jsonObject.getString("status");
            String time = jsonObject.getString("time");

            TaskManger workHelper = ((SmarttoniContext) ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT)).getTaskManger();


            Long workId = Long.valueOf(queueId);
            Work work = workHelper.getQueue().getTask(workId);
            workHelper.queueUpdateTask(getUser().getId(), workId , Integer.parseInt(status), Integer.parseInt(time), TaskManger.CHEF_CLOSED);

            Task task = ServiceLocator.getInstance().getDatabaseAdapter().getTaskById(work.getTaskId());

            if(work.getStatus() == Work.COMPLETED && task.getPrintLabel()){
                try {
                    PrinterManager.getInstance().printTask(getUser().getId(),work,"",task);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<Boolean>() {
            }.getType();
            response.send(gson.toJson(true, type));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
