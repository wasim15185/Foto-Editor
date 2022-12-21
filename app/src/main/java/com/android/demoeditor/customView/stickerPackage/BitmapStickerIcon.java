package com.android.demoeditor.customView.stickerPackage;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
 import android.view.MotionEvent;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author wupanjie
 */
public class BitmapStickerIcon extends DrawableSticker implements StickerIconEvent {
  public static final float DEFAULT_ICON_RADIUS = 30f;
  public static final float DEFAULT_ICON_EXTRA_RADIUS = 10f;
  public static final float DEFAULT_ICON_SHADOW_RADIUS = 5f;


  @IntDef({ LEFT_TOP, RIGHT_TOP, LEFT_BOTTOM, RIGHT_BOTOM }) @Retention(RetentionPolicy.SOURCE)
  public @interface Gravity {

  }

  public static final int LEFT_TOP = 0;
  public static final int RIGHT_TOP = 1;
  public static final int LEFT_BOTTOM = 2;
  public static final int RIGHT_BOTOM = 3;

  private float iconRadius = DEFAULT_ICON_RADIUS;
  private float iconExtraRadius = DEFAULT_ICON_EXTRA_RADIUS;
  private float iconShadowRadius=DEFAULT_ICON_SHADOW_RADIUS ;
  private float x;
  private float y;
  @Gravity private int position = LEFT_TOP;

  private StickerIconEvent iconEvent;

  public BitmapStickerIcon(Drawable drawable, @Gravity int gravity) {
    super(drawable);
    this.position = gravity;
  }

  public void draw(Canvas canvas, Paint paint) {

    paint.setShadowLayer(  iconShadowRadius,0f, 0f, Color.DKGRAY);
    canvas.drawCircle(x, y, iconRadius, paint);
    super.draw(canvas);
  }

  public float getX() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public float getIconRadius() {
    return iconRadius;
  }

  public void setIconRadius(float iconRadius) {
    this.iconRadius = iconRadius;
  }

  public float getIconExtraRadius() {
    return iconExtraRadius;
  }

  public void setIconExtraRadius(float iconExtraRadius) {
    this.iconExtraRadius = iconExtraRadius;
  }

  @Override public void onActionDown(StickerView stickerView, MotionEvent event) {
    if (iconEvent != null) {
      iconEvent.onActionDown(stickerView, event);
    }
  }

  @Override public void onActionMove(StickerView stickerView, MotionEvent event) {
    if (iconEvent != null) {
      iconEvent.onActionMove(stickerView, event);
    }
  }

  @Override public void onActionUp(StickerView stickerView, MotionEvent event) {
    if (iconEvent != null) {
      iconEvent.onActionUp(stickerView, event);
    }
  }

  public StickerIconEvent getIconEvent() {
    return iconEvent;
  }

  public void setIconEvent(StickerIconEvent iconEvent) {
    this.iconEvent = iconEvent;
  }

  @Gravity public int getPosition() {
    return position;
  }

  public void setPosition(@Gravity int position) {
    this.position = position;
  }
}
