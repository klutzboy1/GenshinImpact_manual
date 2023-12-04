package com.example.GenshinImpact_manual;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.GenshinImpact_manual.entity.User;


/*volley是Android 提供的网络请求库（类似的还有 OkHttp 或 Retrofit等）*/
public class LogInActivity extends Activity {
    // 调用Actvity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 关联登录界面
        setContentView(R.layout.activity_log_in);

        //使用this.getIntent()获取当前Activity的Intent
        Intent intent = this.getIntent();

        // 关联邮箱、密码和登录、注册按钮
        EditText email = (EditText) this.findViewById(R.id.EmailEdit);
        EditText passWord = (EditText) this.findViewById(R.id.PassWordEdit);
        Button loginButton = (Button) this.findViewById(R.id.LoginButton);
        Button signUpButton = (Button) this.findViewById(R.id.SignUpButton);

        // 登录按钮监听器
        loginButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 获取用户名和密码
                        String strEmail = email.getText().toString().trim();
                        String strPassWord = passWord.getText().toString().trim();


                        try {
                            // 使用RSA加密密码
                            String encryptedPassword = RSAUtil.encrypt(strPassWord, RSAUtil.publicKeyString);

                            // 创建user对象用以传输数据
                            User user = new User(strEmail, encryptedPassword);

                            // 构建包含用户登录信息的JSON对象
                            JSONObject params = new JSONObject();
                            params.put("userLoginInfo", user);

                            // 构建 Volley 的请求队列
                            RequestQueue queue = Volley.newRequestQueue(LogInActivity.this);

                            // 构建登录请求
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://121.40.131.167:8080/login", params,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                /*code状态码 200登录成功 403登录失败*/
                                                int code = response.getInt("code");
                                                if (code == 200) {
                                                    // 登录成功，处理返回的数据
                                                    String token = response.getString("token");
                                                    Toast.makeText(LogInActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();

                                                    // 跳转到SecondActivity页面
                                                    // 创建一个 Intent 对象，指定当前活动(LogInActivity.this)和目标活动(SecondActivity.class)
                                                    Intent intent = new Intent(LogInActivity.this, SecondActivity.class);

                                                    // 可以通过 putExtra() 方法传递数据到下一个活动
                                                    intent.putExtra("token", token);

                                                    // 启动目标活动
                                                    startActivity(intent);
                                                } else {
                                                    // 登录失败，处理错误消息
                                                    String msg = response.getString("msg");
                                                    Toast.makeText(LogInActivity.this, "登录失败: " + msg, Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // 网络请求错误
                                            Toast.makeText(LogInActivity.this, "网络请求错误: " + error.toString(), Toast.LENGTH_SHORT).show();
                                            // 处理请求失败的情况
                                            error.printStackTrace();
                                        }
                                    });

                            // 将请求添加到队列中执行
                            queue.add(jsonObjectRequest);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        // 注册按钮监听器
        signUpButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 跳转到注册界面
                        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                        startActivity(intent);
                    }
                }
        );

    }
}