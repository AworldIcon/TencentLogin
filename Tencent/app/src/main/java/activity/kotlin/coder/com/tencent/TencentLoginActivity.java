package activity.kotlin.coder.com.tencent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import static android.R.attr.name;

public class TencentLoginActivity extends AppCompatActivity {
    Button login;
    Button exit;
    Tencent mtencent;
    BaseUIListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tencent_login);
        login= (Button) findViewById(R.id.login);
        exit= (Button) findViewById(R.id.exit);
        mtencent = Tencent.createInstance("1106137869", this.getApplicationContext());
        listener=new BaseUIListener(this,mtencent);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mtencent.isSessionValid())
                {
                    mtencent.login(TencentLoginActivity.this, "all", listener);
                }
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtencent.logout(TencentLoginActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode,resultCode,data,listener);
//        if(){}
       // getUserInfo();

    }

}

