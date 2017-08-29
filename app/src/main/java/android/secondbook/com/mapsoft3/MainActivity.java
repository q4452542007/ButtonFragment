package android.secondbook.com.mapsoft3;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnClickListener {

    private Button homeBtn, pathBtn, animeBtn, varietyBtn;
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

        setBackgroundColorById(R.id.home_btn);
        ft.replace(R.id.fragment_content, new HomeFragment());
        ft.commit();

    }

    private void findById() {
        homeBtn = (Button) this.findViewById(R.id.home_btn);
        pathBtn = (Button) this.findViewById(R.id.path_btn);
        animeBtn = (Button) this.findViewById(R.id.anime_btn);
        varietyBtn = (Button) this.findViewById(R.id.variety_btn);
        homeBtn.setOnClickListener(this);
        pathBtn.setOnClickListener(this);
        animeBtn.setOnClickListener(this);
        varietyBtn.setOnClickListener(this);

        btnList.add(homeBtn);
        btnList.add(pathBtn);
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

            case R.id.home_btn:
                setBackgroundColorById(R.id.home_btn);
                ft.replace(R.id.fragment_content, new HomeFragment());
                break;

            case R.id.path_btn:
                setBackgroundColorById(R.id.path_btn);
                ft.replace(R.id.fragment_content, ChangePathFragment.newInstance("book"));
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
