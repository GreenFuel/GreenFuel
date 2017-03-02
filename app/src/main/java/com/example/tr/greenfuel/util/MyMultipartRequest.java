package com.example.tr.greenfuel.util;

import com.android.internal.http.multipart.Part;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by tangpeng on 2017/3/2.
 */

public class MyMultipartRequest extends StringRequest {

    private Part[] parts;

    public MyMultipartRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, Part[] parts) {
        super(method, url, listener, errorListener);
        this.parts = parts;
    }

    public MyMultipartRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener, Part[] parts) {
        super(url, listener, errorListener);
        this.parts = parts;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + Part.getBoundary();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Part.sendParts(outputStream,parts);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("getBody()"+e.getMessage());
        }
        return outputStream.toByteArray();
    }
}
