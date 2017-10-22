/*
package onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.util.ArrayList;

import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Data.ViewCountVO;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Remote.RetrofitClient;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Remote.RetrofitService;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_ViewCount;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.View.DetailActivity;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.View.Fragment.Fragment_Detail_Info;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

*/
/**
 * Created by Beom2 on 2017-10-07.
 *//*


public class View_DataManager {

    private ViewCountVO repo;
    private ArrayList<Model_ViewCount> tempList;
    private ArrayList<Model_ViewCount> viewList;

    public void setData(String getid,String getUrl,String getArea)
    {
        final String id="joker1649";
        final String url=getUrl;
        String area=getArea;

        AndroidNetworking.post("http://13.124.87.34:3000/pview")
                .addBodyParameter("id",id)
                .addBodyParameter("url",url)
                .addBodyParameter("area",area)
                .addHeaders("Content-Type", "multipart/form-data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        */
/*DetailActivity.viewList.add(new Model_ViewCount(init,id,text,date));
                        adapter_comment = new Adapter_Comment(getContext(), commentList);
                        rv_comment.setLayoutManager(manager);
                        rv_comment.setAdapter(adapter_comment);*//*

                        loadData(url);
                    }
                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }


    public void loadData(final String url) {
        RetrofitService retrofitService = RetrofitClient.retrofit.create(RetrofitService.class);
        Call<ViewCountVO> call = retrofitService.getViewData();
        call.enqueue(new Callback<ViewCountVO>() {
            @Override
            public void onResponse(Call<ViewCountVO> call, Response<ViewCountVO> response) {
                repo = response.body();
                tempList = repo.getList();
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getUrl().equals(url)) {
                        String tempUrl = tempList.get(i).getUrl();
                        String hit= tempList.get(i).getHit();
                        if(tempUrl.equals(url)) {
                            viewList.add(new Model_ViewCount(tempUrl, hit));
                        }
                    }

                }
                for(int i=0; i<viewList.size();i++) {
                    if(viewList.get(i).getUrl().equals(url)) {
                        hitsTv.setText("Hits: " + viewList.get(i).getHit());
                    }
                }
            }

            @Override
            public void onFailure(Call<ViewCountVO> call, Throwable t) {

            }
        });

    }
}
*/
