package onetwopunch.seoulinsangshot.com.seoulinsangshot.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import onetwopunch.seoulinsangshot.com.seoulinsangshot.Controller.BottomNavigationViewHelper;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Area_DataManager;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Test;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.R;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    Button btn_test;

    Intent primary;
    Intent album;
    Intent notify;



    //avf를 위한 부분
    AdapterViewFlipper avf;
    ImageView mainImage;
    TextView hitTv;
    TextView likeTv;
    TextView nameKorean;
    TextView nameEnglish;
    ArrayList<ImageView> indexes = new ArrayList<ImageView>();
    ArrayList<Model_Test> tempList = new ArrayList<Model_Test>();

    //온터치리스너를 위한 처리 실수
    float xAtDown;
    float xAtUp;

    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_search:
                    setIntentFlag(primary);
                    startActivity(primary);
                    return true;
                case R.id.navigation_album:
                    setIntentFlag(album);
                    startActivity(album);
                    return true;
                case R.id.navigation_notifications:
                    setIntentFlag(notify);
                    startActivity(notify);
                    return true;
                case R.id.navigation_login:
                    CustomDialog customDialog=new CustomDialog();
                    FragmentManager fm = getSupportFragmentManager();
                    customDialog.show(fm, "Login Dialog");
                    return true;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#050518")));

        Drawable d = getResources().getDrawable(R.drawable.actionbar);
        getSupportActionBar().setBackgroundDrawable(d);

        primary = new Intent(getApplicationContext(), PrimaryActivity.class);
        album = new Intent(getApplicationContext(), AlbumActivity.class);
        notify = new Intent(getApplicationContext(), NotifyActivity.class);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        btn_test = (Button) findViewById(R.id.btn_test);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), SelectActivity.class));

            }
        });

        Area_DataManager manager=new Area_DataManager();
        manager.loadData();


        //////////////////////////////////////////////////////////////

        ///////////////// AVF를 위한 부분 /////////////////////////////

        /////////////////////////////////////////////////////////////

        indexes.add((ImageView) findViewById(R.id.indexLight1));
        indexes.add((ImageView) findViewById(R.id.indexLight2));
        indexes.add((ImageView) findViewById(R.id.indexLight3));
        indexes.add((ImageView) findViewById(R.id.indexLight4));
        indexes.add((ImageView) findViewById(R.id.indexLight5));

        nameKorean =(TextView)findViewById(R.id.main_areaName) ;
        hitTv=(TextView)findViewById(R.id.main_hitText);
        likeTv=(TextView)findViewById(R.id.main_likeText);


        //아직 메인을 위한 서버가 없으므로
//        tempList.add(BaseActivity.testArr.get(0));
//        tempList.add(BaseActivity.testArr.get(1));
//        tempList.add(BaseActivity.testArr.get(2));
//        tempList.add(BaseActivity.testArr.get(3));
//        tempList.add(BaseActivity.testArr.get(4));

        /*
            우선 for문을 통해서 base에서 로드가 안 됬다면 아예 대표사진도 안 뜨도록 설정함.
         */
        Log.v("Size", String.valueOf(BaseActivity.testArr.size()));
        for(int i=0; i<BaseActivity.testArr.size(); i++){
            tempList.add(BaseActivity.testArr.get(i));
            if(i == 5){
                i = BaseActivity.testArr.size();
            }
        }


        //어뎁터 연결 ,널포인터 뜰 수 있음.
        avf = (AdapterViewFlipper) findViewById(R.id.main_AVF);
        avf.setAdapter(new galleryAdapter(this,tempList)); //갤러리 어뎁터에 연결
        avf.setOnTouchListener(this);//터치리스너에 연결

        //onCreate 종료
    }


    //터치로 이미지 넘기는 메서드 onTouch를 오버라이딩 해야함.
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v != avf) return false;
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            xAtDown = event.getX();
        }
        //else if 부분이 중요함.
        else if (event.getAction() == MotionEvent.ACTION_UP)
        {
            xAtUp = event.getX();
            if (xAtDown > xAtUp)
            {
                avf.setInAnimation(v.getContext(), R.animator.right_in);
                avf.setOutAnimation(v.getContext(), R.animator.left_out);
                avf.showNext();
            }
            else if (xAtDown < xAtUp)
            {
                avf.setInAnimation(v.getContext(), R.animator.left_in);
                avf.setOutAnimation(v.getContext(), R.animator.right_out);
                avf.showPrevious();
                //다시 오른쪽->왼쪽으로 슬라이드 되도록 원상복구 시켜놓음.
                avf.setInAnimation(v.getContext(), R.animator.right_in);
                avf.setOutAnimation(v.getContext(), R.animator.left_out);
            }
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

        navigation.setSelectedItemId(R.id.navigation_home);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_primary, menu);
        return true;
    }

    public void setIntentFlag(Intent intent){
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    ///////////////////////////////////////

    public class galleryAdapter extends BaseAdapter {
        private final Context mContext;
        LayoutInflater inflater;
        ArrayList<Model_Test>tempArr;


        public galleryAdapter(Context mContext,ArrayList<Model_Test> tempArr) {
            this.mContext = mContext;
            this.tempArr=tempArr;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return tempList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View converView, ViewGroup parent) {

            if (converView == null) {
                converView = inflater.inflate(R.layout.item_main, parent, false);
            }

            ImageView mimageView = (ImageView)converView.findViewById(R.id.mainImage);
            Picasso.with(mContext).load(tempArr.get(position).getUrl()).into(mimageView);
            ;


            for(int i=0;i<tempArr.size();i++)
            {
                String areaName = tempArr.get(i).getName();
                // baseActivity에서는 좋아요수나 조회수를 안 읽어옴
                // Model_Test에는 두 데이터가 없음.
                //String hits=tempArr.get(i).getHit();
                //String likes=tempArr.get(i).getLike();
                if(i==position){
                   nameKorean.setText(areaName);
                }
            }
            //사진이 전환되면서 버튼 위 indexLight도 변하게끔 하는 부분.
            for (int i = 0; i < indexes.size(); i++) {
                ImageView index = indexes.get(i);
                if (i == position) {
                    index.setImageResource(R.drawable.indexafter);
                } else {
                    index.setImageResource(R.drawable.indexbefore);
                }
            }
            return converView;
        }

    }

}
