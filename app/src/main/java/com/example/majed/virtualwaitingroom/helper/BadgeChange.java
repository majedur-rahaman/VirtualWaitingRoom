package com.example.majed.virtualwaitingroom.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import com.example.majed.virtualwaitingroom.R;

/**
 * Created by majed on 8/3/2017.
 */

public class BadgeChange {

   static LayerDrawable icon;

    public BadgeChange(LayerDrawable icon){
        this.icon=icon;
    }
    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BagdeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BagdeDrawable) {
            badge = (BagdeDrawable) reuse;
        } else {
            badge = new BagdeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

    public static void setBadgeCount(Context context, String count) {

        BagdeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BagdeDrawable) {
            badge = (BagdeDrawable) reuse;
        } else {
            badge = new BagdeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }
}
