package com.ditmarcastro.reviewmovie.restApi;

import android.content.Intent;

import org.json.JSONObject;

public interface OnRestLoadListener {
    public void onRestLoadComplete(JSONObject obj, int id);

    void onActionResult(int requestCode, int resultCode, Intent data);
}