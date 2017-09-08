package android.secondbook.com.mapsoft3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by WangChang on 2016/5/15.
 */
public class OptionFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_TV = "option";

    private String content;

    private ImageButton checkBtn,changeBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_option, container, false);
        checkBtn = view.findViewById(R.id.checkbtn);
        changeBtn = view.findViewById(R.id.changebtn);
        checkBtn.setOnClickListener(this);
        changeBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static OptionFragment newInstance(String content) {
        OptionFragment fragment = new OptionFragment();
        return fragment;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.checkbtn:
                Intent gps_intent = new Intent(getActivity(),
                        LocationActivity.class);
                getActivity().startActivity(gps_intent);
                break;

            case R.id.changebtn:
                MainActivity mainActivity = (MainActivity ) getActivity();
                mainActivity.setPathBtn();
                break;

            default:
                break;
        }
        // 不要忘记提交

    }

}
