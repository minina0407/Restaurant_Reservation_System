/*************************************************************************************************
 * JANUARY 8, 2018
 * Mentesnot Aboset
 *  ************************************************************************************************/
package com.restaurant_reservation_system.controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.restaurant_reservation_system.R;
import com.restaurant_reservation_system.database.User;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    static ArrayList<User> userArray;
    User who;
    EditText email;
    EditText password;
    String u_email;
    String u_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userArray = new ArrayList<User>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Thread thread = new Thread(runnable);
        thread.start();

        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success = login();
                if (success) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("name",who.getName());
                    intent.putExtra("id",who.getID());
                    startActivity(intent);
                } else {
                    onClickShowAlert(view);
                }
            }
        });

        TextView linkRegister = (TextView) findViewById(R.id.linkRegister);
        linkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onClickShowAlert(View view) {
        AlertDialog.Builder myAlertBuilder =
                new AlertDialog.Builder(LoginActivity.this);
        // alert??? title??? Messege ??????
        myAlertBuilder.setTitle("Alert");
        myAlertBuilder.setMessage("???????????? ?????????????????????. ?????? ????????? ?????????");
        // ?????? ?????? (Ok ????????? Cancle ?????? )
        myAlertBuilder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // OK ????????? ????????? ??????
                Toast.makeText(getApplicationContext(), "Pressed OK",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        // Alert??? ??????????????? ???????????? ?????????(show??? ???????????? Alert??? ?????????)
        myAlertBuilder.show();
    }


    public boolean login() {
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        u_email = email.getText().toString();
        u_password = password.getText().toString();
        for (int i = 0; i < userArray.size(); i++) {
            if (u_email.equals(userArray.get(i).getID()) && u_password.equals(userArray.get(i).getPw())) {
                who = new User(userArray.get(i).getID(), userArray.get(i).getPw(), userArray.get(i).getName(), userArray.get(i).getPhoneNumber(),
                        userArray.get(i).getPenalty(), userArray.get(i).getAdmin());
                return true;
            }
        }
        return false;
    }

    Runnable runnable = new Runnable() { //??????: https://javapp.tistory.com/132
        @Override
        public void run() {
            try {
                String site = "http://192.168.219.100/user_inform.php";
                URL url = new URL(site);
                //??????
                URLConnection conn = url.openConnection();
                //????????? ???????????? ?????? ???????????? ??????
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(isr);

                String str = null;
                StringBuffer buf = new StringBuffer();

                do {
                    str = br.readLine();
                    if (str != null) {
                        buf.append(str);
                    }
                } while (str != null);

                String data = buf.toString();  //json ????????? ??? ?????????

                data = data.replace("[", "");
                data = data.replace("]", "");
                data = data.replace("{", "");
                String[] test = data.split("\\},");
                test[test.length - 1] = test[test.length - 1].replace("}", "");
                for (int i = 0; i < test.length; i++) {
                    test[i] = test[i].replace("\"name\":", "");
                    test[i] = test[i].replace("\"id\":", "");
                    test[i] = test[i].replace("\"phoneNumber\":", "");
                    test[i] = test[i].replace("\"pw\":", "");
                    test[i] = test[i].replace("\"penalty\":", "");
                    test[i] = test[i].replace("\"admin\":", "");
                    test[i] = test[i].replace("\"", "");
                    String inform[] = test[i].split(",");
                    userArray.add(new User(inform[0], inform[1], inform[2], inform[3], inform[4], inform[5]));
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}