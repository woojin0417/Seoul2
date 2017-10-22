package onetwopunch.seoulinsangshot.com.seoulinsangshot.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONObject;

import java.util.Arrays;

import onetwopunch.seoulinsangshot.com.seoulinsangshot.R;

import static com.facebook.FacebookSdk.getApplicationContext;
import static onetwopunch.seoulinsangshot.com.seoulinsangshot.View.BaseActivity.email;
import static onetwopunch.seoulinsangshot.com.seoulinsangshot.View.BaseActivity.isLogined;
import static onetwopunch.seoulinsangshot.com.seoulinsangshot.View.BaseActivity.name;


/**
 * Created by MG_PARK on 2017-10-06.
 */

public class CustomDialog extends DialogFragment implements GoogleApiClient.OnConnectionFailedListener{


    private Button btn_cancel;
    private Button btn_complete;
    private Button btn_sign_out;
    private SignInButton btn_sign_in;
    private LinearLayout sign_out_layout;
    private TextView txt_dialog_comment;

    private LoginButton btn_facebook;
    private CallbackManager callback;

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;


    public interface OnCompleteListener{
        void onInputedData(String email, String name);
    }

    private OnCompleteListener mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnCompleteListener) activity;
        }
        catch (ClassCastException e) {
            Log.d("DialogFragmentExample", "Activity doesn't implement the OnCompleteListener interface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.CYAN));
        View view = inflater.inflate(R.layout.login_dialog, container);

        txt_dialog_comment=(TextView)view.findViewById(R.id.txt_dialog_comment);

        Toast.makeText(getApplicationContext(),"<email,name : "+ email+","+name+","+isLogined,Toast.LENGTH_LONG).show();
        btn_sign_out=(Button)view.findViewById(R.id.sign_out_button);
        sign_out_layout=(LinearLayout)view.findViewById(R.id.sign_out_and_disconnect);

        btn_cancel=(Button)view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                dismiss();
            }
        });

        btn_complete=(Button)view.findViewById(R.id.btn_complete);
        btn_complete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                if(isLogined==0){
                    dismiss();

                }else{
                    email=null;
                    name=null;
                    isLogined=0;
                    dismiss();
                }



            }
        });

        callback = CallbackManager.Factory.create();

        btn_facebook = (LoginButton) view.findViewById(R.id.btn_facebook);
        btn_facebook.setReadPermissions(Arrays.asList("public_profile", "email"));
        btn_facebook.setFragment(this);
        btn_facebook.registerCallback(callback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("result",object.toString());

                        try {
                            email = object.getString("email");       // 이메일
                            name = object.getString("name");         // 이름

                            Log.d("TAG","페이스북 이메일 -> " + email);
                            Log.d("TAG","페이스북 이름 -> " + name);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
//
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
                isLogined=2;
                dismiss();

            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity().getApplicationContext(), "User sign in canceled!", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Log.e("LoginErr",error.toString());
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        if(mGoogleApiClient == null || !mGoogleApiClient.isConnected()){
            try {
                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        btn_sign_in = (SignInButton) view.findViewById(R.id.sign_in_button);
        btn_sign_in.setSize(SignInButton.SIZE_STANDARD);
        btn_sign_in.setScopes(gso.getScopeArray());
        btn_sign_in.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

        btn_sign_out.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (mGoogleApiClient != null) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    // [START_EXCLUDE]
                                    email = null;
                                    name = null;
                                    isLogined = 0;
                                    updateUI(false);
                                    // [END_EXCLUDE]
                                }
                            });

                }
                dismiss();
            }
        });

        if(isLogined!=0){
            txt_dialog_comment.setText("로그아웃 하시겠습니까?");
            if(isLogined==1){
                btn_facebook.setVisibility(View.INVISIBLE);
                btn_sign_in.setVisibility(View.GONE);
                sign_out_layout.setVisibility(View.VISIBLE);

            }else if(isLogined==2){
                btn_sign_in.setVisibility(View.INVISIBLE);
            }
        }



        return view;
    }

    @Override
    public void onResume(){
        super.onResume();



    }

    @Override
    public void onStart() {
        super.onStart();


        if(mGoogleApiClient!=null) {

            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                // and the GoogleSignInResult will be available instantly.
                Log.d(TAG, "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                // If the user has not previously signed in on this device or the sign-in has expired,
                // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                // single sign-on will occur in this branch.
                showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(GoogleSignInResult googleSignInResult) {
                        hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
            }
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        callback.onActivityResult(requestCode, resultCode, data);
    }

    public void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();
            email=acct.getEmail();
            name=acct.getDisplayName();

            isLogined=1;
            dismiss();

            updateUI(true);

        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);

        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            btn_sign_in.setVisibility(View.GONE);
            sign_out_layout.setVisibility(View.VISIBLE);

        } else {

            btn_sign_in.setVisibility(View.VISIBLE);
            sign_out_layout.setVisibility(View.GONE);
        }
    }

}
