package com.example.apos.api;

import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
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

import retrofit2.Response;

public class APIHelper extends BaseAPIHelper {

	private static APIHelper instance;
	private final static int RESULT_OK = 200;
	private final static int RESULT_CONNECTION_SUCCESS = 201;
	private final static int HTTP_UNAUTHORIZED = 401;

	public static void refreshBase() {
		instance = null;
	}

	public static synchronized APIHelper init(App app) {
		if (null == instance) {
			instance = new APIHelper();
			instance.setApplication(app);
			instance.createRestAdapter();
		}
		return instance;
	}

	private String getString(int stringId)
	{
		return App.getAppContext().getString(stringId);
	}

	public APIService getApiService() {
		return apiService;
	}

	private String showGeneralizedError() {
		return getString(R.string.response_msg_common);
	}


	public void serverAuth(String userName, String password, final OnRequestComplete<UserDTO> onRequestComplete) {
		apiService.serverAuth(userName, password).enqueue(new ResponseHandler<UserDTO>(onRequestComplete, "userLogin()") {
			@Override
			public void onSuccess(Response<UserDTO> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	public void checkServer(final OnRequestComplete<Boolean> onRequestComplete) {
		apiService.checkServer().enqueue(new ResponseHandler<Boolean>(onRequestComplete, "checkServer()") {
			@Override
			public void onSuccess(final Response<Boolean> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	public void serverAuthByQRScan(String DebitCardString, final OnRequestComplete<UserDTO> onRequestComplete) {
		apiService.serverAuthByQRScan(DebitCardString).enqueue(new ResponseHandler<UserDTO>(onRequestComplete, "userLogin()") {
			@Override
			public void onSuccess(Response<UserDTO> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	public void funGetSetupValues(String POSCode, final OnRequestComplete<clsGlobalDTO> onRequestComplete) {
		apiService.funGetSetupValues(POSCode).enqueue(new ResponseHandler<clsGlobalDTO>(onRequestComplete, "funInitSetupWebService()") {
			@Override
			public void onSuccess(Response<clsGlobalDTO> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	public void getLastBillDate(final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.getLastBillDate().enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "getLastBillDate()") {
			@Override
			public void onSuccess(final Response<JsonObject> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	public void getTerminalRegistrationDetails(String macAddress, String terminalName, String clientCode, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.getTerminalRegistrationDetails(macAddress, terminalName, clientCode).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "getTerminalRegistrationDetails()") {
			@Override
			public void onSuccess(Response<JsonObject> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	public void funRegisterTerminal(String hostName, String macAddress, String clientCode, String userCode, String terminalName, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funRegisterTerminal(hostName, macAddress, clientCode, userCode, terminalName).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "getRegisterTerminal()") {
			@Override
			public void onSuccess(final Response<JsonObject> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	public void funCheckAPOSLicence(String clientCode,String physicalAddress, String hostName, String userCode, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.checkAPOSLicence(clientCode,physicalAddress, hostName, clientCode).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funCheckAPOSLicence()") {
			@Override
			public void onSuccess(Response<JsonObject> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	/*********** For POS Selction Screen ************************************/

	public void funGetPOSList(String userCode, String clientCode, final OnRequestComplete<ArrayList<clsPosSelectionMaster>> onRequestComplete) {
		apiService.funGetPOSList(userCode, clientCode).enqueue(new ResponseHandler<ArrayList<clsPosSelectionMaster>>(onRequestComplete, "getPOSList()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsPosSelectionMaster>> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	/*********** For Main Menu Selction Screen ************************************/

	public void funGetMainMenuList(String userCode, String moduleType, String clientCode, boolean SuperUser, String posCode, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funGetMainMenuList(userCode, moduleType, clientCode, SuperUser, posCode).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "getMainMenuList()") {
			@Override
			public void onSuccess(final Response<JsonObject> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	/*KOT Screen -- get TDH With Modifier List*/
	public void funCheckAndGetModifierListForTDH(String clientCode, final OnRequestComplete<ArrayList<clsTDHWithModifierDTO>> onRequestComplete) {
		apiService.funCheckAndGetModifierListForTDH(clientCode).enqueue(new ResponseHandler<ArrayList<clsTDHWithModifierDTO>>(onRequestComplete, "funGetModifierListForTDH()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsTDHWithModifierDTO>> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	/********Get TDH list **************/
	public void funGetTDHListWS(String clientCode, final OnRequestComplete<JsonArray> onRequestComplete) {
		apiService.funGetTDHListWS(clientCode).enqueue(new ResponseHandler<JsonArray>(onRequestComplete, "funGetTDHListWS()") {
			@Override
			public void onSuccess(final Response<JsonArray> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	/******** Load Prevoius Kot Item List **************/
	public void funLoadPrevoiusKotItemList(String TableNo, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funLoadPrevoiusKotItemList(TableNo).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funLoadPrevoiusKotItemList()") {
			@Override
			public void onSuccess(final Response<JsonObject> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	/******** Load Table List **************/
	public void funGetTableList(String POSCode, String CMSIntegration, String memberAsTable, final OnRequestComplete<ArrayList<clsTableMaster>> onRequestComplete) {
		apiService.funGetTableList(POSCode, CMSIntegration, memberAsTable).enqueue(new ResponseHandler<ArrayList<clsTableMaster>>(onRequestComplete, "funGetTableList()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsTableMaster>> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	/******** Load Menu List **************/
	public void funGetMenuHeadList(String POSCode, String clientCode, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funGetMenuHeadList(POSCode, clientCode).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funGetMenuHeadList()") {
			@Override
			public void onSuccess(final Response<JsonObject> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	/******** Load Item data counter Wise **************/
	public void funGetItemPriceDtlCounterWise(String POSCode, String areaCode, String menuHeadCode, String areaWisePricing, String fromDate
			, String toDate, boolean flgAllItems, String menuType, final OnRequestComplete<ArrayList<clsKotItemsListBean>> onRequestComplete) {
		apiService.funGetItemPriceDtlCounterWise(POSCode, areaCode,menuHeadCode, areaWisePricing,  fromDate
				, toDate, flgAllItems, menuType).enqueue(new ResponseHandler<ArrayList<clsKotItemsListBean>>(onRequestComplete, "funGetItemPriceDtl()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsKotItemsListBean>> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	/******** Load Item detail List **************/
////// ?POSCode="+clsGlobalFunctions.gPOSCode+"&areaCode="++"&menuHeadCode="+params[0]+"&areaWisePricing="+clsGlobalFunctions.gAreaWisePricing+"&fromDate="+objGlobal.funGetCurrentDate()+"&toDate="+objGlobal.funGetCurrentDate()+"&flgAllItems="+flgAllItems+"&menuType="+menuType;
	public void funGetItemPriceDtl(String POSCode, String areaCode, String menuHeadCode, String areaWisePricing, String fromDate
			, String toDate, boolean flgAllItems, String menuType, String tableNo,final OnRequestComplete<ArrayList<clsKotItemsListBean>> onRequestComplete) {
		apiService.funGetItemPriceDtl(POSCode, areaCode,  menuHeadCode,areaWisePricing, fromDate
				, toDate, flgAllItems, menuType,tableNo).enqueue(new ResponseHandler<ArrayList<clsKotItemsListBean>>(onRequestComplete, "funGetItemPriceDtl()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsKotItemsListBean>> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}



	public void funGetCounterWiseMenuList(String POSCode, String counterCode, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funGetCounterWiseMenuList(POSCode, counterCode).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funGetCounterWiseMenuList()") {
			@Override
			public void onSuccess(final Response<JsonObject> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	public void funGetSubMenuList(String POSCode, String clientCode, String menuCode, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funGetSubMenuList(POSCode, clientCode, menuCode).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funGetSubMenuList()") {
			@Override
			public void onSuccess(final Response<JsonObject> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	/********** Insert Debit Cart Temprary  Details *************/
	public void funInsertDebitCartTmpDtl(JSONObject objDebitDtl, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funInsertDebitCartTmpDtl(objDebitDtl).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funInsertDebitCartTmpDtl()") {
			@Override
			public void onSuccess(Response<JsonObject> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	/*******funGetKOTDataFromWS***********/
	public void funGetKOTDataFromWS(String kotNo, String posCode, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funGetKOTDataFromWS(kotNo, posCode).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funGetKOTDataFromWS()") {
			@Override
			public void onSuccess(Response<JsonObject> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	/********** Save KOT Details *************/
	public void funSaveAndPrintKOT(JsonObject objKOTDtl, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funSaveAndPrintKOT(objKOTDtl).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funSaveAndPrintKOT()") {
			@Override
			public void onSuccess(Response<JsonObject> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	/*******Get Modifier List****/
	public void funGetModifierList(String itemcode,String clientCode, final OnRequestComplete<ArrayList<clsItemModifierBean>> onRequestComplete) {
		apiService.funGetModifierList(itemcode,clientCode).enqueue(new ResponseHandler<ArrayList<clsItemModifierBean>>(onRequestComplete, "funGetModifierList()") {
			@Override
			public void onSuccess(Response<ArrayList<clsItemModifierBean>> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}
	/********Tax Calculate*********/
	public void funTaxCalculate(JsonObject objTaxDtl, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funTaxCalculate(objTaxDtl).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funTaxCalculate()") {
			@Override
			public void onSuccess(Response<JsonObject> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}



	/******** Load BillWise Report Data **************/
	public void funGetBillwiseSalesReport(String POSCode,String userCode,String fromDate,String toDate,String reportType,final OnRequestComplete<ArrayList<clsSalesReportDtl>> onRequestComplete)
	{
		apiService.funGetBillwiseSalesReport(POSCode,userCode,fromDate, toDate,reportType).enqueue(new ResponseHandler<ArrayList<clsSalesReportDtl>>(onRequestComplete, "funSalesReport()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsSalesReportDtl>> response) throws Exception
			{
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				}
				else if(RESULT_OK==response.code())
				{
					onRequestComplete.onSuccess(response.body());
				}
				else
				{
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	/**********funGetTDHMenuList*******/
	public void funGetTDHMenuList(String clientCode,String itemcode, final OnRequestComplete<ArrayList<clsTDHBean>> onRequestComplete) {
		apiService.funGetTDHMenuList(clientCode,itemcode).enqueue(new ResponseHandler<ArrayList<clsTDHBean>>(onRequestComplete, "funGetTDHMenuList()") {
			@Override
			public void onSuccess(Response<ArrayList<clsTDHBean>> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}
	/*funCheckKOT*/
	public void funCheckKOT(String tableNo,String POSCode,String posName, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funCheckKOT(tableNo,POSCode,posName).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funCheckKOT()") {
			@Override
			public void onSuccess(Response<JsonObject> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}
	/*funGetItemDetailsByExternalCode*/
	public void funGetItemDetailsByExternalCode(String POSCode,String externalCode,String AreaPricing,String tableNo,String areaCode, final OnRequestComplete<ArrayList<clsKotItemsListBean>> onRequestComplete) {
		apiService.funGetItemDetailsByExternalCode(POSCode,externalCode,AreaPricing,tableNo,areaCode).enqueue(new ResponseHandler<ArrayList<clsKotItemsListBean>>(onRequestComplete, "funGetItemDetailsByExternalCode()") {
			@Override
			public void onSuccess(Response<ArrayList<clsKotItemsListBean>> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	/*Direct Biller Get Customer Info*/
	public void funGetCustomerInfo(String mobNo,String POSCode,String clientCode,final OnRequestComplete<ArrayList<clsCustomerMaster> > onRequestComplete) {
		apiService.funGetCustomerInfo(mobNo,POSCode,clientCode).enqueue(new ResponseHandler<ArrayList<clsCustomerMaster> >(onRequestComplete, "funGetCustomerInfo()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsCustomerMaster> > response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	/*Direct Biller Load Customer Type List*/
	public void funLoadCustomerTypeList(String clientCode,final OnRequestComplete<ArrayList<clsCustomerMaster> > onRequestComplete) {
		apiService.funLoadCustomerTypeList(clientCode).enqueue(new ResponseHandler<ArrayList<clsCustomerMaster> >(onRequestComplete, "funLoadCustomerTypeList()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsCustomerMaster> > response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	public void funSaveNewCustomerDtl(String custname, String mobileNo, String custTypeCode, String address, String gClientCode, String gUserCode, String dateTime,final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funSaveNewCustomerDtl(custname,mobileNo,custTypeCode,address, gClientCode,gUserCode,dateTime).enqueue(new ResponseHandler<JsonObject >(onRequestComplete, "funSaveNewCustomerDtl()") {
			@Override
			public void onSuccess(final Response<JsonObject > response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	//For Make Bill Screen
	public void funGetBusyTableList(String POSCode, String CMSIntegration, String memberAsTable, final OnRequestComplete<ArrayList<clsTableMaster>> onRequestComplete) {
		apiService.funGetBusyTableList(POSCode, CMSIntegration, memberAsTable).enqueue(new ResponseHandler<ArrayList<clsTableMaster>>(onRequestComplete, "funGetTableList()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsTableMaster>> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	public void funGenerateBillNo(String POSCode,final OnRequestComplete<HashMap> onRequestComplete) {
		apiService.funGenerateBillNo(POSCode).enqueue(new ResponseHandler<HashMap>(onRequestComplete, "generateBillNo()") {
			@Override
			public void onSuccess(final Response<HashMap> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	public void funGetTableBillData(String POSCode, String TableNo,String ClientCode, String POSStartDate,
									String ApplyPromotion, String ApplyPromotionToBill, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funGetTableBillData(POSCode,TableNo,ClientCode,POSStartDate,ApplyPromotion,ApplyPromotionToBill).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funLoadPrevoiusKotItemList()") {
			@Override
			public void onSuccess(final Response<JsonObject> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	public void funSaveBill(JsonObject objBillDtl,String billSeries,String posCode, final OnRequestComplete<HashMap> onRequestComplete) {
		apiService.funSaveBill(objBillDtl, billSeries,posCode).enqueue(new ResponseHandler<HashMap>(onRequestComplete, "funSaveBill()") {
			@Override
			public void onSuccess(Response<HashMap> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	public void funSaveAllBillData(JsonObject objBillDtl,String billSeries,String posCode, final OnRequestComplete<HashMap> onRequestComplete) {
		apiService.funSaveAllBillData(objBillDtl, billSeries,posCode).enqueue(new ResponseHandler<HashMap>(onRequestComplete, "funSaveBill()") {
			@Override
			public void onSuccess(Response<HashMap> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	public void funGenerateBillTextFile(String billNo,String POSCode,String clientCode,String reprint, final OnRequestComplete<HashMap> onRequestComplete) {
		apiService.funGenerateBillTextFile(billNo,POSCode,clientCode,reprint).enqueue(new ResponseHandler<HashMap>(onRequestComplete, "funGenerateBillTextFile()") {
			@Override
			public void onSuccess(Response<HashMap> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	//For Void KOT

	public void funGetLiveKOTList(String POSCode, String tableNo, String clientCode, final OnRequestComplete<ArrayList<clsVoidKotListDtl>> onRequestComplete) {
		apiService.funGetLiveKOTList(POSCode, tableNo, clientCode).enqueue(new ResponseHandler<ArrayList<clsVoidKotListDtl>>(onRequestComplete, "funGetLiveKOTList()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsVoidKotListDtl>> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	public void funGetKOTData(String POSCode,String KOTNo,String clientCode, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funGetKOTData(POSCode,KOTNo,clientCode).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funGetKOTData()") {
			@Override
			public void onSuccess(Response<JsonObject> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	public void funVoidKOT(JsonObject objVoidKOTDtl, final OnRequestComplete<HashMap> onRequestComplete) {
		apiService.funVoidKOT(objVoidKOTDtl).enqueue(new ResponseHandler<HashMap>(onRequestComplete, "funVoidKOT()") {
			@Override
			public void onSuccess(Response<HashMap> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	//For Bill Settlement Form

	public void funGetBillList(String clientCode,String posCode,final OnRequestComplete<ArrayList<clsBillListDtl>> onRequestComplete)
	{
		apiService.funGetBillList(clientCode,posCode).enqueue(new ResponseHandler<ArrayList<clsBillListDtl>>(onRequestComplete, "funGetBillList()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsBillListDtl>> response) throws Exception
			{
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				}
				else if(RESULT_OK==response.code())
				{
					onRequestComplete.onSuccess(response.body());
				}
				else
				{
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	public void funGetBillDtl(String clientCode,String billNo,final OnRequestComplete<ArrayList<clsBillListDtl>> onRequestComplete)
	{
		apiService.funGetBillDtl(clientCode,billNo).enqueue(new ResponseHandler<ArrayList<clsBillListDtl>>(onRequestComplete, "funGetBillDtl()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsBillListDtl>> response) throws Exception
			{
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				}
				else if(RESULT_OK==response.code())
				{
					onRequestComplete.onSuccess(response.body());
				}
				else
				{
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	public void funInsertBillTaxDtl(JsonObject objTaxDtl, final OnRequestComplete<HashMap> onRequestComplete) {
		apiService.funInsertBillTaxDtl(objTaxDtl).enqueue(new ResponseHandler<HashMap>(onRequestComplete, "funInsertBillTaxDtl()") {
			@Override
			public void onSuccess(Response<HashMap> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	public void funCalculatePromotion(JsonObject objPromotionDtl, final OnRequestComplete<HashMap> onRequestComplete) {
		apiService.funCalculatePromotion(objPromotionDtl).enqueue(new ResponseHandler<HashMap>(onRequestComplete, "funCalculatePromotion()") {
			@Override
			public void onSuccess(Response<HashMap> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	public void funGenerateDirectBillerKOTTextFile(String POSCode,String BillNo,String AreaCode,String reprint, final OnRequestComplete<HashMap> onRequestComplete) {
		apiService.funGenerateDirectBillerKOTTextFile(POSCode,BillNo,AreaCode,reprint).enqueue(new ResponseHandler<HashMap>(onRequestComplete, "funGenerateBillTextFile()") {
			@Override
			public void onSuccess(Response<HashMap> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}
	/*Table Reservation  funGetReservationDetailList*/
	public void funGetReservationDetailList(String formName,String POSCode,String clientCode,final OnRequestComplete<JsonArray> onRequestComplete) {
		apiService.funCallSearchForm(formName,POSCode,clientCode).enqueue(new ResponseHandler<JsonArray>(onRequestComplete, "funGetReservationDetailList()") {
			@Override
			public void onSuccess(final Response<JsonArray> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	public void funGetUnReservedTableList(String formName,String POSCode,String clientCode,final OnRequestComplete<ArrayList<clsTableMaster>> onRequestComplete) {
		apiService.funGetUnReservedTableList(formName,POSCode,clientCode).enqueue(new ResponseHandler<ArrayList<clsTableMaster>>(onRequestComplete, "funGetUnReservedTableList()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsTableMaster>> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	public void funDoneReservation(JsonObject jObjReservation,final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funDoneReservation(jObjReservation).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funDoneReservation()") {
			@Override
			public void onSuccess(final Response<JsonObject> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	public void funSendSMSWS(JsonObject objSendSMSDtl,final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funSendSMSWS(objSendSMSDtl).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funSendSMSWS()") {
			@Override
			public void onSuccess(final Response<JsonObject> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	public void funGetReservationDetail(String POSCode,String fromDate,String toDate,String fromTime,String toTime,final OnRequestComplete<JsonArray> onRequestComplete) {
		apiService.funGetReservationDetail(POSCode,fromDate,toDate,fromTime,toTime).enqueue(new ResponseHandler<JsonArray>(onRequestComplete, "funGetReservationDetail()") {
			@Override
			public void onSuccess(final Response<JsonArray> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}






	public void funGetReasonList(String type, String clientCode, final OnRequestComplete<ArrayList<clsReasonMaster>> onRequestComplete) {
		apiService.funGetReasonList(type,clientCode).enqueue(new ResponseHandler<ArrayList<clsReasonMaster>>(onRequestComplete, "funGetReasonList()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsReasonMaster>> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}





	/******** Load Waiter List **************/
	public void funGetWaiterList(String POSCode, String UserCode, boolean Applicable, final OnRequestComplete<ArrayList<clsWaiterMaster>> onRequestComplete) {
		apiService.funGetWaiterList(POSCode, UserCode, Applicable).enqueue(new ResponseHandler<ArrayList<clsWaiterMaster>>(onRequestComplete, "funGetWaiterList()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsWaiterMaster>> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	//For Mini Make KOT Screen
	public void funGetItemPriceDtlForCustomerOrder(String POSCode, String areaCode, String menuHeadCode, String areaWisePricing, String fromDate
			, String toDate, boolean flgAllItems, String menuType, final OnRequestComplete<ArrayList<clsKotItemsListBean>> onRequestComplete) {
		apiService.funGetItemPriceDtlForCustomerOrder(POSCode, areaCode,  menuHeadCode,areaWisePricing, fromDate
				, toDate, flgAllItems, menuType).enqueue(new ResponseHandler<ArrayList<clsKotItemsListBean>>(onRequestComplete, "funGetItemPriceDtl()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsKotItemsListBean>> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	//For KDS

	/*Cost Center Screen*/
	public void funGetCostCenterList(String formName,String POSCode,String clientCode,final OnRequestComplete<JsonArray> onRequestComplete) {
		apiService.funCallSearchForm(formName,POSCode,clientCode).enqueue(new ResponseHandler<JsonArray>(onRequestComplete, "funGetCostCenterList()") {
			@Override
			public void onSuccess(final Response<JsonArray> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	public void funGetKOTDetailsForKDS(String posCode, String costCenterCode, String posDate, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funGetKOTDetailsForKDS(posCode, costCenterCode, posDate).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funGetKOTDetailsForKDS()") {
			@Override
			public void onSuccess(final Response<JsonObject> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	public void funUpdateProcessedKOTItemsForKDS(JsonObject objKDSDtl, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funUpdateProcessedKOTItemsForKDS(objKDSDtl).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funSaveAndPrintKOT()") {
			@Override
			public void onSuccess(Response<JsonObject> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	public void funGetProcessedKOTDetailsForKPS(String posCode, String waiterNo, String posDate, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funGetProcessedKOTDetailsForKPS(posCode, waiterNo, posDate).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funGetKOTDetailsForKDS()") {
			@Override
			public void onSuccess(final Response<JsonObject> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	public void funUpdatePickedUpKOTItemsForKPS(JsonObject objKPSDtl, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funUpdatePickedUpKOTItemsForKPS(objKPSDtl).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funSaveAndPrintKOT()") {
			@Override
			public void onSuccess(Response<JsonObject> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	/******** Load funSalesReport Report Data **************/
	public void funSalesReport(String POSCode,String userCode,String fromDate,String toDate,String reportType,final OnRequestComplete<ArrayList<clsSalesReportDtl>> onRequestComplete)
	{
		apiService.funSalesReport(POSCode,userCode,fromDate, toDate,reportType).enqueue(new ResponseHandler<ArrayList<clsSalesReportDtl>>(onRequestComplete, "funSalesReport()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsSalesReportDtl>> response) throws Exception
			{
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				}
				else if(RESULT_OK==response.code())
				{
					onRequestComplete.onSuccess(response.body());
				}
				else
				{
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	/******** Load funPOSSalesReport Report Data **************/
	public void funPOSSalesReport(String fromDate,String toDate,String reportType,final OnRequestComplete<ArrayList<clsSalesReportDtl>> onRequestComplete)
	{
		apiService.funPOSSalesReport(fromDate, toDate,reportType).enqueue(new ResponseHandler<ArrayList<clsSalesReportDtl>>(onRequestComplete, "funPOSSalesReport()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsSalesReportDtl>> response) throws Exception
			{
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				}
				else if(RESULT_OK==response.code())
				{
					onRequestComplete.onSuccess(response.body());
				}
				else
				{
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}



	//For table status view
	public void funGetTableStatus(String posCode,String type,String areaCode,final OnRequestComplete<ArrayList<clsTableMaster>> onRequestComplete)
	{
		apiService.funGetTableStatus(posCode,type,areaCode).enqueue(new ResponseHandler<ArrayList<clsTableMaster>>(onRequestComplete, "funGetTableStatus()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsTableMaster>> response) throws Exception
			{
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				}
				else if(RESULT_OK==response.code())
				{
					onRequestComplete.onSuccess(response.body());
				}
				else
				{
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}
	/**********Reprint KOT**********/
	public void funReprintKOT(String POSCode,String tableNo,String KOTNo,String deviceName, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funReprintKOT(POSCode,tableNo,KOTNo,deviceName).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funReprintKOT()") {
			@Override
			public void onSuccess(Response<JsonObject> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	public void funGetRefundSlip(String posCode,String clientCode,final OnRequestComplete<ArrayList<clsReprintDocumentBean>> onRequestComplete)
	{
		apiService.funGetRefundSlip(posCode,clientCode).enqueue(new ResponseHandler<ArrayList<clsReprintDocumentBean>>(onRequestComplete, "funGetRefundSlip()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsReprintDocumentBean>> response) throws Exception
			{
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				}
				else if(RESULT_OK==response.code())
				{
					onRequestComplete.onSuccess(response.body());
				}
				else
				{
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	public void funPrintRefundSlip(String posCode,String clientCode,String cardRefundNo,final OnRequestComplete<ArrayList<clsReprintDocumentBean>> onRequestComplete)
	{
		apiService.funPrintRefundSlip(posCode,clientCode,cardRefundNo).enqueue(new ResponseHandler<ArrayList<clsReprintDocumentBean>>(onRequestComplete, "funGetRefundSlip()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsReprintDocumentBean>> response) throws Exception
			{
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				}
				else if(RESULT_OK==response.code())
				{
					onRequestComplete.onSuccess(response.body());
				}
				else
				{
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	public void funGetRechargeSlip(String posCode,String clientCode, final OnRequestComplete<ArrayList<clsReprintDocumentBean>> onRequestComplete)
	{
		apiService.funGetRechargeSlip(posCode,clientCode).enqueue(new ResponseHandler<ArrayList<clsReprintDocumentBean>>(onRequestComplete, "funGetRefundSlip()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsReprintDocumentBean>> response) throws Exception
			{
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				}
				else if(RESULT_OK==response.code())
				{
					onRequestComplete.onSuccess(response.body());
				}
				else
				{
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

	public void funPrintRechargeSlip(String posCode,String clientCode,String RechargeNo,final OnRequestComplete<ArrayList<clsReprintDocumentBean>> onRequestComplete)
	{
		apiService.funPrintRechargeSlip(posCode,clientCode,RechargeNo).enqueue(new ResponseHandler<ArrayList<clsReprintDocumentBean>>(onRequestComplete, "funPrintRechargeSlip()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsReprintDocumentBean>> response) throws Exception
			{
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				}
				else if(RESULT_OK==response.code())
				{
					onRequestComplete.onSuccess(response.body());
				}
				else
				{
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}
	/* Customer order */
	public void funGetMenuHeadListForCustomerOrder(String posCode,String clientCode,final OnRequestComplete<ArrayList<clsKotMenuItemsBean>> onRequestComplete)
	{
		apiService.funGetMenuHeadListForCustomerOrder(posCode,clientCode).enqueue(new ResponseHandler<ArrayList<clsKotMenuItemsBean>>(onRequestComplete, "funGetMenuHeadListForCustomerOrder()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsKotMenuItemsBean>> response) throws Exception
			{
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				}
				else if(RESULT_OK==response.code())
				{
					onRequestComplete.onSuccess(response.body());
				}
				else
				{
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	public void funGetCustomerList(String formName,String POSCode,String clientCode,final OnRequestComplete<ArrayList<clsCustomerMaster>> onRequestComplete) {
		apiService.funGetCustomerList(formName,POSCode,clientCode).enqueue(new ResponseHandler<ArrayList<clsCustomerMaster>>(onRequestComplete, "funGetCustomerList()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsCustomerMaster>> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}
	/*for reprint document */
	public void funLoadReprintBillList(String POSCode,  String clientCode, final OnRequestComplete<JsonArray> onRequestComplete) {
		apiService.funLoadReprintBillList(POSCode, clientCode).enqueue(new ResponseHandler<JsonArray>(onRequestComplete, "funLoadReprintBillList()") {
			@Override
			public void onSuccess(final Response<JsonArray> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	/*Get List Of Area*/
	public void funGetAreaList(String formName,String POSCode,String clientCode,final OnRequestComplete<JsonArray> onRequestComplete) {
		apiService.funCallSearchForm(formName,POSCode,clientCode).enqueue(new ResponseHandler<JsonArray>(onRequestComplete, "funGetAreaList()") {
			@Override
			public void onSuccess(final Response<JsonArray> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	public void funLoadDirectBillListWS(String POSCode,  String clientCode, final OnRequestComplete<JsonArray> onRequestComplete) {
		apiService.funLoadDirectBillListWS(POSCode, clientCode).enqueue(new ResponseHandler<JsonArray>(onRequestComplete, "funLoadDirectBillListWS()") {
			@Override
			public void onSuccess(final Response<JsonArray> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	public void funGetItemPriceDtlList(String POSCode,String fromDate,String toDate,final OnRequestComplete<ArrayList<clsKotItemsListBean>> onRequestComplete) {
		apiService.funGetItemPriceDtl(POSCode, fromDate, toDate).enqueue(new ResponseHandler<ArrayList<clsKotItemsListBean>>(onRequestComplete, "funGetItemPriceDtl()") {
			@Override
			public void onSuccess(final Response<ArrayList<clsKotItemsListBean>> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}

/*For load Bill series list*/

	public void funGetBillSeriesList(String POSCode, List<clsBillSeriesItemDtl> listSelectedItems, final OnRequestComplete<HashMap<String,List<clsBillSeriesItemDtl>>> onRequestComplete) {
		apiService.funGetBillSeriesList(POSCode,listSelectedItems).enqueue(new ResponseHandler<HashMap<String,List<clsBillSeriesItemDtl>>>(onRequestComplete, "funGetBillSeriesList()") {
			@Override
			public void onSuccess(final Response<HashMap<String,List<clsBillSeriesItemDtl>>> response) throws Exception {
				if (RESULT_CONNECTION_SUCCESS == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	/*******funGetBillPrintingDataFromWS***********/
	public void funPrintBillDataFromWS(String billNo, String cardNo,String transactionType, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funPrintBillDataFromWS(billNo, cardNo,transactionType).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funPrintBillDataFromWS()") {
			@Override
			public void onSuccess(Response<JsonObject> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


	/*******funPrintDirectBillerKOTFromWS***********/
	public void funPrintDirectBillerKOTFromWS(String POSCode, String billNo,String areaCode, final OnRequestComplete<JsonObject> onRequestComplete) {
		apiService.funPrintDirectBillerKOTFromWS(POSCode, billNo,areaCode).enqueue(new ResponseHandler<JsonObject>(onRequestComplete, "funPrintDirectBillerKOTFromWS()") {
			@Override
			public void onSuccess(Response<JsonObject> response) throws Exception {
				if (RESULT_OK == response.code()) {
					onRequestComplete.onSuccess(response.body());
				} else {
					onRequestComplete.onFailure(showGeneralizedError(), response.code());
				}
			}
		});
	}


}