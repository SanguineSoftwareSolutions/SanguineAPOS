package com.example.apos.activity;

import com.example.apos.bean.clsKOTItemDtlBean;
import com.example.apos.bean.clsKotItemsListBean;
import com.example.apos.bean.clsSettlementDtl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class clsGlobalFunctions {
	public static String gCallingAvailable = "No";
	public static HashMap gHashMapItemDtl = new HashMap<String, ArrayList>();
	public static HashMap gHashMapCounterDtl = null;
	public static ArrayList arrListItemDtl = new ArrayList();
	public static String gAPOSWebSrviceURL;
	public static String gBillPrinterType = "External";//TODO: Remove hardcoded value
	public static String gKOTPrinterType;
	public static String gCounterCode, gTheme = "Default";
	public static String gCounterWiseBilling,gEnableBillSeries;
	public static String gPOSCode, gPOSName, gUserCode, gSuperUser, gUserName;
	public static String gPOSDate,gPOSDateHeader;
	public static Map<String, List<clsSettlementDtl>> gHashMapSettlementDtl = null;
	public static String gClientCode, gClientName, gNegativeBilling, gStartDate, gEndDate, gNatureOfBusinnes, gEnableKOT, gPrintVatNo, gVatNo;
	public static String gShowBill, gPrintServiceTaxNo, gServiceTaxNo, gPOSType, gWebServiceLink, gDataSendFrequency, gHOServerDate, gEnableKOTForDirectBiller;
	public static String gAreaWisePricing, gDirectBillerAreaCode, gBillFormatType, gSkipWaiterAndPax, gSkipWaiter, gSkipPax, gActivePromotions;
	public static String gPriceFrom, gCMSIntegrationYN, gCMSPOSCode, gMultipleKOTPrintYN, gPrintKOTYN, gTreatMemberAsTable;
	public static String gMultipleWaiterSelection;
	public static String gMemberCodeForKotInMposByCardSwipe;
	public static String gCMSMemberForKOTMPOS;
	public static String gCMSWebServiceURL, gSanguineWebserviceURL = "";
	public static String gPOSVatNo;
	public static String gPOSServiceTaxNo;
	public static String gPrintPOSVatNo, gPOSVerion;
	public static String gPrintPOSServiceTaxNo, gLastBillDate = "", gMenuItemSortingOn;
	public static String gUseVatAndServiceTaxNoFromPOS, gStatus, gMemberCodeForMakeBillInMPOS, gCalculateTaxOnMakeKOT;
	public static String gItemWiseKOTPrintYN, gLastPOSForDayEnd, gCMSPostingType, gPopUpToApplyPromotionsOnBill, gEnablePMSIntegration = "", gPhoneNo = "", gCustName = "", gCardUsingScanQR = "N";
	public static boolean gHasTerminalLicence = false;
	public static List<clsKOTItemDtlBean> gPendingKOTItem = new ArrayList<clsKOTItemDtlBean>();
	public static int gShiftNo;
	public static String gShiftEnd, gDayEnd, gPOSStartDate, gBillDateTimeType, gEnableTableReservationForCustomer = "N";
	public static Map<String, ArrayList<clsKotItemsListBean>> gHashMapItemList =null;
	public static ArrayList gArrListMenuItemMaster  = null;

	void funInitSetupValues() {
		//new InitSetupWebService().execute();
	}

	public static String funGetCurrentDateTime() {
		Date dtCurrentDate = new Date();
		String dateTime = (dtCurrentDate.getYear() + 1900) + "-" + (dtCurrentDate.getMonth() + 1)
				+ "-" + (dtCurrentDate.getDate()) + " " + (dtCurrentDate.getHours())
				+ ":" + (dtCurrentDate.getMinutes()) + ":" + (dtCurrentDate.getSeconds());
		return dateTime;
	}

	public static String funGetCurrentDate() {
		Date dtCurrentDate = new Date();
		String date = (dtCurrentDate.getYear() + 1900) + "-" + (dtCurrentDate.getMonth() + 1)
				+ "-" + (dtCurrentDate.getDate());
		return date;
	}


	public static String funGetPOSDateTime() {
		Date dtCurrentDate = new Date();
		String POSDateTime = gPOSDate + " " + (dtCurrentDate.getHours())
				+ ":" + (dtCurrentDate.getMinutes()) + ":" + (dtCurrentDate.getSeconds());
		return POSDateTime;
	}


	//Convert byte array to string. Used for card swipe functionality.
	public static String byteArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder(a.length * 2);
		for (byte b : a)
			sb.append(String.format("%02x", b & 0xff));
		return sb.toString();
	}

	//ﾭ￞������%252011000002?��;201100002?��;201100002?
	public static String funConvertCardString(byte[] track) {
		String cardNo = "";
		String cardString = "";

		for (int i = 7; i < track.length; i++) {
			cardString = cardString + (char) track[i];
		}


		System.out.println("Card Data==" + cardString);
		if (cardString.contains("%B")) {
			cardNo = cardString.substring(2, cardString.indexOf("^"));
			System.out.println("ISO==" + cardNo);
		} else {
			if (cardString.length() > 0) {
				// cardString="%2016000358?��;2016000358?��;2016000358?";
				StringBuilder sb = new StringBuilder(cardString);
				int percIndex = sb.indexOf("%");
				String allTracks = "";
				if (sb.toString().contains("?")) {
					System.out.println("In IF == " + sb);
					allTracks = sb.substring(percIndex, sb.lastIndexOf("?") + 1);
					System.out.println("In IF == " + allTracks);
				} else {
					allTracks = sb.toString();
					//allTracks="%2016000358?��;2016000358?��;2016000358?";
					System.out.println("In IF == " + allTracks);
				}
				String[] arrText = allTracks.split(";");
				System.out.println("all tracks1= " + arrText[0]);
				System.out.println("all tracks2= " + arrText[1]);
				System.out.println("all tracks3= " + arrText[2]);
				String track1 = "", track2 = "", track3 = "";

				if (arrText.length > 0) {
					if (sb.toString().contains("?")) {
						StringBuilder sbArrLength = new StringBuilder(arrText[0]);
						if (sbArrLength.toString().contains("?")) {
							track1 = arrText[0].substring(1, arrText[0].indexOf("?")).replaceAll("%", "");
						}
						if (arrText.length > 1) {
							track2 = arrText[1].substring(0, arrText[1].indexOf("?")).replaceAll("%", "");
						}
						if (arrText.length > 2) {
							track3 = arrText[2].substring(0, arrText[2].indexOf("?")).replaceAll("%", "");
						}
					} else {
						track1 = arrText[0].replaceAll("%", "");
						track2 = arrText[1].replaceAll("%", "");
						track3 = arrText[2].replaceAll("%", "");
					}
				}

				System.out.println(track1);
				System.out.println(track2);
				System.out.println(track3);

				if (!track1.isEmpty()) {
					cardNo = track1;
				} else if (!track2.isEmpty()) {
					cardNo = track2;
				} else if (!track3.isEmpty()) {
					cardNo = track2;
				}
				//cardNo=cardNo.substring(0,10);
				//cardNo = track1 + ";" + track2 + ";" + track3;
				System.out.println(cardNo);
				cardNo = cardNo.substring(0, 10);
				// cardNo="2011100004";
			}

			//cardNo="%2011000002?;201100002?;201100002?";
			//cardNo="2011100010";
			cardNo = "2016180330";
		}

		return cardNo;
	}


}









