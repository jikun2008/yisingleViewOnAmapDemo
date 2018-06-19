package com.map.test.study.demo.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.PolylineOptions;
import com.map.test.study.demo.R;
import com.map.test.study.demo.base.BaseMapActivity;
import com.map.test.study.demo.data.TestDataUtils;
import com.yisingle.amapview.lib.base.view.marker.BaseMarkerView;
import com.yisingle.amapview.lib.view.PointMarkerView;
import com.yisingle.amapview.lib.viewholder.MapInfoWindowViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * @author jikun
 * Created by jikun on 2018/4/27.
 * 平滑移动效果
 */
public class PointMarkerUseMoveActivty extends BaseMapActivity {

    private TextureMapView textureMapView;


    private PointMarkerView<String> pointMarkerView;


    private List<LatLng> nowListPoints = TestDataUtils.readLatLngsnow();

    private List<LatLng> resumeListPoints = TestDataUtils.readLatLngsresume();


    @Override
    protected void afterMapViewLoad() {

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_marker_map);
        textureMapView = findViewById(R.id.textureMapView);
        initMapView(savedInstanceState, textureMapView);

        //移动并画移动的坐标点线
        moveToCameraAndDrawLine();


        pointMarkerView = new PointMarkerView.Builder(getApplicationContext(), getAmap())
                //这里要设置锚点在markrer的中间
                .setAnchor(0.5f, 0.5f)
                .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.car)).create();

        pointMarkerView.setInfoWindowView(new BaseMarkerView.InfoWindowView<String>(R.layout.start_info_window, "") {
            @Override
            public void bindData(MapInfoWindowViewHolder viewHolder, String data) {

                viewHolder.setText(R.id.tvInfoWindow, data);

            }
        });


        startMove(null);

        pointMarkerView.showInfoWindow("我的代码");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        pointMarkerView.destory();
    }


    public void startMove(View view) {


        moveCamre(nowListPoints);
        pointMarkerView.startMove(nowListPoints, false);
    }

    public void stopMove(View view) {


        pointMarkerView.stopMove();
    }

    public void resumeMove(View view) {

        moveCamre(TestDataUtils.readLatLngsAll());
        pointMarkerView.startMove(resumeListPoints, true);

    }


    public void startOtherMove(View view) {


        moveCamre(resumeListPoints);
        pointMarkerView.startMove(resumeListPoints, false);


    }


    public void moveToCameraAndDrawLine() {


        // 获取轨迹坐标点
        List<LatLng> points = TestDataUtils.readLatLngsnow();

        List<LatLng> points1 = TestDataUtils.readLatLngsresume();

        //setCustomTextureList(bitmapDescriptors)
        getMapView().getMap().addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.mipmap.custtexture_smooth))
                .addAll(points)
                .useGradient(true)
                .width(30));

        //setCustomTextureList(bitmapDescriptors)
        getMapView().getMap().addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.mipmap.custtexture_slow))
                .addAll(points1)
                .useGradient(true)
                .width(30));

        LatLngBounds.Builder b = LatLngBounds.builder();
        List<LatLng> latLngList = new ArrayList<>();
        latLngList.addAll(points);
        latLngList.addAll(points1);
        for (int i = 0; i < latLngList.size(); i++) {
            b.include(latLngList.get(i));
        }
        LatLngBounds bounds = b.build();
        getMapView().getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }


    private void moveCamre(List<LatLng> latLngList) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < latLngList.size(); i++) {
            b.include(latLngList.get(i));
        }
        LatLngBounds bounds = b.build();
        getMapView().getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }
}
