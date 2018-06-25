package com.map.test.study.demo.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.map.test.study.demo.R;
import com.map.test.study.demo.base.BaseMapActivity;
import com.yisingle.amapview.lib.view.LocationMarkerView;


/**
 * @author jikun
 * Created by jikun on 2018/4/11.
 */
public class LocationMarkerActivity extends BaseMapActivity {

    private TextureMapView textureMapView;


    private LocationMarkerView<String> locationMarkerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_maker_map);
        textureMapView = findViewById(R.id.textureMapView);
        initMapView(savedInstanceState, textureMapView);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationMarkerView.destory();

    }

    @Override
    protected void afterMapViewLoad() {

        locationMarkerView =
                new LocationMarkerView.Builder(getApplicationContext(), getAmap())
                        .create();

        locationMarkerView.startLocation();

        locationMarkerView.setListener(new LocationMarkerView.OnLocationMarkerViewListener() {
            @Override
            public void onLocationSuccess(AMapLocation loc) {
                moveToCamera(new LatLng(loc.getLatitude(), loc.getLongitude()));
            }

            @Override
            public void onLocationFailed(AMapLocation loc) {
                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onRotationSuccess(float angle) {

            }

            @Override
            public void onRotationFailed(String erroInfo) {

            }
        });


    }


    public void testLocation(View view) {

        if (null != locationMarkerView.getMarker() && null != locationMarkerView.getMarker().getPosition()) {
            moveToCamera(locationMarkerView.getMarker().getPosition());
        }

    }


    public void testShowInfoWindow(View view) {
        boolean isShow = locationMarkerView.isShowInfoWindow();
        if (isShow) {
            locationMarkerView.hideInfoWindow();
        } else {
            locationMarkerView.showInfoWindow("显示的文字效果");
        }

        TextView textView = (TextView) view;
        textView.setText(isShow ? "显示Window" : "隐藏Window");

    }


    public void showOrHideView(View view) {
        boolean isShow = !locationMarkerView.isVisible();
        locationMarkerView.setVisible(isShow);
        TextView textView = (TextView) view;
        textView.setText(!isShow ? "显示Marker" : "隐藏Marker");
    }

    private void moveToCamera(LatLng latLng) {
        //设置缩放级别
        float zoom = 17;
        if (null != getAmap()) {
            //zoom - 缩放级别，[3-20]。
            getAmap().animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        }

    }


}
