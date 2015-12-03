/*
 * This program is part of the OpenLMIS logistics management information
 * system platform software.
 *
 * Copyright © 2015 ThoughtWorks, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version. This program is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details. You should
 * have received a copy of the GNU Affero General Public License along with
 * this program. If not, see http://www.gnu.org/licenses. For additional
 * information contact info@OpenLMIS.org
 */
package org.openlmis.core.view.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.openlmis.core.R;
import org.openlmis.core.exceptions.LMISException;
import org.openlmis.core.model.Product;
import org.openlmis.core.model.RnrFormItem;
import org.openlmis.core.utils.DateUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;

public class MMIARnrForm extends LinearLayout {
    private ViewGroup leftViewGroup;

    @Getter
    private ViewGroup rightViewGroup;
    private LayoutInflater layoutInflater;

    @Getter
    private RnrFormHorizontalScrollView rnrItemsHorizontalScrollView;
    private HashMap<String, List<String>> rnrFormItemConfigList = new HashMap<>();

    @Getter
    private View leftHeaderView;
    @Getter
    private ViewGroup rightHeaderView;

    public MMIARnrForm(Context context) {
        super(context);
        init(context);
    }

    public MMIARnrForm(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        layoutInflater = LayoutInflater.from(context);
        View container = layoutInflater.inflate(R.layout.view_mmia_rnr_form, this, true);
        rnrItemsHorizontalScrollView = (RnrFormHorizontalScrollView) container.findViewById(R.id.vg_right_scrollview);
        leftViewGroup = (ViewGroup) container.findViewById(R.id.rnr_from_list_product_name);
        rightViewGroup = (ViewGroup) container.findViewById(R.id.rnr_from_list);
    }

    public void initView(ArrayList<RnrFormItem> list) {
        addHeaderView();
        addItemView(list);
    }

    private void addHeaderView() {
        leftHeaderView = addLeftHeaderView();
        rightHeaderView = addRightHeaderView();
    }

    private void addItemView(ArrayList<RnrFormItem> rnrFormItemList) {
        initRnrFormItemConfigList();
        addViewByMedicineType(rnrFormItemList, Product.MEDICINE_TYPE_ADULT);
        addDividerView(Product.MEDICINE_TYPE_ADULT);
        addDividerView(Product.MEDICINE_TYPE_ADULT);

        addViewByMedicineType(rnrFormItemList, Product.MEDICINE_TYPE_BABY);
        addDividerView(Product.MEDICINE_TYPE_BABY);
        addViewByMedicineType(rnrFormItemList, Product.MEDICINE_TYPE_OTHER);
        addDividerView(Product.MEDICINE_TYPE_OTHER);
    }

    private void addViewByMedicineType(ArrayList<RnrFormItem> dataList, String medicineTypeName) {
        List<String> fnms = rnrFormItemConfigList.get(medicineTypeName);
        for (String fnm : fnms) {
            for (RnrFormItem item : dataList) {
                if (fnm.equals(item.getProduct().getCode())) {
                    View leftView = addLeftView(item, false, medicineTypeName);
                    ViewGroup rightView = addRightView(item, false);
                    setItemSize(leftView, rightView);
                }
            }
        }
    }

    private void addDividerView(String medicineType) {
        View leftView = inflaterLeftView();
        leftViewGroup.addView(leftView);
        setLeftViewColor(medicineType, leftView);
        ViewGroup rightView = inflateRightView();
        rightViewGroup.addView(rightView);
        setItemSize(leftView, rightView);
    }

    private View inflaterLeftView() {
        return layoutInflater.inflate(R.layout.item_rnr_from_product_name, this, false);
    }

    private ViewGroup inflateRightView() {
        return (ViewGroup) layoutInflater.inflate(R.layout.item_rnr_from, this, false);
    }

    public void initRnrFormItemConfigList() {
        rnrFormItemConfigList.put(Product.MEDICINE_TYPE_ADULT, Arrays.asList(getResources().getStringArray(R.array.medicine_adult)));
        rnrFormItemConfigList.put(Product.MEDICINE_TYPE_BABY, Arrays.asList(getResources().getStringArray(R.array.medicine_baby)));
        rnrFormItemConfigList.put(Product.MEDICINE_TYPE_OTHER, Arrays.asList(getResources().getStringArray(R.array.medicine_other)));
    }

    public void setItemSize(final View leftView, final ViewGroup rightView) {
        post(new Runnable() {
            @Override
            public void run() {
                setRightItemWidth(rightView);
                syncItemHeight(leftView, rightView);
            }
        });
    }

    private void setRightItemWidth(final ViewGroup rightView) {
        int rightWidth = rnrItemsHorizontalScrollView.getWidth();
        int rightViewGroupWidth = rightViewGroup.getWidth();

        if (rightViewGroupWidth < rightWidth) {
            int childCount = rightView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                rightView.getChildAt(i).getLayoutParams().width = getRightViewWidth(rightWidth, childCount);
            }
            rightView.getChildAt(0).getLayoutParams().width = getRightViewWidth(rightWidth, childCount) + getRightViewRemainderWidth(rightWidth, childCount);
        }
    }

    public void syncItemHeight(final View leftView, final View rightView) {
        int leftHeight = leftView.getHeight();
        int rightHeight = rightView.getHeight();
        if (leftHeight > rightHeight) {
            ViewGroup.LayoutParams layoutParams = rightView.getLayoutParams();
            layoutParams.height = leftHeight;
            rightView.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = leftView.getLayoutParams();
            layoutParams.height = rightHeight;
            leftView.setLayoutParams(layoutParams);

            if (rightView.getLayoutParams() instanceof FrameLayout.LayoutParams) {
                rightView.setLayoutParams(new FrameLayout.LayoutParams(layoutParams.width, layoutParams.height));
            } else {
                rightView.setLayoutParams(layoutParams);
            }
        }
    }

    public View addLeftHeaderView() {
        return addLeftView(null, true, null);
    }

    private View addLeftView(RnrFormItem item, boolean isHeaderView, String medicineType) {
        View view = inflaterLeftView();
        TextView tvPrimaryName = (TextView) view.findViewById(R.id.tv_primary_name);
        if (isHeaderView) {
            tvPrimaryName.setText(R.string.label_rnrfrom_left_header);
            tvPrimaryName.setGravity(Gravity.CENTER);
            view.setBackgroundResource(R.color.color_mmia_info_name);
        } else {
            Product product = item.getProduct();
            tvPrimaryName.setText(product.getPrimaryName());
            setLeftViewColor(medicineType, view);
            leftViewGroup.addView(view);
        }

        return view;
    }

    private void setLeftViewColor(String medicineType, View view) {
        switch (medicineType) {
            case Product.MEDICINE_TYPE_ADULT:
                view.setBackgroundResource(R.color.color_regime_adult);
                break;
            case Product.MEDICINE_TYPE_BABY:
                view.setBackgroundResource(R.color.color_regime_baby);
                break;
            case Product.MEDICINE_TYPE_OTHER:
                view.setBackgroundResource(R.color.color_regime_other);
                break;
            default:
                break;
        }
    }


    public ViewGroup addRightHeaderView() {
        return addRightView(null, true);
    }

    private ViewGroup addRightView(RnrFormItem item, boolean isHeaderView) {
        ViewGroup inflate = inflateRightView();

        TextView tvIssuedUnit = (TextView) inflate.findViewById(R.id.tv_issued_unit);
        TextView tvInitialAmount = (TextView) inflate.findViewById(R.id.tv_initial_amount);
        TextView tvReceived = (TextView) inflate.findViewById(R.id.tv_received);
        TextView tvIssued = (TextView) inflate.findViewById(R.id.tv_issued);
        TextView tvAdjustment = (TextView) inflate.findViewById(R.id.tv_adjustment);
        TextView tvInventory = (TextView) inflate.findViewById(R.id.tv_inventory);
        TextView tvValidate = (TextView) inflate.findViewById(R.id.tv_validate);

        if (isHeaderView) {
            tvIssuedUnit.setText(R.string.label_issued_unit);
            tvInitialAmount.setText(R.string.label_initial_amount);
            tvReceived.setText(R.string.label_received_mmia);
            tvIssued.setText(R.string.label_issued_mmia);
            tvAdjustment.setText(R.string.label_adjustment);
            tvInventory.setText(R.string.label_inventory);
            tvValidate.setText(R.string.label_validate);

            inflate.setBackgroundResource(R.color.color_mmia_info_name);

        } else {
            tvIssuedUnit.setText(item.getProduct().getStrength());
            tvInitialAmount.setText(String.valueOf(item.getInitialAmount()));
            tvReceived.setText(String.valueOf(item.getReceived()));
            tvIssued.setText(String.valueOf(item.getIssued()));
            tvAdjustment.setText(String.valueOf(item.getAdjustment()));
            tvInventory.setText(String.valueOf(item.getInventory()));
            rightViewGroup.addView(inflate);

            try {
                if (!TextUtils.isEmpty(item.getValidate())) {
                    tvValidate.setText(DateUtil.convertDate(item.getValidate(), "dd/MM/yyyy", "MMM yyyy"));
                }
            } catch (ParseException e) {
                new LMISException(e).reportToFabric();
            }
        }
        return inflate;
    }

    private int getRightViewWidth(int rightWidth, int childCount) {
        return (rightWidth - (childCount - 1) * getDividerWidth()) / childCount;
    }

    private int getRightViewRemainderWidth(int rightWidth, int childCount) {
        return (rightWidth - (childCount - 1) * getDividerWidth()) % childCount;
    }

    private int getDividerWidth() {
        return (int) getResources().getDimension(R.dimen.divider);
    }



}
