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
package org.openlmis.core.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.AbstractModule;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlmis.core.LMISTestRunner;
import org.openlmis.core.exceptions.LMISException;
import org.openlmis.core.manager.UserInfoMgr;
import org.openlmis.core.model.BaseInfoItem;
import org.openlmis.core.model.Product;
import org.openlmis.core.model.Program;
import org.openlmis.core.model.Regimen;
import org.openlmis.core.model.RegimenItem;
import org.openlmis.core.model.RnRForm;
import org.openlmis.core.model.RnRFormSignature;
import org.openlmis.core.model.RnrFormItem;
import org.openlmis.core.model.User;
import org.openlmis.core.model.repository.ProductRepository;
import org.openlmis.core.model.repository.ProgramRepository;
import org.openlmis.core.model.repository.RnrFormRepository;
import org.openlmis.core.model.repository.RnrFormSignatureRepository;
import org.openlmis.core.network.adapter.RnrFormAdapter;
import org.openlmis.core.utils.Constants;
import org.openlmis.core.utils.DateUtil;
import org.openlmis.core.utils.JsonFileReader;
import org.robolectric.RuntimeEnvironment;
import roboguice.RoboGuice;

@RunWith(LMISTestRunner.class)
@SuppressWarnings("PMD")
public class RnrFormAdapterTest {

  private RnrFormAdapter rnrFormAdapter;
  private RnRForm rnRForm;
  private ProductRepository mockProductRepository;
  private ProgramRepository mockProgramRepository;
  private RnrFormRepository mockRnrFormRepository;
  private RnrFormSignatureRepository mockRnrFormSignatureRepository;

  @Before
  public void setUp() throws LMISException {
    mockProductRepository = mock(ProductRepository.class);
    mockProgramRepository = mock(ProgramRepository.class);
    mockRnrFormRepository = mock(RnrFormRepository.class);
    mockRnrFormSignatureRepository = mock(RnrFormSignatureRepository.class);

    RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, new MyTestModule());
    rnrFormAdapter = RoboGuice.getInjector(RuntimeEnvironment.application)
        .getInstance(RnrFormAdapter.class);
    rnRForm = new RnRForm();
    UserInfoMgr.getInstance().setUser(new User("user", "password"));
    Program program = new Program();
    program.setProgramCode(Constants.MMIA_PROGRAM_CODE);
    rnRForm.setProgram(program);
  }

  @Test
  public void shouldSerializeRnrFormWithCommentsToJsonObject() throws LMISException {
    rnRForm.setComments("XYZ");
    rnRForm.setSubmittedTime(DateUtil.today());
    rnRForm.setEmergency(true);

    JsonElement rnrJson = rnrFormAdapter.serialize(rnRForm, RnRForm.class, null);
    assertEquals("\"XYZ\"", rnrJson.getAsJsonObject().get("clientSubmittedNotes").toString());
    assertEquals("true", rnrJson.getAsJsonObject().get("emergency").toString());
  }

  @Test
  public void shouldSerializeRnrFormWithSubmittedTime() throws Exception {

    rnRForm.setSubmittedTime(DateUtil.parseString("2015-10-14 01:01:11", "yyyy-MM-dd HH:mm:ss"));

    JsonElement rnrJson = rnrFormAdapter.serialize(rnRForm, RnRForm.class, null);
    assertThat(rnrJson.getAsJsonObject().get("clientSubmittedTime").toString(),
        is("\"2015-10-14 01:01:11\""));
  }

  @Test
  public void shouldSerializeRnrFormWithExpirationDate() throws Exception {

    ArrayList<RnrFormItem> rnrFormItemListWrapper = new ArrayList<>();
    RnrFormItem rnrFormItem = new RnrFormItem();
    Product product = new Product();
    product.setCode("P1");
    rnrFormItem.setProduct(product);
    rnrFormItem.setValidate("10/11/2015");
    rnrFormItemListWrapper.add(rnrFormItem);
    rnRForm.setRnrFormItemListWrapper(rnrFormItemListWrapper);

    rnRForm.setSubmittedTime(DateUtil.parseString("2015-10-14 01:01:11", "yyyy-MM-dd HH:mm:ss"));

    JsonElement rnrJson = rnrFormAdapter.serialize(rnRForm, RnRForm.class, null);
    assertThat(rnrJson.getAsJsonObject().get("products").getAsJsonArray().get(0).getAsJsonObject()
        .get("expirationDate").toString(), is("\"10/11/2015\""));
  }


  @Test
  public void shouldSerializeRnrFormWithRnrFormItem() throws Exception {
    ArrayList<RnrFormItem> rnrFormItemListWrapper = new ArrayList<>();
    RnrFormItem object = new RnrFormItem();
    Product product = new Product();
    product.setCode("P1");
    object.setProduct(product);
    object.setInventory(100L);
    object.setValidate("10/11/2015");
    object.setRequestAmount(111L);
    object.setApprovedAmount(222L);
    rnrFormItemListWrapper.add(object);
    rnRForm.setRnrFormItemListWrapper(rnrFormItemListWrapper);

    JsonElement rnrJson = rnrFormAdapter.serialize(rnRForm, RnRForm.class, null);

    JsonObject regimens = rnrJson.getAsJsonObject().get("products").getAsJsonArray().get(0)
        .getAsJsonObject();
    assertThat(regimens.get("productCode").toString(), is("\"P1\""));
    assertThat(regimens.get("stockInHand").toString(), is("100"));
    assertThat(regimens.get("reasonForRequestedQuantity").toString(), is("\"reason\""));
    assertThat(regimens.get("expirationDate").toString(), is("\"10/11/2015\""));
    assertThat(regimens.get("quantityRequested").toString(), is("111"));
    assertThat(regimens.get("quantityApproved").toString(), is("222"));

  }

  @Test
  public void shouldSerializeRnrFormWithRegimen() throws Exception {
    ArrayList<RegimenItem> regimenItems = new ArrayList<>();
    RegimenItem item = new RegimenItem();
    Regimen regimen = new Regimen();
    regimen.setName("name1");
    regimen.setCode("code1");
    item.setRegimen(regimen);
    item.setAmount(10L);
    regimenItems.add(item);
    rnRForm.setRegimenItemListWrapper(regimenItems);

    JsonElement rnrJson = rnrFormAdapter.serialize(rnRForm, RnRForm.class, null);

    JsonObject regimens = rnrJson.getAsJsonObject().get("regimens").getAsJsonArray().get(0)
        .getAsJsonObject();
    assertThat(regimens.get("code").toString(), is("\"code1\""));
    assertThat(regimens.get("name").toString(), is("\"name1\""));
    assertThat(regimens.get("patientsOnTreatment").toString(), is("10"));
  }

  @Test
  public void shouldSerializeRnrFormWithBaseInfo() throws Exception {
    ArrayList<BaseInfoItem> baseInfoItems = new ArrayList<>();
    BaseInfoItem baseInfoItem = new BaseInfoItem();
    baseInfoItem.setValue("Value1");
    baseInfoItem.setName("Name1");
    baseInfoItems.add(baseInfoItem);
    rnRForm.setBaseInfoItemListWrapper(baseInfoItems);

    JsonElement rnrJson = rnrFormAdapter.serialize(rnRForm, RnRForm.class, null);

    JsonObject patientQuantifications = rnrJson.getAsJsonObject().get("patientQuantifications")
        .getAsJsonArray().get(0).getAsJsonObject();
    assertThat(patientQuantifications.get("category").toString(), is("\"Name1\""));
    assertThat(patientQuantifications.get("total").toString(), is("\"Value1\""));
  }

  @Test
  public void shouldSerializeRnrFormSignatureWithBaseInfo() throws Exception {
    List<RnRFormSignature> rnRFormSignatureList = new ArrayList<>();
    rnRFormSignatureList.add(new RnRFormSignature(rnRForm, "abc", RnRFormSignature.TYPE.SUBMITTER));

    when(mockRnrFormSignatureRepository.queryByRnrFormId(any(Long.class)))
        .thenReturn(rnRFormSignatureList);
    JsonElement rnrJson = rnrFormAdapter.serialize(rnRForm, RnRForm.class, null);
    JsonObject rnrSignature = rnrJson.getAsJsonObject().get("rnrSignatures").getAsJsonArray().get(0)
        .getAsJsonObject();

    assertThat(rnrSignature.get("text").toString(), is("\"abc\""));
    assertThat(rnrSignature.get("type").toString(),
        is("\"" + RnRFormSignature.TYPE.SUBMITTER.toString() + "\""));
  }

  @Test
  public void shouldDeserializeRnrFormJson() throws LMISException {
    when(mockProductRepository.getByCode(anyString())).thenReturn(new Product());
    when(mockProgramRepository.queryByCode(anyString())).thenReturn(new Program());

    String json = JsonFileReader.readJson(getClass(), "RequisitionResponse.json");

    RnRForm rnRForm = rnrFormAdapter.deserialize(new JsonParser().parse(json), null, null);
    assertThat(rnRForm.getComments(), is("I don't know"));

    RnrFormItem rnrFormItem = rnRForm.getRnrFormItemListWrapper().get(0);
    assertThat(rnrFormItem.getInitialAmount(), is(10L));
    assertThat(rnrFormItem.getInventory(), is(10L));
    assertThat(rnrFormItem.getRequestAmount(), is(20L));
    assertThat(rnrFormItem.getApprovedAmount(), is(30L));
    verify(mockProductRepository).getByCode("08S17");

    RegimenItem regimenItem = rnRForm.getRegimenItemListWrapper().get(0);
    assertThat(regimenItem.getAmount(), is(1L));
    assertThat(regimenItem.getRegimen().getName(), is("ABC+3TC+EFZ"));
    assertThat(regimenItem.getRegimen().getCode(), is("018"));

    BaseInfoItem baseInfoItem = rnRForm.getBaseInfoItemListWrapper().get(0);
    assertThat(baseInfoItem.getName(), is("Total Patients"));
    assertThat(baseInfoItem.getValue(), is("30"));

    assertThat(rnRForm.getSubmittedTime(), is(new Date(1445937080000L)));

    assertThat(rnRForm.getPeriodBegin(), is(new Date(1455937080000L)));
    assertThat(rnRForm.getPeriodEnd(), is(new Date(1465937080000L)));
  }


  @Test
  public void shouldSerializeRnrFormWithPeriodDate() throws Exception {
    UserInfoMgr.getInstance().setUser(new User("user", "password"));

    rnRForm.setPeriodBegin(DateUtil.parseString("2015-10-15 01:01:11", "yyyy-MM-dd HH:mm:ss"));
    rnRForm.setPeriodEnd(DateUtil.parseString("2015-11-15 01:01:11", "yyyy-MM-dd HH:mm:ss"));

    JsonElement rnrJson = rnrFormAdapter.serialize(rnRForm, RnRForm.class, null);
    assertNotNull(rnrJson.getAsJsonObject().get("actualPeriodStartDate"));
    assertNotNull(rnrJson.getAsJsonObject().get("actualPeriodEndDate"));
  }

  @Test
  public void shouldSerializeRnrFormWithoutSubmittedTime() throws Exception {
    UserInfoMgr.getInstance().setUser(new User("user", "password"));

    rnRForm.setSubmittedTime(null);

    JsonElement rnrJson = rnrFormAdapter.serialize(rnRForm, RnRForm.class, null);
    assertNull(rnrJson.getAsJsonObject().get("clientSubmittedTime"));
  }

  public class MyTestModule extends AbstractModule {

    @Override
    protected void configure() {
      bind(ProductRepository.class).toInstance(mockProductRepository);
      bind(ProgramRepository.class).toInstance(mockProgramRepository);
      bind(RnrFormRepository.class).toInstance(mockRnrFormRepository);
      bind(RnrFormSignatureRepository.class).toInstance(mockRnrFormSignatureRepository);
    }
  }

}
