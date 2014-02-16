package com.hct.scene.launcher2;

import android.content.ContentValues;
import android.graphics.drawable.Drawable;

public class FurnitureInfo extends ItemInfo{
	
	int style;
	
	CharSequence title;
	
	CharSequence className;
	
	Drawable bg;
	public FurnitureInfo() {
		super();
		itemType = LauncherSettings.Favorites.ITEM_TYPE_FURNITURE;
	}

	public FurnitureInfo(ItemInfo info) {
		super(info);
	}
	public void setBg(Drawable icon){
		bg = icon;
	}

	@Override
	void onAddToDatabase(ContentValues values) {
		super.onAddToDatabase(values);
		values.put(LauncherSettings.Favorites.FURNITURE_CLASS, className.toString());
		values.put(LauncherSettings.Favorites.FURNITURE_STYLE, style);
		values.put(LauncherSettings.Favorites.TITLE, title.toString());
	}
	
}
