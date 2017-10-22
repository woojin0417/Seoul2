package onetwopunch.seoulinsangshot.com.seoulinsangshot.View;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;

import onetwopunch.seoulinsangshot.com.seoulinsangshot.Controller.Adapter_Notify;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Controller.BottomNavigationViewHelper;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Notify;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.R;

public class NotifyActivity extends AppCompatActivity {

    Intent home;
    Intent primary;
    Intent album;


    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setIntentFlag(home);
                    startActivity(home);
                    return true;
                case R.id.navigation_search:
                    setIntentFlag(primary);
                    startActivity(primary);
                    return true;
                case R.id.navigation_album:
                    setIntentFlag(album);
                    startActivity(album);
                    return true;
                case R.id.navigation_login:
                    CustomDialog customDialog=new CustomDialog();
                    FragmentManager fm = getSupportFragmentManager();
                    customDialog.show(fm, "Login Dialog");
                    return true;
                case R.id.navigation_notifications:
                    return true;

            }
            return false;
        }

    };


    private RecyclerView recyclerView;
    ArrayList<Model_Notify> notifyArrayList;
    Adapter_Notify adapter_notify;
    LinearLayoutManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#050518")));

        Drawable d = getResources().getDrawable(R.drawable.actionbar);
        getSupportActionBar().setBackgroundDrawable(d);
        notifyArrayList= new ArrayList<>();

        recyclerView=(RecyclerView) findViewById(R.id.rv_notify);
        manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        adapter_notify= new Adapter_Notify(BaseActivity.notifyList,getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter_notify);
        recyclerView.smoothScrollToPosition(0);


        home = new Intent(getApplicationContext(), MainActivity.class);
        primary = new Intent(getApplicationContext(), PrimaryActivity.class);
        album = new Intent(getApplicationContext(), AlbumActivity.class);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_notifications);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    @Override
    protected void onResume() {
        super.onResume();

        navigation.setSelectedItemId(R.id.navigation_notifications);

    }

    public void setIntentFlag(Intent intent){
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }
}
