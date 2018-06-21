package com.map.test.study.demo.didi;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.map.test.study.demo.R;
import com.map.test.study.demo.base.BaseMapActivity;
import com.map.test.study.demo.didi.test.TestNearByDataUtils;
import com.map.test.study.demo.utils.TwoArrayUtils;
import com.yisingle.amapview.lib.view.LocationMarkerView;
import com.yisingle.amapview.lib.view.TextMarkerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jikun
 * Created by jikun on 2018/6/12.
 */
public class DidiAllActivity extends BaseMapActivity {
    private TextureMapView textureMapView;

    private List<TextMarkerView> textMarkerViewList = new ArrayList<>();

    private List<LatLng> latLngList = new ArrayList<>();

    private LocationMarkerView locationMarkerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_didi);
        textureMapView = findViewById(R.id.textureMapView);
        initMapView(savedInstanceState, textureMapView);


    }

    @Override
    protected void afterMapViewLoad() {
        initLocationView();
        getAmap().setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {

                initNearByPointText(new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude));

            }
        });

    }


    //定位的点
    private void initLocationView() {
        locationMarkerView = new LocationMarkerView.Builder(getApplicationContext(), getAmap())
                //设置有方向传感器的问题
                .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.location_map_direction))
                //设置没有重力传感器的图片
                .setWithOutSensorDrawableId(R.mipmap.location_map_no_direction)
                .create();
        locationMarkerView.startLocation();


        locationMarkerView.setListener(new LocationMarkerView.onLocationMarkerViewListenerAdapter() {
            @Override
            public void onLocationSuccess(AMapLocation loc) {
                super.onLocationSuccess(loc);
                // getAmap().moveCamera(CameraUpdateFactory.newLatLngZoom(locationMarkerView.getPosition(), 15));
            }
        });

    }

    /**
     * 附近的点
     */
    private void initNearByPointText(LatLng latLng) {
        latLngList = TestNearByDataUtils.produceLatLngList(10, latLng);

        TwoArrayUtils.looperCompare(textMarkerViewList, latLngList, new TwoArrayUtils.Listener<List<TextMarkerView>, List<LatLng>>() {


            @Override
            public void onOneMore(List<TextMarkerView> more, List<TextMarkerView> remain) {

                if (null != textMarkerViewList && null != latLngList) {
                    for (TextMarkerView view : more) {
                        view.destory();
                    }

                    textMarkerViewList = remain;

                    setNearByText();
                }


            }

            @Override
            public void onTwoMore(List<LatLng> more, List<LatLng> remain) {
                for (LatLng latLng : more) {
                    textMarkerViewList.add(produce());
                }


                setNearByText();


            }

            @Override
            public void onSizeEqual() {


            }
        });

    }

    private void setNearByText() {
        for (int i = 0; i < textMarkerViewList.size(); i++) {

            if (i < latLngList.size()) {
                textMarkerViewList.get(i).setPosition(latLngList.get(i));
                textMarkerViewList.get(i).setText("附近的点" + i);
            }


        }
    }


    private TextMarkerView produce() {
        TextMarkerView textMarkerView = new TextMarkerView.Builder(getApplicationContext(), getAmap())
                .create();
        return textMarkerView;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationMarkerView.destory();
    }
}
