package android.secondbook.com.mapsoft3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by WangChang on 2016/5/15.
 */
public class TvFragment extends Fragment {

    private static final String ARG_TV = "tv";

    private String content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView tv = (TextView) getActivity().findViewById(R.id.tv);
        tv.setText(getArguments().getString("ARGS"));
    }

    public static TvFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        TvFragment fragment = new TvFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
