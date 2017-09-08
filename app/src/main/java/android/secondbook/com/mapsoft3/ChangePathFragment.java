package android.secondbook.com.mapsoft3;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by WangChang on 2016/5/15.
 */
public class ChangePathFragment extends Fragment {

    private static final String ARG_PATH = "path";

    private static final String TAG = "ChangePathFragment";

    private Spinner spinner;
    private List<String> pathnum_list;
    private List<String> data_uplist;
    private List<String> data_downlist;

    private StationLab mStationLab;



    private ArrayAdapter<String> arr_adapter;

    private RecyclerView mUpRecyclerView;

    private RecyclerView mDownRecyclerView;

    private StationAdapter upAdapter;
    private StationAdapter downAdapter;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_path, container, false);

        mUpRecyclerView = view.findViewById(R.id.uppath_recycler_view);
        mDownRecyclerView = view.findViewById(R.id.downpath_recycler_view);
        mUpRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDownRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        spinner = (Spinner) view.findViewById(R.id.spinner);
        //数据
        pathnum_list = new ArrayList<String>();
        if (pathnum_list != null) {
            readPath();
        }
        //适配器
        arr_adapter= new ArrayAdapter<String>(getActivity(), R.layout.spinner, pathnum_list);
        //设置样式
        arr_adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String pathNum = arr_adapter.getItem(i);
                data_uplist = new ArrayList<String>();
                data_downlist = new ArrayList<String>();
                mStationLab =StationLab.get();
                Path mPath = mStationLab.getPath(pathNum);
                if (mPath.getNum() == null) {
                    readStaion(pathNum,mPath);
                }
                upAdapter = new StationAdapter(mPath.getUpStations());
                mUpRecyclerView.setAdapter(upAdapter);
                downAdapter = new StationAdapter(mPath.getDownStations());
                mDownRecyclerView.setAdapter(downAdapter);
                upAdapter.notifyDataSetChanged();
                downAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;

    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static ChangePathFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PATH,path);

        ChangePathFragment fragment = new ChangePathFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public void readPath() {
        try {
            /**
             * 后续考虑问题,比如Excel里面的图片以及其他数据类型的读取
             **/

                String inPath = getInnerSDCardPath();
                InputStream is = new FileInputStream(inPath + "/pathnum.xls");
//          Workbook book = Workbook.getWorkbook(new File("mnt/sdcard/test.xls"));
                Workbook book = Workbook.getWorkbook(is);

                int num = book.getNumberOfSheets();
                // 获得第一个工作表对象
                for (int k = 0; k < num; k++) {
                    Sheet sheet = book.getSheet(0);
                    int Rows = sheet.getRows();
                    int Cols = sheet.getColumns();
                    for (int i = 0; i < Cols; ++i) {
                        for (int j = 0; j < Rows; ++j) {
                            pathnum_list.add(sheet.getCell(i, j).getContents());
                        }
                    }
                }
                book.close();
            } catch(Exception e){
                System.out.println(e);
            }

    }
    public void readStaion(String pathNum,Path mPath) {
        try {
            /**
             * 后续考虑问题,比如Excel里面的图片以及其他数据类型的读取
             **/
            String inPath = getInnerSDCardPath();
            InputStream is = new FileInputStream(inPath+"/"+pathNum+".xls");
            mPath.setNum(pathNum);
//          Workbook book = Workbook.getWorkbook(new File("mnt/sdcard/test.xls"));
            Workbook book = Workbook.getWorkbook(is);
            int num = book.getNumberOfSheets();
            // 获得第一个工作表对象
            for (int k =0; k < num; k++) {

                Sheet sheet = book.getSheet(0);
                int Rows = sheet.getRows();
                int Cols = sheet.getColumns();
                for (int i = 0; i < Cols; ++i) {
                    for (int j = 0; j < Rows; ++j) {
                        // getCell(Col,Row)获得单元格的值
                        if (i == 0) {
                            mPath.addUpStation(mPath.getStation(sheet.getCell(i, j).getContents()));
                        }
                        if (i == 1) {
                            mPath.addDownStation(mPath.getStation(sheet.getCell(i, j).getContents()));
                        }
                    }
                }
            }
            mStationLab.addPath(mPath);
            book.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private class StationHolder extends RecyclerView.ViewHolder {

        private TextView mPathTextView;


        public StationHolder(View itemView) {
            super(itemView);
//          itemView.setOnClickListener(this);
            mPathTextView = itemView.findViewById(R.id.path_text_view);
        }

        public void bindStation(String sationName) {
            mPathTextView.setText(sationName);
        }

       /* @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(),
                    mStation.getName() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
        }*/
    }

    private class StationAdapter extends RecyclerView.Adapter<StationHolder> {
        List<Station> mList;

        public StationAdapter(List<Station> list) {
            mList = list;
        }

        @Override
        public StationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.station_list, parent, false);
            return new StationHolder(view);
        }

        @Override
        public void onBindViewHolder(final StationHolder holder, final int position) {
            holder.bindStation(mList.get(position).getName());
        }


        @Override
        public int getItemCount () {

            return mList.size();
        }

    }

    public String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Destroy");
    }
}
