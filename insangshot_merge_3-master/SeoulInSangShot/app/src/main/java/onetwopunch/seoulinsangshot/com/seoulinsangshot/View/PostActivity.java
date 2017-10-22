package onetwopunch.seoulinsangshot.com.seoulinsangshot.View;

import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import onetwopunch.seoulinsangshot.com.seoulinsangshot.Controller.Constants;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Image;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.R;

import static onetwopunch.seoulinsangshot.com.seoulinsangshot.R.array.season;

public class PostActivity extends AppCompatActivity {

    Intent home;
    Intent externalAlbum;

    ImageView iv_select_pic;
    EditText edt_content;
    EditText edt_device;
    Spinner spn_device;
    EditText edt_filter;
    Spinner spn_season;
    Spinner spn_simple_time;
    Button btn_upload;

    String path;

    static String seasonTemp;
    static String simpleTimeTemp;

    List<Integer> idList; // 랜드함수 중복 방지를 위해 임시로 만든 리스트 -> 데이터베이스에 칼럼 추가하거나 url에서 따와야 할듯


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#050518")));

        Drawable d = getResources().getDrawable(R.drawable.actionbar);
        getSupportActionBar().setBackgroundDrawable(d);

        home = new Intent(this, MainActivity.class);

        idList = new ArrayList<>();

        iv_select_pic = (ImageView) findViewById(R.id.iv_select_pic);
        iv_select_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                externalAlbum = new Intent(Intent.ACTION_PICK);
                externalAlbum.setType("image/jpeg");
                try{
                    startActivityForResult(externalAlbum, 100);
                }catch (Exception e){

                }
            }
        });

        edt_content = (EditText) findViewById(R.id.edt_content);

        edt_device = (EditText) findViewById(R.id.edt_device);

        spn_device = (Spinner) findViewById(R.id.spn_device);
        edt_device.setText(String.valueOf(spn_device.getItemAtPosition(0)));
        String[] strArr1 = getResources().getStringArray(R.array.device_type);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, strArr1);
        spn_device.setAdapter(adapter1);
        spn_device.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) adapterView.getChildAt(0)).setTextSize(18);
                if(!String.valueOf(spn_device.getItemAtPosition(i)).equals("기타")){
                    edt_device.setText(String.valueOf(spn_device.getItemAtPosition(i)));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edt_filter = (EditText) findViewById(R.id.edt_filter);

        spn_season = (Spinner) findViewById(R.id.spn_season);
        String[] strArr2 = getResources().getStringArray(season);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, strArr2);
        spn_season.setAdapter(adapter2);
        spn_season.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) adapterView.getChildAt(0)).setTextSize(18);
                seasonTemp = String.valueOf(spn_season.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spn_simple_time = (Spinner) findViewById(R.id.spn_simple_time);
        String[] strArr3 = getResources().getStringArray(R.array.simpleTime);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, strArr3);
        spn_simple_time.setAdapter(adapter3);
        spn_simple_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) adapterView.getChildAt(0)).setTextSize(18);
                simpleTimeTemp = String.valueOf(spn_simple_time.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_upload = (Button) findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                java.util.Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                final String formatDate = sdfNow.format(cal.getTime());
                final String id = "KwakGee"; // 회원가입 구현 후 구현
                final String area = "YS-1"; // Fragment_Best에서 Intent 형식으로 받아와 사용. -> 플로팅 버튼 구현 후 구현
                final String content = edt_content.getText().toString();
                final String photoName = String.valueOf(createRandomId());
                final String deviceType = edt_device.getText().toString();
                final String filter = edt_filter.getText().toString();
                final String season = seasonTemp;
                final String simpleTime = simpleTimeTemp;

                AlertDialog.Builder ad = new AlertDialog.Builder(PostActivity.this);
                ad.setMessage("내용 : " + content + "\n" + "기종 : " + deviceType + "\n" + "필터 : " + filter + "\n"
                        + "계절 : " + season + "\n" + "시간대 : " + simpleTime + "\n" + "확실합니까?");
                ad.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        iv_select_pic.buildDrawingCache();
                        Bitmap bitmap = iv_select_pic.getDrawingCache();
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,50,out);
                        byte[] b = out.toByteArray();
                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                        AndroidNetworking.post(Constants.TEST_BASE_URL + "tupload")
                                .addBodyParameter("base64", encodedImage)
                                .addBodyParameter("pic_id", id)
                                .addBodyParameter("area", area)
                                .addBodyParameter("photoName", photoName)
                                .addBodyParameter("phoneType", deviceType)
                                .addBodyParameter("phoneApp", filter)
                                .addBodyParameter("season", season)
                                .addBodyParameter("time", simpleTime)
                                .addBodyParameter("tip", content)
                                .addBodyParameter("nowdate", formatDate)
                                .addHeaders("Content-Type", "multipart/form-data")
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // do anything with response

                                        Toast.makeText(PostActivity.this, "게시글 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                        BaseActivity.imageList.add(new Model_Image(Constants.req_URL+"user/"+photoName+".png", id, content, formatDate,area,filter,deviceType, simpleTime));
                                        finish();
                                    }
                                    @Override
                                    public void onError(ANError error) {
                                        // handle error
                                        Toast.makeText(PostActivity.this, "서버와 연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                });
                ad.setNegativeButton("아니요", null);
                ad.show();

            }
        });


    }

    public int createRandomId(){

        boolean overlap = false;
        int randNumber = 0;

        while(true){
            overlap = false;
            randNumber = (int)((Math.random() * (99999999 - 10000000 + 1)) + 10000000);
            for(int i=0; i<idList.size(); i++){
                if(randNumber == idList.get(i)){
                    overlap = true;
                }
            }
            if(!overlap){
                idList.add(randNumber);
                break;
            }
        }
        return randNumber;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null)
            return;
        switch (requestCode){
            case 100:
                if(resultCode == RESULT_OK){
                    path = getPathFromURI(data.getData());
                    ExifInterface exif = null; // 사진앨범 읽기 권한 ???
                                                // 아니면 파일을 찾을 수 없음
                                                // java.io.FileNotFoundException: /storage/emulated/0/Pictures/1508140674163.jpg (Permission denied)
                    try{
                        exif = new ExifInterface(path);
                    }catch (IOException e){
                        Log.v("this", e.toString());
                    }

                    int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int exifDegree = exifOrientationToDegrees(exifOrientation);

                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    iv_select_pic.setImageBitmap(rotate(bitmap, exifDegree));

//                    iv_select_pic.setImageURI(data.getData());
                }
        }
    }

    private String getPathFromURI(Uri contentUri){
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public int exifOrientationToDegrees(int exifOrientation){
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90){
            return 90;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180){
            return 180;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270){
            return 270;
        }return 0;
    }

    public Bitmap rotate(Bitmap bitmap, float degrees){

        Matrix matrix = new Matrix();

        matrix.postRotate(degrees);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_primary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setIntentFlag(home);
        startActivity(home);

        return super.onOptionsItemSelected(item);
    }

    public void setIntentFlag(Intent intent){
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

}
