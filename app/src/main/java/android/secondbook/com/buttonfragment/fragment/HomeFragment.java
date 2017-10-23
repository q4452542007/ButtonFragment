package android.secondbook.com.buttonfragment.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.secondbook.com.buttonfragment.R;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by WangChang on 2016/5/15.
 */
public class HomeFragment extends Fragment {

    private TextView tv_time;
    private static final int msgKey1 = 1;

    private IntentFilter mIntentFilter;
    private NetworkChangeReceiver mNetworkChangeReceiver;

    private ImageView wifiImage;

    private static Context context = null;

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Button okButton, photoButton, videoButton, browseButton;
    private Spinner systemSpinner, channelSpinner;

    private boolean isOnPause = false;
    // 功能相关
    private MediaRecorder mediaRecorder;
    private String videoFile = null;
    private boolean isRecord = false; // 判断是否录像

    private String dateString = null; // 保存文件
    public static String extsd_path = null;
    private int height = 0; // 制式通道的改变
    private int width = 0;

    private Camera camera = null;
    private Camera.Parameters param = null;
    private boolean previewRunning = false;
    private boolean isOpen = false;

    public LocationClient mLocationClient;
    private TextView speed,latitude,longitude;
    private float mSpeed;
    private String mTime;
    private StringBuilder stringBuilder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        if (getArguments() != null) {
            String mParam1 = getArguments().getString("param");
            TextView busPath =  (TextView)view.findViewById(R.id.bus_path);
            busPath.setText(mParam1);
        }

        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        wifiImage = (ImageView) view.findViewById(R.id.imageView);
        tv_time = (TextView) view.findViewById(R.id.mytime);
        speed = (TextView) view.findViewById(R.id.bus_speed);
        latitude = (TextView) view.findViewById(R.id.latitude);


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;//图片宽高都为原来的二分之一，即图片为原来的四分之一
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.nowifi, options);
        wifiImage.setImageBitmap(bitmap);

        // 初始化surfaceView
        /*surfaceView = (SurfaceView) view.findViewById(R.id.surface);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new MySurfaceViewCallback());
        surfaceHolder.setType(surfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);*/

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        getActivity().registerReceiver(mNetworkChangeReceiver, mIntentFilter);
        /*new TimeThread().start();*/

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permission = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permission, 1);
        } else {
            requestLocation();
        }

        return view;
    }
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(1000);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public static HomeFragment newInstance(String content) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("param", content);
        fragment.setArguments(args);
        return fragment;
    }




    /*public class TimeThread extends  Thread{
        @Override
        public void run() {
            super.run();
            do{
                try {
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    }*/
/*    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case msgKey1:
                    long time = System.currentTimeMillis();
                    Date date = new Date(time);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 EEE");
                    tv_time.setText(format.format(date));
                    break;
                default:
                    break;
            }
        }
    };*/
    class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 5;//图片宽高都为原来的二分之一，即图片为原来的四分之一
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.wifi, options);
                wifiImage.setImageBitmap(bitmap);
            } else {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;//图片宽高都为原来的二分之一，即图片为原来的四分之一
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.nowifi, options);
                wifiImage.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CloseCamera();
        getActivity().unregisterReceiver(mNetworkChangeReceiver);
    }

    private class MySurfaceViewCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            // TODO Auto-generated method stub
            System.out.println("------surfaceChanged------");
            surfaceHolder = holder;
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            System.out.println("------surfaceCreated------");
            surfaceHolder = holder;

            ok_choice();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            System.out.println("------surfaceDestroyed------");
            surfaceView = null;
            surfaceHolder = null;
        }
    }

    private void ok_choice() {
        // TODO Auto-generated method stub
        System.out.println("------ok button down------");
            height = 290;
            width = 503;



        InitCamera();
    }

    // 初始化camera
    private void InitCamera() {
        System.out.println("------InitCamera------");

        if (!isOpen) {
            camera = Camera.open(); // 取得第一个摄像头
            param = camera.getParameters();// 获取param
            param.setPreviewSize(width, height);// 设置预览大小
            param.setPreviewFpsRange(4, 10);// 预览照片时每秒显示多少帧的范围张
            param.setPictureFormat(ImageFormat.JPEG);// 图片形式
            param.set("jpeg-quality", 95);
            param.setPictureSize(1600, 900);
            camera.setParameters(param);
            try {
                camera.setPreviewDisplay(surfaceHolder);// 设置预览显示
            } catch (IOException e) {
            }
            // 进行预览
            if (!previewRunning) {
                camera.startPreview(); // 进行预览
                previewRunning = true; // 已经开始预览
            }

            isOpen = true;
        }
    }

    // 关闭摄像头
    private void CloseCamera() {
        if (camera != null) {
            System.out.println("------CloseCamera------");
            if (previewRunning) {
                camera.stopPreview(); // 停止预览
                previewRunning = false;
            }
            camera.release();
            camera = null;
            isOpen = false;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(getActivity(), "发生位置错误", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;
            default:
        }
    }
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("维度：").append(location.getLatitude()).append("\n");
            currentPosition.append("经线：").append(location.getLongitude()).append("\n");
            mTime = location.getTime();
            /*currentPosition.append("速度：").append(location.getSpeed()).append("\n");
            currentPosition.append("国家：").append(location.getCountry()).append("\n");
            currentPosition.append("省：").append(location.getProvince()).append("\n");
            currentPosition.append("市：").append(location.getCity()).append("\n");
            currentPosition.append("区：").append(location.getDistrict()).append("\n");
            currentPosition.append("街道：").append(location.getStreet()).append("\n");*/
            currentPosition.append("定位方式：");
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                currentPosition.append("GPS");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                currentPosition.append("网络");
            }
            mSpeed=location.getSpeed();
            stringBuilder = currentPosition;

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    speed.setText(String.valueOf(mSpeed));
                    latitude.setText(stringBuilder);
                    tv_time.setText(mTime);
                }
            });
           /*if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation) {
               navigateTo(location);
           }*/
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }


    }

}
