package com.hct.scene.launcher2;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.hct.scene.launcher.R;

public class PreShortcutInstaller {
	private static final String TAG = "preshortcutinstall";
	private Context mContext;
	private Intent mIntent;
	private static final String mpreapksDir = "/apkmanager";

	PreShortcutInstaller(Context context, Intent intent) {
		mContext = context;
		mIntent = intent;
	}

	public void installApk(String apk_name) {
		Log.v(TAG, "installApk");

		File SDCard = Environment.getExternalStorageDirectory();
		String preapkDir = SDCard.getAbsolutePath() + SDCard.separator + mpreapksDir;

		// 检查是否安装过
		if (checkAppInfo(mContext, mIntent.getComponent().getPackageName())) {
			startApp(mIntent);
			return;
		}

		// 启动安装界面
		String apkPathName = preapkDir + SDCard.separator + apk_name;

		File file = null;
		file = new File(apkPathName);
		if (!file.exists()) {
			// T卡中对应的APK文件不存在
			Log.v(TAG, "open file error" + apkPathName);
			Toast.makeText(mContext, R.string.str_file_not_find, Toast.LENGTH_SHORT).show();
			return;
		}

		Intent file_intent = new Intent();
		file_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		file_intent.setAction(Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(file);
		Log.v(TAG, "Open uri file: " + uri);
		file_intent.setDataAndType(uri, "application/vnd.android.package-archive");
		mContext.startActivity(file_intent);
	}

	// 检查 app 是否在所有应用的列表中存在
	private boolean checkAppInfo(Context context, String packageName) {
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(mainIntent, 0);
		for (ResolveInfo info : apps) {
			if (info.activityInfo.packageName.equals(packageName)) {
				return true;
			}
		}

		return false;
	}

	// 运行应用
	public void startApp(Intent appIntent) {
		Intent intent = appIntent;
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}
}
