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

package org.openlmis.core.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import lombok.Setter;
import org.openlmis.core.R;

public class SimpleSelectDialogFragment extends BaseDialogFragment {

  public static final String SELECTIONS = "selections";

  @Setter
  private AdapterView.OnItemClickListener movementTypeOnClickListener;
  private String[] selections;

  public SimpleSelectDialogFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      selections = getArguments().getStringArray(SELECTIONS);
    }
  }

  public SimpleSelectDialogFragment(AdapterView.OnItemClickListener movementTypeOnClickListener,
      String[] selections) {
    this.movementTypeOnClickListener = movementTypeOnClickListener;
    this.selections = selections;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
    ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), R.layout.item_movement_type,
        R.id.tv_option, selections);

    builder.setAdapter(adapter, null);
    final AlertDialog alertDialog = builder.create();

    alertDialog.getListView().setOnItemClickListener(movementTypeOnClickListener);
    return alertDialog;
  }
}
