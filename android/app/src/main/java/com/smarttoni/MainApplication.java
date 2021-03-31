package com.smarttoni;

import android.app.Application;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.Toast;

import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.soloader.SoLoader;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.database.DbOpenHelper;
import com.smarttoni.entities.DaoMaster;
import com.smarttoni.entities.DaoSession;
import com.smarttoni.react.modules.SmartToniRNPackage;

import org.greenrobot.greendao.database.Database;

import java.util.List;

import io.cobrowse.CobrowseIO;

public class MainApplication extends Application implements ReactApplication {


    public static SmartTONiService serverService;
    private DaoSession daoSession;
    private Database db;

    public DaoSession getDaoSession() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DbOpenHelper.Companion.getDB_NAME());
        if (db == null)
            db = helper.getWritableDb();
        if (daoSession == null)
            daoSession = new DaoMaster(db).newSession();
        return daoSession;
    }

    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            List<ReactPackage> packages = new PackageList(this).getPackages();
            packages.add(new SmartToniRNPackage());
            return packages;
        }

        @Override
        protected String getJSMainModuleName() {
            return "index";
        }
    };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, /* native exopackage */ false);

        ServiceLocator.getInstance().initBasic(this);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName(this, MainActivity.class));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        WallpaperManager wm = WallpaperManager.getInstance(getApplicationContext());
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wallpaperp);
                //wm.setBitmap
                wm.setResource(R.drawable.wallpaperp);
                //Lock Screen Wallpaper
                wm.setResource(R.drawable.wallpaperp, WallpaperManager.FLAG_LOCK);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        startActivity(intent);

        startCobrowseIO();

    }

    private void startCobrowseIO(){
        CobrowseIO.instance().license("8bTZ9kvD5XLa2w");
        CobrowseIO.instance().start(this);
    }

}
