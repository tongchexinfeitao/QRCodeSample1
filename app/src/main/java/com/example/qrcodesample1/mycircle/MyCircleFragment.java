package com.example.qrcodesample1.mycircle;

import android.annotation.SuppressLint;
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
import com.example.qrcodesample1.utils.RetrofiManager;
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
        getMyCircle();

    }

    /**
     * 联网请求
     */
    private void getMyCircle() {
        //请求头
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("userId", "8112");
        headersMap.put("sessionId", "15687965780908112");

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
}
