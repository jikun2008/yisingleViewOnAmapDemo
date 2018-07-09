package com.map.test.study.demo.view;

import android.content.Context;
import android.support.annotation.NonNull;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;

import com.yisingle.amapview.lib.utils.TwoArrayUtils;
import com.yisingle.amapview.lib.view.TextMarkerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jikun
 * Created by jikun on 2018/6/22.
 * 附近的点坐标方法
 */
public class NearByPointViewGroup {


    private AMap amap;

    private Context context;
    private List<TextMarkerView> textMarkerViewList = new ArrayList<>();


    private List<NearByData> nearByDataList = new ArrayList<>();


    public NearByPointViewGroup(Context context, AMap amap) {
        this.amap = amap;
        this.context = context;
    }

    public void setTextAndPoint(@NonNull List<NearByData> nearByData) {

        nearByDataList = nearByData;

        TwoArrayUtils.looperCompare(textMarkerViewList, nearByData, new TwoArrayUtils.Listener<List<TextMarkerView>, List<NearByData>>() {
            @Override
            public void onOneMore(List<TextMarkerView> more, List<TextMarkerView> remain) {
                if (null != textMarkerViewList && null != nearByDataList) {
                    for (TextMarkerView view : more) {
                        view.destory();
                    }
                    textMarkerViewList = remain;
                    setNearByText();
                }
            }

            @Override
            public void onTwoMore(List<NearByData> more, List<NearByData> remain) {
                for (NearByData data : more) {
                    textMarkerViewList.add(produce());
                }
                setNearByText();
            }
            @Override
            public void onSizeEqual() {
                setNearByText();
            }
        });

    }


    private TextMarkerView produce() {
        return new TextMarkerView.Builder(getContext(), getAmap())
                .setZindex(2)
                .create();

    }


    public AMap getAmap() {
        return amap;
    }


    public Context getContext() {
        return context;
    }


    private void setNearByText() {
        for (int i = 0; i < textMarkerViewList.size(); i++) {

            if (i < nearByDataList.size()) {
                NearByData data = nearByDataList.get(i);
                textMarkerViewList.get(i).setPosition(data.getLatLng());
                textMarkerViewList.get(i).setText(data.getText());
            }


        }
    }


    public static class NearByData {
        private String text;
        private LatLng latLng;


        public NearByData(@NonNull String text, @NonNull LatLng latLng) {
            this.text = text;
            this.latLng = latLng;
        }

        public String getText() {
            return text;
        }

        public LatLng getLatLng() {
            return latLng;
        }
    }

    public void destory() {

        for (TextMarkerView textMarkerView : textMarkerViewList) {
            textMarkerView.destory();
        }

    }


}
