package com.philong.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.philong.karaoke.MainActivity;
import com.philong.karaoke.R;
import com.philong.model.BaiHat;

import java.util.List;

/**
 * Created by Long on 08/06/2016.
 */
public class BHAdapter extends ArrayAdapter<BaiHat>{

    private Activity context;
    private int resource;
    private List<BaiHat> objects;

    public BHAdapter(Activity context, int resource, List<BaiHat> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    private class ViewHolder{
        private TextView txtMaBH, txtTenBH, txtTacGia;
        private ImageView imgYeuThich;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null){
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(resource, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.txtMaBH = (TextView)row.findViewById(R.id.txtMaBH);
            viewHolder.txtTenBH = (TextView)row.findViewById(R.id.txtTenBH);
            viewHolder.txtTacGia = (TextView)row.findViewById(R.id.txtTacGia);
            viewHolder.imgYeuThich = (ImageView)row.findViewById(R.id.imgYeuThich);
            row.setTag(viewHolder);
        }
            final ViewHolder viewHolder= (ViewHolder) row.getTag();
            final BaiHat bh = objects.get(position);
            viewHolder.txtMaBH.setText(String.valueOf(bh.getMaBH()));
            viewHolder.txtTenBH.setText(bh.getTenBH());
            viewHolder.txtTacGia.setText(bh.getTacGia());
            if(bh.getYeuThich() == 1){
                viewHolder.imgYeuThich.setImageResource(R.drawable.ic_star_24dp);
            }else if(bh.getYeuThich() == 0){
                viewHolder.imgYeuThich.setImageResource(R.drawable.ic_star_outline_24dp);
            }
            viewHolder.imgYeuThich.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(bh.getYeuThich() == 1){
                       viewHolder.imgYeuThich.setImageResource(R.drawable.ic_star_outline_24dp);
                       bh.setYeuThich(0);
                   }else if(bh.getYeuThich() == 0){
                       viewHolder.imgYeuThich.setImageResource(R.drawable.ic_star_24dp);
                       bh.setYeuThich(1);
                   }
                    ContentValues values = new ContentValues();
                    values.put("YEUTHICH", String.valueOf(bh.getYeuThich()));
                    MainActivity.database.update("ArirangSongList", values, "MABH=?", new String[]{String.valueOf(bh.getMaBH())});

                }
            });
        return row;
    }


}


