package com.example.tr.greenfuel.loginRegister.aboutCar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tr.greenfuel.R;

import java.util.List;

/**
 * Created by tangpeng on 2017/2/22.
 */

public class CarBrandListViewAdapter extends BaseAdapter {

    private List<CarBrand> carBrandList;
    private Context context;

    public CarBrandListViewAdapter(List<CarBrand> carBrandList, Context context) {
        this.carBrandList = carBrandList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return carBrandList.size();
    }

    @Override
    public Object getItem(int position) {
        return carBrandList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_car_brand,null);
            viewHolder = new ViewHolder();
            viewHolder.brandName = (TextView)convertView.findViewById(R.id.brandName);
            viewHolder.sortLetter = (TextView)convertView.findViewById(R.id.alpha);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.brandName.setText(carBrandList.get(position).getBrandName());

        int section = getSectionFromPosition(position);
        if(position == getPositionFromSection(section)){//如果相等说明此item对应的首字母是第一次出现，则应该显示出该字母
            viewHolder.sortLetter.setText(carBrandList.get(position).getSortLetter());
            viewHolder.sortLetter.setVisibility(View.VISIBLE);
        }else {//如果不相等说明，此item对应的首字母不是第一次出现，即前面已经在listview中标识了，故之后的相同首字母的item都不要显示
            viewHolder.sortLetter.setVisibility(View.GONE); //不能设为INVISIBLE，因为空间还会占空间
        }
        return convertView;
    }

    public int getPositionFromSection(int section){//计算当前item对应的首字母在carBrandList中首次出现的位置
        for(int i = 0 ; i < carBrandList.size(); i++){
            if(carBrandList.get(i).getSortLetter().charAt(0) == section){
                return i;
            }
        }
        return -1;
    }

    private int getSectionFromPosition(int position){//根据position获得对应项的首字母的ascii码值
        return carBrandList.get(position).getSortLetter().charAt(0);
    }

    class ViewHolder{//保持对listview每一项中的子控件的引用
        TextView brandName;
        TextView sortLetter;
    }
}
