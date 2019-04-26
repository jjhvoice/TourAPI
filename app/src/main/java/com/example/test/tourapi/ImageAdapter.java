package com.example.test.tourapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private ArrayList<ListViewItem> items = new ArrayList<>();

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ListViewItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_list_view_item, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageview);

        ListViewItem item = getItem(position);

        imageView.setImageBitmap(item.getBitmap());

        return convertView;
    }

    public void addItem(Bitmap bitmap) {
        ListViewItem item = new ListViewItem();

        item.setBitmap(bitmap);

        items.add(item);
    }
}
