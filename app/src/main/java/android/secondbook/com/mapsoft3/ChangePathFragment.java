package android.secondbook.com.mapsoft3;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

//    private static final int TAG_POSITION = 1;

    private RecyclerView mCrimeRecyclerView;

    private StationAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_path, container, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.path_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        readExcel();
        updateUI();
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
    private void updateUI() {
        StationLab stationLab = StationLab.get(getActivity());
        List<Station> stations = stationLab.getStations();

        mAdapter = new StationAdapter(stations);
        mCrimeRecyclerView.setAdapter(mAdapter);
    }
    private class StationHolder extends RecyclerView.ViewHolder {

        private TextView mPathTextView;

        private Station mStation;

        public StationHolder(View itemView) {
            super(itemView);
//            itemView.setOnClickListener(this);

            mPathTextView = itemView.findViewById(R.id.path_text_view);
        }

        public void bindStation(Station station) {
            mStation = station;
            mPathTextView.setText(mStation.getName());
        }

       /* @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(),
                    mStation.getName() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
        }*/
    }

    private class StationAdapter extends RecyclerView.Adapter<StationHolder> implements View.OnClickListener{

        private List<Station> mStations;

        ItemClickListener listener;

        private List<Boolean> isClicks;

        public StationAdapter(List<Station> stations) {
            mStations = stations;
            isClicks = new ArrayList<>();
            for(int i = 0;i<mStations.size();i++){
                isClicks.add(false);
            }
        }
        public void setOnClickListener(ItemClickListener listener){

            this.listener = listener;
        }

        @Override
        public StationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.path_list, parent, false);
            view.setOnClickListener(this);
            return new StationHolder(view);
        }

        @Override
        public void onBindViewHolder(final StationHolder holder, final int position) {
            Station station = mStations.get(position);
            holder.bindStation(station);
            holder.itemView.setTag(position);
            if(isClicks.get(position)){
                holder.mPathTextView.setTextColor(Color.parseColor("#00a0e9"));
            }else{
                holder.mPathTextView.setTextColor(Color.parseColor("#FF000000"));
            }
        }


            @Override
            public int getItemCount () {

                return mStations.size();
            }


        @Override
        public void onClick(View v) {
            int position = (int)v.getTag();
 //           Station station = (Station) v.getTag(TAG_PATH);
            for(int i = 0; i <isClicks.size();i++)
            {
                isClicks.set(i,false);
            }
            isClicks.set(position,true);
            notifyDataSetChanged();
            if(listener != null){
                listener.onItemClick(v, position);
            }

        }
    }


    public void readExcel() {
        try {
            /**
             * 后续考虑问题,比如Excel里面的图片以及其他数据类型的读取
             **/
            String inPath = getInnerSDCardPath();
            InputStream is = new FileInputStream(inPath+"/pathnum.xls");
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
                        Station station = new Station();
                        station.setName(sheet.getCell(i,j).getContents());
                        StationLab.get(getActivity()).addStation(station);
                    }
                }
            }
            book.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
    public interface ItemClickListener{
        void onItemClick(View v,int position);
    }

}
