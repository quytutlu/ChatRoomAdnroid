package com.example.androidclient;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity{
	ProgressDialog dialog;
	String url;
	EditText user;
	EditText pass;
	String active;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		user=(EditText) findViewById(R.id.user);
		pass=(EditText) findViewById(R.id.pass);
		dialog=new ProgressDialog(this);
	}
	public void Onclick(View v){
		url="http://52.68.172.187/index.php?cmd=dangnhap&tendangnhap=" + user.getText() + "&matkhau=" + pass.getText();
		new ParseJSONTask().execute();
	}
	private void updateUI() {
		if (active.equals("1")) {
			Toast.makeText(getApplicationContext(), "Đăng nhập thành công!",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(Login.this,com.example.androidclient.MainActivity.class);
			intent.putExtra("TenDangNhap", user.getText().toString());
			startActivity(intent);
		}else if(active.equals("-1")){
			Toast.makeText(getApplicationContext(), "Tài khoản chưa kích hoạt!",
					Toast.LENGTH_SHORT).show();
		}else if(active.equals("0")){
			Toast.makeText(getApplicationContext(), "Đăng nhập thất bại!",
					Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onBackPressed() {
		finish();
	}
	private class ParseJSONTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("Đang đăng nhập...");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			WebServiceHandler webServiceHandler = new WebServiceHandler();
			String jsonstr = webServiceHandler.getJSONData(url);
			if (jsonstr == null) {
				return false;
			}
			try {
				JSONObject jsonObject = new JSONObject(jsonstr);
				active = jsonObject.getString("active");
				return true;
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"Không kết nối được server", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			if (result == false) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						Login.this);
				builder.setTitle("Lỗi!");
				builder.setMessage("Kiểm tra kết nối mạng");
				builder.show();
				return;
			}
			updateUI();
		}
	}
}
