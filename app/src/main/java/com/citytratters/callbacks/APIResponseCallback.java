package com.citytratters.callbacks;


import com.citytratters.network.RestResponse;

public interface APIResponseCallback<T> {

    void onResponse(RestResponse<T> response);
}
