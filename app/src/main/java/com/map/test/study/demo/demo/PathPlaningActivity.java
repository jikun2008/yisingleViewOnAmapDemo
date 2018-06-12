package com.map.test.study.demo.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DriveRouteResult;
import com.map.test.study.demo.R;
import com.map.test.study.demo.base.BaseMapActivity;
import com.yisingle.amapview.lib.view.PathPlaningView;
import com.yisingle.amapview.lib.view.PointMarkerView;


/**
 * @author jikun
 * Created by jikun on 2018/5/24.
 */
public class PathPlaningActivity extends BaseMapActivity {
    private TextureMapView textureMapView;

    private PathPlaningView pathPlaningView;

    private TextView infoTextView;


    @Override
    protected void afterMapViewLoad() {
        pathPlaningView = new PathPlaningView.Builder(getApplicationContext(), getAmap())
                .setAuotDrawPath(true)
                .setEndMarkBuilder(new PointMarkerView.Builder(getApplicationContext(), getAmap())
                        .setText("终点").
                                setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.amap_end)))
                .setStartMarkBuilder(new PointMarkerView.Builder(getApplicationContext(), getAmap())
                        .setText("起点")
                        .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.amap_start)))
                .create();


        beginPlanning(null);


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planing_map);
        textureMapView = findViewById(R.id.textureMapView);
        infoTextView = findViewById(R.id.infoTextView);
        initMapView(savedInstanceState, textureMapView);


    }


    public void beginPlanning(View view) {


        planing(new LatLonPoint(30.655688, 104.065957), new LatLonPoint(30.654825, 104.066048));


    }

    public void beginOtherPlanning(View view) {

        planing(new LatLonPoint(30.644944, 104.021797), new LatLonPoint(30.554866, 104.069045));


    }

    private void planing(LatLonPoint start, LatLonPoint end) {

        pathPlaningView.beginDriveRouteSearched(start, end,
                new PathPlaningView.OnPathPlaningCallBack() {
                    @Override
                    public void onStart() {
                        infoTextView.setText("开始路径规划");

                    }

                    @Override
                    public void onSucccess(DriveRouteResult routeResult) {
                        infoTextView.setText("路径规划成功");
                        moveCamera(routeResult.getStartPos(), routeResult.getTargetPos());

                    }

                    @Override
                    public void onFailed(String errorInfo) {
                        infoTextView.setText("路径规划失败");

                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pathPlaningView.destory();
    }

    private void moveCamera(LatLonPoint start, LatLonPoint end) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        LatLng latLng1 = new LatLng(start.getLatitude(), start.getLongitude());
        LatLng latLng2 = new LatLng(end.getLatitude(), end.getLongitude());
        b.include(latLng1);
        b.include(latLng2);
        LatLngBounds bounds = b.build();
        getMapView().getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }


}
