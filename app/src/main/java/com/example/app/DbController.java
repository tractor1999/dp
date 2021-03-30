package com.example.app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DbController {
    private DaoMaster.DevOpenHelper mHelper;

    private SQLiteDatabase db;

    private DaoMaster mDaoMaster;

    private DaoSession mDaoSession;

    private Context mContext;

    private String[] array = {"Acceleration","Angular","Magneticfield"};

    private AccelerationDao accelerationDao;
    private AngularDao angularDao;
    private MagneticfieldDao magneticfieldDao;

    private static DbController mDbController;

    public static DbController getInstance(Context context){
        if(mDbController == null){
            synchronized (DbController.class) {
                if (mDbController == null) {
                    mDbController = new DbController(context);
                }
            }
        }
        return mDbController;
    }
    /**
     * 初始化
     * @param context
     */
    private DbController(Context context){
        this.mContext = context;
        mHelper = new DaoMaster.DevOpenHelper(context,"tlj.db",null);
        mDaoMaster = new DaoMaster(getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        accelerationDao = mDaoSession.getAccelerationDao();
        angularDao =mDaoSession.getAngularDao();
        magneticfieldDao = mDaoSession.getMagneticfieldDao();
    }

    private SQLiteDatabase getWritableDatabase(){
        if(mHelper == null){
            mHelper = new DaoMaster.DevOpenHelper(mContext,"tlj.db",null);
        }
        SQLiteDatabase db  = mHelper.getWritableDatabase();
        return db;
    }

    private SQLiteDatabase getReadableDatabase(){
        if(mHelper == null){
            mHelper = new DaoMaster.DevOpenHelper(mContext,"tlj.db",null);
        }
        SQLiteDatabase db  = mHelper.getReadableDatabase();
        return db;
    }


    public void ac_insert(Acceleration acceleration){ accelerationDao.insert(acceleration); }

    public void ang_insert(Angular angular){
        angularDao.insert(angular);
    }

    public void mag_insert(Magneticfield magneticfield){
        magneticfieldDao.insert(magneticfield);
    }


    public void exportCsv(){
        for(int i=0;i<3;i++){
            exportCsv1(i);
        }
    }

    public void exportCsv1(int flag){
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        String str   =   formatter.format(curDate);
        String sensor = array[flag];
        str = String.format("%s %s.csv",sensor,str);
        File file = new File(exportDir, str);
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            db = getReadableDatabase();
            String sql = String.format("SELECT * FROM %s",sensor);
            Cursor curCSV = db.rawQuery(sql,null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                //Which column you want to exprort
                String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2), curCSV.getString(3),curCSV.getString(4)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }


    public void exportTxt(){
        for(int i=0;i<3;i++){
            exportTxt1(i);
        }
    }


    public void exportTxt1(int flag){
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        Log.d("tlj",exportDir.toString());
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        String sensor = array[flag];
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        String str   =   formatter.format(curDate);
        str = String.format("%s %s.txt",sensor,str);
        File file = new File(exportDir, str);
        try
        {
            file.createNewFile();
            db = getReadableDatabase();
            String sql = String.format("SELECT * FROM %s",sensor);
            Cursor curCSV = db.rawQuery(sql,null);
            curCSV.moveToFirst();
            Writer output = new BufferedWriter(new FileWriter(file));
            String txtstr = String.format("%s %s %s %s %s\n",curCSV.getString(0),curCSV.getString(1), curCSV.getString(2), curCSV.getString(3),curCSV.getString(4));
            output.write(txtstr);
            while(curCSV.moveToNext())
            {
                //Which column you want to exprort
                txtstr = String.format("%s %s %s %s %s\n",curCSV.getString(0),curCSV.getString(1), curCSV.getString(2), curCSV.getString(3),curCSV.getString(4));
                output.write(txtstr);
            }
            output.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }
}
