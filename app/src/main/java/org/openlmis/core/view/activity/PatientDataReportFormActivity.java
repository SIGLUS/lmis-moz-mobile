package org.openlmis.core.view.activity;

import android.content.Context;
import android.content.Intent;

import org.joda.time.DateTime;
import org.openlmis.core.R;
import org.openlmis.core.googleAnalytics.ScreenName;
import org.openlmis.core.utils.Constants;
import org.openlmis.core.view.fragment.RapidTestReportFormFragment;

import roboguice.inject.ContentView;

@ContentView(R.layout.activity_rapid_test_report_form)
public class RapidTestReportFormActivity extends BaseActivity {

    @Override
    protected ScreenName getScreenName() {
        return ScreenName.RapidTestReportFormScreen;
    }

    @Override
    protected int getThemeRes() {
        return R.style.AppTheme_BlueGray;
    }

    @Override
    public void onBackPressed() {
        ((RapidTestReportFormFragment) getFragmentManager().findFragmentById(R.id.fragment_rapid_test_report_form)).onBackPressed();
    }

    public static Intent getIntentToMe(Context context, long formId, DateTime periodBegin) {
        Intent intent = new Intent(context, RapidTestReportFormActivity.class);
        intent.putExtra(Constants.PARAM_FORM_ID, formId);
        intent.putExtra(Constants.PARAM_PERIOD_BEGIN, periodBegin);
        return intent;
    }
}
