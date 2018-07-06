package com.map.test.study.demo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DriveRouteResult;
import com.map.test.study.demo.R;
import com.yisingle.amapview.lib.base.view.marker.AbstractMarkerView;
import com.yisingle.amapview.lib.view.PathPlaningView;
import com.yisingle.amapview.lib.view.PointMarkerView;

import java.util.List;

/**
 * @author jikun
 * Created by jikun on 2018/7/5.
 */
public class CarMoveOnLineViewGroup {

    private final float distance = 2000f;


    private Context context;

    private AMap aMap;

    /**
     * 移动车辆位置
     */
    private PointMarkerView<String> carMoveView;


    private PathPlaningView<String, String> pathPlaningView;


    private LatLng endLatlng;


    public CarMoveOnLineViewGroup(@NonNull Context context, @NonNull AMap aMap) {
        this.context = context;
        this.aMap = aMap;

        carMoveView = new PointMarkerView.Builder(context, aMap)
                .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.car))
                .setAnchor(0.5f, 0.5f).create();

        pathPlaningView = new PathPlaningView.Builder(context, aMap)
                .setStartMarkBuilder(new PointMarkerView.Builder(context, aMap).setVisible(false)).create();

        carMoveView.setMoveListener(new AbstractMarkerView.OnMoveListener() {
            @Override
            public void onMove(LatLng latLng) {
                //如果endLatln为null 不用画线
                if (null == endLatlng || null == latLng) {
                    return;
                }


                //如果路径规划的起点和 移动起点小于1000米那么也不画线
                LatLonPoint latLonPoint = pathPlaningView.getStartLatLonPoint();
                if (null != latLonPoint) {
                    LatLng lineStartLatLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
                    if (AMapUtils.calculateLineDistance(lineStartLatLng, latLng) < 50) {
                        Log.e("测试代码", "测试代码<ditance=" + AMapUtils.calculateLineDistance(lineStartLatLng, latLng));
                        return;
                    }
                }


                drawLine(latLng, endLatlng);


            }
        });
    }


    private void drawLine(LatLng start, LatLng end) {
        Log.e("测试代码", "测试代码drawLine");
        LatLonPoint startPoint = new LatLonPoint(start.latitude, start.longitude);

        LatLonPoint endPoint = new LatLonPoint(end.latitude, end.longitude);

        pathPlaningView.beginDriveRouteSearched(startPoint, endPoint, true,
                new PathPlaningView.OnPathPlaningCallBack() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSucccess(DriveRouteResult routeResult) {

                    }

                    @Override
                    public void onFailed(String errorInfo) {

                    }
                });

    }

    public void setEndLatlng(LatLng endLatlng) {
        this.endLatlng = endLatlng;
    }

    public void startMove(List<LatLng> list, LatLng endLatlng) {
        this.endLatlng = endLatlng;
        startMove(list);
    }

    private void startMove(List<LatLng> list) {

        if (null != list && list.size() > 0 && null != carMoveView) {

            LatLng nowLatLng = carMoveView.getPosition();
            if (null == nowLatLng) {
                //如果carMoveView的坐标点为空 那么直接移动
                carMoveView.startMove(list, false);

            } else {
                //----1/2=0.5  强制转换为0 实际为0
                int middle = list.size() / 2;
                LatLng middleLatLng = list.get(middle);
                //当传递过来的坐标数组中间的坐标   与marker现在的坐标距离大于distance
                // 那么跳过以前坐标数组  直接到现在的坐标运动

                if (AMapUtils.calculateLineDistance(nowLatLng, nowLatLng) >= distance) {
                    carMoveView.startMove(list, false);

                } else {
                    carMoveView.startMove(list, true);
                }

            }


        }


    }


    public void destory() {
        if (null != carMoveView) {
            carMoveView.destory();
        }

        if (null != pathPlaningView) {
            pathPlaningView.destory();
        }

    }
}
