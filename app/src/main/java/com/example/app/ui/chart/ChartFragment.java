package com.example.app.ui.chart;

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

;import static android.content.Context.SENSOR_SERVICE;

public class ChartFragment extends Fragment implements View.OnClickListener, SensorEventListener {



    private Spinner spinner;
    private View root;
    // 折线编号
    public static final int LINE_NUMBER_1 = 0;
    public static final int LINE_NUMBER_2 = 1;
    public static final int LINE_NUMBER_3 = 2;

    public static final int ACCELERATION = 4;
    public static final int ANGULAR = 5;
    public static final int MAGNETICFIELD = 6;
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
    private SensorManager msensormanager;
    private long origin_time ;
    private int  FLAG  = ACCELERATION;

    /**
     * 功能：启动方式
     */
    /*public static void startActivity(Context context) {
        context.startActivity(new Intent(context, LineChartDemo.class));
    }*/

    /*private DemoHandler mDemoHandler; // 自定义Handler*/
    private Random mRandom = new Random(); // 随机产生点
    private DecimalFormat mDecimalFormat = new DecimalFormat("#.00");   // 格式化浮点数位两位小数


    Button mBtnStart;   // 开始添加点
    Button mBtnPause;   // 暂停添加点
    CheckBox mCheckBox1;
    CheckBox mCheckBox2;
    CheckBox mCheckBox3;
    List<CheckBox> mCheckBoxList = new ArrayList<>();

    LineChart mLineChart; // 折线表，存线集合
    LineData mLineData; // 线集合，所有折现以数组的形式存到此集合中
    XAxis mXAxis; //X轴
    YAxis mLeftYAxis; //左侧Y轴
    YAxis mRightYAxis; //右侧Y轴
    Legend mLegend; //图例
    LimitLine mLimitline; //限制线

    //  Y值数据链表
    List<Float> mList1 = new ArrayList<>();
    List<Float> mList2 = new ArrayList<>();
    List<Float> mList3 = new ArrayList<>();

    // Chart需要的点数据链表
    List<Entry> mEntries1 = new ArrayList<>();
    List<Entry> mEntries2 = new ArrayList<>();
    List<Entry> mEntries3 = new ArrayList<>();

    // LineDataSet:点集合,即一条线
    LineDataSet mLineDataSet1 = new LineDataSet(mEntries1, "x轴分量");
    LineDataSet mLineDataSet2 = new LineDataSet(mEntries2, "y轴分量");
    LineDataSet mLineDataSet3 = new LineDataSet(mEntries3, "z轴分量");


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*chartViewModel =
                ViewModelProviders.of(this).get(ChartViewModel.class);*/
        root = inflater.inflate(R.layout.fragment_chart, container, false);
        /*final TextView textView = root.findViewById(R.id.text_gallery);
        chartViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        initSensor();
        initView();
        initLineChart(0);
        Toast.makeText(getActivity(),"12300",Toast.LENGTH_SHORT);
        return root;
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        Sensor cur_sensor = event.sensor;
        if(cur_sensor==asensor&&FLAG==ACCELERATION){
            ax = event.values[0];
            ay = event.values[1];
            az = event.values[2];
            Long time = event.timestamp;


            addLine1Data((float)ax);
            addLine2Data((float)ay);
            addLine3Data((float)az);
        }
        if(cur_sensor==usensor&&FLAG==ANGULAR){
            ux = event.values[0];
            uy = event.values[1];
            uz = event.values[2];
            Long time = event.timestamp;
            time = time-origin_time;
            Log.d("tlj",String.format("ux=%f,uy=%f,uz=%f",ux,uy,uz));
            addLine1Data((float)ux);
            addLine2Data((float)uy);
            addLine3Data((float)uz);
        }
        if(cur_sensor==msensor&&FLAG==MAGNETICFIELD){
            mx = event.values[0];
            my = event.values[1];
            mz = event.values[2];
            Long time = event.timestamp;
            time = time-origin_time;
            Log.d("tlj",String.format("mx=%f,my=%f,mz=%f",mx,my,mz));
            addLine1Data((float)mx);
            addLine2Data((float)my);
            addLine3Data((float)mz);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int val){

    }
    /**
     * 功能：产生随机数（小数点两位）
     */
    public Float getRandom(Float seed) {
        return Float.valueOf(mDecimalFormat.format(mRandom.nextFloat() * seed));
    }

    /**
     * 功能：初始化基本控件，button，checkbox
     */
    public void initView() {
        mCheckBox1 = root.findViewById(R.id.checkBox);
        mCheckBox2 = root.findViewById(R.id.checkBox2);
        mCheckBox3 = root.findViewById(R.id.checkBox3);
        mCheckBoxList.add(mCheckBox1);
        mCheckBoxList.add(mCheckBox2);
        mCheckBoxList.add(mCheckBox3);
        spinner = root.findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
                mLineChart.clearAllViewportJobs();
                mLineChart.removeAllViewsInLayout();
                mLineChart.removeAllViews();
                switch (pos){
                    case 0:
                        initLineChart(0);
                        FLAG = ACCELERATION;
                        break;
                    case 1:
                        initLineChart(1);
                        FLAG = ANGULAR;
                        break;
                    case 2:
                        initLineChart(2);
                        FLAG = MAGNETICFIELD;
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        // 设置MarkerView
        /*setMarkerView(mLineChart);*/
        mCheckBox1.setOnClickListener(this);
        mCheckBox2.setOnClickListener(this);
        mCheckBox3.setOnClickListener(this);

    }

    private void initSensor(){
        msensormanager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        asensor = msensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        usensor  =msensormanager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        msensor = msensormanager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        msensormanager.registerListener(this,asensor,SensorManager.SENSOR_DELAY_NORMAL);
        msensormanager.registerListener(this,usensor,SensorManager.SENSOR_DELAY_NORMAL);
        msensormanager.registerListener(this,msensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * 功能：初始化LineChart
     */
    public void initLineChart(int id) {
        mLineChart = root.findViewById(R.id.lineChart);
        mXAxis = mLineChart.getXAxis(); // 得到x轴
        mLeftYAxis = mLineChart.getAxisLeft(); // 得到侧Y轴
        mRightYAxis = mLineChart.getAxisRight(); // 得到右侧Y轴
        mLegend = mLineChart.getLegend(); // 得到图例
        mLineData = new LineData();
        mLineDataSet1.setDrawValues(false);
        mLineDataSet2.setDrawValues(false);
        mLineDataSet3.setDrawValues(false);
        mLineChart.setData(mLineData);
        // 设置图标基本属性
        setChartBasicAttr(mLineChart);

        // 设置XY轴
        switch (id) {
            case 0:
                setXYAxis(mLineChart, mXAxis, mLeftYAxis, mRightYAxis);
                break;
            case 1:
                setXYAxis1(mLineChart, mXAxis, mLeftYAxis, mRightYAxis);
                break;
            case 2:
                setXYAxis2(mLineChart, mXAxis, mLeftYAxis, mRightYAxis);
                break;
        }

        // 添加线条
        initLine();
        // 设置图例
        createLegend(mLegend);
        Description description = new Description();
        description.setEnabled(false);
        mLineChart.setDescription(description);
    }


    /**
     * 功能：设置图标的基本属性
     */
    void setChartBasicAttr(LineChart lineChart) {
        /***图表设置***/
        lineChart.setDrawGridBackground(false); //是否展示网格线
        lineChart.setDrawBorders(true); //是否显示边界
        lineChart.setDragEnabled(true); //是否可以拖动
        lineChart.setScaleEnabled(true); // 是否可以缩放
        lineChart.setTouchEnabled(true); //是否有触摸事件
        //设置XY轴动画效果
        //lineChart.animateY(2500);
        lineChart.animateX(1500);
    }

    /**
     * 功能：设置XY轴
     */
    void setXYAxis(LineChart lineChart, XAxis xAxis, YAxis leftYAxis, YAxis rightYAxis) {
        /***XY轴的设置***/
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //X轴设置显示位置在底部
        xAxis.setAxisMinimum(0f); // 设置X轴的最小值
        xAxis.setAxisMaximum(5000); // 设置X轴的最大值
        xAxis.setLabelCount(10, false); // 设置X轴的刻度数量，第二个参数表示是否平均分配
        xAxis.setGranularity(0.1f); // 设置X轴坐标之间的最小间隔
        lineChart.setVisibleXRangeMaximum(10);// 当前统计图表中最多在x轴坐标线上显示的总量
        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinimum(-20f);
        rightYAxis.setAxisMinimum(-20f);
        leftYAxis.setAxisMaximum(28f);
        rightYAxis.setAxisMaximum(28f);
        leftYAxis.setGranularity(1f);
        rightYAxis.setGranularity(1f);
        /*leftYAxis.setLabelCount(20);*/

        lineChart.setVisibleYRangeMaximum(50, YAxis.AxisDependency.LEFT);// 当前统计图表中最多在Y轴坐标线上显示的总量
        lineChart.setVisibleYRangeMaximum(50, YAxis.AxisDependency.RIGHT);// 当前统计图表中最多在Y轴坐标线上显示的总量
        leftYAxis.setEnabled(true);

//        leftYAxis.setCenterAxisLabels(true);// 将轴标记居中
//        leftYAxis.setDrawZeroLine(true); // 原点处绘制 一条线
//        leftYAxis.setZeroLineColor(Color.RED);
//        leftYAxis.setZeroLineWidth(1f);
    }
    void setXYAxis2(LineChart lineChart, XAxis xAxis, YAxis leftYAxis, YAxis rightYAxis) {
        /***XY轴的设置***/
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //X轴设置显示位置在底部
        xAxis.setAxisMinimum(0f); // 设置X轴的最小值
        xAxis.setAxisMaximum(5000); // 设置X轴的最大值
        xAxis.setLabelCount(10, false); // 设置X轴的刻度数量，第二个参数表示是否平均分配
        xAxis.setGranularity(0.1f); // 设置X轴坐标之间的最小间隔
        lineChart.setVisibleXRangeMaximum(10);// 当前统计图表中最多在x轴坐标线上显示的总量
        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinimum(-200f);
        rightYAxis.setAxisMinimum(-200f);
        leftYAxis.setAxisMaximum(200f);
        rightYAxis.setAxisMaximum(200f);
        leftYAxis.setLabelCount(10,false);
        rightYAxis.setLabelCount(10,false);
        leftYAxis.setGranularity(1f);
        rightYAxis.setGranularity(1f);
        /*leftYAxis.setLabelCount(20);*/


        leftYAxis.setEnabled(true);

//        leftYAxis.setCenterAxisLabels(true);// 将轴标记居中
//        leftYAxis.setDrawZeroLine(true); // 原点处绘制 一条线
//        leftYAxis.setZeroLineColor(Color.RED);
//        leftYAxis.setZeroLineWidth(1f);
    }
    void setXYAxis1(LineChart lineChart, XAxis xAxis, YAxis leftYAxis, YAxis rightYAxis) {
        /***XY轴的设置***/
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //X轴设置显示位置在底部
        xAxis.setAxisMinimum(0f); // 设置X轴的最小值
        xAxis.setAxisMaximum(5000); // 设置X轴的最大值
        xAxis.setLabelCount(10, false); // 设置X轴的刻度数量，第二个参数表示是否平均分配
        xAxis.setGranularity(0.1f); // 设置X轴坐标之间的最小间隔
        lineChart.setVisibleXRangeMaximum(10);// 当前统计图表中最多在x轴坐标线上显示的总量
        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinimum(-15f);
        rightYAxis.setAxisMinimum(-15f);
        leftYAxis.setAxisMaximum(15f);
        rightYAxis.setAxisMaximum(15f);
        leftYAxis.setLabelCount(6,false);
        rightYAxis.setLabelCount(6,false);
        leftYAxis.setGranularity(1f);
        rightYAxis.setGranularity(1f);
        /*leftYAxis.setLabelCount(20);*/


        leftYAxis.setEnabled(true);

//        leftYAxis.setCenterAxisLabels(true);// 将轴标记居中
//        leftYAxis.setDrawZeroLine(true); // 原点处绘制 一条线
//        leftYAxis.setZeroLineColor(Color.RED);
//        leftYAxis.setZeroLineWidth(1f);
    }
    /**
     * 功能：对图表中的曲线初始化，添加三条，并且默认显示第一条
     */
    void initLine() {

        createLine(mList1, mEntries1, mLineDataSet1, Color.parseColor("#FF0000"), mLineData, mLineChart);
        createLine(mList2, mEntries2, mLineDataSet2, Color.parseColor("#FFA500"), mLineData, mLineChart);
        createLine(mList3, mEntries3, mLineDataSet3, Color.parseColor("#FFFF00"), mLineData, mLineChart);


        // mLineData.getDataSetCount() 总线条数
        // mLineData.getEntryCount() 总点数
        // mLineData.getDataSetByIndex(index).getEntryCount() 索引index处折线的总点数
        // 每条曲线添加到mLineData后，从索引0处开始排列
        for (int i = 0; i < mLineData.getDataSetCount(); i++) {
            mLineChart.getLineData().getDataSets().get(i).setVisible(false); //
        }
        if(mCheckBox1.isChecked())showLine(0);
        if(mCheckBox2.isChecked())showLine(1);
        if(mCheckBox3.isChecked())showLine(2);
    }

    /**
     * 功能：根据索引显示或隐藏指定线条
     */
    public void showLine(int index) {
        mLineChart
                .getLineData()
                .getDataSets()
                .get(index)
                .setVisible(mCheckBoxList.get(index).isChecked());
        mLineChart.invalidate();
    }

    /**
     * 功能：动态创建一条曲线
     */
    private void createLine(List<Float> dataList, List<Entry> entries, LineDataSet lineDataSet, int color, LineData lineData, LineChart lineChart) {
        for (int i = 0; i < dataList.size(); i++) {
            /**
             * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
             * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
             */
            Entry entry = new Entry(i, dataList.get(i));// Entry(x,y)
            entries.add(entry);
        }

        // 初始化线条
        initLineDataSet(lineDataSet, color, LineDataSet.Mode.CUBIC_BEZIER);

        if (lineData == null) {
            lineData = new LineData();
            lineData.addDataSet(lineDataSet);
            lineChart.setData(lineData);
        } else {
            lineChart.getLineData().addDataSet(lineDataSet);
        }

        lineChart.invalidate();
    }


    /**
     * 曲线初始化设置,一个LineDataSet 代表一条曲线
     *
     * @param lineDataSet 线条
     * @param color       线条颜色
     * @param mode
     */
    private void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color); // 设置曲线颜色

        lineDataSet.setLineWidth(1f); // 设置折线宽度
        lineDataSet.setDrawCircles(false);
        lineDataSet.setValueTextSize(10f);

        lineDataSet.setDrawFilled(true); //设置折线图填充
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }

    }


    /**
     * 功能：创建图例
     */
    private void createLegend(Legend legend) {
        /***折线图例 标签 设置***/
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(12f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);
        legend.setEnabled(true);
    }


    /**
     * 设置 可以显示X Y 轴自定义值的 MarkerView
     */
    /*public void setMarkerView(LineChart lineChart) {
        LineChartMarkViewDemo mv = new LineChartMarkViewDemo(this);
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);
        lineChart.invalidate();
    }*/


    /**
     * 动态添加数据
     * 在一个LineChart中存放的折线，其实是以索引从0开始编号的
     *
     * @param yValues y值
     */
    public void addEntry(LineData lineData, LineChart lineChart, float yValues, int index) {

        // 通过索引得到一条折线，之后得到折线上当前点的数量
        float xCount = (float) (lineData.getDataSetByIndex(index).getEntryCount());


        Entry entry = new Entry(xCount, yValues); // 创建一个点
        lineData.addEntry(entry, index); // 将entry添加到指定索引处的折线中

        //通知数据已经改变
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();

        //把yValues移到指定索引的位置
        lineChart.moveViewToAnimated(xCount-9 , yValues, YAxis.AxisDependency.LEFT, 100);// TODO: 2019/5/4 内存泄漏，异步 待修复
        lineChart.invalidate();
    }


    public void addEntry1(LineData lineData, LineChart lineChart, float xValues, float yValues, int index) {


        Entry entry = new Entry(xValues, yValues); // 创建一个点
        lineData.addEntry(entry, index); // 将entry添加到指定索引处的折线中

        //通知数据已经改变
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();

        //把yValues移到指定索引的位置
        lineChart.moveViewToAnimated(xValues - 4, yValues, YAxis.AxisDependency.LEFT, 10);// TODO: 2019/5/4 内存泄漏，异步 待修复
        lineChart.invalidate();
    }

    /**
     * 功能：第1条折线添加一个点
     */
    public void addLine1Data(float yValues) {
        addEntry(mLineData, mLineChart, yValues, LINE_NUMBER_1);
    }

    /**
     * 功能：第2条折线添加一个点
     */
    public void addLine2Data(float yValues) {
        addEntry(mLineData, mLineChart, yValues, LINE_NUMBER_2);
    }


    public void addLine3Data(float yValues) {
        addEntry(mLineData, mLineChart, yValues, LINE_NUMBER_3);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 清空消息
       /* mDemoHandler.removeCallbacksAndMessages(null);
        mDemoHandler = null;*/

        // moveViewToAnimated 移动到某个点，有内存泄漏，暂未修复，希望网友可以指着
        mLineChart.clearAllViewportJobs();
        mLineChart.removeAllViewsInLayout();
        mLineChart.removeAllViews();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.checkBox:
                showLine(LINE_NUMBER_1);
                break;
            case R.id.checkBox2:
                showLine(LINE_NUMBER_2);
                break;
            case R.id.checkBox3:
                showLine(LINE_NUMBER_3);
                break;
            default:
        }
    }

}
