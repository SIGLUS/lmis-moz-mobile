package org.openlmis.core.view.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

public class RequisitionBodyHorizontalScrollView extends HorizontalScrollView {

  public RequisitionBodyHorizontalScrollView(Context context) {
    super(context);
  }

  public RequisitionBodyHorizontalScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public RequisitionBodyHorizontalScrollView(Context context, AttributeSet attrs,
      int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
    return true;
  }

  @Override
  protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
    return 0;
  }
}
