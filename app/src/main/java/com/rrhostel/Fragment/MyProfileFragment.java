package com.rrhostel.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rrhostel.Activity.HomeActivity;
import com.rrhostel.Activity.LoginActivity;
import com.rrhostel.Bean.LoginResponce;
import com.rrhostel.R;
import com.rrhostel.Utility.AppController;
import com.rrhostel.Utility.Constant;
import com.rrhostel.Utility.UIUtils;
import com.rrhostel.Utility.UserUtils;
import com.rrhostel.Utility.Utils;
import com.rrhostel.Utility.ValidatorUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyProfileFragment extends Fragment {


    private View mMainView;
    private Context mContext;
    private boolean mIsRequestInProgress;
    private boolean mProgressBarShowing = false;
    private RelativeLayout mProgressBarLayout;

    private AutoCompleteTextView mEtName, mEtEmail, mEtAddress, mEtAge, mEtPhoneNo;
    private AutoCompleteTextView name_emergency, relationship, mobile_no_emergency, email_emergency;
    private Button btn_update, btn_update_emergency;

    //DatabaseReference databaseReference;
    // String Database_Path = "All_UserName_Database";

    // public FirebaseAuth firebaseAuth;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;

        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.fragment_my_profile, container, false);

        ((HomeActivity) mContext).setTitle("My profile");

        init();
        return mMainView;
    }

    private void init() {

        //databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        // firebaseAuth = FirebaseAuth.getInstance();


        mProgressBarLayout = mMainView.findViewById(R.id.rl_progressBar);
        mEtPhoneNo = mMainView.findViewById(R.id.mobile_no);
        mEtName = mMainView.findViewById(R.id.name);
        mEtAddress = mMainView.findViewById(R.id.address);
        mEtAge = mMainView.findViewById(R.id.age);
        mEtEmail = mMainView.findViewById(R.id.email);
        name_emergency = mMainView.findViewById(R.id.name_emergency);
        relationship = mMainView.findViewById(R.id.relationship);
        mobile_no_emergency = mMainView.findViewById(R.id.mobile_no_emergency);
        email_emergency = mMainView.findViewById(R.id.email_emergency);
        btn_update = mMainView.findViewById(R.id.btn_update);
        btn_update_emergency = mMainView.findViewById(R.id.btn_update_emergency);

        LoginResponce loginResponce = UserUtils.getInstance().getUserInfo(mContext);

        if (loginResponce.getFullName() != null && loginResponce.getEmail() != null && loginResponce.getAge() != null && loginResponce.getAddress() != null && loginResponce.getPhone() != null) {


            mEtPhoneNo.setText(loginResponce.getPhone());
            mEtName.setText(loginResponce.getFullName());
            mEtAddress.setText(loginResponce.getAddress());
            mEtAge.setText(loginResponce.getAge());
            mEtEmail.setText(loginResponce.getEmail());
            mEtEmail.setEnabled(false);
        }


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setValidation();
            }
        });
        btn_update_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setValidationForRelatives();
            }
        });

    }

    private void setValidationForRelatives() {

        UIUtils.hideKeyBoard(getActivity());
        if (ValidatorUtils.NotEmptyValidator(mContext, name_emergency, true, getString(R.string.UserRelativeName))
                && ValidatorUtils.NotEmptyValidator(mContext, relationship, true, getString(R.string.UserRelativeRelationShip))
                && ValidatorUtils.NotEmptyValidator(mContext, email_emergency, true, getString(R.string.LoginUserNameTxt))
                && ValidatorUtils.NotEmptyValidator(mContext, email_emergency, true, getString(R.string.RegisterInvalidEmail))
                && ValidatorUtils.EmailValidator(mContext, mobile_no_emergency, true, getString(R.string.UserRelativePhoneNo))
                && ValidatorUtils.MinimumLengthValidator(mContext, mobile_no_emergency, 10, true,
                getString(R.string.RegisterPasswordMinimumLength1))) {



        /*    FirebaseUser user = task.getResult().getUser();

            if (user != null) {
                DatabaseReference current_user = databaseReference.child(user.getUid());
                // DatabaseReference current_user1 = databaseReference1.child(user.getEmail());
                current_user.child("Email").setValue(aStrEmail);
                current_user.child("firebase_user_id").setValue(user.getUid());


            }*/

            startHttpRequestForEmergencyDetailed();


        }

    }

    private void startHttpRequestForEmergencyDetailed() {


        boolean internetAvailable = Utils.isConnectingToInternet(mContext);

        if (internetAvailable) {
            mIsRequestInProgress = true;
            String baseUrl = Constant.API_UPDATE_PROFILE_RELATION;
            showProgressBar();
            StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject root = new JSONObject(response);
                                String strStatus = root.optString("status");
                                String strMsg = root.optString("message");
                                if (strStatus.equals("Failed")) {

                                    hideProgressBar();
                                    new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                            .setTitle(getString(R.string.app_name))
                                            .setMessage(strMsg)
                                            .setCancelable(false)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Whatever...
                                                    dialog.dismiss();
                                                }
                                            }).show();


                                } else {
                                    Gson gson = new GsonBuilder().create();
                                    JsonParser jsonParser = new JsonParser();
                                    JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                                    LoginResponce loginResponseData = gson.fromJson(jsonResp, LoginResponce.class);
                                    if (loginResponseData != null) {
                                        hideProgressBar();

                                        loginSuccessfully(loginResponseData);

                                    } else {

                                        hideProgressBar();
                                        new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                                .setTitle(getString(R.string.app_name))
                                                .setMessage("Unable to add relatives information.")
                                                .setCancelable(false)
                                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Whatever...
                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                    }
                                }

                            } catch (Exception e) {
                                hideProgressBar();
                                new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                        .setTitle(getString(R.string.app_name))
                                        .setMessage("Unable to add relatives information.")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressBar();
                            if (error instanceof NoConnectionError) {
                                UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
                            } else {
                                hideProgressBar();
                                new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                        .setTitle(getString(R.string.app_name))
                                        .setMessage("Unable to add relatives information.")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Whatever...
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }

                        }
                    }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("relative_name", name_emergency.getText().toString());
                    params.put("relationship", relationship.getText().toString());
                    params.put("relative_phone", mobile_no_emergency.getText().toString());
                    params.put("relative_email", email_emergency.getText().toString());
                    params.put("userId", UserUtils.getInstance().getUserID(mContext));
                    return params;
                }
            };
            mStrRequest.setShouldCache(false);
            mStrRequest.setTag("");
            AppController.getInstance().addToRequestQueue(mStrRequest);
            mStrRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
        }


    }

    private void setValidation() {

        UIUtils.hideKeyBoard(getActivity());
        if (ValidatorUtils.NotEmptyValidator(mContext, mEtName, true, getString(R.string.LoginUserNameTxt1))

                && ValidatorUtils.NotEmptyValidator(mContext, mEtEmail, true, getString(R.string.LoginUserNameWithEmailTxt))
                && ValidatorUtils.EmailValidator(mContext, mEtEmail, true, getString(R.string.RegisterInvalidEmail))
                && ValidatorUtils.NotEmptyValidator(mContext, mEtPhoneNo, true, getString(R.string.UserPhoneNo))
                && ValidatorUtils.MinimumLengthValidator(mContext, mEtPhoneNo, 10, true,
                getString(R.string.RegisterPasswordMinimumLength1))
                && ValidatorUtils.NotEmptyValidator(mContext, mEtAddress, true, getString(R.string.UserAddress))
                && ValidatorUtils.NotEmptyValidator(mContext, mEtAge, true, getString(R.string.UserAge))) {

            startHttpRequestForUpdateProfile();

        }
    }

    private void startHttpRequestForUpdateProfile() {


        boolean internetAvailable = Utils.isConnectingToInternet(mContext);

        if (internetAvailable) {
            mIsRequestInProgress = true;
            String baseUrl = Constant.API_UPDATE_PROFILE;
            showProgressBar();
            StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject root = new JSONObject(response);
                                String strStatus = root.optString("status");
                                String strMsg = root.optString("message");
                                if (strStatus.equals("Failed")) {

                                    hideProgressBar();
                                    new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                            .setTitle(getString(R.string.app_name))
                                            .setMessage(strMsg)
                                            .setCancelable(false)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Whatever...
                                                    dialog.dismiss();
                                                }
                                            }).show();


                                } else {
                                    Gson gson = new GsonBuilder().create();
                                    JsonParser jsonParser = new JsonParser();
                                    JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                                    LoginResponce loginResponseData = gson.fromJson(jsonResp, LoginResponce.class);
                                    if (loginResponseData != null) {
                                        hideProgressBar();

                                        loginSuccessfully(loginResponseData);

                                    } else {

                                        hideProgressBar();
                                        new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                                .setTitle(getString(R.string.app_name))
                                                .setMessage("Unable to update profile.")
                                                .setCancelable(false)
                                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Whatever...
                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                    }
                                }

                            } catch (Exception e) {
                                hideProgressBar();
                                new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                        .setTitle(getString(R.string.app_name))
                                        .setMessage("Unable to update profile.")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressBar();
                            if (error instanceof NoConnectionError) {
                                UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
                            } else {
                                hideProgressBar();
                                new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                        .setTitle(getString(R.string.app_name))
                                        .setMessage("Unable to update profile.")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Whatever...
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }

                        }
                    }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("fullName", mEtName.getText().toString());
                    params.put("phone", mEtPhoneNo.getText().toString());
                    params.put("age", mEtAge.getText().toString());
                    params.put("address", mEtAddress.getText().toString());
                    params.put("userId", UserUtils.getInstance().getUserID(mContext));
                    return params;
                }
            };
            mStrRequest.setShouldCache(false);
            mStrRequest.setTag("");
            AppController.getInstance().addToRequestQueue(mStrRequest);
            mStrRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
        }

    }

    private void loginSuccessfully(LoginResponce aLoginResponseObj) {
        UserUtils.getInstance().setUserLoggedIn(mContext, true);
        UserUtils.getInstance().saveUserInfo(mContext, aLoginResponseObj);
        UserUtils.getInstance().setUserId(mContext, aLoginResponseObj.getUserId());

        new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                .setTitle(getString(R.string.app_name))
                .setMessage("Your profile is updated successfully.")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Whatever...
                        dialog.dismiss();
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                }).show();
    }


    private void hideProgressBar() {
        mIsRequestInProgress = false;
        if (mProgressBarShowing) {
            mProgressBarLayout.setVisibility(View.GONE);
            mProgressBarShowing = false;
        }
    }

    private void showProgressBar() {
        if (!mProgressBarShowing) {
            mProgressBarLayout.setVisibility(View.VISIBLE);
            mProgressBarShowing = true;
        }
    }


}
