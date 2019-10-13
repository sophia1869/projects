package com.example.nicolemorris.lifestyle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class
ProfilePicFragment extends Fragment
        implements View.OnClickListener {

    public static final int PICK_IMAGE = 1;

    Button bTakePic,bSelectPic,bNext;
    ProfilePicOnDataPass mDataPasser;
    ImageView mIvPic;
    String image;
    String name;

    FileOutputStream out;




    //Callback interface
    public interface ProfilePicOnDataPass{
        public void onProfilePicDataPass(String image);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mDataPasser = (ProfilePicOnDataPass) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnDataPass");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_pic, container, false);
        bNext = view.findViewById(R.id.b_next);
        bNext.setOnClickListener(this);

        bTakePic = view.findViewById(R.id.button_take_pic);
        bTakePic.setOnClickListener(this);

        bSelectPic = view.findViewById(R.id.button_select_pic);
        bSelectPic.setOnClickListener(this);

        //name = getArguments().getString("username");

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if(data == null){
                Toast.makeText(getContext(), "Unable to upload image", Toast.LENGTH_SHORT).show();
            }else{
                mIvPic = (ImageView) getActivity().findViewById(R.id.iv_profile);
                Bundle extras = data.getExtras();
                if(extras != null){
                    Uri profile_image = getImageUri((Bitmap)extras.get("data"));
                    image = profile_image.toString();
                    mIvPic.setImageURI(profile_image);
                }else {
                    try {
                        Uri profile_image = data.getData();
                        image = profile_image.toString();
                        System.out.println("uri is "+image);
                        //System.out.print("real path "+getPath(getContext(), profile_image));
                        //image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageFilePath);
                        mIvPic.setImageURI(profile_image);
                    }catch(Exception e){
                        System.out.println(e);
                        Toast.makeText(getActivity(), "Not able to get the image from gallery", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        }else{
            System.out.println("result is not ok");
        }
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.b_next: {
                //NEED TO ADD DATE TO PASS FOR STORAGE :)
                mDataPasser.onProfilePicDataPass(image);
                break;
            }
            case R.id.button_select_pic:{
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                if(intent.resolveActivity(getActivity().getPackageManager())!=null){
                    startActivityForResult(intent, PICK_IMAGE);
                }
                break;
            }
            case R.id.button_take_pic: {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getActivity().getPackageManager())!=null){
                    startActivityForResult(intent, PICK_IMAGE);
                }
                break;
            }
        }
    }

//    private Uri convertBitmaptoUri(Bitmap image){
//        Uri tempUri = getImageUri(getApplicationContext(), photo);
//        File finalFile = new File(getRealPathFromURI(tempUri));
//    }

    public Uri getImageUri(Bitmap image) {
        try {
            File f = new File(getContext().getCacheDir(), "temp");
            f.createNewFile();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            return Uri.fromFile(f);
        }catch(Exception e){
            Toast.makeText(getActivity(), "Unable to get Image", Toast.LENGTH_SHORT).show();
            return  null;
        }
    }

//    private String getRealPathFromURI(Uri uri) {
//        String result;
//        String [] proj={MediaStore.Images.Media.DATA};
//        Cursor cursor = getContext().getContentResolver().query(uri, proj, null, null, null);
//        if (cursor == null) { // Source is Dropbox or other similar local file path
//            result = uri.getPath();
//        } else {
//            cursor.moveToFirst();
//            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            result = cursor.getString(idx);
//            cursor.close();
//        }
//        return result;
//    }
//
//    @SuppressLint("NewApi")
//    public static String getPath(Context context, Uri uri) {
//
//        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//        if (ContextCompat.checkSelfPermission(getContext(),
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//        }
//
//        // DocumentProvider
//        else if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                Uri contentUri = null;
//                if ("image".equals(type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//
//                final String selection = "_id=?";
//                final String[] selectionArgs = new String[] {
//                        split[1]
//                };
//
//                return getDataColumn(context, contentUri, selection, selectionArgs);
//
//        }
//        // MediaStore (and general)
//        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            return getDataColumn(context, uri, null, null);
//        }
//        // File
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//    }
//
//    /**
//     * Get the value of the data column for this Uri. This is useful for
//     * MediaStore Uris, and other file-based ContentProviders.
//     *
//     * @param context The context.
//     * @param uri The Uri to query.
//     * @param selection (Optional) Filter used in the query.
//     * @param selectionArgs (Optional) Selection arguments used in the query.
//     * @return The value of the _data column, which is typically a file path.
//     */
//    public static String getDataColumn(Context context, Uri uri, String selection,
//                                       String[] selectionArgs) {
//
//        Cursor cursor = null;
//        final String column = "_data";
//        final String[] projection = {
//                column
//        };
//
//        try {
//            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
//                    null);
//            if (cursor != null && cursor.moveToFirst()) {
//                final int column_index = cursor.getColumnIndexOrThrow(column);
//                return cursor.getString(column_index);
//            }
//        } finally {
//            if (cursor != null)
//                cursor.close();
//        }
//        return null;
//    }
//
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is ExternalStorageProvider.
//     */
//    public static boolean isExternalStorageDocument(Uri uri) {
//        return "com.android.externalstorage.documents".equals(uri.getAuthority());
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is DownloadsProvider.
//     */
//    public static boolean isDownloadsDocument(Uri uri) {
//        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is MediaProvider.
//     */
//    public static boolean isMediaDocument(Uri uri) {
//        return "com.android.providers.media.documents".equals(uri.getAuthority());
//    }

}
