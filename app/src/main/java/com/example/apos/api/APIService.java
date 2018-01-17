package com.example.apos.api;

import com.example.apos.bean.clsBillListDtl;
import com.example.apos.bean.clsBillSeriesItemDtl;
import com.example.apos.bean.clsCustomerMaster;
import com.example.apos.bean.clsDirectBillSelectedListItemBean;
import com.example.apos.bean.clsItemModifierBean;
import com.example.apos.bean.clsKotItemsListBean;
import com.example.apos.bean.clsKotMenuItemsBean;
import com.example.apos.bean.clsPosSelectionMaster;
import com.example.apos.bean.clsReasonMaster;
import com.example.apos.bean.clsReprintDocumentBean;
import com.example.apos.bean.clsSalesReportDtl;
import com.example.apos.bean.clsTDHBean;
import com.example.apos.bean.clsTableMaster;
import com.example.apos.bean.clsVoidKotListDtl;
import com.example.apos.bean.clsWaiterMaster;
import com.example.apos.dto.UserDTO;
import com.example.apos.dto.clsGlobalDTO;
import com.example.apos.dto.clsTDHWithModifierDTO;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface APIService {


	@GET("prjSanguineWebService/APOSIntegration/funAuthenticateUser")
	Call<UserDTO> serverAuth(@Query("strUserCode") String userName,
							 @Query("strPassword") String password);

	@GET("prjSanguineWebService/APOSIntegration/funInvokeAPOSWebService")
	Call<Boolean> checkServer();


	@GET("prjSanguineWebService/APOSIntegration/funAuthenticateUserByScan")
	Call<UserDTO> serverAuthByQRScan(@Query("DebitCardString") String DebitCardString);

	@GET("prjSanguineWebService/APOSIntegration/funGetSetupValues")
	Call<clsGlobalDTO> funGetSetupValues(@Query("POSCode") String POSCode);

	@GET("prjSanguineWebService/APOSIntegration/funGetLastBillDate")
	Call<JsonObject> getLastBillDate();

	@GET("prjSanguineWebService/APOSIntegration/funGetTerminalRegistrationDetails")
	Call<JsonObject> getTerminalRegistrationDetails(@Query("MacAddress") String MACAddress,
													@Query("TerminalName") String terminalName, @Query("ClientCode") String clientCode);

	@GET("prjSanguineWebService/APOSIntegration/funRegisterTerminal")
	Call<JsonObject> funRegisterTerminal(@Query("HostName") String HostName,
										 @Query("MacAddress") String MacAddress, @Query("ClientCode") String clientCode,
										 @Query("UserCode") String UserCode, @Query("TerminalName") String TerminalName);



	@GET("prjSanguineWebService/APOSIntegration/funCheckAPOSLicence")
	Call<JsonObject> checkAPOSLicence(@Query("clientCode") String clientCode,@Query("physicalAddress") String physicalAddress,
									  @Query("hostName") String hostName, @Query("userCode") String userCode);


	/**
	 * FOR POS SELECTION SCREEN
	 **/

	@GET("prjSanguineWebService/APOSIntegration/funGetPosMaster")
	Call<ArrayList<clsPosSelectionMaster>> funGetPOSList(@Query("UserCode") String UserCode, @Query("clientCode") String clientCode);


	@GET("prjSanguineWebService/APOSIntegration/funGetMainMenu")
	Call<JsonObject> funGetMainMenuList(@Query("UserCode") String UserCode, @Query("ModuleType") String ModuleType,
										@Query("clientCode") String clientCode, @Query("SuperUser") boolean SuperUser,
										@Query("POSCode") String POSCode);




	@GET("prjSanguineWebService/APOSIntegration/funCheckAndGetModifierListForTDH")
	Call<ArrayList<clsTDHWithModifierDTO>> funCheckAndGetModifierListForTDH(@Query("ClientCode") String ClientCode);

	@GET("prjSanguineWebService/APOSIntegration/funGetTDHList")
	Call<JsonArray> funGetTDHListWS(@Query("clientCode") String clientCode);

	@GET("prjSanguineWebService/APOSIntegration/funGetTableList")
	Call<ArrayList<clsTableMaster>> funGetTableList(@Query("POSCode") String POSCode, @Query("CMSIntegration") String CMSIntegration,
													@Query("memberAsTable") String memberAsTable);

	@GET("prjSanguineWebService/APOSIntegration/funGetMenuHeadList")
	Call<JsonObject> funGetMenuHeadList(@Query("POSCode") String POSCode, @Query("clientCode") String clientCode);

	////// ?POSCode="+clsGlobalFunctions.gPOSCode+"&areaCode="+clsGlobalFunctions.gDirectBillerAreaCode+"&menuHeadCode="+params[0]+"&areaWisePricing="+clsGlobalFunctions.gAreaWisePricing+"&fromDate="+objGlobal.funGetCurrentDate()+"&toDate="+objGlobal.funGetCurrentDate()+"&flgAllItems="+flgAllItems+"&menuType="+menuType;
	@GET("prjSanguineWebService/APOSIntegration/funGetItemPriceDtlCounterWise")
	Call<ArrayList<clsKotItemsListBean>> funGetItemPriceDtlCounterWise(@Query("POSCode") String POSCode
			, @Query("areaCode") String areaCode, @Query("menuHeadCode") String menuHeadCode,
																	   @Query("areaWisePricing") String areaWisePricing, @Query("fromDate") String fromDate
			, @Query("toDate") String toDate, @Query("flgAllItems") boolean flgAllItems, @Query("menuType") String menuType);

	@GET("prjSanguineWebService/APOSIntegration/funGetItemPriceDtl")
	Call<ArrayList<clsKotItemsListBean>> funGetItemPriceDtl(@Query("POSCode") String POSCode
			, @Query("areaCode") String areaCode, @Query("menuHeadCode") String menuHeadCode
			, @Query("areaWisePricing") String areaWisePricing, @Query("fromDate") String fromDate
			, @Query("toDate") String toDate, @Query("flgAllItems") boolean flgAllItems, @Query("menuType") String menuType,@Query("tableNo") String tableNo);

	@GET("prjSanguineWebService/APOSIntegration/funGetCounterWiseMenu")
	Call<JsonObject> funGetCounterWiseMenuList(@Query("POSCode") String POSCode, @Query("CounterCode") String counterCode);

	@GET("prjSanguineWebService/APOSIntegration/funGetSubMenuList")
	Call<JsonObject> funGetSubMenuList(@Query("POSCode") String POSCode, @Query("clientCode") String clientCode, @Query("menuCode") String menuCode);

	@POST("prjSanguineWebService/APOSIntegration/funInsertDCTempTable")
	Call<JsonObject> funInsertDebitCartTmpDtl(@Body JSONObject objDebitDtl);

	@GET("prjSanguineWebService/APOSIntegration/funGetKOTDataForPrint")
	Call<JsonObject> funGetKOTDataFromWS(@Query("KOTNo") String KOTNo, @Query("POSCode") String POSCode);

	@GET("prjSanguineWebService/APOSIntegration/funGetPreviousKOTDetails")
	Call<JsonObject> funLoadPrevoiusKotItemList(@Query("TableNo") String TableNo);

	@POST("prjSanguineWebService/APOSIntegration/funSaveAndPrintKOT")
	Call<JsonObject>funSaveAndPrintKOT(@Body JsonObject objKOTDtl);

	@GET("prjSanguineWebService/APOSIntegration/funGetModifierList")
	Call<ArrayList<clsItemModifierBean>>funGetModifierList(@Query("ItemCode")String itemcode, @Query("clientCode")String clientCode);

	@POST("prjSanguineWebService/APOSIntegration/funCalculateTax")
	Call<JsonObject>funTaxCalculate(@Body JsonObject objTaxDtl);


	//For Sales Flash Screen

	@GET("prjSanguineWebService/APOSIntegration/funGetBillwiseSalesReport")
	Call<ArrayList<clsSalesReportDtl>>funGetBillwiseSalesReport(@Query("POSCode") String POSCode, @Query("UserCode") String userCode,
																@Query("FromDate") String fromDate, @Query("ToDate") String toDate, @Query("ReportType") String reportType);

	@GET("prjSanguineWebService/APOSIntegration/funGetTDHMenu")
	Call<ArrayList<clsTDHBean>>funGetTDHMenuList(@Query("ClientCode")String clientCode,@Query("ItemCode")String itemcode);

	@GET("prjSanguineWebService/APOSIntegration/funGenerateCheckKOTTextFile")
	Call<JsonObject>funCheckKOT(@Query("tableNo") String tableNo,@Query("POSCode")String POSCode,@Query("POSName")String POSName);

	@GET("prjSanguineWebService/APOSIntegration/funGetItemDetailsByExternalCode")
	Call<ArrayList<clsKotItemsListBean>>funGetItemDetailsByExternalCode(@Query("POSCode")String POSCode,@Query("ExternalCode") String ExternalCode,@Query("areaWisePricing")String areaWisePricing
				,@Query("tableNo")String tableNo,@Query("areaCode")String areaCode);

	/*Direct Biller Get Customer Info*/
	@GET("prjSanguineWebService/APOSIntegration/funGetCustomerDetail")
	Call<ArrayList<clsCustomerMaster>>funGetCustomerInfo(@Query("MobileNo")String MobileNo,@Query("POSCode")String POSCode,@Query("ClientCode")String ClientCode);


	/*Direct Biller Load Customer Type List*/
	@GET("prjSanguineWebService/APOSIntegration/funGetCustomerType")
	Call<ArrayList<clsCustomerMaster>>funLoadCustomerTypeList(@Query("ClientCode")String ClientCode);

	/*Direct Biller Save New Customer Dtl*/
	@GET("prjSanguineWebService/APOSIntegration/funSaveNewCustomer")
	Call<JsonObject> funSaveNewCustomerDtl(@Query("CustomerName") String customerName,@Query("MobileNo") String mobileNo,
										   @Query("CustType") String custType,@Query("Address") String address,
										   @Query("ClientCode") String clientCode,@Query("UserCode") String userCode,@Query("DateTime") String dateTime);

	//For Make Bill Screen
	@GET("prjSanguineWebService/APOSIntegration/funGetBusyTableList")
	Call<ArrayList<clsTableMaster>> funGetBusyTableList(@Query("POSCode") String POSCode, @Query("CMSIntegration") String CMSIntegration,
														@Query("memberAsTable") String memberAsTable);

	@GET("prjSanguineWebService/APOSIntegration/funGenerateBillNo")
	Call<HashMap> funGenerateBillNo(@Query("POSCode") String POSCode);

	@GET("prjSanguineWebService/APOSIntegration/funGetTableBillData")
	Call<JsonObject> funGetTableBillData(@Query("POSCode") String POSCode, @Query("TableNo") String TableNo,
										 @Query("ClientCode") String ClientCode, @Query("POSStartDate") String POSStartDate,
										 @Query("ApplyPromotion") String ApplyPromotion, @Query("ApplyPromotionToBill") String ApplyPromotionToBill);

	@POST("prjSanguineWebService/APOSIntegration/funSaveBill")
	Call<HashMap> funSaveBill(@Body JsonObject objBillDtl,@Query("billSeries") String billSeries,@Query("POSCode") String POSCode);

	@POST("prjSanguineWebService/APOSIntegration/funSaveAllBillData")
	Call<HashMap> funSaveAllBillData(@Body JsonObject objBillDtl,@Query("billSeries") String billSeries,@Query("POSCode") String POSCode);


	@GET("prjSanguineWebService/APOSIntegration/funGenerateBillTextFile")
	Call<HashMap>funGenerateBillTextFile(@Query("BillNo")String billNo,@Query("PosCode") String POSCode,@Query("ClientCode")String clientCode,@Query("reprint")String reprint);

	//For Void KOT

	@GET("prjSanguineWebService/APOSIntegration/funGetLiveKOTList")
	Call<ArrayList<clsVoidKotListDtl>> funGetLiveKOTList(@Query("POSCode") String POSCode, @Query("TableNo") String tableNo, @Query("ClientCode") String clientCode);

	@GET("prjSanguineWebService/APOSIntegration/funGetKOTData")
	Call<JsonObject> funGetKOTData(@Query("POSCode") String POSCode, @Query("KOTNo") String KOTNo,@Query("ClientCode") String clientCode);

	@POST("prjSanguineWebService/APOSIntegration/funVoidKOT")
	Call<HashMap>funVoidKOT(@Body JsonObject objVoidKOTDtl);

	//For Bill Settlement Form

	@GET("prjSanguineWebService/APOSIntegration/funGetBillList")
	Call<ArrayList<clsBillListDtl>>funGetBillList(@Query("ClientCode") String clientCode, @Query("POSCode") String POSCode);

	@GET("prjSanguineWebService/APOSIntegration/funGetBillDtl")
	Call<ArrayList<clsBillListDtl>> funGetBillDtl(@Query("ClientCode") String clientCode, @Query("BillNo") String BillNo);

	@POST("prjSanguineWebService/APOSIntegration/funInsertBillTaxDtl")
	Call<HashMap> funInsertBillTaxDtl(@Body JsonObject objTaxDtl);

	@POST("prjSanguineWebService/APOSIntegration/funCalculatePromotion")
	Call<HashMap> funCalculatePromotion(@Body JsonObject objPromotionDtl);


	@GET("prjSanguineWebService/APOSIntegration/funGenerateDirectBillerKOTTextFile")
	Call<HashMap>funGenerateDirectBillerKOTTextFile(@Query("POSCode")String POSCode,@Query("BillNo") String BillNo,@Query("AreaCode")String AreaCode,@Query("reprint")String reprint);

	@GET("prjSanguineWebService/APOSIntegration/funCallSearchForm")
	Call<ArrayList<clsTableMaster>> funGetUnReservedTableList(@Query("FormName") String FormName,@Query("POSCode") String POSCode,@Query("ClientCode") String ClientCode);

	@POST("prjSanguineWebService/APOSIntegration/funSaveReservation")
	Call<JsonObject> funDoneReservation(@Body JsonObject jObjReservation);

	@POST("prjSanguineWebService/WebPOSTransactions/funSendSMS")
	Call<JsonObject> funSendSMSWS(@Body JsonObject objSendSMSDtl);

	@GET("prjSanguineWebService/APOSIntegration/funGetReservationDetail")
	Call<JsonArray> funGetReservationDetail(@Query("POSCode")String POSCode,@Query("FromDate")String FromDate,@Query("ToDate")String ToDate,
											@Query("FromTime")String FromTime,@Query("ToTime")String ToTime);


	@GET("prjSanguineWebService/APOSIntegration/funGetWaiterList")
	Call<ArrayList<clsWaiterMaster>> funGetWaiterList(@Query("POSCode") String POSCode, @Query("UserCode") String UserCode, @Query("Applicable") boolean applicable);


	@GET("prjSanguineWebService/APOSIntegration/funGetReasonList")
	Call<ArrayList<clsReasonMaster>> funGetReasonList(@Query("Type") String type, @Query("clientCode") String clientCode);


	//For Mini Make KOT Screen
	@GET("prjSanguineWebService/APOSIntegration/funGetItemPriceDtlForCustomerOrder")
	Call<ArrayList<clsKotItemsListBean>> funGetItemPriceDtlForCustomerOrder(@Query("POSCode") String POSCode
			, @Query("areaCode") String areaCode, @Query("menuHeadCode") String menuHeadCode
			, @Query("areaWisePricing") String areaWisePricing, @Query("fromDate") String fromDate
			, @Query("toDate") String toDate, @Query("flgAllItems") boolean flgAllItems, @Query("menuType") String menuType);

	//For KDS

	@GET("prjSanguineWebService/APOSIntegration/funCallSearchForm")
	Call<JsonArray> funCallSearchForm(@Query("FormName") String FormName,@Query("POSCode") String POSCode,@Query("ClientCode") String ClientCode);

	@GET("prjSanguineWebService/APOSIntegration/funGetKOTDetailsForKDS")
	Call<JsonObject> funGetKOTDetailsForKDS(@Query("POSCode") String POSCode, @Query("CostCenterCode") String CostCenterCode,@Query("POSDate") String POSDate);

	@POST("prjSanguineWebService/APOSIntegration/funUpdateProcessedKOTItemsForKDS")
	Call<JsonObject>funUpdateProcessedKOTItemsForKDS(@Body JsonObject objKDSDtl);

	@GET("prjSanguineWebService/APOSIntegration/funGetProcessedKOTDetailsForKPS")
	Call<JsonObject> funGetProcessedKOTDetailsForKPS(@Query("POSCode") String POSCode, @Query("WaiterNo") String WaiterNo,@Query("POSDate") String POSDate);

	@POST("prjSanguineWebService/APOSIntegration/funUpdatePickedUpKOTItemsForKPS")
	Call<JsonObject>funUpdatePickedUpKOTItemsForKPS(@Body JsonObject objKPSDtl);

	//For Sales Flash Screen

	@GET("prjSanguineWebService/APOSIntegration/funSalesReport")
	Call<ArrayList<clsSalesReportDtl>>funSalesReport(@Query("POSCode") String POSCode, @Query("UserCode") String userCode,
													 @Query("FromDate") String fromDate, @Query("ToDate") String toDate, @Query("ReportType") String reportType);

	//For funPOSSalesReport Screen

	@GET("prjSanguineWebService/APOSIntegration/funPOSSalesReport")
	Call<ArrayList<clsSalesReportDtl>>funPOSSalesReport(@Query("FromDate") String fromDate, @Query("ToDate") String toDate, @Query("ReportType") String reportType);

	//For clsViewTableStatusScreen
	@GET("prjSanguineWebService/APOSIntegration/funGetTableStatus")
	Call<ArrayList<clsTableMaster>>funGetTableStatus(@Query("POSCode") String POSCode,@Query("Type") String type,@Query("AreaCode") String areaCode);

	/**********Reprint KOT**********/
	@GET("prjSanguineWebService/APOSIntegration/funReprintKOT")
	Call<JsonObject>funReprintKOT(@Query("POSCode") String POSCode,@Query("TableNo") String tableNo,
								  @Query("KOTNo") String KOTNo,@Query("deviceName")String deviceName);

	@GET("prjSanguineWebService/APOSIntegration/funGetRefundSlip")
	Call<ArrayList<clsReprintDocumentBean>>funGetRefundSlip(@Query("POSCode") String POSCode, @Query("ClientCode") String ClientCode);

	@GET("prjSanguineWebService/APOSIntegration/funPrintRefundSlip")
	Call<ArrayList<clsReprintDocumentBean>>funPrintRefundSlip(@Query("POSCode") String POSCode, @Query("ClientCode") String ClientCode, @Query("RefundNo") String cardRefundNo);

	@GET("prjSanguineWebService/APOSIntegration/funGetRechargeSlip")
	Call<ArrayList<clsReprintDocumentBean>>funGetRechargeSlip(@Query("POSCode") String POSCode, @Query("ClientCode") String ClientCode);


	@GET("prjSanguineWebService/APOSIntegration/funPrintRechargeSlip")
	Call<ArrayList<clsReprintDocumentBean>>funPrintRechargeSlip(@Query("POSCode") String POSCode, @Query("ClientCode") String ClientCode,@Query("RechargeNo") String RechargeNo);

	/*Customer Order*/
	@GET("prjSanguineWebService/APOSIntegration/funGetMenuHeadListForCustomerOrder")
	Call<ArrayList<clsKotMenuItemsBean>>funGetMenuHeadListForCustomerOrder(@Query("POSCode") String POSCode, @Query("clientCode") String ClientCode);


	@GET("prjSanguineWebService/APOSIntegration/funCallSearchForm")
	Call<ArrayList<clsCustomerMaster>> funGetCustomerList(@Query("FormName") String FormName,@Query("POSCode") String POSCode,@Query("ClientCode") String ClientCode);

	@GET("prjSanguineWebService/APOSIntegration/funLoadReprintBillList")
	Call<JsonArray> funLoadReprintBillList(@Query("POSCode") String POSCode,@Query("ClientCode") String ClientCode);

	@GET("prjSanguineWebService/APOSIntegration/funLoadReprintDirectBillList")
	Call<JsonArray> funLoadDirectBillListWS(@Query("POSCode") String POSCode,@Query("ClientCode") String ClientCode);


	@GET("prjSanguineWebService/APOSIntegration/funGetItemPriceDtlList")
	Call<ArrayList<clsKotItemsListBean>> funGetItemPriceDtl(@Query("POSCode") String POSCode
			, @Query("fromDate") String fromDate, @Query("toDate") String toDate);

	@POST("prjSanguineWebService/APOSIntegration/funGetBillSeriesList")
	Call<HashMap<String,List<clsBillSeriesItemDtl>>> funGetBillSeriesList(@Query("POSCode") String POSCode, @Body List<clsBillSeriesItemDtl> listSelectedItems);

	@GET("prjSanguineWebService/APOSIntegration/funPrintBill")
	Call<JsonObject> funPrintBillDataFromWS(@Query("billNo") String billNo, @Query("cardNo") String cardNo,@Query("TransactionType") String transactionType);

	@GET("prjSanguineWebService/APOSIntegration/funGenerateDirectBillerKOTTextFileForBluetooth")
	Call<JsonObject> funPrintDirectBillerKOTFromWS(@Query("POSCode") String POSCode, @Query("BillNo") String billNo,@Query("AreaCode") String areaCode);

}

