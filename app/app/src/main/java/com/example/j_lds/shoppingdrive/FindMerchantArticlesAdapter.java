package com.example.j_lds.shoppingdrive;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FindMerchantArticlesAdapter extends BaseAdapter{

    private Context context;
    private final int[] images;
    private final String[] names;
    private final double[] prices;
    View view;

    FindMerchantArticlesAdapter(Context context, int[] images, String[] names, double[] prices) {
        this.context = context;
        this.images = images;
        this.names = names;
        this.prices = prices;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null){
            view = new View(context);
            view = layoutInflater.inflate(R.layout.custom_find_merchant_articles, null);
            ImageView article_image = (ImageView) view.findViewById(R.id.article_list_image);
            TextView article_name = (TextView) view.findViewById(R.id.article_list_name);
            TextView article_price = (TextView) view.findViewById(R.id.article_list_price);

            article_image.setImageResource(images[position]);
            article_name.setText(names[position]);
            article_price.setText(prices[position]+" €");

        }
        return view;
    }

}
























