package android.secondbook.com.buttonfragment.check;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.secondbook.com.buttonfragment.R;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class GPSFragment extends Fragment {

    public static String TABLAYOUT_FRAGMENT = "tab_fragment";
    private int type;
    private LocationManager locManager;
    private AbsoluteLayout layout;
    private MyView1 myView1;
    private MyView2 myView2;

    private TextView tvLatitude, tvLongitude, tvSpeed, tvGpsTime,
            tvsatellItenumber;
    private Button resetbtn, exitbtn;

    private int satellItenumber = 0;
    private ArrayList<Gpsdata> info = null;

    private int upper_startleft = 135;
    private int upper_endleft = 20;
    private int below_startleft = 164;
    private int below_endleft = 330;
    private int upper_endleft_small = 340;
    private int below_endleft_small = 365;
    private int interval = 35;
    private int beizhi = 4;
    private int location = 8;

    public static GPSFragment newInstance(int type) {
        GPSFragment fragment = new GPSFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TABLAYOUT_FRAGMENT, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = (int) getArguments().getSerializable(TABLAYOUT_FRAGMENT);
        }
        // 检查gps设置
        openGPSSettings();

        // 创建LocationManager对象
        locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // 首次定位
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);

        updateView(location);

        // 设置每1秒获取一次GPS的定位信息
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                1, new LocationListener() //
                {
                    @Override
                    public void onLocationChanged(Location location) {
                        // 当GPS定位信息发生改变时，更新位置
                        updateView(location);
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        updateView(null);
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        // 当GPS LocationProvider可用时，更新位置
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        updateView(locManager.getLastKnownLocation(provider));
                    }

                    @Override
                    public void onStatusChanged(String provider, int status,
                                                Bundle extras) {
                    }
                });

        // 检测卫星数量
        locManager.addGpsStatusListener(statusListener);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gps_layout, container, false);
        initView(view);
        return view;
    }


    protected void initView(View view) {
        tvLongitude = (TextView) view.findViewById(R.id.longitude);
        tvLatitude = (TextView) view.findViewById(R.id.latitude);
        tvGpsTime = (TextView) view.findViewById(R.id.time);
        tvSpeed = (TextView) view.findViewById(R.id.speed);
        tvsatellItenumber = (TextView) view.findViewById(R.id.satellitenumber);
        resetbtn = (Button) view.findViewById(R.id.resetbtn);
        exitbtn = (Button) view.findViewById(R.id.exitButton);

        ButtonListener buttoner = new ButtonListener();
        resetbtn.setOnClickListener(buttoner);
        resetbtn.setOnTouchListener(buttoner);
        exitbtn.setOnClickListener(buttoner);

        layout = (AbsoluteLayout) view.findViewById(R.id.base);
        myView1 = new MyView1(getActivity());
        myView2 = new MyView2(getActivity());

        layout.addView(myView1);

    }
    private void openGPSSettings() {
        // TODO Auto-generated method stub
        LocationManager alm = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getActivity(), "GPS normal",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        //Toast.makeText(getActivity(), "请开启GPS！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面
    }

    class ButtonListener implements View.OnClickListener, View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d("test", "resetbtn ---> cancel");
                // resetbtn.setBackgroundColor(Color.BLUE);
                resetbtn.setBackgroundResource(R.drawable.blue);
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d("test", "resetbtn ---> down");
                // resetbtn.setBackgroundColor(Color.LTGRAY);
                resetbtn.setBackgroundResource(R.drawable.green);
            }
            return false;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v.getId() == R.id.resetbtn) {
                Log.d("test", "resetbtn ---> onclick");

                tvLongitude.setText("0.00000");
                tvLatitude.setText("0.00000");
                tvGpsTime.setText("00:00:00");
                tvSpeed.setText("0.0");
                tvsatellItenumber.setText("0");
            }

            if (v.getId() == R.id.exitButton){
                //getActivity().finish();
                System.exit(0);
            }
        }
    }

    public void updateView(Location newLocation) {
        if (newLocation != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
            DecimalFormat formatterValue = new DecimalFormat("#.00000");
            DecimalFormat formatterSpeed = new DecimalFormat("#0.0");

            tvLongitude.setText(formatterValue.format(newLocation
                    .getLongitude()));
            tvLatitude
                    .setText(formatterValue.format(newLocation.getLatitude()));
            tvGpsTime
                    .setText(formatter.format(new Date(newLocation.getTime())));
            tvSpeed.setText(formatterSpeed.format(newLocation.getSpeed()));
        }
    }

    /**
     * 卫星状态监听器
     */

    private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) { // GPS状态变化时的回调，如卫星数
            LocationManager locationManager = (LocationManager) getActivity()
                    .getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            GpsStatus status = locationManager.getGpsStatus(null); // 取当前状态
            String satelliteInfo = updateGpsStatus(event, status);
            tvsatellItenumber.setText(null);
            tvsatellItenumber.setText(satelliteInfo);
        }
    };

    private String updateGpsStatus(int event, GpsStatus status) {
        StringBuilder sb2 = new StringBuilder("");
        if (status == null) {
            sb2.append(0);
        } else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
            // 获取卫星颗数的默认最大值
            int maxSatellites = status.getMaxSatellites();
            // 创建一个迭代器保存所有卫星
            Iterator<GpsSatellite> iters = status.getSatellites().iterator();

            int count = 0;

            info = null;
            info = new ArrayList<Gpsdata>();

            while (iters.hasNext() && count <= maxSatellites) {
                GpsSatellite s = iters.next();

                s.getPrn();
                s.getSnr();
                satellItenumber = count + 1;

                Gpsdata gpsdata = new Gpsdata();
                gpsdata.setSignalintensity(s.getSnr());
                gpsdata.setStatenumber(s.getPrn());

                info.add(gpsdata);

                count++;
            }

            if (myView2 != null) {
                layout.removeView(myView2);
            }
            layout.addView(myView2);

            satellItenumber = count;

            sb2.append(satellItenumber);

        }

        return sb2.toString();
    }



    /**
     * 自定义继承View 的MyView
     *
     * @author Administrator
     */
    private class MyView1 extends View {
        public MyView1(Context context) {
            super(context);
        }

        // 重写onDraw（）
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            // 设置背景为透明
            canvas.drawColor(Color.TRANSPARENT);

            // 画12个白色实心长方形和最下边对应的小方块
            Paint paint = new Paint();
            // 去锯齿
            paint.setAntiAlias(true);
            // 设置paint 的style为 FILL：实心
            paint.setStyle(Paint.Style.FILL);
            // 设置paint的颜色
            paint.setColor(Color.WHITE);
            for (int i = 1; i < 25; i++) {
                int upper_startleft_New = upper_startleft + interval * (i - 1);
                int below_endleft_New = below_startleft + interval * (i - 1);

                canvas.drawRect(upper_startleft_New, upper_endleft,
                        below_endleft_New, below_endleft, paint);
                canvas.drawRect(upper_startleft_New, upper_endleft_small,
                        below_endleft_New, below_endleft_small, paint);
            }
        }
    }

    /**
     * 自定义继承View 的MyView
     *
     * @author Administrator
     *
     */
    private class MyView2 extends View {
        public MyView2(Context context) {
            super(context);
        }

        // 重写onDraw（）
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // 设置背景为透明
            canvas.drawColor(Color.TRANSPARENT);

            if (info != null) {
                int j = 0;
                for (Gpsdata i : info) {
                    j = j + 1;
                    // 画实心灰色信号强度条
                    Paint paint2 = new Paint();
                    // 设置paint 的style为 FILL：实心
                    paint2.setStyle(Paint.Style.FILL);
                    // 设置paint的颜色
                    paint2.setColor(getResources().getColor(R.color.gpsgreen));

                    // 画卫星编号和卫星信号强度数据
                    Paint paint1 = new Paint();
                    // 设置paint的颜色
                    paint1.setColor(Color.BLACK);
                    paint1.setTextSize(24);
                    // 灰色信号条的y轴
                    int upper_startleft_New = upper_startleft + interval
                            * (j - 1);
                    int below_endleft_New = below_startleft + interval
                            * (j - 1);

                    // 绿色背景
                    if (j > 1) {
                        canvas.drawRect(upper_startleft_New, below_endleft
                                        - (int) (i.getSignalintensity() * beizhi),
                                below_endleft_New, below_endleft, paint2);
                    } else {
                        canvas.drawRect(upper_startleft, below_endleft
                                        - (int) (i.getSignalintensity() * beizhi),
                                below_startleft, below_endleft, paint2);
                    }

                    // 信号具体数值
                    if ((int) i.getSignalintensity() > 9) {
                        canvas.drawText(
                                String.valueOf((int) i.getSignalintensity()),
                                upper_startleft + interval * (j - 1),
                                below_endleft
                                        - (int) (i.getSignalintensity() * beizhi)
                                        - 5, paint1);
                    } else {
                        canvas.drawText(
                                String.valueOf((int) i.getSignalintensity()),
                                upper_startleft + interval * (j - 1) + location,
                                below_endleft
                                        - (int) (i.getSignalintensity() * beizhi)
                                        - 5, paint1);
                    }

                    // 编号
                    if (i.getStatenumber() > 9) {
                        canvas.drawText(String.valueOf(i.getStatenumber()),
                                upper_startleft + interval * (j - 1),
                                below_endleft_small - 5, paint1);
                    } else {
                        canvas.drawText(
                                String.valueOf(i.getStatenumber()),
                                upper_startleft + interval * (j - 1) + location,
                                below_endleft_small - 5, paint1);
                    }

                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}