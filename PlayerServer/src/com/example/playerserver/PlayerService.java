package com.example.playerserver;

import java.io.FileDescriptor;
import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class PlayerService extends Service {

	public static final String tag = "PlayerService";

	private MediaPlayer mplayer;

	// 实现aidl文件中定义的接口
	private IBinder mBinder = new IRemoteService.Stub() {

		@Override
		public void stop() throws RemoteException {
			// TODO Auto-generated method stub
			try {
				if (mplayer.isPlaying()) {
					mplayer.stop();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		public void play() throws RemoteException {
			// TODO Auto-generated method stub
			try {
				if (mplayer.isPlaying()) {
					return;
				}
				mplayer.prepare();
				mplayer.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(tag, "PlayerService onBind()");
		if (mplayer == null) {
			mplayer = new MediaPlayer();
			try {
				FileDescriptor fd = getResources().openRawResourceFd(
						R.raw.zhangguorong).getFileDescriptor();
				mplayer.setDataSource(fd);
				mplayer.setLooping(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.i(tag, "player created");
		}
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		if (mplayer != null) {
			mplayer.release();
		}
		Log.i(tag, "PlayerService onUnbind");
		return super.onUnbind(intent);
	}
}
