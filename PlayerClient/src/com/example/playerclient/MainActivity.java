package com.example.playerclient;

import com.example.playerserver.IRemoteService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	final static String tag = "MainActivity";
	public static final String ACTION = "com.example.playerserver.PlayerService";
	private Button playbtn, stopbtn;
	private IRemoteService mService;
	private boolean isBinded = false;
	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			isBinded = false;
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			isBinded = true;
			mService = IRemoteService.Stub.asInterface(service);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		doBind();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		playbtn = (Button) findViewById(R.id.play);
		stopbtn = (Button) findViewById(R.id.stop);
		playbtn.setOnClickListener(clickListener);
		stopbtn.setOnClickListener(clickListener);
	}

	private void doBind() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(ACTION);
		bindService(intent, conn, Context.BIND_AUTO_CREATE);

	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == playbtn.getId()) {
				Log.i(tag, "play");
				try {
					mService.play();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} else {
				Log.i(tag, "stop");
				try {
					mService.stop();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	};

	protected void onDestroy() {
		doUnbind();
		super.onDestroy();

	}

	private void doUnbind() {
		// TODO Auto-generated method stub
		if (isBinded) {
			unbindService(conn);
			mService = null;
			isBinded = false;
		}
	};
}
