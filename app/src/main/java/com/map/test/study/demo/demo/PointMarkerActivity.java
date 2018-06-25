package com.map.test.study.demo.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.map.test.study.demo.R;
import com.map.test.study.demo.base.BaseMapActivity;
import com.yisingle.amapview.lib.base.view.marker.BaseMarkerView;
import com.yisingle.amapview.lib.view.PointMarkerView;
import com.yisingle.amapview.lib.viewholder.MapInfoWindowViewHolder;


/**
 * @author jikun
 * Created by jikun on 2018/5/16.
 */
public class PointMarkerActivity extends BaseMapActivity {
    private TextureMapView textureMapView;
    private PointMarkerView<String> startPointMarkerView;

    private PointMarkerView<String> endPointMarkerView;

    private LatLng startlatLng = new LatLng(30.65769, 104.062388);


    private LatLng endlatLng = new LatLng(30.657792, 104.064759);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startend_maker_map);
        textureMapView = findViewById(R.id.textureMapView);
        initMapView(savedInstanceState, textureMapView);


        startPointMarkerView = new PointMarkerView.Builder(getApplicationContext(), getAmap())
                .setText("测试代码")
                .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.amap_start))
                .create();

        startPointMarkerView.bindInfoWindowView(new BaseMarkerView.BaseInfoWindowView<String>(R.layout.start_info_window, "start") {
            @Override
            public void bindData(MapInfoWindowViewHolder viewHolder, String data) {
                viewHolder.setText(R.id.tvInfoWindow, data);
            }
        });
        startPointMarkerView.showInfoWindow("开始");


        endPointMarkerView = new PointMarkerView.Builder(getApplicationContext(), getAmap())
                .setText("我的代码")
                .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.point_test))
                .setAnchor(0.5f, 1)
                .create();


        endPointMarkerView.bindInfoWindowView(new BaseMarkerView.BaseInfoWindowView<String>(R.layout.end_info_window, "结束") {
            @Override
            public void bindData(MapInfoWindowViewHolder viewHolder, String data) {
                viewHolder.setText(R.id.tvInfoWindow1, data);
            }
        });


        endPointMarkerView.showInfoWindow("结束");


        moveToCamera(startlatLng, endlatLng);

        testSetPoint(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void afterMapViewLoad() {

    }


    public void testSetPoint(View view) {
        if (null != startPointMarkerView.getPosition() && startPointMarkerView.getPosition().equals(startlatLng)) {
            startPointMarkerView.setPosition(endlatLng);
            endPointMarkerView.setPosition(startlatLng);
        } else {
            startPointMarkerView.setPosition(startlatLng);
            endPointMarkerView.setPosition(endlatLng);
        }


    }


    public void testChangeUI(View view) {
        if (null != startPointMarkerView.getPosition() && startPointMarkerView.getPosition().equals(startlatLng)) {
            startPointMarkerView.setText("修改文字起点");
            startPointMarkerView.setPosition(endlatLng);
            endPointMarkerView.setText("修改文字终点");
            endPointMarkerView.setPosition(startlatLng);
        } else {
            startPointMarkerView.setText("修改文字起点1111");
            startPointMarkerView.setPosition(startlatLng);
            endPointMarkerView.setText("修改文字终点1111");
            endPointMarkerView.setPosition(endlatLng);
        }


    }

    int i = 0;

    public void testShowStartInfoWindow(View veiw) {

        i = i + 1;
        startPointMarkerView.showInfoWindow("Start" + i);
        endPointMarkerView.showInfoWindow("End" + i);
    }

    public void testShowEndInfoWindow(View view) {
        endPointMarkerView.showInfoWindow("End");
    }


    private void moveToCamera(LatLng start, LatLng end) {
        //设置缩放级别
        float zoom = 17;
        if (null != getAmap()) {
            //zoom - 缩放级别，[3-20]。

            LatLngBounds.Builder b = LatLngBounds.builder();

            b.include(start);
            b.include(end);
            LatLngBounds bounds = b.build();
            getAmap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }

    }

}
