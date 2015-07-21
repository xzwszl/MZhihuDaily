package com.zxw.madaily.http;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by sony on 2015/7/21.
 */
//public class DailyRequest<T> extends JsonRequest<T> {
//
//    @Override
//    protected Response<T> parseNetworkResponse(NetworkResponse response) {
//
//        Gson gson = new Gson();
//
//       gson.fromJson(new String(response.data), new TypeToken<>());
//
//        return Response.success(t, HttpHeaderParser.parseCharset(response.headers));
//
//        JsonObjectRequest
//    }
//}
