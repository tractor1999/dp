package com.example.app.ui.home;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.app.Acceleration;
import com.example.app.Angular;
import com.example.app.DbController;
import com.example.app.Magneticfield;
import com.example.app.R;
import com.freedom.lauzy.playpauseviewlib.PlayPauseView;

import java.util.ArrayList;

import static android.content.Context.SENSOR_SERVICE;

public class HomeFragment extends Fragment implements SensorEventListener {
    private DbController mDbController;
    private TextView tv_text;
    //加速度数据
    private double ax = 0;
    private double ay = 0;
    private double az = 0;
    //角速度数据
    private double ux = 0;
    private double uy = 0;
    private double uz = 0;
    //磁场强度数据
    private double mx = 0;
    private double my = 0;
    private double mz = 0;

    private Sensor asensor = null;
    private Sensor usensor = null;
    private Sensor msensor = null;

    private Spinner spn_ac;
    private Spinner spn_au;
    private Spinner spn_mg;
    private Spinner spn_file;
    private String choose_sample = null;
    private String choose_file = "txt";

    private int ac_sample =50;
    private int au_sample =50;
    private int mg_sample =50;

    private int MINDELAY_AC;
    private int MINDELAY_AU;
    private int MINDELAY_MG;
    private int [] samples ={1,2,5,10,20,25,50,75,100,200,250,500};
    private PlayPauseView playPauseView;
    private SensorManager msensormanager;
    private  View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        verifyStoragePermissions(getActivity());
        mDbController =DbController.getInstance(getActivity());
        init();
        Event();
        return root;
    }
    private void init(){
        playPauseView = root.findViewById(R.id.play_pause_view);
        msensormanager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        asensor = msensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        usensor  =msensormanager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        msensor = msensormanager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        MINDELAY_AC = asensor.getMinDelay();
        MINDELAY_AU = usensor.getMinDelay();
        MINDELAY_MG = msensor.getMinDelay();
        Log.d("tlj","AC"+MINDELAY_AC);
        Log.d("tlj","AU"+MINDELAY_AU);
        Log.d("tlj","MG"+MINDELAY_MG);
        spn_file =(Spinner)root.findViewById(R.id.spn_file1);
        spn_ac = (Spinner)root.findViewById(R.id.spn_ac);
        spn_au = (Spinner)root.findViewById(R.id.spn_au);
        spn_mg = (Spinner)root.findViewById(R.id.spn_mg);


        ArrayList<CharSequence>ac_array = new ArrayList<>();
        ArrayList<CharSequence>au_array = new ArrayList<>();
        ArrayList<CharSequence>mg_array = new ArrayList<>();
        ArrayList<CharSequence>file_array = new ArrayList<>();
        Log.d("tlj","Length"+samples.length);
        for(int i=0;i<samples.length;i++){
            Log.d("tlj","Length"+samples.length);
            Log.d("tlj","AC"+hzToMicroseconds(samples[i]));
            if(hzToMicroseconds(samples[i])>MINDELAY_AC){
                Log.d("tlj","AC"+hzToMicroseconds(samples[i]));
                ac_array.add(Integer.toString(samples[i])+"Hz");
            }
        }
        for(int i=0;i<samples.length;i++){
            if(hzToMicroseconds(samples[i])>MINDELAY_AU){
                au_array.add(Integer.toString(samples[i])+"Hz");
            }
        }
        for(int i=0;i<samples.length;i++){
            if(hzToMicroseconds(samples[i])>MINDELAY_MG){
                mg_array.add(Integer.toString(samples[i])+"Hz");
            }
        }
        file_array.add("txt");
        file_array.add("csv");
        Log.d("tlj","ac"+ac_array.size());
        Log.d("tlj","au"+Integer.toString(au_array.size()));
        Log.d("tlj","mg"+Integer.toString(mg_array.size()));
        ArrayAdapter<CharSequence>file_adapter=new ArrayAdapter<CharSequence>(getActivity(),android.R.layout.simple_spinner_dropdown_item,file_array);
        ArrayAdapter<CharSequence>ac_adapter=new ArrayAdapter<CharSequence>(getActivity(),android.R.layout.simple_spinner_dropdown_item,ac_array);
        ArrayAdapter<CharSequence>au_adapter=new ArrayAdapter<CharSequence>(getActivity(),android.R.layout.simple_spinner_dropdown_item,au_array);
        ArrayAdapter<CharSequence>mg_adapter=new ArrayAdapter<CharSequence>(getActivity(),android.R.layout.simple_spinner_dropdown_item,mg_array);
        spn_ac.setAdapter(ac_adapter);
        spn_au.setAdapter(au_adapter);
        spn_mg.setAdapter(mg_adapter);
        spn_file.setAdapter(file_adapter);
        spn_ac.setPrompt("请选择加速度计采样频率");
        spn_au.setPrompt("请选择陀螺仪采样频率");
        spn_mg.setPrompt("请选择磁力计采样频率");
        spn_ac.setSelection(Math.min(6,ac_array.size()-1),true);
        spn_au.setSelection(Math.min(6,au_array.size()-1),true);
        spn_mg.setSelection(Math.min(6,mg_array.size()-1),true);

    }

    private int hzToMicroseconds(int hz ){
        return (int)((1.0/hz)*Math.pow(10,6));
    }

    private  void Event(){

        playPauseView.setPlayPauseListener(new PlayPauseView.PlayPauseListener() {
            @Override
            public void play() {
                // do something
                msensormanager.registerListener(HomeFragment.this,asensor,hzToMicroseconds(ac_sample));
                msensormanager.registerListener(HomeFragment.this,usensor,hzToMicroseconds(au_sample));
                msensormanager.registerListener(HomeFragment.this,msensor,hzToMicroseconds(mg_sample));
                Toast.makeText(getActivity(), "开始存储", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void pause() {
                // do something
                if(choose_file.equals("txt")){
                    Log.d("253","111");
                    mDbController.exportTxt();
                }
                else if(choose_file.equals("csv")){
                    mDbController.exportCsv();
                }
                msensormanager.unregisterListener(HomeFragment.this,asensor);
                msensormanager.unregisterListener(HomeFragment.this,usensor);
                msensormanager.unregisterListener(HomeFragment.this,msensor);
                Toast.makeText(getActivity(), "保存路径为"+ Environment.getExternalStorageDirectory(), Toast.LENGTH_SHORT).show();
            }
        });
        spn_ac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
                ac_sample=samples[pos];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        spn_au.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
                au_sample=samples[pos];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        spn_mg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
                mg_sample=samples[pos];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        spn_file.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
                if(pos==0){
                    choose_file = "txt";
                }
                else{
                    choose_file = "csv";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        Sensor cur_sensor = event.sensor;
        if(cur_sensor==asensor){
            ax = event.values[0];
            ay = event.values[1];
            az = event.values[2];
            Long time = event.timestamp;
            mDbController.ac_insert(new Acceleration(null,ax,ay,az,time));
        }
        if(cur_sensor==usensor){
            ux = event.values[0];
            uy = event.values[1];
            uz = event.values[2];
            Long time = event.timestamp;
            mDbController.ang_insert(new Angular(null,ux,uy,uz,time));
        }
        if(cur_sensor==msensor){
            mx = event.values[0];
            my = event.values[1];
            mz = event.values[2];
            Long time = event.timestamp;
            mDbController.mag_insert(new Magneticfield(null,mx,my,mz,time));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int val){

    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };


    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                Log.d("tlj","没有写权限");
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }

        } catch (Exception e) {
            Log.d("tlj","异常");
            e.printStackTrace();
        }
    }
}