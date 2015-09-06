package com.example.androidclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	TextView textResponse; 
	EditText NoiDung;
	Socket sk;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		NoiDung=(EditText) findViewById(R.id.NoiDungTinNhan);
		textResponse = (TextView)findViewById(R.id.response);
		textResponse.setMovementMethod(new ScrollingMovementMethod());
		textResponse.scrollTo(0, Integer.MAX_VALUE);
		MyClientTask myClientTask = new MyClientTask("52.68.172.187",2015);
		myClientTask.execute();
	}
	public void GuiTinNhan(View v){
		DataOutputStream ps;
        try {
            ps = new DataOutputStream(sk.getOutputStream());
            ps.write(("Android!@#."+NoiDung.getText()+"\r\n").getBytes("UTF8"));
            NoiDung.setText("");

        } catch (IOException ex) {
        	ex.printStackTrace();
        }
	}
	public class MyClientTask extends AsyncTask<Void, Void, Void> {
		
		String dstAddress;
		int dstPort;
		
		MyClientTask(String addr, int port){
			dstAddress = addr;
			dstPort = port;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			
			Socket socket = null;
			
			try {
				socket = new Socket(dstAddress, dstPort);
				sk=socket;
				while (true) {
                    DataInputStream br;
                    try {
                        br = new DataInputStream(socket.getInputStream());
                        byte[] data = new byte[1024];
                        int count = br.read(data);
                        String str = null;
                        str = new String(data, "UTF-8");
                        str = str.substring(0, str.indexOf("\r\n"));
                        System.out.println(str);
                        String[] stm = str.split(":");
                        final String temp=str;
                        runOnUiThread(new Runnable() {
							@Override
							public void run() {
								textResponse.setText(textResponse.getText()+"\n"+temp);
								//textResponse.append(textResponse.getText()+"\n"+temp);
							}
						});
                    } catch (IOException ex) {
                        
                    }
                }

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}
}
