package com.example.j_lds.shoppingdrive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyFindMerchantArticlesAdapter extends BaseAdapter{

    Context context;
    private final int[] images;
    private final String[] names;
    private final double[] prices;
    View view;
    LayoutInflater layoutInflater;

    public MyFindMerchantArticlesAdapter(Context context, int[] images, String[] names, double[] prices) {
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
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
























