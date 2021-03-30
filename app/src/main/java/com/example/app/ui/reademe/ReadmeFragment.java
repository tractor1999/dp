package com.example.app.ui.reademe;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.app.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import club.andnext.markdown.MarkdownWebView;

import static android.content.Context.SENSOR_SERVICE;

;

public class ReadmeFragment extends Fragment  {



    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_readme, container, false);
        MarkdownWebView markdownWebView=root.findViewById(R.id.markdown_view);
        markdownWebView.setText(
                "# SensorDataBase\n" +
                        "\n" +
                        "## 功能概要\n" +
                        "\n" +
                        "\u200B\t\u200B\tSensorDataBase是一款Android应用，该应用可以将采集的传感器数据以用户可选的格式存储在手机目录中，并以三维折线图的方式实现传感器数据的可视化，此外，还为用户提供当前设备传感器概况。\n" +
                        "\n" +
                        "## 用法\n" +
                        "\n" +
                        "SensorDataBase分三个模块：\n" +
                        "\n" +
                        "- Home——以用户指定采样频率存储传感器数据\n" +
                        "- Chart——传感器数据可视化\n" +
                        "- Parameter——为用户提供当前设备传感器概况\n" +
                        "\n" +
                        "\u200B\t\u200B\t在Home模块中用户可以分别选择加速度计、陀螺仪、磁力计的采样频率，显示的采样频率均符合当前设备可行采样间隔（具体最大最小采样间隔可在Parameter中查看），用户选择文件类型txt或csv，点击按钮进行数据采集，再次点击停止采集并存储（注意：存储的时间戳单位为纳秒，传感器数据单位可以在Chart中查看）。\n" +
                        "\n" +
                        "\u200B\t\u200B\t在Chart中可以选择加速度计、陀螺仪或磁力计，之后勾选xyz轴复选框查看多维传感器数据可视化。\n" +
                        "\n" +
                        "\u200B\t\u200B\t在Parameter模块供用户查看当前手机传感器特性，如名称、厂商、最小采样间隔、最大采样间隔等。\n" +
                        "\n" +
                        "## 觅航团队简介\n" +
                        "\n" +
                        "\u200B\t\u200B\tSensorDataBase由西安电子科技大学觅航团队开发。觅航团队集导学、科研、竞赛、毕设四位于一体，探索新的教学研模式，主要依托智能手机开发集传感器数据采集、数理和各种导航与定位应用于一体的终端软件。团队核心文化是：以“普世”的定位技术为每个人定位，以“普适”的导航技术为天下人导航，以“朴实”的无差别体验为所有人所有场景不迷航！\n" +
                        "\n" +
                        "- 软件核心开发者：田礼嘉(tljxdlinux@163.com)\n" +
                        "\n" +
                        "- 软件合作开发者：倪启涵、姚路、秦英、杨奕\n" +
                        "\n" +
                        "- 指导老师：刘公绪(liugongxu@xidian.edu.cn)");
        return root;
    }


}
