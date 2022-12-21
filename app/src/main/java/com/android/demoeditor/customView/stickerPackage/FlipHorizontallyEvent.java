package com.android.demoeditor.customView.stickerPackage;

/**
 * @author wupanjie
 */

public class FlipHorizontallyEvent extends AbstractFlipEvent {

  @Override @StickerView.Flip protected int getFlipDirection() {
    return  StickerView.FLIP_HORIZONTALLY;
  }
}
