package android.secondbook.com.mapsoft3;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnClickListener {

    private Button homeBtn, pathBtn, affairBtn, optionBtn;
    private List<Button> btnList = new ArrayList<Button>();
    private FragmentManager fm;
    private FragmentTransaction ft;

    private RelativeLayout mMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mMain = (RelativeLayout) findViewById(R.id.main);
        mMain.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        findById();

        // 進入系統默認為movie
        fm =getSupportFragmentManager();
        ft = fm.beginTransaction();

        setBackgroundColorById(R.id.home_btn);
        ft.replace(R.id.fragment_content, new HomeFragment());
        ft.commit();
        
    }




    private void findById() {
        homeBtn = (Button) this.findViewById(R.id.home_btn);
        pathBtn = (Button) this.findViewById(R.id.path_btn);
        affairBtn = (Button) this.findViewById(R.id.affair_btn);
        optionBtn = (Button) this.findViewById(R.id.option_btn);
        homeBtn.setOnClickListener(this);
        pathBtn.setOnClickListener(this);
        affairBtn.setOnClickListener(this);
        optionBtn.setOnClickListener(this);

        btnList.add(homeBtn);
        btnList.add(pathBtn);
        btnList.add(affairBtn);
        btnList.add(optionBtn);
    }

    private void setBackgroundColorById(int btnId) {
        for (Button btn : btnList) {
            if (btn.getId() == btnId) {
                btn.setBackgroundColor(Color.RED);
            } else {
                btn.setBackgroundColor(Color.WHITE);
            }
        }
    }

    public void setPathBtn(){
        fm =getSupportFragmentManager();
        ft = fm.beginTransaction();
        setBackgroundColorById(R.id.path_btn);
        ft.replace(R.id.fragment_content, ChangePathFragment.newInstance("book"));
        ft.commit();
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.home_btn:
                setBackgroundColorById(R.id.home_btn);
                ft.replace(R.id.fragment_content, new HomeFragment());
                break;

            case R.id.path_btn:
                setBackgroundColorById(R.id.path_btn);
                ft.replace(R.id.fragment_content, ChangePathFragment.newInstance("book"));
                break;

            case R.id.affair_btn:
                setBackgroundColorById(R.id.affair_btn);
                ft.replace(R.id.fragment_content, new AffairFragment());
                break;

            case R.id.option_btn:
                setBackgroundColorById(R.id.option_btn);
                ft.replace(R.id.fragment_content, new OptionFragment());
                break;

            default:
                break;
        }
        // 不要忘记提交
        ft.commit();
    }
}
