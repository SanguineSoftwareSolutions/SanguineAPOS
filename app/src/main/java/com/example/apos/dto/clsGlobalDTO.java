
package com.example.apos.dto;

import com.example.apos.activity.clsGlobalFunctions;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Vinayak  on 14/09/2017.
 */

public class clsGlobalDTO implements Serializable {

	@SerializedName("Status")
	private String status;

	@SerializedName("ClientCode")
	private String gClientCode;

	@SerializedName("ClientName")
	private String gClientName;

	@SerializedName("NegativeBilling")
	private String gNegativeBilling;

	@SerializedName("StartDate")
	private String gStartDate;

	@SerializedName("EndDate")
	private String gEndDate;

	@SerializedName("NatureOfBusinnes")
	private String gNatureOfBusinnes;

	@SerializedName("EnableKOT")
	private String gEnableKOT;

	@SerializedName("PrintVatNo")
	private String gPrintVatNo;

	@SerializedName("VatNo")
	private String gVatNo;

	@SerializedName("ShowBill")
	private String gShowBill;

	@SerializedName("PrintServiceTaxNo")
	private String gPrintServiceTaxNo;

	@SerializedName("ServiceTaxNo")
	private String gServiceTaxNo;

	@SerializedName("POSType")
	private String gPOSType;

	@SerializedName("WebServiceLink")
	private String gWebServiceLink;

	@SerializedName("DataSendFrequency")
	private String gDataSendFrequency;

	@SerializedName("HOServerDate")
	private String gHOServerDate;

	@SerializedName("EnableKOTForDirectBiller")
	private String gEnableKOTForDirectBiller;

	@SerializedName("AreaWisePricing")
	private String gAreaWisePricing;

	@SerializedName("DirectBillerAreaCode")
	private String gDirectBillerAreaCode;

	@SerializedName("BillFormatType")
	private String gBillFormatType;

	@SerializedName("SkipWaiterAndPax")
	private String gSkipWaiterAndPax;

	@SerializedName("SkipWaiter")
	private String gSkipWaiter;

	@SerializedName("SkipPax")
	private String gSkipPax;

	@SerializedName("ActivePromotions")
	private String gActivePromotions;

	@SerializedName("PriceFrom")
	private String gPriceFrom;

	@SerializedName("CMSIntegrationYN")
	private String gCMSIntegrationYN;

	@SerializedName("CMSWebServiceURL")
	private String gCMSWebServiceURL;

	@SerializedName("CMSPOSCode")
	private String gCMSPOSCode;

	@SerializedName("MultipleKOTPrintYN")
	private String gMultipleKOTPrintYN;

	@SerializedName("PrintKOTYN")
	private String gPrintKOTYN;

	@SerializedName("TreatMemberAsTable")
	private String gTreatMemberAsTable;

	@SerializedName("MultiWaiterSelectionOnMakeKOT")
	private String gMultipleWaiterSelection;

	@SerializedName("MemberCodeForKotInMposByCardSwipe")
	private String gMemberCodeForKotInMposByCardSwipe;

	@SerializedName("CMSMemberForKOTMPOS")
	private String gCMSMemberForKOTMPOS;

	@SerializedName("UseVatAndServiceTaxNoFromPOS")
	private String gUseVatAndServiceTaxNoFromPOS;

	@SerializedName("MemberCodeForMakeBillInMPOS")
	private String gMemberCodeForMakeBillInMPOS;

	@SerializedName("CalculateTaxOnMakeKOT")
	private String gCalculateTaxOnMakeKOT;

	@SerializedName("MenuItemSortingOn")
	private String gMenuItemSortingOn;

	@SerializedName("ItemWiseKOTPrintYN")
	private String gItemWiseKOTPrintYN;

	@SerializedName("LastPOSForDayEnd")
	private String gLastPOSForDayEnd;

	@SerializedName("CMSPostingType")
	private String gCMSPostingType;

	@SerializedName("PopUpToApplyPromotionsOnBill")
	private String gPopUpToApplyPromotionsOnBill;

	@SerializedName("EnablePMSIntegrationYN")
	private String gEnablePMSIntegration;

	@SerializedName("ChangeTheme")
	private String gTheme;

	@SerializedName("strScanQRYN")
	private String gCardUsingScanQR;

	@SerializedName("strEnableTableReservationForCustomer")
	private String gEnableTableReservationForCustomer;

	private String gCallingAvailable = "Yes";

	@SerializedName("strEnableBillSeries")
    private String gEnableBillSeries;

	public String getgEnableBillSeries() {
		return gEnableBillSeries;
	}

	public void setgEnableBillSeries(String gEnableBillSeries) {
		this.gEnableBillSeries = gEnableBillSeries;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getgClientCode() {
		return gClientCode;
	}

	public void setgClientCode(String gClientCode) {
		this.gClientCode = gClientCode;
	}

	public String getgClientName() {
		return gClientName;
	}

	public void setgClientName(String gClientName) {
		this.gClientName = gClientName;
	}

	public String getgNegativeBilling() {
		return gNegativeBilling;
	}

	public void setgNegativeBilling(String gNegativeBilling) {
		this.gNegativeBilling = gNegativeBilling;
	}

	public String getgStartDate() {
		return gStartDate;
	}

	public void setgStartDate(String gStartDate) {
		this.gStartDate = gStartDate;
	}

	public String getgEndDate() {
		return gEndDate;
	}

	public void setgEndDate(String gEndDate) {
		this.gEndDate = gEndDate;
	}

	public String getgNatureOfBusinnes() {
		return gNatureOfBusinnes;
	}

	public void setgNatureOfBusinnes(String gNatureOfBusinnes) {
		this.gNatureOfBusinnes = gNatureOfBusinnes;
	}

	public String getgEnableKOT() {
		return gEnableKOT;
	}

	public void setgEnableKOT(String gEnableKOT) {
		this.gEnableKOT = gEnableKOT;
	}

	public String getgPrintVatNo() {
		return gPrintVatNo;
	}

	public void setgPrintVatNo(String gPrintVatNo) {
		this.gPrintVatNo = gPrintVatNo;
	}

	public String getgVatNo() {
		return gVatNo;
	}

	public void setgVatNo(String gVatNo) {
		this.gVatNo = gVatNo;
	}

	public String getgShowBill() {
		return gShowBill;
	}

	public void setgShowBill(String gShowBill) {
		this.gShowBill = gShowBill;
	}

	public String getgPrintServiceTaxNo() {
		return gPrintServiceTaxNo;
	}

	public void setgPrintServiceTaxNo(String gPrintServiceTaxNo) {
		this.gPrintServiceTaxNo = gPrintServiceTaxNo;
	}

	public String getgServiceTaxNo() {
		return gServiceTaxNo;
	}

	public void setgServiceTaxNo(String gServiceTaxNo) {
		this.gServiceTaxNo = gServiceTaxNo;
	}

	public String getgPOSType() {
		return gPOSType;
	}

	public void setgPOSType(String gPOSType) {
		this.gPOSType = gPOSType;
	}

	public String getgWebServiceLink() {
		return gWebServiceLink;
	}

	public void setgWebServiceLink(String gWebServiceLink) {
		this.gWebServiceLink = gWebServiceLink;
	}

	public String getgDataSendFrequency() {
		return gDataSendFrequency;
	}

	public void setgDataSendFrequency(String gDataSendFrequency) {
		this.gDataSendFrequency = gDataSendFrequency;
	}

	public String getgHOServerDate() {
		return gHOServerDate;
	}

	public void setgHOServerDate(String gHOServerDate) {
		this.gHOServerDate = gHOServerDate;
	}

	public String getgEnableKOTForDirectBiller() {
		return gEnableKOTForDirectBiller;
	}

	public void setgEnableKOTForDirectBiller(String gEnableKOTForDirectBiller) {
		this.gEnableKOTForDirectBiller = gEnableKOTForDirectBiller;
	}

	public String getgAreaWisePricing() {
		return gAreaWisePricing;
	}

	public void setgAreaWisePricing(String gAreaWisePricing) {
		this.gAreaWisePricing = gAreaWisePricing;
	}

	public String getgDirectBillerAreaCode() {
		return gDirectBillerAreaCode;
	}

	public void setgDirectBillerAreaCode(String gDirectBillerAreaCode) {
		this.gDirectBillerAreaCode = gDirectBillerAreaCode;
	}

	public String getgBillFormatType() {
		return gBillFormatType;
	}

	public void setgBillFormatType(String gBillFormatType) {
		this.gBillFormatType = gBillFormatType;
	}

	public String getgSkipWaiterAndPax() {
		return gSkipWaiterAndPax;
	}

	public void setgSkipWaiterAndPax(String gSkipWaiterAndPax) {
		this.gSkipWaiterAndPax = gSkipWaiterAndPax;
	}

	public String getgSkipWaiter() {
		return gSkipWaiter;
	}

	public void setgSkipWaiter(String gSkipWaiter) {
		this.gSkipWaiter = gSkipWaiter;
	}

	public String getgSkipPax() {
		return gSkipPax;
	}

	public void setgSkipPax(String gSkipPax) {
		this.gSkipPax = gSkipPax;
	}

	public String getgActivePromotions() {
		return gActivePromotions;
	}

	public void setgActivePromotions(String gActivePromotions) {
		this.gActivePromotions = gActivePromotions;
	}

	public String getgPriceFrom() {
		return gPriceFrom;
	}

	public void setgPriceFrom(String gPriceFrom) {
		this.gPriceFrom = gPriceFrom;
	}

	public String getgCMSIntegrationYN() {
		return gCMSIntegrationYN;
	}

	public void setgCMSIntegrationYN(String gCMSIntegrationYN) {
		this.gCMSIntegrationYN = gCMSIntegrationYN;
	}

	public String getgCMSWebServiceURL() {
		return gCMSWebServiceURL;
	}

	public void setgCMSWebServiceURL(String gCMSWebServiceURL) {
		this.gCMSWebServiceURL = gCMSWebServiceURL;
	}

	public String getgCMSPOSCode() {
		return gCMSPOSCode;
	}

	public void setgCMSPOSCode(String gCMSPOSCode) {
		this.gCMSPOSCode = gCMSPOSCode;
	}

	public String getgMultipleKOTPrintYN() {
		return gMultipleKOTPrintYN;
	}

	public void setgMultipleKOTPrintYN(String gMultipleKOTPrintYN) {
		this.gMultipleKOTPrintYN = gMultipleKOTPrintYN;
	}

	public String getgPrintKOTYN() {
		return gPrintKOTYN;
	}

	public void setgPrintKOTYN(String gPrintKOTYN) {
		this.gPrintKOTYN = gPrintKOTYN;
	}

	public String getgTreatMemberAsTable() {
		return gTreatMemberAsTable;
	}

	public void setgTreatMemberAsTable(String gTreatMemberAsTable) {
		this.gTreatMemberAsTable = gTreatMemberAsTable;
	}

	public String getgMultipleWaiterSelection() {
		return gMultipleWaiterSelection;
	}

	public void setgMultipleWaiterSelection(String gMultipleWaiterSelection) {
		this.gMultipleWaiterSelection = gMultipleWaiterSelection;
	}

	public String getgMemberCodeForKotInMposByCardSwipe() {
		return gMemberCodeForKotInMposByCardSwipe;
	}

	public void setgMemberCodeForKotInMposByCardSwipe(String gMemberCodeForKotInMposByCardSwipe) {
		this.gMemberCodeForKotInMposByCardSwipe = gMemberCodeForKotInMposByCardSwipe;
	}

	public String getgCMSMemberForKOTMPOS() {
		return gCMSMemberForKOTMPOS;
	}

	public void setgCMSMemberForKOTMPOS(String gCMSMemberForKOTMPOS) {
		this.gCMSMemberForKOTMPOS = gCMSMemberForKOTMPOS;
	}

	public String getgUseVatAndServiceTaxNoFromPOS() {
		return gUseVatAndServiceTaxNoFromPOS;
	}

	public void setgUseVatAndServiceTaxNoFromPOS(String gUseVatAndServiceTaxNoFromPOS) {
		this.gUseVatAndServiceTaxNoFromPOS = gUseVatAndServiceTaxNoFromPOS;
	}

	public String getgMemberCodeForMakeBillInMPOS() {
		return gMemberCodeForMakeBillInMPOS;
	}

	public void setgMemberCodeForMakeBillInMPOS(String gMemberCodeForMakeBillInMPOS) {
		this.gMemberCodeForMakeBillInMPOS = gMemberCodeForMakeBillInMPOS;
	}

	public String getgCalculateTaxOnMakeKOT() {
		return gCalculateTaxOnMakeKOT;
	}

	public void setgCalculateTaxOnMakeKOT(String gCalculateTaxOnMakeKOT) {
		this.gCalculateTaxOnMakeKOT = gCalculateTaxOnMakeKOT;
	}

	public String getgMenuItemSortingOn() {
		return gMenuItemSortingOn;
	}

	public void setgMenuItemSortingOn(String gMenuItemSortingOn) {
		this.gMenuItemSortingOn = gMenuItemSortingOn;
	}

	public String getgItemWiseKOTPrintYN() {
		return gItemWiseKOTPrintYN;
	}

	public void setgItemWiseKOTPrintYN(String gItemWiseKOTPrintYN) {
		this.gItemWiseKOTPrintYN = gItemWiseKOTPrintYN;
	}

	public String getgLastPOSForDayEnd() {
		return gLastPOSForDayEnd;
	}

	public void setgLastPOSForDayEnd(String gLastPOSForDayEnd) {
		this.gLastPOSForDayEnd = gLastPOSForDayEnd;
	}

	public String getgCMSPostingType() {
		return gCMSPostingType;
	}

	public void setgCMSPostingType(String gCMSPostingType) {
		this.gCMSPostingType = gCMSPostingType;
	}

	public String getgPopUpToApplyPromotionsOnBill() {
		return gPopUpToApplyPromotionsOnBill;
	}

	public void setgPopUpToApplyPromotionsOnBill(String gPopUpToApplyPromotionsOnBill) {
		this.gPopUpToApplyPromotionsOnBill = gPopUpToApplyPromotionsOnBill;
	}

	public String getgEnablePMSIntegration() {
		return gEnablePMSIntegration;
	}

	public void setgEnablePMSIntegration(String gEnablePMSIntegration) {
		this.gEnablePMSIntegration = gEnablePMSIntegration;
	}

	public String getgTheme() {
		return gTheme;
	}

	public void setgTheme(String gTheme) {
		this.gTheme = gTheme;
	}

	public String getgCardUsingScanQR() {
		return gCardUsingScanQR;
	}

	public void setgCardUsingScanQR(String gCardUsingScanQR) {
		this.gCardUsingScanQR = gCardUsingScanQR;
	}

	public String getgEnableTableReservationForCustomer() {
		return gEnableTableReservationForCustomer;
	}

	public void setgEnableTableReservationForCustomer(String gEnableTableReservationForCustomer) {
		this.gEnableTableReservationForCustomer = gEnableTableReservationForCustomer;
	}

	public String getgCallingAvailable() {
		return gCallingAvailable;
	}

	public void setgCallingAvailable(String gCallingAvailable) {
		this.gCallingAvailable = gCallingAvailable;
	}


	public void savePreference() {
		clsGlobalFunctions.gClientCode = getgClientCode();
		clsGlobalFunctions.gClientName = getgClientName();
		clsGlobalFunctions.gNegativeBilling = getgNegativeBilling();
		clsGlobalFunctions.gStartDate = getgStartDate();
		clsGlobalFunctions.gEndDate = getgEndDate();
		clsGlobalFunctions.gNatureOfBusinnes = getgNatureOfBusinnes();
		clsGlobalFunctions.gEnableKOT = getgEnableKOT();
		clsGlobalFunctions.gPrintVatNo = getgPrintVatNo();
		clsGlobalFunctions.gVatNo = getgVatNo();
		clsGlobalFunctions.gShowBill = getgShowBill();
		clsGlobalFunctions.gPrintServiceTaxNo = getgPrintServiceTaxNo();
		clsGlobalFunctions.gServiceTaxNo = getgServiceTaxNo();
		clsGlobalFunctions.gPOSType = getgPOSType();
		clsGlobalFunctions.gWebServiceLink = getgWebServiceLink();
		clsGlobalFunctions.gDataSendFrequency = getgDataSendFrequency();
		clsGlobalFunctions.gHOServerDate = getgHOServerDate();
		clsGlobalFunctions.gEnableKOTForDirectBiller = getgEnableKOTForDirectBiller();
		clsGlobalFunctions.gAreaWisePricing = getgAreaWisePricing();
		clsGlobalFunctions.gDirectBillerAreaCode = getgDirectBillerAreaCode();
		clsGlobalFunctions.gBillFormatType = getgBillFormatType();
		clsGlobalFunctions.gSkipWaiterAndPax = getgSkipWaiterAndPax();
		clsGlobalFunctions.gSkipWaiter = getgSkipWaiter();
		clsGlobalFunctions.gSkipPax = getgSkipPax();
		clsGlobalFunctions.gActivePromotions = getgActivePromotions();
		clsGlobalFunctions.gPriceFrom = getgPriceFrom();
		clsGlobalFunctions.gCMSIntegrationYN = getgCMSIntegrationYN();
		clsGlobalFunctions.gCMSWebServiceURL = getgCMSWebServiceURL();
		clsGlobalFunctions.gCMSPOSCode = getgCMSPOSCode();
		clsGlobalFunctions.gMultipleKOTPrintYN = getgMultipleKOTPrintYN();
		clsGlobalFunctions.gPrintKOTYN = getgPrintKOTYN();
		clsGlobalFunctions.gTreatMemberAsTable = getgTreatMemberAsTable();
		clsGlobalFunctions.gMultipleWaiterSelection = getgMultipleWaiterSelection();
		clsGlobalFunctions.gMemberCodeForKotInMposByCardSwipe = getgMemberCodeForKotInMposByCardSwipe();
		clsGlobalFunctions.gCMSMemberForKOTMPOS = getgCMSMemberForKOTMPOS();
		clsGlobalFunctions.gUseVatAndServiceTaxNoFromPOS = getgUseVatAndServiceTaxNoFromPOS();
		clsGlobalFunctions.gMemberCodeForMakeBillInMPOS = getgMemberCodeForMakeBillInMPOS();
		clsGlobalFunctions.gCalculateTaxOnMakeKOT = getgCalculateTaxOnMakeKOT();
		clsGlobalFunctions.gMenuItemSortingOn = getgMenuItemSortingOn();
		clsGlobalFunctions.gItemWiseKOTPrintYN = getgItemWiseKOTPrintYN();
		clsGlobalFunctions.gLastPOSForDayEnd = getgLastPOSForDayEnd();
		clsGlobalFunctions.gCMSPostingType = getgCMSPostingType();
		clsGlobalFunctions.gPopUpToApplyPromotionsOnBill = getgPopUpToApplyPromotionsOnBill();
		clsGlobalFunctions.gEnablePMSIntegration = getgEnablePMSIntegration();
		clsGlobalFunctions.gTheme = getgTheme();
		clsGlobalFunctions.gCardUsingScanQR = getgCardUsingScanQR();
		clsGlobalFunctions.gEnableTableReservationForCustomer = getgEnableTableReservationForCustomer();
		clsGlobalFunctions.gCallingAvailable = "Yes";
		clsGlobalFunctions.gEnableBillSeries=getgEnableBillSeries();
	}
}
