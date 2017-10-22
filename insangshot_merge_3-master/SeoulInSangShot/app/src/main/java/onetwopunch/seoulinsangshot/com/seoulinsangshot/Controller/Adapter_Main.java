package onetwopunch.seoulinsangshot.com.seoulinsangshot.Controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Main;

/**
 * Created by 301 on 2017-10-20.
 */

public class Adapter_Main extends BaseAdapter {
    ArrayList<Model_Main> tempArr;
    Context adapterContext;

    public Adapter_Main(ArrayList<Model_Main> tempArr, Context adapterContext) {
        this.tempArr = tempArr;
        this.adapterContext = adapterContext;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    //온바인더 역할
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
