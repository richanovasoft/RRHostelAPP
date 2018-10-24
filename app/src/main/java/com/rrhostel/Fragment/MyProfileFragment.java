package com.rrhostel.Fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
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
import com.rrhostel.Bean.StatusBean;
import com.rrhostel.R;
import com.rrhostel.Utility.AppController;
import com.rrhostel.Utility.AppHelper;
import com.rrhostel.Utility.Constant;
import com.rrhostel.Utility.FilePath;
import com.rrhostel.Utility.ImageUtils;
import com.rrhostel.Utility.UIUtils;
import com.rrhostel.Utility.UserUtils;
import com.rrhostel.Utility.Utils;
import com.rrhostel.Utility.ValidatorUtils;
import com.rrhostel.Utility.VolleyMultipartRequest;
import com.rrhostel.custom.CustomRegularTextView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class MyProfileFragment extends Fragment {


    private View mMainView;
    private Context mContext;
    private boolean mIsRequestInProgress;
    private boolean mProgressBarShowing = false;
    private RelativeLayout mProgressBarLayout;

    private AutoCompleteTextView mEtName, mEtEmail, mEtAddress, mEtAge, mEtPhoneNo;
    private AutoCompleteTextView name_emergency, relationship, mobile_no_emergency, email_emergency;
    private Button btn_update, btn_update_emergency;

    private ImageView img_edit_profile;
    private Uri mStrUpdateProfileUrl;
    private String mSelectedFilePath;
    private CircleImageView civ_profile;
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


        img_edit_profile = mMainView.findViewById(R.id.img_edit_profile);
        civ_profile = mMainView.findViewById(R.id.civ_profile);
        img_edit_profile.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                requestPermissionForGallery();
            }
        });

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
                && ValidatorUtils.NotEmptyValidator(mContext, mobile_no_emergency, true, getString(R.string.UserRelativePhoneNo))
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissionForGallery() {
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.MY_PERMISSIONS_REQUEST_FOR_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.MY_PERMISSIONS_REQUEST_FOR_EXTERNAL_STORAGE) {
            boolean permissionDenied = false;

            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    permissionDenied = true;
                    boolean showRationale = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permission);
                    }
                    if (!showRationale) {
                        // user also CHECKED "never ask again"
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting
                        showDialogForPermissionSetting();
                    } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) {
                        showDialogForPermission();
//                        showRationale(permission, R.string.permission_denied_contacts);
                        // user did NOT check "never ask again"
                        // this is a good place to explain the user
                        // why you need the permission and ask if he wants
                        // to accept it (the rationale)
                    }
                }
            }
            if (!permissionDenied) {

                selectImagePopUp();
            }
        }

        if (requestCode == Constant.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initCameraPermission();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


    private void showDialogForPermissionSetting() {
        new android.app.AlertDialog.Builder(mContext)
                .setTitle(getString(R.string.PermissionGoSettingTitle))
                .setMessage(getString(R.string.PermissionGoSettingMsg))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.PermissionGoSettingTxt), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        final Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + mContext.getPackageName()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        mContext.startActivity(i);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showDialogForPermission() {
        new android.app.AlertDialog.Builder(mContext)
                .setTitle(getString(R.string.PermissionDeniedTitle))
                .setMessage(getString(R.string.PermissionDeniedMsg))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.PermissionDeniedSureTxt), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.PermissionDeniedRetryTxt), new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestPermissionForGallery();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void selectImagePopUp() {


        View view = getLayoutInflater().inflate(R.layout.custom_profile_dialog, null);
        final BottomSheetDialog sheetDialog = new BottomSheetDialog(mContext);
        sheetDialog.setContentView(view);

        CustomRegularTextView tv_remove = view.findViewById(R.id.tv_remove);
        CustomRegularTextView tv_camera = view.findViewById(R.id.tv_camera);
        CustomRegularTextView tv_gallery = view.findViewById(R.id.tv_gallery);
        RelativeLayout rl_list_bootom = view.findViewById(R.id.rl_list_bootom);

        rl_list_bootom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetDialog.dismiss();
            }
        });

        tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetDialog.dismiss();

            }
        });

        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCameraPermission();
                sheetDialog.dismiss();

            }
        });


        tv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initGalleryIntent();
                sheetDialog.dismiss();

            }
        });

        sheetDialog.show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initCameraPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Toast.makeText(mContext, "Permission to use Camera", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.CAMERA}, Constant.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } else {
            captureImage();
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constant.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }

    private void initGalleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), Constant.GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constant.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null) {
                    Uri tempUri = getImageUri(mContext, imageBitmap);
                    setImagePath(tempUri);

                } else {
                    System.out.println("error = " + requestCode);
                }

            }
        }


        if (requestCode == Constant.GALLERY_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mStrUpdateProfileUrl = data.getData();
                if (mStrUpdateProfileUrl != null) {
                    CropImage.activity(data.getData())
                            .start(getActivity());
                }
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri selectedImage = result.getUri();
                if (selectedImage != null) {
                    setImagePath(selectedImage);

                } else {
                    //UIUtils.showToast(mContext, "Something issue for uploading.");
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                //UIUtils.showToast(mContext, "" + error);
            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void setImagePath(Uri tempUri) {
        mStrUpdateProfileUrl = tempUri;
        mSelectedFilePath = FilePath.getPath(mContext, mStrUpdateProfileUrl);
        if (mSelectedFilePath != null && mStrUpdateProfileUrl.getPath() != null && !mStrUpdateProfileUrl.getPath().equals("")) {
            try {
                Bitmap mProfileBitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), mStrUpdateProfileUrl);
                mProfileBitmap = ImageUtils.getScaledImage(mStrUpdateProfileUrl, mContext);
                if (mProfileBitmap != null) {
                    requestForUploadImage();

                }


            } catch (IOException e) {
                e.printStackTrace();
                // UIUtils.showToast(mContext, getString(R.string.LoadFailedMsg));
            }
        }
    }

    private void requestForUploadImage() {

        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {
            String baseUrl = Constant.API_UPDATE_IMAGE;
            showProgressBar();
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                    baseUrl, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    try {
                        String resultResponse = new String(response.data);
                        Gson gson = new GsonBuilder().create();
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonResp = jsonParser.parse(resultResponse).getAsJsonObject();
                        StatusBean responseBean = gson.fromJson(jsonResp, StatusBean.class);
                        if (responseBean != null ) {
                            hideProgressBar();

                            setImageBitmap(responseBean.getmStrpath());


                        } else {
                            hideProgressBar();

                            civ_profile.setImageResource(R.drawable.ic_user_profile);

                        }

                    } catch (Exception e) {
                        hideProgressBar();
                        civ_profile.setImageResource(R.drawable.ic_user_profile);

                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mIsRequestInProgress) {
                        hideProgressBar();
                        if (error.getClass().equals(NoConnectionError.class)) {
                            UIUtils.showToast(mContext, getResources().getString(R.string.InternetErrorMsg));
                        } else {
                            UIUtils.showToast(mContext, getResources().getString(R.string.ErrorMsg));
                        }
                    }
                }
            }) {

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    // file name could found file base or direct access from real path
                    // for now just get bitmap data from ImageView
                    params.put("avatar", new DataPart(mSelectedFilePath.substring(mSelectedFilePath.lastIndexOf("/") + 1),
                            AppHelper.readBytesFromFile(mSelectedFilePath)));
                    return params;
                }


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("userid", "" + UserUtils.getInstance().getUserID(mContext));
                    return params;
                }
            };

            multipartRequest.setShouldCache(false);
            multipartRequest.setTag("");
            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(multipartRequest);
        }
    }

    private void setImageBitmap(String aUserProfile) {
        if (aUserProfile != null) {

            Picasso.with(mContext).load(aUserProfile).placeholder(R.drawable.ic_user_profile).into(civ_profile);
        } else {

            civ_profile.setImageResource(R.drawable.ic_user_profile);
        }

    }


}
