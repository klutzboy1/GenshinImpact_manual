package com.example.GenshinImpact_manual;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.GenshinImpact_manual.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends Activity {
    // 调用Actvity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //关联注册界面
        setContentView(R.layout.activity_sign_up);

        // 关联邮箱、密码、确认密码和注册、返回登录按钮
        EditText email = (EditText) this.findViewById(R.id.EmailEdit);
        EditText passWord = (EditText) this.findViewById(R.id.PassWordEdit);
        EditText passWordAgain = (EditText) this.findViewById(R.id.PassWordAgainEdit);
        Button signUpButton = (Button) this.findViewById(R.id.SignUpButton);
        Button backLoginButton = (Button) this.findViewById(R.id.BackLoginButton);

        // 立即注册按钮监听器
        signUpButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //trim() 方法用于删除字符串的头尾空白符，空白符包括：空格、制表符 tab、换行符等其他空白符
                        String strEmail = email.getText().toString().trim();
                        String strPassWord = passWord.getText().toString().trim();
                        String strPassWordAgain = passWordAgain.getText().toString().trim();
                        //注册格式粗检
                        if (!strEmail.contains("@")) {
                            Toast.makeText(SignUpActivity.this, "邮箱格式不正确！", Toast.LENGTH_SHORT).show();
                        } else if (strPassWord.length() > 16) {
                            Toast.makeText(SignUpActivity.this, "密码长度必须小于16！", Toast.LENGTH_SHORT).show();
                        } else if (strPassWord.length() < 6) {
                            Toast.makeText(SignUpActivity.this, "密码长度必须大于6！", Toast.LENGTH_SHORT).show();
                        } else if (!strPassWord.equals(strPassWordAgain)) {
                            Toast.makeText(SignUpActivity.this, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();

                            // 密码符合要求，进行加密并发送数据到服务器
                            try {
                                // 使用RSA加密密码
                                String encryptedPassword = RSAUtil.encrypt(strPassWord, RSAUtil.publicKeyString);

                                // 创建user对象用以传输数据
                                User user = new User(strEmail,encryptedPassword);

                                // 创建包含用户注册信息的JSON对象
                                JSONObject jsonBody = new JSONObject();
                                jsonBody.put("userRegisterInfo", user);

                                // 创建Volley的RequestQueue
                                RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);

                                // 设置POST请求
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                        Request.Method.POST,
                                        "http://121.40.131.167:8080/register",
                                        jsonBody,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                // 处理服务器响应
                                                try {
                                                    int code = response.getInt("code");
                                                    String msg = response.getString("msg");
                                                    if (code == 200) {
                                                        // 注册成功,打印注册成功消息
                                                        Toast.makeText(SignUpActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        // 注册失败，打印注册失败原因
                                                        Toast.makeText(SignUpActivity.this, "注册失败: " + msg, Toast.LENGTH_SHORT).show();
                                                        // 处理响应信息
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // 处理请求错误
                                                error.printStackTrace();
                                            }
                                        }
                                );

                                /*当调用 requestQueue.add(jsonObjectRequest); 方法时，Volley库会将这个请求对象添加到队列中
                                ，并开始执行这个请求。这个过程会触发向后台服务器发送网络请求，等待服务器响应。
                                Volley库会在后台异步处理这个请求，在请求的生命周期内负责建立网络连接、发送请求数据，
                                等待服务器响应，并处理服务器返回的数据。当服务器响应返回后
                                ，Volley会调用相应的回调函数（如 onResponse 和 onErrorResponse）来处理服务器返回的结果。*/

                                // 将POST请求添加到请求队列中，同时将这个POST请求发送给后台服务器
                                requestQueue.add(jsonObjectRequest);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        // 返回登录按钮监听器
        backLoginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 跳转到登录界面
                        Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }
}