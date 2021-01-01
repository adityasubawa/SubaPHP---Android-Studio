package com.suba.php;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.squareup.picasso.*;

import java.util.List;
import java.util.Map;

public class CustomImageAdapter extends SimpleAdapter {

    public CustomImageAdapter(Context context, List<? extends Map<String, ?>> list, int resource, String[] from, int[] to){
        super(context, list, resource, from, to);
    }
    public View getView(int position, View convertView, ViewGroup parent){

        View v = super.getView(position, convertView, parent);

        ImageView imgvw = (ImageView) v.getTag();
        if(imgvw == null){
            imgvw = (ImageView) v.findViewById(R.id.imageIv);
            v.setTag(imgvw);
        }
        // get the url from the data in the `Map`
        String url = ((Map)getItem(position)).get("gambar").toString();
        Picasso.get().load(url).into(imgvw);
        return v;
    }
}
