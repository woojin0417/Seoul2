package onetwopunch.seoulinsangshot.com.seoulinsangshot.Controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Image;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.R;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.View.Best_ReplyActivity;

/**
 * Created by user on 2017-10-03.
 */

public class CoverFlowAdapter extends BaseAdapter {
    List<Model_Image> imageList;
    private Context mContext;

    public CoverFlowAdapter(Context context, List<Model_Image> imageList) {
        this.mContext = context;
        this.imageList = imageList;
    }

    @Override
    public int getCount() {

        return imageList.size();
    }

    @Override
    public Object getItem(int pos) {
        return imageList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_coverflow, null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) rowView.findViewById(R.id.image);
            viewHolder.text = (TextView) rowView.findViewById(R.id.label);
            viewHolder.frameLayout=(FrameLayout)rowView.findViewById(R.id.coverFrameLayout);
            viewHolder.featureCoverFlow=(FeatureCoverFlow)rowView.findViewById(R.id.coverflow);
            Picasso.with(mContext).load(imageList.get(position).getUrl()).into(viewHolder.image);

            rowView.setTag(viewHolder);

            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, Best_ReplyActivity.class);
                    intent.putExtra("email",imageList.get(position).getId());
                    intent.putExtra("image",imageList.get(position).getUrl());
                    intent.putExtra("tip",imageList.get(position).getTip());
                    intent.putExtra("phoneApp",imageList.get(position).getPhoneApp());
                    intent.putExtra("phoneType",imageList.get(position).getPhoneType());
                    setIntentFlag(intent);
                    mContext.startActivity(intent);
                }
            });
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.image.setImageResource(imageList.get(position).getUrl().charAt(0));
        Picasso.with(mContext).load(imageList.get(position).getUrl()).into(holder.image);

        return rowView;
    }
    public static class ViewHolder {
        public ImageView image;
        public TextView text;
        public ImageView button;
        public FrameLayout frameLayout;
        public FeatureCoverFlow featureCoverFlow;
    }
    public void setIntentFlag(Intent intent){
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

}
