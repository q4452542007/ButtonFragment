package android.secondbook.com.mapsoft3;

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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class LocationActivity extends AppCompatActivity {
	// ����LocationManager����
	private LocationManager locManager;
	private AbsoluteLayout layout;
	private MyView1 myView1;
	private MyView2 myView2;

	private TextView tvLatitude, tvLongitude, tvSpeed, tvGpsTime,
			tvsatellItenumber;
	private Button resetbtn;

	private int satellItenumber = 0;
	private ArrayList<Gpsdata> info = null;

	private int upper_startleft = 135; // ��
	private int upper_endleft = 20; // ��
	private int below_startleft = 164; // ��
	private int below_endleft = 330; // ��
	private int upper_endleft_small = 340; // ��
	private int below_endleft_small = 365; // ��
	private int interval = 35; // ���
	private int beizhi = 4; // ����ֵ
	private int location = 8; // ����λ��

	private final String TAG = "G3Example";

	private ImageButton mIcon3G;
	private TextView mLabel3G;

	String s1, s2, s3, s4, s5, s6, s7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location);

		// ��ʼ������
		initView();

		// ���gps����
		openGPSSettings();

		// ����LocationManager����
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// �״ζ�λ
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		Location location = locManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		updateView(location);

		// ����ÿ1���ȡһ��GPS�Ķ�λ��Ϣ
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
				1, new LocationListener() //
				{
					@Override
					public void onLocationChanged(Location location) {
						// ��GPS��λ��Ϣ�����ı�ʱ������λ��
						updateView(location);
					}

					@Override
					public void onProviderDisabled(String provider) {
						updateView(null);
					}

					@Override
					public void onProviderEnabled(String provider) {
						// ��GPS LocationProvider����ʱ������λ��
						if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
							// TODO: Consider calling
							//    ActivityCompat#requestPermissions
							// here to request the missing permissions, and then overriding
							//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
							//                                          int[] grantResults)
							// to handle the case where the user grants the permission. See the documentation
							// for ActivityCompat#requestPermissions for more details.
							return;
						}
						updateView(locManager.getLastKnownLocation(provider));
					}

					@Override
					public void onStatusChanged(String provider, int status,
												Bundle extras) {
					}
				});

		// �����������
		locManager.addGpsStatusListener(statusListener);

		mLabel3G = (TextView) findViewById(R.id.Label_3GDetail);

		TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		tel.listen(new PhoneStateMonitor(),
				PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
						| PhoneStateListener.LISTEN_SERVICE_STATE);

		s1 = this.getString(R.string.gsm_isgsm);
		s2 = this.getString(R.string.gsm_cdma_dbm);
		s3 = this.getString(R.string.gsm_cdma_ecio);
		s4 = this.getString(R.string.gsm_evdo_dbm);
		s5 = this.getString(R.string.gsm_evdo_ecio);
		s6 = this.getString(R.string.gsm_gsm_signal);
		s7 = this.getString(R.string.gsm_gsm_bit);
	}

	private void openGPSSettings() {
		// TODO Auto-generated method stub
		LocationManager alm = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Toast.makeText(LocationActivity.this, "GPS normal",
					Toast.LENGTH_SHORT).show();
			return;
		}

		// Toast.makeText(LocationActivity.this, "�뿪��GPS��", Toast.LENGTH_SHORT)
		// .show();
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivityForResult(intent, 0); // ��Ϊ������ɺ󷵻ص���ȡ����
	}

	private void initView() {
		// TODO Auto-generated method stub
		tvLongitude = (TextView) findViewById(R.id.longitude);
		tvLatitude = (TextView) findViewById(R.id.latitude);
		tvGpsTime = (TextView) findViewById(R.id.time);
		tvSpeed = (TextView) findViewById(R.id.speed);
		tvsatellItenumber = (TextView) findViewById(R.id.satellitenumber);
		resetbtn = (Button) findViewById(R.id.resetbtn);

		ButtonListener buttoner = new ButtonListener();
		resetbtn.setOnClickListener(buttoner);
		resetbtn.setOnTouchListener(buttoner);

		layout = (AbsoluteLayout) findViewById(R.id.base);
		myView1 = new MyView1(this);
		myView2 = new MyView2(this);

		layout.addView(myView1);
	}

	class ButtonListener implements OnClickListener, OnTouchListener {
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
	 * ����״̬������
	 */

	private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) { // GPS״̬�仯ʱ�Ļص�����������
			LocationManager locationManager = (LocationManager) LocationActivity.this
					.getSystemService(Context.LOCATION_SERVICE);
			if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			GpsStatus status = locationManager.getGpsStatus(null); // ȡ��ǰ״̬
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
			// ��ȡ���ǿ�����Ĭ�����ֵ
			int maxSatellites = status.getMaxSatellites();
			// ����һ��������������������
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_MENU) {// ����menu���¼�
												// do something...
			System.exit(0);
		}

		if (keyCode == KeyEvent.KEYCODE_BACK) {// ���ط��ذ�ť�¼�
												// do something...
			System.exit(0);
		}

		return true;
	}

	/**
	 * �Զ���̳�View ��MyView
	 * 
	 * @author Administrator
	 */
	private class MyView1 extends View {
		public MyView1(Context context) {
			super(context);
		}

		// ��дonDraw����
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			// ���ñ���Ϊ͸��
			canvas.drawColor(Color.TRANSPARENT);

			// ��12����ɫʵ�ĳ����κ����±߶�Ӧ��С����
			Paint paint = new Paint();
			// ȥ���
			paint.setAntiAlias(true);
			// ����paint ��styleΪ FILL��ʵ��
			paint.setStyle(Paint.Style.FILL);
			// ����paint����ɫ
			paint.setColor(Color.WHITE);
			for (int i = 1; i < 8; i++) {
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
	 * �Զ���̳�View ��MyView
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyView2 extends View {
		public MyView2(Context context) {
			super(context);
		}

		// ��дonDraw����
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			// ���ñ���Ϊ͸��
			canvas.drawColor(Color.TRANSPARENT);

			if (info != null) {
				int j = 0;
				for (Gpsdata i : info) {
					j = j + 1;
					// ��ʵ�Ļ�ɫ�ź�ǿ����
					Paint paint2 = new Paint();
					// ����paint ��styleΪ FILL��ʵ��
					paint2.setStyle(Paint.Style.FILL);
					// ����paint����ɫ
					paint2.setColor(getResources().getColor(R.color.gpsgreen));

					// �����Ǳ�ź������ź�ǿ������
					Paint paint1 = new Paint();
					// ����paint����ɫ
					paint1.setColor(Color.BLACK);
					paint1.setTextSize(24);
					// ��ɫ�ź�����y��
					int upper_startleft_New = upper_startleft + interval
							* (j - 1);
					int below_endleft_New = below_startleft + interval
							* (j - 1);

					// ��ɫ����
					if (j > 1) {
						canvas.drawRect(upper_startleft_New, below_endleft
								- (int) (i.getSignalintensity() * beizhi),
								below_endleft_New, below_endleft, paint2);
					} else {
						canvas.drawRect(upper_startleft, below_endleft
								- (int) (i.getSignalintensity() * beizhi),
								below_startleft, below_endleft, paint2);
					}

					// �źž�����ֵ
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

					// ���
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

	public class PhoneStateMonitor extends PhoneStateListener {
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			super.onSignalStrengthsChanged(signalStrength);

			mLabel3G.setText(s1 + signalStrength.isGsm() + "\n" + s2
					+ signalStrength.getCdmaDbm() + " Dbm" + "\n" + s3
					+ signalStrength.getCdmaEcio() + " dB*10" + "\n" + s4
					+ signalStrength.getEvdoDbm() + " Dbm" + "\n" + s5
					+ signalStrength.getEvdoEcio() + " dB*10" + "\n" + s6
					+ signalStrength.getGsmSignalStrength() + "\n" + s7
					+ signalStrength.getGsmBitErrorRate());

		}

		public void onServiceStateChanged(ServiceState serviceState) {
			super.onServiceStateChanged(serviceState);

			switch (serviceState.getState()) {
				case ServiceState.STATE_EMERGENCY_ONLY:
					Log.d(TAG, "3G STATUS : STATE_EMERGENCY_ONLY");
					break;
				case ServiceState.STATE_IN_SERVICE:
					Log.d(TAG, "3G STATUS : STATE_IN_SERVICE");
					break;
				case ServiceState.STATE_OUT_OF_SERVICE:
					Log.d(TAG, "3G STATUS : STATE_OUT_OF_SERVICE");
					break;
				case ServiceState.STATE_POWER_OFF:
					Log.d(TAG, "3G STATUS : STATE_POWER_OFF");
					break;
				default:
					break;
			}
		}
	}
}
