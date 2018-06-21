package com.map.test.study.demo.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.map.test.study.demo.R;
import com.map.test.study.demo.base.BaseMapActivity;
import com.yisingle.amapview.lib.view.TextMarkerView;


/**
 * @author jikun
 * Created by jikun on 2018/5/7.
 */
public class TextMarkerActivity extends BaseMapActivity {
    private TextureMapView textureMapView;

    private TextMarkerView textMarkerView;

    private LatLng latLng = new LatLng(30.657457, 104.06577);


    @Override
    protected void afterMapViewLoad() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_maker_map);
        textureMapView = findViewById(R.id.textureMapView);
        initMapView(savedInstanceState, textureMapView);


        textMarkerView = new TextMarkerView.Builder(getApplicationContext(), getAmap())

//                .setTextPaddingLeftOrRight(90)
                .setText("附近的点1")
                .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.hot_point)).create();
        moveToCamera(latLng);

        testSetPoint(null);


    }


    public void testChangeText(View view) {
        if (textMarkerView.getText().equals("附近的点1")) {
            textMarkerView.setText("附近的点0");
        } else {
            textMarkerView.setText("附近的点1");
        }

    }


    public void testSetPoint(View view) {
        if (null != textMarkerView.getPosition() && textMarkerView.getPosition().equals(latLng)) {
            textMarkerView.setPosition(new LatLng(30.65832, 104.065936));
        } else {
            textMarkerView.setPosition(latLng);
        }
    }


    public void showOrHideMarker(View view) {
        boolean isShow = !textMarkerView.isVisible();
        textMarkerView.setVisible(isShow);
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        textMarkerView.destory();


    }
}
