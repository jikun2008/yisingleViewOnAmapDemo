package com.map.test.study.demo.didi;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.map.test.study.demo.R;
import com.map.test.study.demo.base.BaseMapActivity;
import com.yisingle.amapview.lib.view.LocationMarkerView;
import com.yisingle.amapview.lib.view.TextMarkerView;

import java.util.List;

/**
 * @author jikun
 * Created by jikun on 2018/6/12.
 */
public class DidiAllActivity extends BaseMapActivity {
    private TextureMapView textureMapView;

    private List<TextMarkerView> textMarkerViewList;

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
        locationMarkerView = new LocationMarkerView.Builder(getApplicationContext(), getAmap())
                //设置有方向传感器的问题
                .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.location_map_direction))
                //设置没有重力传感器的图片
                .setWithOutSensorDrawableId(R.mipmap.location_map_no_direction)
                .create();
        locationMarkerView.startLocation();

    }
}
