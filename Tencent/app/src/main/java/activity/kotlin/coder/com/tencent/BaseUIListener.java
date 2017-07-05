package activity.kotlin.coder.com.tencent;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseUIListener implements IUiListener {
	private Context mContext;
	private Tencent mtencent;
	private String mScope;
	private boolean mIsCaneled;
	private static final int ON_COMPLETE = 0;
	private static final int ON_ERROR = 1;
	private static final int ON_CANCEL = 2;
	private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case ON_COMPLETE:
                JSONObject response = (JSONObject)msg.obj;
                Util.showResultDialog(mContext, response.toString(), "onComplete");
                Util.dismissDialog();
//				mtencent.requestAsync(Constants.PACKAGE_QQ, null,
//						Constants.HTTP_GET, new BaseApiListener("get_simple_userinfo", false,mContext),
//						null);
				initOpenIdAndToken(response);
				getUserInfo(mtencent);
                break;
            case ON_ERROR:
                UiError e = (UiError)msg.obj;
                Util.showResultDialog(mContext, "errorMsg:" + e.errorMessage
                        + "errorDetail:" + e.errorDetail, "onError");
                Util.dismissDialog();
                break;
            case ON_CANCEL:
                Util.toastMessage((Activity)mContext, "onCancel");
                break;
            }
        }	    
	};
	private void initOpenIdAndToken(Object object) {
		JSONObject jb = (JSONObject) object;
		try {
			String openID = jb.getString("openid");  //openid用户唯一标识
			String access_token = jb.getString("access_token");
			String expires = jb.getString("expires_in");

			mtencent.setOpenId(openID);
			mtencent.setAccessToken(access_token, expires);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void getUserInfo(Tencent mtencent) {
		QQToken token = mtencent.getQQToken();
		UserInfo mInfo = new UserInfo(mContext, token);
		mInfo.getUserInfo(new IUiListener() {
			@Override
			public void onComplete(Object object) {
				try {
					JSONObject jb = new JSONObject(object.toString());
					Log.d("zw--dd","figureurl_qq_2---"+jb.getString("figureurl_qq_2"));

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					//String  name = jb.getString("nickname");
//                    figureurl = jb.getString("figureurl_qq_2");  //头像图片的url
//                    nickName.setText(name);
//                    Picasso.with(MainActivity.this).load(figureurl).into(figure);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(UiError uiError) {
			}

			@Override
			public void onCancel() {
			}
		});
	}
	public BaseUIListener(Context mContext,Tencent mtencent) {
		super();
		this.mContext = mContext;
		this.mtencent = mtencent;
	}

	
	public BaseUIListener(Context mContext, String mScope) {
		super();
		this.mContext = mContext;
		this.mScope = mScope;
	}
	
	public void cancel() {
		mIsCaneled = true;
	}


	@Override
	public void onComplete(Object response) {
		if (mIsCaneled) return;
	    Message msg = mHandler.obtainMessage();
	    msg.what = ON_COMPLETE;
	    msg.obj = response;
	    mHandler.sendMessage(msg);
	}

	@Override
	public void onError(UiError e) {
		if (mIsCaneled) return;
	    Message msg = mHandler.obtainMessage();
        msg.what = ON_ERROR;
        msg.obj = e;
        mHandler.sendMessage(msg);
	}

	@Override
	public void onCancel() {
		if (mIsCaneled) return;
	    Message msg = mHandler.obtainMessage();
        msg.what = ON_CANCEL;
        mHandler.sendMessage(msg);
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

}
