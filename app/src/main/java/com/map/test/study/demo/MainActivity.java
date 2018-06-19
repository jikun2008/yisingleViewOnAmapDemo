package com.map.test.study.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.map.test.study.demo.base.BaseActivity;
import com.map.test.study.demo.demo.LocationMarkerActivity;
import com.map.test.study.demo.demo.PathPlaningActivity;
import com.map.test.study.demo.demo.PointMarkerActivity;
import com.map.test.study.demo.demo.PointMarkerUseMoveActivty;
import com.map.test.study.demo.demo.TextMarkerActivity;
import com.yisingle.amapview.lib.utils.GaoDeErrorUtils;


public class MainActivity extends BaseActivity {

    private RouteSearch routeSearch;

    private Button btCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btCheck = findViewById(R.id.btCheck);
        routeSearch = new RouteSearch(this);


        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {


                btCheck.setText("点我再次验证\n" + GaoDeErrorUtils.getErrorInfo(i));
                dissmissLoadingDialog();

            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });
        startConfimGaode();


    }

    public void startConfimGaode() {
        showLoadingDialog();
        btCheck.setText("使用地图的路径规划中");
        LatLonPoint from = new LatLonPoint(30.537107, 104.06951);
        LatLonPoint to = new LatLonPoint(30.657349, 104.065837);
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(from, to);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, "");
        routeSearch.calculateDriveRouteAsyn(query);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void test(View view) {
        startConfimGaode();
    }


    public void toLocationMarkerMapActivity(View view) {
        Intent intent = new Intent();
        intent.setClass(this, LocationMarkerActivity.class);
        startActivity(intent);
    }

    public void toMoveMarkerMapActivty(View view) {
        Intent intent = new Intent();
        intent.setClass(this, PointMarkerUseMoveActivty.class);
        startActivity(intent);
    }


    public void toNearByMarkeActivity(View view) {
        Intent intent = new Intent();
        intent.setClass(this, TextMarkerActivity.class);
        startActivity(intent);
    }

    public void toStartEndActivity(View view) {
        Intent intent = new Intent();
        intent.setClass(this, PointMarkerActivity.class);
        startActivity(intent);
    }

    public void toPathPlaningActivity(View view) {
        Intent intent = new Intent();
        intent.setClass(this, PathPlaningActivity.class);
        startActivity(intent);
    }


}
