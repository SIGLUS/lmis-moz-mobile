package org.openlmis.core.view.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.openlmis.core.R;
import org.openlmis.core.view.viewmodel.malaria.PatientDataReportViewModel;

import roboguice.inject.InjectView;

public abstract class PatientDataReportViewHolderBase extends BaseViewHolder {

    @InjectView(R.id.tv_period)
    protected TextView tvPeriod;

    @InjectView(R.id.tv_report_status)
    protected TextView tvReportStatus;


    public PatientDataReportViewHolderBase(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.item_patient_data_report_pending, parent, false));
    }

    public PatientDataReportViewHolderBase(View itemView) {
        super(itemView);
    }

    public abstract void populate(final PatientDataReportViewModel patientDataReportViewModel);

}
