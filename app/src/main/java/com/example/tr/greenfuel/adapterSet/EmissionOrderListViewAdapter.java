package com.example.tr.greenfuel.adapterSet;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.tr.greenfuel.R;
import com.example.tr.greenfuel.util.PixelUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by tangpeng on 2017/2/27.
 */

public class EmissionOrderListViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<Map<String, Object>> mapList;
    private int layoutId;
    private String[] keys;
    private int[] viewsId;

    private RequestQueue requestQueue;

    public EmissionOrderListViewAdapter(Context mContext, List<Map<String, Object>> mapList, int layoutId, String[] keys, int[] viewsId) {
        this.mContext = mContext;
        this.mapList = mapList;
        this.layoutId = layoutId;
        this.keys = keys;
        this.viewsId = viewsId;
        requestQueue = Volley.newRequestQueue(mContext);
    }

    @Override
    public int getCount() {
        return mapList == null ? 0 : mapList.size();
    }

    @Override
    public Object getItem(int position) {
        return mapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(layoutId, null);
            viewHolder = new ViewHolder();
            viewHolder.order = (TextView) convertView.findViewById(viewsId[0]);
            viewHolder.name = (TextView) convertView.findViewById(viewsId[1]);
            viewHolder.emissionConsumption = (TextView) convertView.findViewById(viewsId[2]);
            viewHolder.header = (ImageView) convertView.findViewById(viewsId[3]);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.order.setText((String) (mapList.get(position)).get(keys[0]));
        viewHolder.name.setText((String) (mapList.get(position)).get(keys[1]));
        viewHolder.emissionConsumption.setText(mapList.get(position).get(keys[2]) + " L");

        ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache());
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(viewHolder.header, R.mipmap.user1, R.mipmap.user1);
        imageLoader.get((String) mapList.get(position).get(keys[3]), imageListener, PixelUtil.dp2px(40, mContext), PixelUtil.dp2px(40, mContext));
        return convertView;
    }

    class ViewHolder {
        TextView order;
        ImageView header;
        TextView name;
        TextView emissionConsumption;
    }

    class BitmapCache implements ImageLoader.ImageCache {

        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = 10 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }

    }

}
