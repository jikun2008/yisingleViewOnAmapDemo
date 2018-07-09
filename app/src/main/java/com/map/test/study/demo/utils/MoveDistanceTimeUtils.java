package com.map.test.study.demo.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.TMC;
import com.amap.api.services.route.WalkRouteResult;
import com.yisingle.amapview.lib.utils.SpatialRelationExpandUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author jikun
 * Created by jikun on 2018/7/9.
 */
public class MoveDistanceTimeUtils {


    private final int successCode = 1000;

    private Context context;

    /**
     * 是否正在路径规划
     */
    private boolean isRouteSearching = false;

    /**
     * 是否需要进行路径规划
     */
    private boolean isRouteSearchSuccess = false;


    private DrivePath drivePath;

    private ThreadPoolExecutor threadPoolExecutor;


    private OnDriverPathListener onDriverPathListener;

    public MoveDistanceTimeUtils(Context context) {
        this.context = context;


        //线程池  采用  线程池数为1 的
        threadPoolExecutor = new ThreadPoolExecutor(
                1,
                1,
                6000,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue(10), new ThreadFactory() {

            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                return new Thread("MoveDistanceTimeUtils_Thread");
            }
        }, new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    private void searchRoute(LatLonPoint stratLatLonPoint, LatLonPoint endLatLonPoint) {

        isRouteSearching = true;
        RouteSearch routeSearch = new RouteSearch(getContext());
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
                isRouteSearching = false;
            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
                isRouteSearching = false;
                if (i == successCode) {
                    if (null != driveRouteResult && null != driveRouteResult.getPaths() && driveRouteResult.getPaths().size() > 0) {
                        drivePath = driveRouteResult.getPaths().get(0);
                        isRouteSearchSuccess = true;
                        if (null != onDriverPathListener) {
                            onDriverPathListener.onCallBack(drivePath);
                        }
                    }

                } else {


                }
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
                isRouteSearching = false;
            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
                isRouteSearching = false;
            }
        });


        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(stratLatLonPoint, endLatLonPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, "");
        routeSearch.calculateDriveRouteAsyn(query);


    }


    public void moveCalcuDistanceTime(LatLng move, LatLng end) {
        moveCalcuDistanceTime(new LatLonPoint(move.latitude, move.longitude), new LatLonPoint(end.latitude, end.longitude));
    }

    /**
     * 移动并计算距离
     */
    public void moveCalcuDistanceTime(final LatLonPoint move, LatLonPoint end) {

        if (isRouteSearching) {
            //如果正在路径规划,不进行计算  直接返回
            Log.e("测试代码", "路径规划正在查询,所以直接返回");
            return;
        }

        if (!isRouteSearchSuccess) {
            //判断如果需要路径规划
            searchRoute(move, end);
        } else {
            if (null == drivePath) {
                Log.e("测试代码", "drivePath---null");
                return;
            }

            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {

                    calculat(move, drivePath);
                }
            });

        }

    }


    private void calculat(LatLonPoint move, DrivePath drivePath) {


        //返回此方案中的道路的总长度，单位米。
        float allDistance = drivePath.getDistance();
        //返回此方案中的道路的总时间，单位s
        long allDuration = drivePath.getDuration();
        if (allDuration < 0) {
            allDuration = 1;
        }
        BigDecimal speedBigDecimal = new BigDecimal(allDistance)
                .divide(new BigDecimal(allDistance), 2, BigDecimal.ROUND_HALF_UP);

        List<LatLng> totalLatLng = new ArrayList<>();


        //线路中交通情况
        List<TMC> trafficTmcs = new ArrayList<>();


        List<LatLonPoint> tmcList = new ArrayList<>();
        //组装数据
        List<DriveStep> driveStepList = drivePath.getSteps();
        for (DriveStep step : driveStepList) {
            // 获取交通状态不同的坐标点的集合
            // 后面将根据trafficTmcs来进行交通状态画线
            // trafficPolyLinewViews会根据trafficTmcs来画线
            for (TMC tmc : step.getTMCs()) {
                tmcList = tmc.getPolyline();
                Pair<Integer, LatLonPoint> pair = SpatialRelationExpandUtil.calShortestDistancePoint(tmcList, move);

                float realDistance = DistanceUtils.calculateTwoPointDistance(move, pair.second);

                Log.e("测试代码", "测试代码realDistance=" + realDistance);
                if (pair.first > 0 && pair.first + 1 <= tmcList.size()) {


                    List removeList = tmc.getPolyline().subList(0, pair.first + 1);


                    Log.e("测试代码", "测试代码pair.first=" + pair.first + "----pair.second=" + pair.second);


                    float removeDistance = DistanceUtils.calcaulateListDistance(removeList);

                    allDistance = allDistance - removeDistance;
                    if (allDistance < 0) {
                        allDistance = 5;
                    }

                    long removeDuration = new BigDecimal(removeDistance)
                            .divide(speedBigDecimal, 2, BigDecimal.ROUND_HALF_UP).longValue();

                    allDuration = allDuration - removeDuration;

                    if (allDuration < 0) {
                        allDuration = 30;
                    }

                    removeList.clear();


                }

                if (null != tmc.getPolyline() && tmc.getPolyline().size() != 0) {
                    trafficTmcs.add(tmc);

                    for (LatLonPoint latLonPoint : tmc.getPolyline()) {
                        totalLatLng.add(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
                    }

                }

            }


        }
        drivePath.setDistance(allDistance);
        drivePath.setDuration(allDuration);
        if (null != onDriverPathListener) {
            onDriverPathListener.onCallBack(drivePath);
        }
    }

    public Context getContext() {
        return context;
    }


    public interface OnDriverPathListener {
        void onCallBack(DrivePath path);
    }
}
