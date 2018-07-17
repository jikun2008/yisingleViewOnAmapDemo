package com.map.test.study.demo.didi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.map.test.study.demo.R;
import com.map.test.study.demo.base.BaseMapActivity;
import com.map.test.study.demo.data.TestDataUtils;
import com.map.test.study.demo.view.CarMoveOnLineViewGroup;

import java.util.List;

/**
 * @author jikun
 * Created by jikun on 2018/7/5.
 */
public class DidiDetailActivity extends BaseMapActivity {


    private TextureMapView textureMapView;

    private CarMoveOnLineViewGroup carMoveOnLineViewGroup;

    private List<LatLng> nowListPoints = TestDataUtils.readLatLngs();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_didi_detail);
        textureMapView = findViewById(R.id.textureMapView);
        initMapView(savedInstanceState, textureMapView);


    }

    @Override
    protected void afterMapViewLoad() {

        carMoveOnLineViewGroup = new CarMoveOnLineViewGroup(getApplicationContext(), getAmap());

        carMoveOnLineViewGroup.startMove(nowListPoints, new LatLng(30.657616, 104.06625));
        moveCamre(nowListPoints);


    }

    private void moveCamre(List<LatLng> latLngList) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < latLngList.size(); i++) {
            b.include(latLngList.get(i));
        }
        LatLngBounds bounds = b.build();
        getMapView().getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        carMoveOnLineViewGroup.destory();
    }

    public void test(View view) {

    }
}
