package uk.org.willmott.mediasyncer.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * The simple adapter used to display the library listing components. It uses picasso to load the
 * pictures for better memory management.
 * Created by tomw on 10/09/2016.
 */
public class SeriesSimpleAdapter extends SimpleAdapter {

    public SeriesSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to){
        super(context, data, resource, from, to);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  super.getView(position, convertView, parent);

        // Get the image view that's inside the first linear layout that's in side the main liniear
        // layout of the list item (have a look at list_item_library.xml).
        ImageView imageView = (ImageView) ((LinearLayout) ((LinearLayout) view).getChildAt(0)).getChildAt(0);
        String url = (String) ((Map) getItem(position)).get("image_url");
        Picasso.with(view.getContext()).load(url).resize(54,80).into(imageView);
        return view;
    }
}
