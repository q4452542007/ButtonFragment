package android.secondbook.com.mapsoft3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnClickListener {

    private Button movieBtn, tvBtn, animeBtn, varietyBtn;
    private List<Button> btnList = new ArrayList<Button>();
    private FragmentManager fm;
    private FragmentTransaction ft;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        findById();

        // 進入系統默認為movie
        fm =getSupportFragmentManager();
        ft = fm.beginTransaction();

        setBackgroundColorById(R.id.movie_btn);
        ft.replace(R.id.fragment_content, new HomeFragment());
        ft.commit();

    }

    private void findById() {
        movieBtn = (Button) this.findViewById(R.id.movie_btn);
        tvBtn = (Button) this.findViewById(R.id.tv_btn);
        animeBtn = (Button) this.findViewById(R.id.anime_btn);
        varietyBtn = (Button) this.findViewById(R.id.variety_btn);
        movieBtn.setOnClickListener(this);
        tvBtn.setOnClickListener(this);
        animeBtn.setOnClickListener(this);
        varietyBtn.setOnClickListener(this);

        btnList.add(movieBtn);
        btnList.add(tvBtn);
        btnList.add(animeBtn);
        btnList.add(varietyBtn);
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


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (v.getId()) {

            case R.id.movie_btn:
                setBackgroundColorById(R.id.movie_btn);
                ft.replace(R.id.fragment_content, new HomeFragment());
                break;

            case R.id.tv_btn:
                setBackgroundColorById(R.id.tv_btn);
                ft.replace(R.id.fragment_content, BookFragment.newInstance("book"));
                break;

            case R.id.anime_btn:
                setBackgroundColorById(R.id.anime_btn);
                ft.replace(R.id.fragment_content, new MusicFragment());
                break;

            case R.id.variety_btn:
                setBackgroundColorById(R.id.variety_btn);
                ft.replace(R.id.fragment_content, new TvFragment());
                break;

            default:
                break;
        }
        // 不要忘记提交
        ft.commit();
    }




}
