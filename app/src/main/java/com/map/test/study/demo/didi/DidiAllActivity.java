package com.map.test.study.demo.didi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.map.test.study.demo.R;
import com.map.test.study.demo.base.BaseMapActivity;
import com.map.test.study.demo.didi.test.TestNearByDataUtils;
import com.map.test.study.demo.view.NearByCarViewGroup;
import com.map.test.study.demo.view.NearByPointViewGroup;
import com.yisingle.amapview.lib.view.LocationMarkerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jikun
 * Created by jikun on 2018/6/12.
 */
public class DidiAllActivity extends BaseMapActivity {
    private TextureMapView textureMapView;

    private LocationMarkerView locationMarkerView;


    /**
     * 附近的点
     */
    private NearByPointViewGroup nearByPointViewGroup;


    /**
     * 附近的车辆
     */
    private NearByCarViewGroup nearByCarViewGroup;


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
        initNearByView();
        getAmap().setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {

                produceSetDataNearByPointView(cameraPosition);
                produceSetDataNearByCarView(cameraPosition);

            }
        });

        getAmap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.546173, 104.06934), 14));

    }


    private void initNearByView() {
        nearByPointViewGroup = new NearByPointViewGroup(getApplicationContext(), getAmap());
        nearByCarViewGroup = new NearByCarViewGroup(getApplicationContext(), getAmap());

    }

    private void produceSetDataNearByPointView(CameraPosition cameraPosition) {
        List<NearByPointViewGroup.NearByData> list = new ArrayList<>();
        //产生坐标附近随机的点
        List<LatLng> latLngList = TestNearByDataUtils.produceLatLngList(10, new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude));
        for (int i = 0; i < latLngList.size(); i++) {
            list.add(new NearByPointViewGroup.NearByData("附近的点" + i, latLngList.get(i)));
        }
        nearByPointViewGroup.setTextAndPoint(list);

    }

    private void produceSetDataNearByCarView(CameraPosition cameraPosition) {


        nearByCarViewGroup.setLatLngList(TestNearByDataUtils.produceList(10, new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude)));
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


        locationMarkerView.setListener(new LocationMarkerView.OnLocationMarkerViewListenerAdapter() {
            @Override
            public void onLocationSuccess(AMapLocation loc) {
                super.onLocationSuccess(loc);
                // getAmap().moveCamera(CameraUpdateFactory.newLatLngZoom(locationMarkerView.getPosition(), 15));
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != locationMarkerView) {
            locationMarkerView.destory();
        }

        if (null != nearByPointViewGroup) {
            nearByPointViewGroup.destory();
        }

        if (null != nearByCarViewGroup) {
            nearByCarViewGroup.destory();
        }
    }


    public void testToDidiDetailActivity(View view) {
        Intent intent = new Intent(this, DidiDetailActivity.class);
        startActivity(intent);


    }
}
