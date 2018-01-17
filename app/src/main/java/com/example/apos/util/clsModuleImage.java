package com.example.apos.util;


import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;

import java.util.HashMap;

public class clsModuleImage
{
    public HashMap<String, Integer> hmImageNames=null;

    public clsModuleImage()
    {
        hmImageNames=new HashMap<String, Integer>();
        if(clsGlobalFunctions.gTheme.equals("Default"))
        {
           // hmImageNames.put("imgshowcard",R.mipmap.imgshowcard);
           // hmImageNames.put("imgrechargecard",R.mipmap.imgrechargecard);
            hmImageNames.put("imgdirectbiller",R.mipmap.imgdirectbiller);
            hmImageNames.put("imgmakekot",R.mipmap.imgmakekot);
            hmImageNames.put("imgvoidkot",R.mipmap.imgvoidkot);
          //  hmImageNames.put("imgregisterdebitcard",R.mipmap.imgregisterdebitcard);
            hmImageNames.put("imgmakebill",R.mipmap.imgmakebill);
            hmImageNames.put("imgsalesreport",R.mipmap.imgsalesreport);
            hmImageNames.put("imgreprintdocs",R.mipmap.imgreprintdocs);
            hmImageNames.put("imgsettlebill",R.mipmap.imgsettlebill);
            hmImageNames.put("imgtablestatusreport",R.mipmap.imgtablestatusreport);
            hmImageNames.put("imgnckot",R.mipmap.imgnckot);
            hmImageNames.put("imgtakeaway",R.mipmap.imgtakeaway);
            hmImageNames.put("imgtablereservation",R.mipmap.imgtablereservation);
            hmImageNames.put("imgposwisesales",R.mipmap.imgposwisesales);
            hmImageNames.put("imgcustomerorder",R.mipmap.imgcustomerorder);
            hmImageNames.put("imgnonavailableitem",R.mipmap.imgnonavailableitem);
            hmImageNames.put("imgminimakekot",R.mipmap.imgminimakekot);
            hmImageNames.put("imgdayend",R.mipmap.imgdayend);
            hmImageNames.put("imgkdsforkotbookandprocess",R.mipmap.imgkdsforkotbookandprocess);
            hmImageNames.put("imgkps",R.mipmap.imgkps);
        }
        else if(clsGlobalFunctions.gTheme.equals("Color"))
        {
           // hmImageNames.put("imgshowcard",R.mipmap.imgshowcard);
           // hmImageNames.put("imgrechargecard",R.mipmap.imgrechargecard);
            hmImageNames.put("imgdirectbiller",R.mipmap.imgdirectbiller);
            hmImageNames.put("imgmakekot",R.mipmap.imgmakekot);
            hmImageNames.put("imgvoidkot",R.mipmap.imgvoidkot);
         //   hmImageNames.put("imgregisterdebitcard",R.mipmap.imgregisterdebitcard);
            hmImageNames.put("imgmakebill",R.mipmap.imgmakebill);
            hmImageNames.put("imgsalesreport",R.mipmap.imgsalesreport);
            hmImageNames.put("imgreprintdocs",R.mipmap.imgreprintdocs);
            hmImageNames.put("imgsettlebill",R.mipmap.imgsettlebill);
            hmImageNames.put("imgtablestatusreport",R.mipmap.imgtablestatusreport);
            hmImageNames.put("imgnckot",R.mipmap.imgnckot);
            hmImageNames.put("imgtakeaway",R.mipmap.imgtakeaway);
            hmImageNames.put("imgtablereservation",R.mipmap.imgtablereservation);
            hmImageNames.put("imgposwisesales",R.mipmap.imgposwisesales);
            hmImageNames.put("imgcustomerorder",R.mipmap.imgcustomerorder);
            hmImageNames.put("imgnonavailableitem",R.mipmap.imgnonavailableitem);
            hmImageNames.put("imgminimakekot",R.mipmap.imgminimakekot);
            hmImageNames.put("imgdayend",R.mipmap.imgdayend);
            hmImageNames.put("imgkdsforkotbookandprocess",R.mipmap.imgkdsforkotbookandprocess);
            hmImageNames.put("imgkps",R.mipmap.imgkps);
        }
        else
        {
         //   hmImageNames.put("imgshowcard",R.mipmap.imgshowcard1);
           // hmImageNames.put("imgrechargecard",R.mipmap.imgrechargecard1);
            hmImageNames.put("imgdirectbiller",R.mipmap.imgdirectbiller1);
            hmImageNames.put("imgmakekot",R.mipmap.imgmakekot1);
            hmImageNames.put("imgvoidkot",R.mipmap.imgvoidkot1);
          //  hmImageNames.put("imgregisterdebitcard",R.mipmap.imgregisterdebitcard1);
            hmImageNames.put("imgmakebill",R.mipmap.imgmakebill1);
            hmImageNames.put("imgsalesreport",R.mipmap.imgsalesreport1);
            hmImageNames.put("imgreprintdocs",R.mipmap.imgreprintdocs1);
            hmImageNames.put("imgsettlebill",R.mipmap.imgsettlebill1);
            hmImageNames.put("imgtablestatusreport",R.mipmap.imgtablestatusreport1);
            hmImageNames.put("imgnckot",R.mipmap.imgnckot1);
            hmImageNames.put("imgtakeaway",R.mipmap.imgtakeaway1);
            hmImageNames.put("imgtablereservation",R.mipmap.imgtablereservation1);
            hmImageNames.put("imgposwisesales",R.mipmap.imgposwisesales1);
            hmImageNames.put("imgcustomerorder",R.mipmap.imgcustomerorder1);
            hmImageNames.put("imgnonavailableitem",R.mipmap.imgnonavailableitem1);
            hmImageNames.put("imgminimakekot",R.mipmap.imgminimakekot1);
            hmImageNames.put("imgdayend",R.mipmap.imgdayend1);
            hmImageNames.put("imgkdsforkotbookandprocess",R.mipmap.imgkdsforkotbookandprocess1);
            hmImageNames.put("imgkps",R.mipmap.imgkps1);
        }


  /*    hmImageNames.put("imgadvancebookingreceipt", R.drawable.imgadvancebookingreceipt);
        hmImageNames.put("imgadvanceorder", R.drawable.imgadvanceorder);
        hmImageNames.put("imgadvanceorderflash",R.drawable.imgadvanceorderflash);
        hmImageNames.put("imgadvanceordertypemaster", R.drawable.imgadvanceordertypemaster);
        hmImageNames.put("imgareamaster", R.drawable.imgareamaster);
        hmImageNames.put("imgareawise", R.drawable.imgareawise);
        hmImageNames.put("imgasignhomedelivery",R.drawable.imgasignhomedelivery);
        hmImageNames.put("imgauditflash",R.drawable.imgauditflash);
        hmImageNames.put("imgauditorreport",R.drawable.imgauditorreport);
        hmImageNames.put("imgaveragepc",R.drawable.imgaveragepc);
        hmImageNames.put("imgavgitemsperbill",R.drawable.imgavgitemsperbill);
        hmImageNames.put("imgavgvalue",R.drawable.imgavgvalue);
        hmImageNames.put("imgbillwise",R.drawable.imgbillwise);
        hmImageNames.put("imgbillwisereport",R.drawable.imgbillwisereport);
        hmImageNames.put("imgblank",R.drawable.imgblank);
        hmImageNames.put("imgbulkmenuitempricing",R.drawable.imgbulkmenuitempricing);
        hmImageNames.put("imgcardtypemaster",R.drawable.imgcardtypemaster);
        hmImageNames.put("imgcashmanagement",R.drawable.imgcashmanagement);
        hmImageNames.put("imgchangemodule",R.drawable.imgchangemodule);
        hmImageNames.put("imgchangepos",R.drawable.imgchangepos);
        hmImageNames.put("imgcmnbtn1",R.drawable.imgcmnbtn1);
        hmImageNames.put("imgcomplimentarysettlementreport",R.drawable.imgrechargecard);
        hmImageNames.put("imgconsumptionreport",R.drawable.imgconsumptionreport);
        hmImageNames.put("imgcostcenter",R.drawable.imgshowcard);
        hmImageNames.put("imgcostcenterreport",R.drawable.imgcostcenterreport);
        hmImageNames.put("imgcostcenterwise",R.drawable.imgcostcenterwise);
        hmImageNames.put("imgcountermaster",R.drawable.imgcountermaster);
        hmImageNames.put("imgcustareamaster", R.drawable.imgcustareamaster);
        hmImageNames.put("imgcustomermaster",R.drawable.imgcustomermaster);
        hmImageNames.put("imgcustomertypemaster",R.drawable.imgcustomertypemaster);
        hmImageNames.put("imgcustwise",R.drawable.imgcustwise);
        hmImageNames.put("imgdailycollectionreport",R.drawable.imgdailycollectionreport);
        hmImageNames.put("imgdailysalesreport",R.drawable.imgdailysalesreport);
        hmImageNames.put("imgdayend",R.drawable.imgdayend);
        hmImageNames.put("imgdayendflash",R.drawable.imgdayendflash);
        hmImageNames.put("imgdayendwithoutdetails",R.drawable.imgdayendwithoutdetails);
        hmImageNames.put("imgdaywisesales",R.drawable.imgdaywisesales);
        hmImageNames.put("imgdebcardstatus",R.drawable.imgdebcardstatus);
        hmImageNames.put("imgdebitcardflash",R.drawable.imgdebitcardflash);
        hmImageNames.put("imgdelboycategorymaster",R.drawable.imgdelboycategorymaster);
        hmImageNames.put("imgdeliveryboy",R.drawable.imgdeliveryboy);
        hmImageNames.put("imgdeliveryboyincentive",R.drawable.imgdeliveryboyincentive);
        hmImageNames.put("imgdeliveryboywise",R.drawable.imgdeliveryboywise);
        hmImageNames.put("imgdiscountreport",R.drawable.imgdiscountreport);
        hmImageNames.put("imggiftvoucher",R.drawable.imggiftvoucher);
        hmImageNames.put("imggiftvoucherissue",R.drawable.imggiftvoucherissue);
        hmImageNames.put("imggroup",R.drawable.imggroup);
        hmImageNames.put("imggroupwise",R.drawable.imggroupwise);
        hmImageNames.put("imggroupwisereport",R.drawable.imggroupwisereport);
        hmImageNames.put("imggrpsubgrpwise",R.drawable.imggrpsubgrpwise);
        hmImageNames.put("imgguestcreditreport",R.drawable.imgguestcreditreport);
        hmImageNames.put("imghomedeliverywise",R.drawable.imghomedeliverywise);
        hmImageNames.put("imghourlywise",R.drawable.imghourlywise);
        hmImageNames.put("imghourlywiseitems",R.drawable.imghourlywiseitems);
        hmImageNames.put("imgimportdata",R.drawable.imgimportdata);
        hmImageNames.put("imgimportdata1",R.drawable.imgimportdata1);
        hmImageNames.put("imgitemmodifier",R.drawable.imgitemmodifier);
        hmImageNames.put("imgitemmodifierwise",R.drawable.imgitemmodifierwise);
        hmImageNames.put("imgitemwise",R.drawable.imgitemwise);
        hmImageNames.put("imgitemwisereport",R.drawable.imgitemwisereport);
        hmImageNames.put("imgkitchendisplaysystem",R.drawable.imgkitchendisplaysystem);
        hmImageNames.put("imgkottobill",R.drawable.imgkottobill);
        hmImageNames.put("imgkottobill1",R.drawable.imgkottobill1);
        hmImageNames.put("imglogout",R.drawable.imglogout);
        hmImageNames.put("imgloyaltymaster",R.drawable.imgloyaltymaster);
        hmImageNames.put("imgloyaltypointreport",R.drawable.imgloyaltypointreport);
        hmImageNames.put("imgmakebillfromkot",R.drawable.imgmakebillfromkot);
        hmImageNames.put("imgmasters",R.drawable.imgmasters);
        hmImageNames.put("imgmenuhead",R.drawable.imgmenuhead);
        hmImageNames.put("imgmenuheadwise",R.drawable.imgmenuheadwise);
        hmImageNames.put("imgmenuheadwisereport",R.drawable.imgmenuheadwisereport);
        hmImageNames.put("imgmenuheadwisewithmodifier",R.drawable.imgmenuheadwisewithmodifier);
        hmImageNames.put("imgmenuitem",R.drawable.imgmenuitem);
        hmImageNames.put("imgmodifiergroupmaster",R.drawable.imgmodifiergroupmaster);
        hmImageNames.put("imgmodifybill",R.drawable.imgmodifybill);
        hmImageNames.put("imgmovekot",R.drawable.imgmovekot);
        hmImageNames.put("imgmovetable",R.drawable.imgmovetable);
        hmImageNames.put("imgmulticostcenterkds",R.drawable.imgmulticostcenterkds);
        hmImageNames.put("imgnonchargablekotreport",R.drawable.imgnonchargablekotreport);
        hmImageNames.put("imgoperatorwise",R.drawable.imgoperatorwise);
        hmImageNames.put("imgoperatorwisereport",R.drawable.imgoperatorwisereport);
        hmImageNames.put("imgorderanalysisreport",R.drawable.imgorderanalysisreport);
        hmImageNames.put("imgphysicalstockposting",R.drawable.imgphysicalstockposting);
        hmImageNames.put("imgposmaster",R.drawable.imgposmaster);
        hmImageNames.put("imgpostposdatatocms",R.drawable.imgpostposdatatocms);
        hmImageNames.put("imgpostposdatatoho",R.drawable.imgpostposdatatoho);
        hmImageNames.put("imgposwisesales",R.drawable.imgposwisesales);
        hmImageNames.put("imgpricemenu",R.drawable.imgpricemenu);
        hmImageNames.put("imgproductionorder",R.drawable.imgproductionorder);
        hmImageNames.put("imgpromotionmaster",R.drawable.imgpromotionmaster);
        hmImageNames.put("imgpromotionreport",R.drawable.imgpromotionreport);
        hmImageNames.put("imgpropertysetup",R.drawable.imgpropertysetup);
        hmImageNames.put("imgreasonmaster",R.drawable.imgreasonmaster);
        hmImageNames.put("imgrechargedetail",R.drawable.imgrechargedetail);
        hmImageNames.put("imgrecipemaster",R.drawable.imgrecipemaster);
        hmImageNames.put("imgreordertime",R.drawable.imgreordertime);
        hmImageNames.put("imgreports",R.drawable.imgreports);
        hmImageNames.put("imgsalessummaryflash",R.drawable.imgsalessummaryflash);
        hmImageNames.put("imgsettlebill",R.drawable.imgsettlebill);
        hmImageNames.put("imgsettlementwisereport",R.drawable.imgsettlementwisereport);
        hmImageNames.put("imgshiftmaster",R.drawable.imgshiftmaster);
        hmImageNames.put("imgshortcutkeysetup",R.drawable.imgshortcutkeysetup);
        hmImageNames.put("imgsplitbill",R.drawable.imgsplitbill);
        hmImageNames.put("imgstatistics",R.drawable.imgstatistics);
        hmImageNames.put("imgstockadjustment",R.drawable.imgstockadjustment);
        hmImageNames.put("imgstockflashreport",R.drawable.imgstockflashreport);
        hmImageNames.put("imgstockin",R.drawable.imgstockin);
        hmImageNames.put("imgstockinoutflash",R.drawable.imgstockinoutflash);
        hmImageNames.put("imgstockout",R.drawable.imgstockout);
        hmImageNames.put("imgsubgroup",R.drawable.imgsubgroup);
        hmImageNames.put("imgsubgroupwise",R.drawable.imgsubgroupwise);
        hmImageNames.put("imgsubgroupwisereport",R.drawable.imgsubgroupwisereport);
        hmImageNames.put("imgtablemaster",R.drawable.imgtablemaster);
        hmImageNames.put("imgtablereservation",R.drawable.imgtablereservation);
        hmImageNames.put("imgtablewise",R.drawable.imgtablewise);
        hmImageNames.put("imgtaxbreakupsummaryreport",R.drawable.imgtaxbreakupsummaryreport);
        hmImageNames.put("imgtaxmaster",R.drawable.imgtaxmaster);
        hmImageNames.put("imgtaxregeneration",R.drawable.imgtaxregeneration);
        hmImageNames.put("imgtaxsummaryteport",R.drawable.imgtaxsummaryteport);
        hmImageNames.put("imgtaxwisereport",R.drawable.imgtaxwisereport);
        hmImageNames.put("imgtaxwisesales",R.drawable.imgtaxwisesales);
        hmImageNames.put("imgtdh",R.drawable.imgtdh);
        hmImageNames.put("imgtipreport",R.drawable.imgtipreport);
        hmImageNames.put("imgtools",R.drawable.imgtools);
        hmImageNames.put("imgtransactions",R.drawable.imgtransactions);
        hmImageNames.put("imgunsettlebill",R.drawable.imgunsettlebill);
        hmImageNames.put("imguserregistration",R.drawable.imguserregistration);
        hmImageNames.put("imgvoidadvorder",R.drawable.imgvoidadvanceorder);
        hmImageNames.put("imgvoidbill",R.drawable.imgvoidbill);
        hmImageNames.put("imgvoidbillreport",R.drawable.imgvoidbillreport);
        hmImageNames.put("imgvoidkotreport",R.drawable.imgvoidkotreport);
        hmImageNames.put("imgvoidstock",R.drawable.imgvoidstock);
        hmImageNames.put("imgwaiterincentivesreports",R.drawable.imgwaiterincentivesreports);
        hmImageNames.put("imgwaitermaster",R.drawable.imgwaitermaster);
        hmImageNames.put("imgwaiterwise",R.drawable.imgwaiterwise);
        hmImageNames.put("imgwaiterwiseitemreport",R.drawable.imgwaiterwiseitemreport);
        hmImageNames.put("imgwaiterwisesalesreport",R.drawable.imgwaiterwisesalesreport);
        hmImageNames.put("imgzonemaster",R.drawable.imgzonemaster);
        hmImageNames.put("imgsettlement",R.drawable.imgsettlement);
*/

    }

    public int funGetImage(String imageName)
    {
        System.out.println("IMG="+imageName);
        int image=0;
        if(clsGlobalFunctions.gPOSVerion.equals("Lite"))
        {
            if(hmImageNames.containsKey("imgsalesreport"))
            {
                image=Integer.parseInt(hmImageNames.get("imgsalesreport").toString());
            }
        }
        else
        {
            if(hmImageNames.containsKey(imageName))
            {
                image=Integer.parseInt(hmImageNames.get(imageName).toString());
            }
        }
        return image;
    }
}