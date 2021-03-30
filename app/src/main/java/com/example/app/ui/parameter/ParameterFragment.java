package com.example.app.ui.parameter;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.app.R;

import static android.content.Context.SENSOR_SERVICE;

public class ParameterFragment extends Fragment {

    private String[] sensors={"加速度计","陀螺仪","磁力计"};
    private Sensor asensor = null;
    private Sensor usensor = null;
    private Sensor msensor = null;
    private SensorManager msensormanager;
    private TextView tv_para1;
    private TextView tv_para2;
    private TextView tv_para3;
    private View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_parameter, container, false);
        init();
        printSensorParameter(asensor,tv_para1,0);
        printSensorParameter(usensor,tv_para2,1);
        printSensorParameter(msensor,tv_para3,2);
        return root;
    }
    private void init(){
        msensormanager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        asensor = msensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        usensor  =msensormanager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        msensor = msensormanager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        tv_para1 = (TextView)root.findViewById(R.id.tv_para1);
        tv_para2 = (TextView)root.findViewById(R.id.tv_para2);
        tv_para3 = (TextView)root.findViewById(R.id.tv_para3);
        tv_para1.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv_para2.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv_para3.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    private void printSensorParameter(Sensor sensor,TextView tv,int type){

        String sname = sensor.getName();
        String vendor = sensor.getVendor();
        String version = Integer.toString(sensor.getVersion());
        String maxDelay = Integer.toString(sensor.getMaxDelay());
        String minDelay = Integer.toString(sensor.getMinDelay());
        String maximumRange =Float.toString(sensor.getMaximumRange());
        String reportMode = Integer.toString(sensor.getReportingMode());
        String resolution = Float.toString(sensor.getResolution());
        String isDynamic = Boolean.toString(sensor.isDynamicSensor());
        String isWake = Boolean.toString(sensor.isWakeUpSensor());
        Log.d("123",sname);
        String s = String.format(" %s\n\u3000\u3000名称\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000 %s ",sensors[type],sname);
        s += String.format("\n\u3000\u3000厂商\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000 %s ",vendor);
        s += String.format("\n\u3000\u3000版本\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000 %s ",version);
        s += String.format("\n\u3000\u3000最大延迟\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000 %sus ",maxDelay);
        s += String.format("\n\u3000\u3000最小延迟\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000 %sus ",minDelay);
        s += String.format("\n\u3000\u3000最大范围\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000 %s ",maximumRange);
        s += String.format("\n\u3000\u3000报告模式\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000 %s ",reportMode);
        s += String.format("\n\u3000\u3000分辨率\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000  %s ",resolution);
        s += String.format("\n\u3000\u3000是否为动态传感器\u3000\u3000\u3000\u3000 %s ",isDynamic);
        s += String.format("\n\u3000\u3000是否为唤醒传感器\u3000\u3000\u3000\u3000 %s",isWake);
        tv.setText(s);
    }

}

