package com.vish.travelbook.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageUtils {

    public static final int SELECT_PICTURE_REQUEST_CODE = 1;

    public static String encodeImageToBase64(Uri imageUri, Context context) {
        Bitmap selectedImage = null;
        String base64Image = "";
        try {
            InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
            selectedImage = BitmapFactory.decodeStream(imageStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (selectedImage != null) {
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            base64Image = Base64.encodeToString(bytes, Base64.NO_WRAP);
        }
        return base64Image;
    }

    public static Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
