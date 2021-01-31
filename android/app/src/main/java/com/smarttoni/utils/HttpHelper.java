package com.smarttoni.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.webkit.URLUtil;

import com.koushikdutta.async.http.NameValuePair;
import com.koushikdutta.async.http.body.JSONObjectBody;
import com.koushikdutta.async.http.body.MultipartFormDataBody;
import com.koushikdutta.async.http.body.StringBody;
import com.koushikdutta.async.http.body.UrlEncodedFormBody;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;

import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpHelper {


    public static String mysqlDateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    public static JSONObject postDataToJson(AsyncHttpServerRequest request) {

        JSONObject json = new JSONObject();
        try {

            if (request.getBody() instanceof UrlEncodedFormBody) {
                UrlEncodedFormBody body = (UrlEncodedFormBody) request.getBody();
                for (NameValuePair pair : body.get()) {
                    json.put(pair.getName(), pair.getValue());
                }
            } else if (request.getBody() instanceof JSONObjectBody) {
                json = ((JSONObjectBody) request.getBody()).get();
            } else if (request.getBody() instanceof StringBody) {
                json.put("body", ((StringBody) request.getBody()).get());
            } else if (request.getBody() instanceof MultipartFormDataBody) {
                MultipartFormDataBody body = (MultipartFormDataBody) request.getBody();
                for (NameValuePair pair : body.get()) {
                    json.put(pair.getName(), pair.getValue());
                }
            }

        } catch (Exception e) {
        }
        return json;
    }


    public static long convertMysqlDatetimeToTimestamp(String datetime) {
        datetime = datetime.replace("T", " ");

        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat(HttpHelper.mysqlDateTimeFormat);
        Date lFromDate = null;
        try {
            lFromDate = datetimeFormatter1.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lFromDate.getTime();
    }

    public static Date convertMysqlDateToDate(String datetime) {
        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat(HttpHelper.mysqlDateTimeFormat);
        Date lFromDate = null;
        try {
            lFromDate = datetimeFormatter1.parse(datetime);
        } catch (ParseException e) {
            lFromDate = new Date();
        }
        return lFromDate;
    }

    public static String convertMillisecondsToMysqlDateTimeString(long milliseconds) {
        Date date = new Date(milliseconds);
        DateFormat df = new SimpleDateFormat(HttpHelper.mysqlDateTimeFormat);
        return df.format(date);
    }

    public static String downloadFile(Context context, String url, String name, String description) {
        if (url != null && !url.isEmpty()) {
//            File direct = new File(Environment.getExternalStorageDirectory()
//                    + "/smartKitchenServer");
//
//            if (!direct.exists()) {
//                direct.mkdirs();
//            }
            DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            if (!url.equals("")) {
                //        check whether the file exists
                File newFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + "/" + URLUtil.guessFileName(url, null, null));
                if (!newFile.exists()) {
                    Uri downloadUri = Uri.parse(url);

                    DownloadManager.Request request = new DownloadManager.Request(
                            downloadUri);

                    String fileName = URLUtil.guessFileName(url, null, null);
                    request.setAllowedNetworkTypes(
                            DownloadManager.Request.NETWORK_WIFI
                                    | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false).setTitle(name)
                            .setDescription(description)
                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, URLUtil.guessFileName(url, null, null));

                    mgr.enqueue(request);
                }

            }
            return HttpHelper.getFilenameFromUrl(url);


        } else {
            return url;
        }


    }

    public static String getFilenameFromUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

}
