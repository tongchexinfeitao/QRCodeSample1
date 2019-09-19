package com.example.qrcodesample1.mycircle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qrcodesample1.R;
import com.example.qrcodesample1.mycircle.greendao.GreenDaoUtil;
import com.example.qrcodesample1.mycircle.greendao.MyCircleBeanTable;
import com.example.qrcodesample1.utils.RetrofiManager;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 我的圈子 多条目展示
 */
public class MyCircleFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.xrv_my_circle)
    XRecyclerView mXrvMyCircle;

    List<MyCircleBean.ResultBean> list = new ArrayList<>();
    private MyCircleAdapter mMyCircleAdapter;

    private boolean isLoadMore;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_mycircle_layout, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        return inflate;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //支持上拉
        mXrvMyCircle.setLoadingMoreEnabled(true);
        //设置下拉和上啦监听
        mXrvMyCircle.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //当前是刷新，修改isLoadMore的状态为false
                isLoadMore = false;
                page = 1;
                //重新联网请求
                getMyCircle();
            }

            @Override
            public void onLoadMore() {
                //当前是加载更多， 修改isLoadMore的状态为true
                isLoadMore = true;
                page++;
                //重新联网请求
                getMyCircle();
            }
        });
        mXrvMyCircle.setLayoutManager(new LinearLayoutManager(getContext()));
        mMyCircleAdapter = new MyCircleAdapter(list);
        mXrvMyCircle.setAdapter(mMyCircleAdapter);

        if (hasNetwork()) {
            getMyCircle();
        } else {
            // TODO: 2019/9/19 读取缓存思路：
            //  1、先查询
            //  2、查出的集合长度大于0的时候,取出第一条数据 get(0)拿到 MyCircleBeanTable 对象
            //  3、取出MyCircleBeanTable 对象中的json （ getJson ）
            //  4、将json 转成 MyCircleBean
            //  5、取出 MyCircleBean 中数据集合
            //  6、 刷新适配器

            List<MyCircleBeanTable> databaseList = GreenDaoUtil.getDaoSession(getContext()).getMyCircleBeanTableDao()
                    .queryBuilder()
                    .list();
            //必须判断长度大于0才能去获取第一条数据
            if (databaseList.size() > 0) {
                //取出数据库中缓存的  MyCircleBeanTable 对象
                MyCircleBeanTable myCircleBeanTable = databaseList.get(0);
                //从MyCircleBeanTable对象中取出json
                String json = myCircleBeanTable.getJson();
                //解析json
                MyCircleBean myCircleBean = new Gson().fromJson(json, MyCircleBean.class);
                List<MyCircleBean.ResultBean> myCircleBeanResults = myCircleBean.getResult();
                // TODO: 2019/9/19 刷新适配器
                mMyCircleAdapter.setData(myCircleBeanResults, false);
                Log.e("TAG", "无网读取数据库成功");
            }
        }
    }

    /**
     * 联网请求
     */
    @SuppressLint("CheckResult")
    private void getMyCircle() {
        //请求头
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("userId", "8112");
        headersMap.put("sessionId", "15688780695488112");

        //get的查询参数
        Map<String, Integer> paramsMap = new HashMap<>();
        paramsMap.put("page", page);
        paramsMap.put("count", 10);

        RetrofiManager.getInstance()
                .create()
                .getMyCircle(headersMap, paramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MyCircleBean>() {
                    @Override
                    public void accept(MyCircleBean myCircleBean) throws Exception {
                        //隐藏等待框
                        if (isLoadMore) {
                            mXrvMyCircle.loadMoreComplete();
                        } else {
                            mXrvMyCircle.refreshComplete();
                        }
                        if ("0000".equals(myCircleBean.getStatus())) {
                            if (myCircleBean.getResult().size() == 0) {
                                Toast.makeText(getContext(), "没有数据了", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Log.e("TAG", "请求成功" + myCircleBean.toString());
                            Toast.makeText(getContext(), "请求成功", Toast.LENGTH_SHORT).show();
                            mMyCircleAdapter.setData(myCircleBean.getResult(), isLoadMore);


                            // TODO: 2019/9/19 缓存数据的 思路：
                            //  1、先清除数据库 （deleteAll()） 防止重复添加
                            //  2、将 MyCircleBean 转成 json
                            //  3、将json通过构造器，创建出MyCircleBeanTable 对象
                            //  5、将 MyCircleBeanTable 对象 插入到数据库

                            //先清除数据库，防止重复缓存
                            GreenDaoUtil.getDaoSession(getContext()).getMyCircleBeanTableDao()
                                    .deleteAll();
                            //先把bean转成bean
                            String toJson = new Gson().toJson(myCircleBean);
                            //创建一个数据库表映射对象
                            MyCircleBeanTable myCircleBeanTable = new MyCircleBeanTable(null, toJson);
                            // TODO: 2019/9/19 插入到数据库
                            GreenDaoUtil.getDaoSession(getContext()).getMyCircleBeanTableDao()
                                    .insert(myCircleBeanTable);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //隐藏等待框
                        if (isLoadMore) {
                            mXrvMyCircle.loadMoreComplete();
                        } else {
                            mXrvMyCircle.refreshComplete();
                        }
                        Log.e("TAG", "请求失败" + throwable.toString());
                        Toast.makeText(getContext(), "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public boolean hasNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() ? true : false;
    }
}

