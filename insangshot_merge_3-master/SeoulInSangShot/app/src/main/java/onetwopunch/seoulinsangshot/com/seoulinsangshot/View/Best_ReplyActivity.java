package onetwopunch.seoulinsangshot.com.seoulinsangshot.View;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Data.LikeCountVO;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Remote.RetrofitClient;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Remote.RetrofitService;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_LikeCount;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Best_ReplyActivity extends AppCompatActivity {

    ImageView likeIv;
    ImageView dislikeIv;
    TextView likeTv;
    Animation open, close;

    LikeCountVO likerepo;
    ArrayList<Model_LikeCount> likeTempList;
    public static ArrayList<Model_LikeCount> likeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best__reply);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#050518")));

        Drawable d = getResources().getDrawable(R.drawable.actionbar);
        getSupportActionBar().setBackgroundDrawable(d);
        Intent intent = getIntent();

        final String url=intent.getStringExtra("url");

        likeIv = (ImageView)findViewById(R.id.like);
        dislikeIv = (ImageView)findViewById(R.id.dislike);
        likeTv=(TextView)findViewById(R.id.best_reply_likeText);


        open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);


        dislikeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dislikeIv.startAnimation(close);
                //dislikeIv.setVisibility(View.INVISIBLE);
                dislikeIv.setClickable(false);
                likeIv.setClickable(true);
                likeIv.startAnimation(open);
                likeIv.setVisibility(View.VISIBLE);
                setLikeData(url,"joker1649");

                Log.v("좋아요", "누르기");
            }
        });
        likeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeIv.startAnimation(close);
                likeIv.setClickable(false);
                dislikeIv.setClickable(true);
                dislikeIv.startAnimation(open);
                dislikeIv.setVisibility(View.VISIBLE);
                setDislikeData(url,"joker1649");
                Log.v("좋아요", "취소");
            }
        });

        TextView best_email=(TextView)findViewById(R.id.txt_best_name);
        TextView best_tip=(TextView)findViewById(R.id.txt_best_tip);
        TextView best_phone=(TextView)findViewById(R.id.txt_best_theme);
        TextView best_app=(TextView)findViewById(R.id.txt_best_theme2);
        ImageView best_img=(ImageView)findViewById(R.id.img_best_cover);

        Picasso.with(this).load(intent.getStringExtra("image")).into(best_img);

        best_email.setText(intent.getStringExtra("email"));
        best_tip.setText(intent.getStringExtra("tip"));
        best_phone.setText(intent.getStringExtra("phoneType"));
        best_app.setText(intent.getStringExtra("phoneApp"));


        loadLikeData(url);


    }


    public void setDislikeData(String getUrl,String getId)
    {
        final String url=getUrl;
        String id=getId;

        AndroidNetworking.post("http://13.124.87.34:3000/dellike")
                .addBodyParameter("url", url)
                .addBodyParameter("id", id)
                .addHeaders("Content-Type", "multipart/form-data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadLikeData(url);
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });

    }

    public void setLikeData(String getUrl,String getId)
    {
        final String url=getUrl;
        String id=getId;

        AndroidNetworking.post("http://13.124.87.34:3000/plike")
                .addBodyParameter("url", url)
                .addBodyParameter("id", id)
                .addHeaders("Content-Type", "multipart/form-data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadLikeData(url);
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });

    }
    public void loadLikeData(final String getUrl) {
        likeList=new ArrayList<Model_LikeCount>();
        RetrofitService retrofitService = RetrofitClient.retrofit.create(RetrofitService.class);
        Call<LikeCountVO> call = retrofitService.getLikeData();
        call.enqueue(new Callback<LikeCountVO>() {
            @Override
            public void onResponse(Call<LikeCountVO> call, Response<LikeCountVO> response) {
                likerepo = response.body();
                likeTempList = likerepo.getList();
                for (int i = 0; i < likeTempList.size(); i++) {
                    if (likeTempList.get(i).getUrl().equals(getUrl)) {
                        String url = likeTempList.get(i).getUrl();
                        String id = likeTempList.get(i).getId();
                        if(likeTempList.get(i).getId().equals("joker1649")) //★☆★☆★☆로그인 정보를 파라미터로!!!!!!!//★☆★☆★☆//★☆★☆★☆
                        {
                            Log.d("씨발놈들아","여긴됬따");
                            // dislikeIv.setImageResource(R.drawable.likeicon);
                            dislikeIv.setVisibility(View.INVISIBLE);
                            dislikeIv.setClickable(false);
                            likeIv.setVisibility(View.VISIBLE);
                            likeIv.setClickable(true);
                        }
                        likeList.add(new Model_LikeCount(url, id)); //★☆★☆★☆여기서 생성자에 보낼 변수(like count)추가하기
                    }
                }
                for(int i=0; i<likeList.size();i++)
                {
                    if(likeList.get(i).getUrl().equals(getUrl))
                    {
                        likeTv.setText(likeList.size()+" Likes");//★☆★☆★☆lkeecount값 출력
                    }
                }

            }

            @Override
            public void onFailure(Call<LikeCountVO> call, Throwable t) {

            }
        });
    }
}
