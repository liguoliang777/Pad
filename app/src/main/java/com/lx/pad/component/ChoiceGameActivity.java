package com.lx.pad.component;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lx.pad.ItemType.AppInfo;
import com.lx.pad.R;
import com.lx.pad.adapter.ChoiceAppInfoAdapter;
import com.lx.pad.util.GameInfoDbMgr;
import com.lx.pad.util.PackageList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

public class ChoiceGameActivity extends BaseActivity  implements View.OnClickListener, View.OnFocusChangeListener, OnItemClickListener{
    private List<AppInfo> arrayListAppInfo;
    private List<String> dbArrayListAppInfo;
    private ArrayList<AppInfo> arrayListChoiceAppInfo;
    private GridView gameView;
    private ChoiceAppInfoAdapter choiceAppInfoAdapter;
    private View popupViewHandle;
    private Button popupViewBtnAdd;
    private Button popupViewBtnCancel;
    private GameInfoDbMgr gameInfoDbMgr;
    private ImageView btnBack;
    private TextView headTitleName;

    public ChoiceGameActivity(){
        super();
        arrayListAppInfo = new ArrayList<AppInfo>();
        dbArrayListAppInfo = new ArrayList<String>();
        arrayListChoiceAppInfo = new ArrayList<AppInfo>();
    }

    private void initViewControls(){
        gameView = (GridView)findViewById(R.id.game_view);
        btnBack = (ImageView)findViewById(R.id.btn_back);
        headTitleName = (TextView)findViewById(R.id.head_title_name);
        btnBack.setVisibility(View.VISIBLE);
        headTitleName.setText("选择游戏");
        btnBack.setOnClickListener(this);
        popupViewHandle = findViewById(R.id.popup_view_handle);
        popupViewBtnAdd = (Button)findViewById(R.id.btn_two);
        popupViewBtnAdd.setText("添加");
        popupViewBtnAdd.setFocusable(true);
        popupViewBtnCancel = (Button)findViewById(R.id.btn_one);
        popupViewBtnCancel.setText("取消");
        popupViewBtnCancel.setOnClickListener(this);
        popupViewBtnAdd.setOnClickListener(this);
        popupViewBtnAdd.setOnFocusChangeListener(this);
        popupViewBtnCancel.setOnFocusChangeListener(this);
    }

    private void initChoiceAdapter(){
        choiceAppInfoAdapter = new ChoiceAppInfoAdapter(arrayListAppInfo, this);
        gameView.setAdapter(choiceAppInfoAdapter);
        gameView.setOnItemClickListener(this);
        gameView.setSelector(new ColorDrawable(0));
    }

    private void updateArrayListAppInfo(){
        List<AppInfo> arrayList = PackageList.getAllPackageInfoList(this);
        dbArrayListAppInfo = gameInfoDbMgr.queryAppInfoFromDB();
        if(dbArrayListAppInfo.size() != 0 && arrayList.size() != 0){
            for(int nIndex = 0; nIndex < dbArrayListAppInfo.size(); nIndex++){
                int count = 0;
                while(count < arrayList.size()){
                    if(arrayList.get(count).getPackageName().equals(dbArrayListAppInfo.get(nIndex))){
                        arrayList.remove(count);
                    }else{
                        count++;
                        continue;
                    }
                    break;
                }
            }
        }
        arrayListAppInfo = arrayList;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_back:{
                arrayListChoiceAppInfo.clear();
                finish();
                break;
            }
            case R.id.btn_one:{
                choiceAppInfoAdapter.initAddFlagsTable();
                arrayListChoiceAppInfo.clear();
                popupViewHandle.setVisibility(View.GONE);
                break;
            }
            case R.id.btn_two:{
                if(arrayListChoiceAppInfo.size() <= 0){
                    return ;
                }

                for(int nIndex = 0; nIndex < arrayListChoiceAppInfo.size(); nIndex++){
                    gameInfoDbMgr.insertAppInfoFromDB(arrayListChoiceAppInfo.get(nIndex));
                    int index = 0;
                    while(index < arrayListAppInfo.size()){
                        if(arrayListAppInfo.get(index).getPackageName().equals(arrayListChoiceAppInfo.get(nIndex).getPackageName())){
                            arrayListAppInfo.remove(index);
                        }else{
                            index++;
                            continue;
                        }
                        break;
                    }
                }

                if(arrayListAppInfo.size() > 0){
                    choiceAppInfoAdapter.initArrayListAppInfo(arrayListAppInfo);
                    gameView.setAdapter(choiceAppInfoAdapter);
                    popupViewHandle.setVisibility(View.GONE);
                    return ;
                }

                arrayListChoiceAppInfo.clear();
                finish();
                break;
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int clr;
        switch(v.getId()){
            case R.id.btn_one:{
                if(hasFocus){
                    clr = R.color.tools_txt_bg;
                }else{
                    clr = R.color.grey;
                }
                popupViewBtnCancel.setBackgroundResource(clr);
                break;
            }
            case R.id.btn_two:{
                if(hasFocus){
                    clr = R.color.tools_txt_bg;
                }else{
                    clr = R.color.grey;
                }
                popupViewBtnAdd.setBackgroundResource(clr);
                break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(choiceAppInfoAdapter.addGameByPosition(position)){
            arrayListChoiceAppInfo.add(arrayListAppInfo.get(position));
        }else{
            for(int nIndex = 0; nIndex < arrayListChoiceAppInfo.size(); nIndex++){
                if(arrayListChoiceAppInfo.get(nIndex).getPackageName().equals(arrayListAppInfo.get(position).getPackageName())){
                    arrayListChoiceAppInfo.remove(nIndex);
                }
            }
        }

        if(arrayListChoiceAppInfo.size() > 0){
            popupViewHandle.setVisibility(View.VISIBLE);
        }else{
            popupViewHandle.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity_view);
        gameInfoDbMgr = new GameInfoDbMgr(this);
        initViewControls();
        updateArrayListAppInfo();
        initChoiceAdapter();
    }
}
