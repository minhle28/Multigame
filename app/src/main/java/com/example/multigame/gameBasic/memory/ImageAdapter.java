package com.example.multigame.gameBasic.memory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.Random;

import com.example.multigame.R;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    int count = 0;
    boolean check = false;
    private Integer[] androidPhotos;

    private int sizeOfSolved;

    public ImageAdapter(Context context, boolean shuffle) {
        this.context = context;

        if (check == false) {
            androidPhotos = new Integer[]{R.drawable.kirito,
                    R.drawable.asuna,
                    R.drawable.suguha,
                    R.drawable.bercouli,
                    R.drawable.itsuki,
                    R.drawable.yuna,
                    R.drawable.yuuki,
                    R.drawable.eldrie,
                    R.drawable.eugo,
                    R.drawable.reniy,
                    R.drawable.kirito,
                    R.drawable.asuna,
                    R.drawable.suguha,
                    R.drawable.bercouli,
                    R.drawable.itsuki,
                    R.drawable.yuna,
                    R.drawable.yuuki,
                    R.drawable.eldrie,
                    R.drawable.eugo,
                    R.drawable.reniy,};
        }
        if (shuffle) {
            shuffleArray();
        }
    }

    @Override
    public int getCount() {
        return androidPhotos.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView card;
        if (convertView == null) {
            card = new ImageView(context);
            card.setLayoutParams(new GridView.LayoutParams(85, 85));
            card.setScaleType(ImageView.ScaleType.CENTER_CROP);

        } else {
            card = (ImageView) convertView;
        }
        if (check) {
            if (position < sizeOfSolved) {
                card.setImageResource(androidPhotos[position]);
                card.setTag(R.drawable.checkmark);
            } else {
                card.setImageResource(R.drawable.placeholder);
                card.setTag(R.drawable.placeholder);
            }
        } else {
            card.setImageResource(R.drawable.placeholder);
            card.setTag(R.drawable.placeholder);
        }
        ViewGroup.LayoutParams imagelayout = card.getLayoutParams();
        imagelayout.width = 220;
        imagelayout.height = 220;
        card.setLayoutParams(imagelayout);
        return card;
    }

    public void shuffleArray() {
        Random random = new Random();
        for (int i = androidPhotos.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int a = androidPhotos[index];
            androidPhotos[index] = androidPhotos[i];
            androidPhotos[i] = a;
        }
    }

    public Integer[] getArray() {
        return androidPhotos;
    }

    public void setArray(Integer[] newArray) {
        androidPhotos = newArray;
    }

    public void updateAdapter(Integer[] newArray, int newSize) {
        androidPhotos = null;
        androidPhotos = newArray;
        check = true;
        sizeOfSolved = newSize;
        notifyDataSetChanged();
    }
}
