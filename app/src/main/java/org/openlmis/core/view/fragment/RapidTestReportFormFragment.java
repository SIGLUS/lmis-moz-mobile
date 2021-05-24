package org.openlmis.core.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.openlmis.core.model.Period;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.openlmis.core.R;
import org.openlmis.core.presenter.BaseReportPresenter;
import org.openlmis.core.presenter.RapidTestReportFormPresenter;
import org.openlmis.core.utils.Constants;
import org.openlmis.core.utils.DateUtil;
import org.openlmis.core.utils.ToastUtil;
import org.openlmis.core.view.adapter.RapidTestReportBodyLeftHeaderAdapter;
import org.openlmis.core.view.adapter.RapidTestReportRowAdapter;
import org.openlmis.core.view.holder.RapidTestReportGridViewHolder;
import org.openlmis.core.view.viewmodel.RapidTestReportViewModel;
import org.openlmis.core.view.widget.RapidTestRnrForm;
import org.openlmis.core.view.widget.RnrFormHorizontalScrollView;
import org.openlmis.core.view.widget.SingleClickButtonListener;

import roboguice.RoboGuice;
import roboguice.inject.InjectView;
import rx.Subscription;
import rx.functions.Action1;

public class RapidTestReportFormFragment extends BaseReportFragment {
    private static final String TAG = RapidTestReportFormFragment.class.getSimpleName();

    @InjectView(R.id.rapid_test_top_scrollview)
    RnrFormHorizontalScrollView rapidTestTopScrollView;

    @InjectView(R.id.rapid_view_basic_item_header)
    LinearLayout rnrBasicItemHeader;

    @InjectView(R.id.rapid_view_basic_item_header_left)
    TextView rnrBasicItemHeaderLeft;

    @InjectView(R.id.rapid_test_rnr_form)
    protected RapidTestRnrForm rnrBasicItemListView;

    @InjectView(R.id.vg_rapid_test_report_empty_header)
    ViewGroup emptyHeaderView;

    @InjectView(R.id.vg_rapid_test_report_body_left_header)
    ViewGroup rapidTestBodyLeftHeader;

    @InjectView(R.id.rapid_test_body_left_list)
    RecyclerView rapidTestBodyLeftListView;

    @InjectView(R.id.rv_rapid_report_row_item_list)
    RecyclerView rvReportRowItemListView;

    RapidTestReportFormPresenter presenter;

    RapidTestReportRowAdapter rapidBodyRightAdapter;

    RapidTestReportBodyLeftHeaderAdapter rapidBodyLeftAdapter;

    public static int ROW_HEADER_WIDTH = -1;

    public static int GRID_SIZE = -1;

    @Override
    protected BaseReportPresenter injectPresenter() {
        presenter = RoboGuice.getInjector(getActivity()).getInstance(RapidTestReportFormPresenter.class);
        return presenter;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loading();
        long formId = getActivity().getIntent().getLongExtra(Constants.PARAM_FORM_ID, 0L);
        DateTime periodBegin = (DateTime) getActivity().getIntent().getSerializableExtra(Constants.PARAM_PERIOD_BEGIN);
        Period period = (Period) getActivity().getIntent().getSerializableExtra(Constants.PARAM_PERIOD);
        if (period != null) {
            getActivity().setTitle(getString(R.string.label_rapid_test_title,
                    DateUtil.formatDateWithoutYear(period.getBegin().toDate()),
                    DateUtil.formatDateWithoutYear(period.getEnd().toDate())));
        }


        updateHeaderSize();
        setUpRowItems();
        setUpBodyLeftItems();
        rvReportRowItemListView.setNestedScrollingEnabled(false);
        if (isSavedInstanceState && presenter.getViewModel() != null) {
            updateUI();
        } else {
            loadForm(formId, period);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rapid_test_report_form, container, false);
    }

    @Override
    public void onDestroyView() {
        rnrBasicItemListView.removeListenerOnDestroyView();
        super.onDestroyView();
    }

    private void updateHeaderSize() {
        calculateRowHeaderAndGridSize();
        emptyHeaderView.getLayoutParams().width = ROW_HEADER_WIDTH;
        rapidTestBodyLeftHeader.getLayoutParams().width = ROW_HEADER_WIDTH;
    }

    private void calculateRowHeaderAndGridSize() {
        int totalWidthWithoutBorders = (int) (getResources().getDimension(R.dimen.rapid_view_width));
        GRID_SIZE = totalWidthWithoutBorders / 4;
        ROW_HEADER_WIDTH = (int) getResources().getDimension(R.dimen.rapid_view_Header_view);
    }

    private void loadForm(long formId, Period period) {
        loading();
        Subscription subscription = presenter.loadViewModel(formId, period).subscribe(getOnViewModelLoadedAction());
        subscriptions.add(subscription);
    }

    private void setUpRowItems() {
        rapidBodyRightAdapter = new RapidTestReportRowAdapter(getQuantityChangeListener());
        rvReportRowItemListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvReportRowItemListView.setAdapter(rapidBodyRightAdapter);
    }

    private void setUpBodyLeftItems() {
        rapidBodyLeftAdapter = new RapidTestReportBodyLeftHeaderAdapter();
        rapidTestBodyLeftListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rapidTestBodyLeftListView.setAdapter(rapidBodyLeftAdapter);

    }

    private RapidTestReportGridViewHolder.QuantityChangeListener getQuantityChangeListener() {
        return (columnCode, gridColumnCode) -> {
            presenter.getViewModel().updateTotal(columnCode, gridColumnCode);
            presenter.getViewModel().updateAPEWaring();
            rapidBodyRightAdapter.updateTotal();
            rapidBodyRightAdapter.updateAPE();
        };
    }

    private void updateActionPanel() {
        actionPanelView.setVisibility(presenter.getViewModel().getStatus().isEditable() ? View.VISIBLE : View.GONE);
        updateButtonName();
        actionPanelView.setListener(getOnCompleteClickListener(), getOnSaveClickListener());
    }

    @NonNull
    private SingleClickButtonListener getOnSaveClickListener() {
        return new SingleClickButtonListener() {
            @Override
            public void onSingleClick(View v) {
                onSaveForm();
            }
        };
    }

    @NonNull
    private SingleClickButtonListener getOnCompleteClickListener() {
        return new SingleClickButtonListener() {
            @Override
            public void onSingleClick(View v) {
                String errorMessage = showErrorMessage();
                if (!StringUtils.isEmpty(errorMessage)) {
                    ToastUtil.show(errorMessage);
                    return;
                }
                showSignDialog();
            }

            private String showErrorMessage() {
                String errorMessage = "";
                if (!rnrBasicItemListView.isCompleted()) {
                    errorMessage = getString(R.string.error_empty_rapid_test_product);
                } else if (presenter.getViewModel().isFormEmpty()) {
                    errorMessage = getString(R.string.error_empty_rapid_test_list);
                } else if (!presenter.getViewModel().validatePositive()) {
                    errorMessage = getString(R.string.error_positive_larger_than_consumption);
                } else if (!presenter.getViewModel().validateUnjustified()) {
                    errorMessage = getString(R.string.error_rapid_test_unjustified);
                } else if (!presenter.getViewModel().validateAPES()) {
                    errorMessage = getString(R.string.error_rapid_test_ape);
                } else if (presenter.getViewModel().validateOnlyAPES()) {
                    errorMessage = getString(R.string.error_rapid_test_only_ape);
                }

                return errorMessage;
            }
        };
    }

    public void onSaveForm() {
        loading();
        Subscription subscription = presenter.onAuthoriseDraftForm().subscribe(getOnSavedAction());
        subscriptions.add(subscription);
    }

    public void onSubmitForm() {
        loading();
        Subscription subscription = presenter.onSaveDraftForm().subscribe(getOnSubmittedAction());
        subscriptions.add(subscription);
    }

    private Action1<? super RapidTestReportViewModel> getOnSubmittedAction() {
        return (Action1<RapidTestReportViewModel>) viewModel -> {
            showMessageNotifyDialog();
            updateUIAfterSubmit();
            loaded();
        };
    }

    private void updateButtonName() {
        actionPanelView.setPositiveButtonText(presenter.getViewModel().isDraft() ? getResources().getString(R.string.btn_submit) : getResources().getString(R.string.btn_complete));
    }

    @Override
    protected String getSignatureDialogTitle() {
        return presenter.getViewModel().isDraft() ? getResources().getString(R.string.msg_rapid_test_submit_signature) : getResources().getString(R.string.msg_approve_signature_rapid_test);
    }

    protected Action1<? super Void> getOnSignedAction() {
        return (Action1<Void>) aVoid -> {
            if (presenter.getViewModel().isAuthorized()) {
                onSaveForm();
            } else {
                onSubmitForm();
            }
        };
    }

    public void updateUIAfterSubmit() {
        rapidBodyRightAdapter.notifyDataSetChanged();
        rapidBodyLeftAdapter.notifyDataSetChanged();
        updateButtonName();
    }

    @Override
    protected String getNotifyDialogMsg() {
        return getString(R.string.msg_requisition_signature_message_notify_rapid_test);
    }

    private Action1<? super RapidTestReportViewModel> getOnSavedAction() {
        return (Action1<RapidTestReportViewModel>) viewModel -> {
            loaded();
            finish();
        };
    }

    @NonNull
    private Action1<RapidTestReportViewModel> getOnViewModelLoadedAction() {
        return viewModel -> {
            updateUI();
            if (presenter.isSubmitted()) {
                showMessageNotifyDialog();
            }
        };
    }

    public void updateUI() {
        RapidTestReportViewModel viewModel = presenter.getViewModel();

        if (viewModel.getBasicItems().size() > 0) {
            rnrBasicItemHeader.setVisibility(View.VISIBLE);
            int width = (int)(rnrBasicItemHeader.getWidth()*0.7);
            rnrBasicItemHeaderLeft.setMaxWidth(width);
            rnrBasicItemListView.setVisibility(View.VISIBLE);
            rnrBasicItemListView.initView(viewModel.getBasicItems());
        } else {
            rnrBasicItemHeader.setVisibility(View.GONE);
            rnrBasicItemListView.setVisibility(View.GONE);
        }
        initListener();
        populateFormData(viewModel);
        updateActionPanel();
        loaded();
    }

    private void initListener() {
        rapidTestTopScrollView.setOnScrollChangedListener((l, t, oldL, oldT) -> rnrBasicItemListView.getLeftHeaderScrollView().scrollBy(l - oldL, 0));
        rapidTestBodyLeftListView.setOnTouchListener((v, event) -> true);

        combineRowItemListViewAndLeftListView();
    }

    private void combineRowItemListViewAndLeftListView() {
        rvReportRowItemListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int moveState = RecyclerView.SCROLL_STATE_IDLE;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int rvRightFistVisibleIndex = ((LinearLayoutManager) rvReportRowItemListView.getLayoutManager()).findFirstVisibleItemPosition();
                int rvRightLastVisibleIndex = ((LinearLayoutManager) rvReportRowItemListView.getLayoutManager()).findLastVisibleItemPosition();
                int rvRightRowSize = rvReportRowItemListView.getAdapter().getItemCount();
                int rvReportLeftFistVisibleIndex = ((LinearLayoutManager) rapidTestBodyLeftListView.getLayoutManager()).findFirstVisibleItemPosition();

                rapidTestBodyLeftListView.scrollBy(dx, dy);
                if (rvRightFistVisibleIndex != rvReportLeftFistVisibleIndex
                        && (rvRightLastVisibleIndex == rvRightRowSize - 1) //right scroll to the end
                        && moveState == RecyclerView.SCROLL_STATE_IDLE) {// moving stopped
                    rapidTestBodyLeftListView.smoothScrollToPosition(rvRightLastVisibleIndex);
                    rvReportRowItemListView.smoothScrollToPosition(rvRightLastVisibleIndex);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                moveState = newState;
            }
        });
    }

    private void populateFormData(RapidTestReportViewModel viewModel) {
        rapidBodyRightAdapter.refresh(viewModel);
        rapidBodyLeftAdapter.refresh(viewModel.getItemViewModelList());
    }

}
