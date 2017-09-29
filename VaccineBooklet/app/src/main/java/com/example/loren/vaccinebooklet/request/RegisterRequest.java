package com.example.loren.vaccinebooklet.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.loren.vaccinebooklet.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://vakcinoapp.altervista.org/Register.php";
    private Map<String, String> params;

    public RegisterRequest(String email, String password, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
