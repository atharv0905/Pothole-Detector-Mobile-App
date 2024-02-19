package com.atharv.potholedetector;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import okhttp3.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddPotholeData {

    public void uploadImage(Bitmap imageBitmap,String latitude,String longitude, String url) {
        OkHttpClient client = new OkHttpClient();

        // Convert Bitmap to byte array output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(CompressFormat.JPEG, 100, outputStream);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("photo", "image.jpg",
                        RequestBody.create(MediaType.parse("image/*"), outputStream.toByteArray()))
                .addFormDataPart("latitude",latitude)
                .addFormDataPart("longitude",longitude)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                // Handle successful response
                System.out.println(response.body().string());
            }
        });
    }
}