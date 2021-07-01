package org.openlmis.core.model.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.inject.AbstractModule;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlmis.core.LMISTestApp;
import org.openlmis.core.LMISTestRunner;
import org.openlmis.core.exceptions.LMISException;
import org.openlmis.core.model.Period;
import org.openlmis.core.model.Program;
import org.openlmis.core.model.ReportTypeForm;
import org.openlmis.core.model.builder.ReportTypeFormBuilder;
import org.openlmis.core.model.repository.ReportTypeFormRepository;
import org.openlmis.core.model.repository.StockMovementRepository;
import org.openlmis.core.model.repository.StockRepository;
import org.openlmis.core.utils.Constants;
import org.openlmis.core.utils.DateUtil;
import org.robolectric.RuntimeEnvironment;
import roboguice.RoboGuice;

@RunWith(LMISTestRunner.class)
public class ProgramDataFormPeriodServiceTest {

  private StockRepository mockStockRepository;

  ProgramDataFormPeriodService periodService;
  private StockMovementRepository mockStockMovementRepository;
  private ReportTypeFormRepository mockReportTypeFormRepository;

  @Before
  public void setup() throws LMISException {
    mockStockRepository = mock(StockRepository.class);
    mockStockMovementRepository = mock(StockMovementRepository.class);
    mockReportTypeFormRepository = mock(ReportTypeFormRepository.class);
    RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, new MyTestModule());
    periodService = RoboGuice.getInjector(RuntimeEnvironment.application)
        .getInstance(ProgramDataFormPeriodService.class);
  }


  @Test
  public void shouldGenerate12MonthsAgoPeriodBasedOnLastReportEndDate() throws Exception {
    LMISTestApp.getInstance().setCurrentTimeMillis(
        DateUtil.parseString("2021-05-24 12:00:00", DateUtil.DB_DATE_FORMAT).getTime());

    ReportTypeForm reportTypeForm = new ReportTypeFormBuilder()
        .setActive(true)
        .setCode(Program.RAPID_TEST_CODE)
        .setName(Constants.RAPID_TEST_OLD_CODE)
        .setStartTime(
            new DateTime(DateUtil.parseString("2015-01-01", DateUtil.DB_DATE_FORMAT)).toDate())
        .setLastReportEndTime("2020-01-20 23:59:59")
        .build();
    when(mockReportTypeFormRepository.queryByCode(Program.RAPID_TEST_CODE)).thenReturn(reportTypeForm);
    when(mockReportTypeFormRepository.getReportType(anyString())).thenReturn(reportTypeForm);
    Period period = periodService.getFirstStandardPeriod().get();
    final DateTime currentDate = new DateTime();
    String beginDateBuilder = (currentDate.year().get() - 1)
        + "-"
        + "06"
        + "-21 12:00:00";
    String endDateBuilder = (currentDate.year().get() - 1)
        + "-"
        + "07"
        + "-20 12:00:00";
    final DateTime expectBeginTime = new DateTime(DateUtil.parseString(beginDateBuilder, DateUtil.DB_DATE_FORMAT));
    assertThat(period.getBegin(), is(expectBeginTime));
    final DateTime expectEndTime = new DateTime(DateUtil.parseString(endDateBuilder, DateUtil.DB_DATE_FORMAT));
    assertThat(period.getEnd(), is(expectEndTime));
  }

  @Test
  public void shouldGenerateNextPeriodShouldNotGenerateNextPeriodIfDateIsBefore18th()
      throws Exception {
    LMISTestApp.getInstance().setCurrentTimeMillis(
        DateUtil.parseString("2016-09-16", DateUtil.DB_DATE_FORMAT).getTime());

    //2016-7-21 to 2016-8-20
    Period period = Period.of(DateUtil.parseString("2016-08-12", DateUtil.DB_DATE_FORMAT));

    assertThat(period.generateNextAvailablePeriod().isPresent(), is(false));
  }

  @Test
  public void shouldGenerateNextPeriodShouldGenerateNextPeriodIfDateIsAfter18th() throws Exception {
    LMISTestApp.getInstance().setCurrentTimeMillis(
        DateUtil.parseString("2016-09-19", DateUtil.DB_DATE_FORMAT).getTime());

    //2016-7-21 to 2016-8-20
    Period period = Period.of(DateUtil.parseString("2016-08-12", DateUtil.DB_DATE_FORMAT));

    Period nextPeriod = period.generateNextAvailablePeriod().get();
    assertThat(nextPeriod.getBegin(),
        is(new DateTime(DateUtil.parseString("2016-08-21", DateUtil.DB_DATE_FORMAT))));
    assertThat(nextPeriod.getEnd(),
        is(new DateTime(DateUtil.parseString("2016-09-20", DateUtil.DB_DATE_FORMAT))));
  }


  public class MyTestModule extends AbstractModule {

    @Override
    protected void configure() {
      bind(StockRepository.class).toInstance(mockStockRepository);
      bind(StockMovementRepository.class).toInstance(mockStockMovementRepository);
      bind(ReportTypeFormRepository.class).toInstance(mockReportTypeFormRepository);
    }
  }
}