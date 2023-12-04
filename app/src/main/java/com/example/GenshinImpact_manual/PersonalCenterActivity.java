package com.example.GenshinImpact_manual;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PersonalCenterActivity extends Activity {
    // 关用户名、生日、账户以及底部导航栏按钮
    EditText usernameEdit = (EditText) this.findViewById(R.id.UserNameEdit);
    EditText birthdayEdit = (EditText) this.findViewById(R.id.BirthdayEdit);
    TextView account1 = (TextView) this.findViewById(R.id.FirstAccountView);
    TextView account2 = (TextView) this.findViewById(R.id.SecondAccountView);
    TextView account3 = (TextView) this.findViewById(R.id.ThirdAccountView);
    ImageButton button1 = (ImageButton) this.findViewById(R.id.Button1);
    ImageButton button2 = (ImageButton) this.findViewById(R.id.Button2);
    ImageButton button3 = (ImageButton) this.findViewById(R.id.Button3);
    ImageButton button4 = (ImageButton) this.findViewById(R.id.Button4);
    ImageButton button5 = (ImageButton) this.findViewById(R.id.Button5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //关联注册界面
        setContentView(R.layout.activity_personal_center);

        //调用fetchDataFromServer()在前台显示用户名、生日以及账户
        fetchDataFromServer();

        //判断用户名和生日是否为空
        if(usernameEdit == null && birthdayEdit == null){
            String strUserName = usernameEdit.getText().toString().trim();
            String strbirthday = birthdayEdit.getText().toString().trim();
            //用户输入用户名和生日并传给服务器
            sendDataToServer(strUserName, strbirthday);
        }

        // 导航栏1号按钮监听器
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalCenterActivity.this, FirstActivity.class);
                startActivity(intent);
            }
        });

        // 导航栏2号按钮监听器
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalCenterActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        // 导航栏3号按钮监听器
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalCenterActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });

        // 导航栏4号按钮监听器
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalCenterActivity.this, FourthActivity.class);
                startActivity(intent);
            }
        });

        // 导航栏5号按钮监听器
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalCenterActivity.this, PersonalCenterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchDataFromServer() {
        // 创建一个请求队列
        RequestQueue queue = Volley.newRequestQueue(this);

        // 设置请求的URL，替换成你的后台接口地址
        String url = "http://121.40.131.167:8080/usercenter";

        // 创建一个 JSON 请求
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // 请求成功时的处理
                        // 在这里解析服务器返回的 JSON 数据
                        try {
                            String username = response.getString("username");
                            String birthday = response.getString("birthday");

                            // 假设 "accountInfo" 是一个JSON数组，包含多个字符串
                            JSONArray accountInfoArray = response.getJSONArray("accountInfo");
                            // 创建一个字符串数组，用于存储解析后的数据
                            String[] accountInfo = new String[accountInfoArray.length()];
                            // 循环遍历JSON数组，逐个解析数组元素并存储到字符串数组中
                            for (int i = 0; i < accountInfoArray.length(); i++) {
                                accountInfo[i] = accountInfoArray.getString(i);
                            }

                            // 将数据显示在界面上
                            usernameEdit.setText(username);
                            birthdayEdit.setText(birthday);
                            account1.setText(accountInfo[0]);
                            account2.setText(accountInfo[1]);
                            account3.setText(accountInfo[2]);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 请求失败时的处理
                error.printStackTrace();
            }
        });
        // 将请求添加到请求队列中执行
        queue.add(request);
    }

    private void sendDataToServer(String username, String birthday) {
        // 创建一个请求队列
        RequestQueue queue = Volley.newRequestQueue(this);

        // 设置请求的URL，这里是一个示例URL，请替换成你的后台接口地址
        String url = "http://121.40.131.167:8080/usercenter";

        // 创建一个 JSON 对象，存储用户名、生日信息以及账号信息
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("username", username);
            requestData.put("birthday", birthday);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 创建一个 POST 请求
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // 请求成功时的处理
                        // 可以在这里处理服务器返回的响应信息
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 请求失败时的处理
                // 可以在这里处理错误情况，比如显示错误信息
            }
        });

        // 将请求添加到请求队列中执行
        queue.add(request);
    }
}
