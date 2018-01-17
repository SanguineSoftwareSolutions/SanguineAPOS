package com.example.apos.aposlicence;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class clsClientDetails
{
    public static HashMap<String, clsClientDetails> hmClientDtl;
    private static final SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
    public String id;
    public String Client_Name;
    public Date installDate;
    public Date expiryDate;
    private String posVersion;//Enterprise/Lite
    private int intMAXTerminal;//No. of POS Machines

    private clsClientDetails(String id, String Client_Name, Date installDate, Date expiryDate)
    {
        this.id = id;
        this.Client_Name = Client_Name;
        this.installDate = installDate;
        this.expiryDate = expiryDate;
    }

    private clsClientDetails(String id, String Client_Name, Date installDate, Date expiryDate,String posVersion,int intMAXTerminal)
    {
        this.id = id;
        this.Client_Name = Client_Name;
        this.installDate = installDate;
        this.expiryDate = expiryDate;
        this.posVersion=posVersion;
        this.intMAXTerminal=intMAXTerminal;
    }

    public static clsClientDetails createClientDetails(String id, String clientName, Date installDate, Date expiryDate)
    {
        return new clsClientDetails(id, clientName, installDate, expiryDate);
    }



    public static clsClientDetails createClientDetails(String id, String clientName, Date installDate, Date expiryDate,String posVersion,int intMAXTerminal)
    {
        return new clsClientDetails(id, clientName, installDate, expiryDate,posVersion,intMAXTerminal);
    }

    public static void funAddClientCodeAndName()
    {
        try {
            hmClientDtl = new HashMap<String, clsClientDetails>();
            hmClientDtl.put("000.000", clsClientDetails.createClientDetails("000.000", "Demo Company", dFormat.parse("2014-06-19"), dFormat.parse("2018-10-22"),"Enterprise",1));
            hmClientDtl.put("000.001", clsClientDetails.createClientDetails("001.001", "Monginis - Hadapsar", dFormat.parse("2014-06-19"), dFormat.parse("2014-06-19"),"Enterprise",1));
            hmClientDtl.put("003.001", clsClientDetails.createClientDetails("003.001", "FOODJOCKEYS LLP", dFormat.parse("2014-03-02"), dFormat.parse("2016-03-02"),"Enterprise",1));
            hmClientDtl.put("004.001", clsClientDetails.createClientDetails("004.001", "CLIMAX OF FLAVORS", dFormat.parse("2014-03-02"), dFormat.parse("2016-04-07"),"Enterprise",1));
            hmClientDtl.put("009.001", clsClientDetails.createClientDetails("009.001", "SANSKAR BAZAAR", dFormat.parse("2014-05-01"), dFormat.parse("2016-05-01"),"Enterprise",1));
            hmClientDtl.put("010.001", clsClientDetails.createClientDetails("010.001", "Junnos Pizza", dFormat.parse("2014-05-19"), dFormat.parse("2016-05-07"),"Enterprise",1));//HO
            hmClientDtl.put("010.002", clsClientDetails.createClientDetails("010.002", "JP", dFormat.parse("2014-06-14"), dFormat.parse("2017-11-24"),"Enterprise",1));//renewed on 24-11-2016 also rename Junnos Pizza to JP
            hmClientDtl.put("011.001", clsClientDetails.createClientDetails("011.001", "Soul Cuisine", dFormat.parse("2014-06-19"), dFormat.parse("2016-06-22"),"Enterprise",1));
            hmClientDtl.put("012.001", clsClientDetails.createClientDetails("012.001", "Dales Eden", dFormat.parse("2014-06-25"), dFormat.parse("2015-01-15"),"Enterprise",1));
            hmClientDtl.put("013.001", clsClientDetails.createClientDetails("013.001", "Life Positive Pvt Ltd", dFormat.parse("2014-06-25"), dFormat.parse("2050-01-15"),"Enterprise",1));
            hmClientDtl.put("014.001", clsClientDetails.createClientDetails("014.001", "SONAI RESTAURANT & BAR", dFormat.parse("2014-06-26"), dFormat.parse("2015-06-26"),"Enterprise",1));
            hmClientDtl.put("015.001", clsClientDetails.createClientDetails("015.001", "SWAGAT VEG.", dFormat.parse("2014-04-26"), dFormat.parse("2015-06-05"),"Enterprise",1));
            hmClientDtl.put("016.001", clsClientDetails.createClientDetails("016.001", "BASIL", dFormat.parse("2014-07-07"), dFormat.parse("2017-01-07"),"Enterprise",1));
            hmClientDtl.put("018.001", clsClientDetails.createClientDetails("018.001", "Life Positive Foundation", dFormat.parse("2014-07-11"), dFormat.parse("2015-07-11"),"Enterprise",1));
            hmClientDtl.put("019.001", clsClientDetails.createClientDetails("019.001", "Aisha Fashion Avenue", dFormat.parse("2014-07-14"), dFormat.parse("2015-07-14"),"Enterprise",1));
            hmClientDtl.put("002.002", clsClientDetails.createClientDetails("002.001", "Red Peppers", dFormat.parse("2014-07-14"), dFormat.parse("2017-04-07"),"Enterprise",1));
            hmClientDtl.put("020.001", clsClientDetails.createClientDetails("020.001", "Zeal", dFormat.parse("2014-08-06"), dFormat.parse("2016-11-19"),"Enterprise",1));
            hmClientDtl.put("021.001", clsClientDetails.createClientDetails("021.001", "Mezbaan Carters Blue", dFormat.parse("2014-08-13"), dFormat.parse("2014-11-10"),"Enterprise",1));
            hmClientDtl.put("022.001", clsClientDetails.createClientDetails("022.001", "MR Fried Chicken", dFormat.parse("2014-09-23"), dFormat.parse("2017-08-22"),"Enterprise",1));
            hmClientDtl.put("023.001", clsClientDetails.createClientDetails("023.001", "SID Hospitality", dFormat.parse("2014-09-23"), dFormat.parse("2015-10-15"),"Enterprise",1));
            hmClientDtl.put("024.001", clsClientDetails.createClientDetails("024.001", "Eden Cake Shop", dFormat.parse("2014-09-23"), dFormat.parse("2017-09-23"),"Enterprise",1)); // Outlet 1
            hmClientDtl.put("024.002", clsClientDetails.createClientDetails("024.002", "Eden Cake Shop", dFormat.parse("2015-09-17"), dFormat.parse("2017-09-23"),"Enterprise",1)); // Outlet 2
            hmClientDtl.put("024.003", clsClientDetails.createClientDetails("024.003", "Eden Cake Shop", dFormat.parse("2016-03-23"), dFormat.parse("2017-03-23"),"Enterprise",1)); // Outlet 3
            hmClientDtl.put("025.001", clsClientDetails.createClientDetails("025.001", "Dravidas Bistro", dFormat.parse("2014-10-02"), dFormat.parse("2015-10-15"),"Enterprise",1));
            hmClientDtl.put("026.001", clsClientDetails.createClientDetails("026.001", "FUNBITEZ", dFormat.parse("2014-09-23"), dFormat.parse("2017-05-25"),"Enterprise",1));
            hmClientDtl.put("026.002", clsClientDetails.createClientDetails("026.002", "CREATIVE FOODS", dFormat.parse("2015-07-08"), dFormat.parse("2016-07-08"),"Enterprise",1));
            hmClientDtl.put("026.003", clsClientDetails.createClientDetails("026.003", "CREATIVE FOODS", dFormat.parse("2015-07-08"), dFormat.parse("2016-07-08"),"Enterprise",1));
            hmClientDtl.put("026.000", clsClientDetails.createClientDetails("026.000", "CREATIVE FOODS", dFormat.parse("2015-07-08"), dFormat.parse("2016-07-08"),"Enterprise",1)); //HO Licence
            hmClientDtl.put("027.001", clsClientDetails.createClientDetails("027.001", "Cilantro", dFormat.parse("2014-10-23"), dFormat.parse("2095-01-15"),"Enterprise",1));
            hmClientDtl.put("028.001", clsClientDetails.createClientDetails("028.001", "THE APPETITE MOMOS", dFormat.parse("2014-11-01"), dFormat.parse("2016-12-02"),"Enterprise",1));
            hmClientDtl.put("030.001", clsClientDetails.createClientDetails("030.001", "11 SPICES", dFormat.parse("2014-11-19"), dFormat.parse("2017-12-19"),"Enterprise",1));
            hmClientDtl.put("031.001", clsClientDetails.createClientDetails("031.001", "INTERVAL FOODCOURT", dFormat.parse("2014-11-29"), dFormat.parse("2016-1-29"),"Enterprise",1));
            hmClientDtl.put("032.001", clsClientDetails.createClientDetails("032.001", "ROLLING STOVE", dFormat.parse("2014-11-24"), dFormat.parse("2015-12-24"),"Enterprise",1));
            hmClientDtl.put("032.002", clsClientDetails.createClientDetails("032.002", "ROLLING STOVE", dFormat.parse("2014-11-24"), dFormat.parse("2015-12-24"),"Enterprise",1));
            hmClientDtl.put("032.003", clsClientDetails.createClientDetails("032.003", "ROLLING STOVE", dFormat.parse("2014-11-24"), dFormat.parse("2015-12-24"),"Enterprise",1));
            hmClientDtl.put("033.001", clsClientDetails.createClientDetails("033.001", "THE J", dFormat.parse("2014-12-06"), dFormat.parse("2016-12-06"),"Enterprise",1));
            hmClientDtl.put("034.001", clsClientDetails.createClientDetails("034.001", "THE BLUE ROOF CLUB", dFormat.parse("2014-12-08"), dFormat.parse("2017-03-08"),"Enterprise",1));//renewed on 06-12-2016
            hmClientDtl.put("035.001", clsClientDetails.createClientDetails("035.001", "The Local Cafe", dFormat.parse("2014-12-09"), dFormat.parse("2017-03-31"),"Enterprise",1));// old name was "NE THING FOR CHOCOLATE" rename on 16-08-2016
            //hmClientDtl.put("036.001", clsClientDetails.createClientDetails("036.001", "CREATIVE FOODS",dFormat.parse("2014-12-09"),dFormat.parse("2015-01-09")));
            hmClientDtl.put("037.001", clsClientDetails.createClientDetails("037.001", "BAKERS KRAFT", dFormat.parse("2014-12-19"), dFormat.parse("2016-02-19"),"Enterprise",1));
            hmClientDtl.put("037.002", clsClientDetails.createClientDetails("037.002", "BAKERS KRAFT", dFormat.parse("2014-12-19"), dFormat.parse("2016-02-19"),"Enterprise",1));
            hmClientDtl.put("038.001", clsClientDetails.createClientDetails("038.001", "SPICE FUSION", dFormat.parse("2014-12-19"), dFormat.parse("2017-03-22"),"Enterprise",1));
            hmClientDtl.put("039.001", clsClientDetails.createClientDetails("039.001", "ABHIRUCHI", dFormat.parse("2014-12-31"), dFormat.parse("2015-01-31"),"Enterprise",1));
            hmClientDtl.put("040.001", clsClientDetails.createClientDetails("040.001", "HAPPINESS DELI", dFormat.parse("2015-01-03"), dFormat.parse("2016-01-03"),"Enterprise",1));
            hmClientDtl.put("041.001", clsClientDetails.createClientDetails("041.001", "THE WHITE LOUNGE", dFormat.parse("2015-01-09"), dFormat.parse("2017-01-09"),"Enterprise",1));
            hmClientDtl.put("042.001", clsClientDetails.createClientDetails("042.001", "Ashoka Pure Veg", dFormat.parse("2015-01-09"), dFormat.parse("2018-06-06"),"Enterprise",1));
            hmClientDtl.put("042.002", clsClientDetails.createClientDetails("042.002", "Ashoka Spice", dFormat.parse("2015-01-09"), dFormat.parse("2017-01-09"),"Enterprise",1));
            hmClientDtl.put("043.001", clsClientDetails.createClientDetails("043.001", "HAKKA EXPRESS", dFormat.parse("2015-01-14"), dFormat.parse("2017-01-14"),"Enterprise",1));
            hmClientDtl.put("043.002", clsClientDetails.createClientDetails("043.002", "HAKKA EXPRESS", dFormat.parse("2015-10-14"), dFormat.parse("2016-10-14"),"Enterprise",1));
            hmClientDtl.put("044.001", clsClientDetails.createClientDetails("044.001", "SWEETS INDIA", dFormat.parse("2015-09-11"), dFormat.parse("2015-11-12"),"Enterprise",1)); // HO Licence For OHRIS
            //hmClientDtl.put("044.002", clsClientDetails.createClientDetails("044.002", "HOTEL KAMAL PVT LTD",dFormat.parse("2015-09-11"),dFormat.parse("2015-11-12")));
            hmClientDtl.put("045.001", clsClientDetails.createClientDetails("045.001", "Mr Beans", dFormat.parse("2015-02-05"), dFormat.parse("2016-03-10"),"Enterprise",1));
            hmClientDtl.put("046.001", clsClientDetails.createClientDetails("046.001", "CHANSON HOSPITALITY PVT. LTD", dFormat.parse("2015-02-10"), dFormat.parse("2015-05-11"),"Enterprise",1));
            hmClientDtl.put("047.001", clsClientDetails.createClientDetails("047.001", "ROYAL CONNAUGHT BOAT CLUB", dFormat.parse("2015-03-07"), dFormat.parse("2095-03-07"),"Enterprise",1));
            hmClientDtl.put("048.001", clsClientDetails.createClientDetails("048.001", "WHITE CASTLE", dFormat.parse("2015-03-17"), dFormat.parse("2017-03-01"),"Enterprise",1));
            hmClientDtl.put("049.001", clsClientDetails.createClientDetails("049.001", "DIVINE BOWL", dFormat.parse("2015-03-17"), dFormat.parse("2017-03-17"),"Enterprise",1));
            hmClientDtl.put("050.001", clsClientDetails.createClientDetails("050.001", "SAIRAJ PURE VEG", dFormat.parse("2015-03-17"), dFormat.parse("2017-05-17"),"Enterprise",1));
            hmClientDtl.put("051.001", clsClientDetails.createClientDetails("051.001", "BOMBAY CATERING COMPANY", dFormat.parse("2015-03-23"), dFormat.parse("2015-06-23"),"Enterprise",1));
            hmClientDtl.put("052.001", clsClientDetails.createClientDetails("052.001", "CURRY N BITES", dFormat.parse("2015-04-04"), dFormat.parse("2016-04-04"),"Enterprise",1));
            hmClientDtl.put("053.001", clsClientDetails.createClientDetails("053.001", "WHITE DOVE", dFormat.parse("2015-04-11"), dFormat.parse("2017-02-31"),"Enterprise",1));//renew on 03-11-2016 to 2017-02-31 old date was 2016-09-13
            hmClientDtl.put("054.001", clsClientDetails.createClientDetails("054.001", "J HEARSCH COMPANY", dFormat.parse("2015-04-16"), dFormat.parse("2017-06-25"),"Enterprise",1));
            hmClientDtl.put("055.001", clsClientDetails.createClientDetails("055.001", "BOMBAY BURGER", dFormat.parse("2015-04-16"), dFormat.parse("2017-05-16"),"Enterprise",1));//renew on 08-01-2016
            hmClientDtl.put("056.001", clsClientDetails.createClientDetails("056.001", "SYKZ CAFE", dFormat.parse("2015-05-04"), dFormat.parse("2017-05-04"),"Enterprise",1));
            hmClientDtl.put("057.001", clsClientDetails.createClientDetails("057.001", "LALAz mini punjabb", dFormat.parse("2015-05-11"), dFormat.parse("2015-06-31"),"Enterprise",1));
            hmClientDtl.put("058.001", clsClientDetails.createClientDetails("058.001", "Jainsons", dFormat.parse("2015-05-09"), dFormat.parse("2015-05-20"),"Enterprise",1));
            hmClientDtl.put("059.001", clsClientDetails.createClientDetails("059.001", "SHAGUN", dFormat.parse("2015-05-14"), dFormat.parse("2015-07-14"),"Enterprise",1));
            hmClientDtl.put("060.001", clsClientDetails.createClientDetails("060.001", "FLYING SAUCER SKY BAR", dFormat.parse("2015-06-01"), dFormat.parse("2017-07-30"),"Enterprise",1));//renew on 26-12-2016 till july
            hmClientDtl.put("061.001", clsClientDetails.createClientDetails("061.001", "THE FOOD PLAZA", dFormat.parse("2015-06-01"), dFormat.parse("2017-06-01"),"Enterprise",1));//rename SHAN E MAROL to THE FOOD PLAZA
            hmClientDtl.put("062.001", clsClientDetails.createClientDetails("062.001", "THE PATIO", dFormat.parse("2015-06-24"), dFormat.parse("2016-07-24"),"Enterprise",1));
            hmClientDtl.put("063.001", clsClientDetails.createClientDetails("063.001", "NFC", dFormat.parse("2015-07-01"), dFormat.parse("2016-07-24"),"Enterprise",1));
            hmClientDtl.put("064.001", clsClientDetails.createClientDetails("064.001", "Cafe Olivo", dFormat.parse("2015-07-06"), dFormat.parse("2017-07-24"),"Enterprise",1));
            hmClientDtl.put("065.001", clsClientDetails.createClientDetails("065.001", "The Pizza Farm", dFormat.parse("2015-07-23"), dFormat.parse("2017-07-23"),"Enterprise",1));//renew on 08-01-2016
            hmClientDtl.put("066.001", clsClientDetails.createClientDetails("066.001", "Next Step Restaurant", dFormat.parse("2015-08-04"), dFormat.parse("2016-12-19"),"Enterprise",1));
            hmClientDtl.put("067.001", clsClientDetails.createClientDetails("067.001", "SHUBHAM GLOBAL FOODS LTD (CURRYMIA)", dFormat.parse("2015-08-08"), dFormat.parse("2016-08-08"),"Enterprise",1));
            hmClientDtl.put("068.001", clsClientDetails.createClientDetails("068.001", "Saloni Retail LLP", dFormat.parse("2015-08-11"), dFormat.parse("2016-08-11"),"Enterprise",1));
            hmClientDtl.put("069.001", clsClientDetails.createClientDetails("069.001", "DAIWONG", dFormat.parse("2015-08-13"), dFormat.parse("2017-09-16"),"Enterprise",1));//renewed on 16-09-2016
            hmClientDtl.put("070.001", clsClientDetails.createClientDetails("070.001", "Bubsterrs", dFormat.parse("2015-08-20"), dFormat.parse("2017-09-01"),"Enterprise",1));//renewed on 01-09-2016
            hmClientDtl.put("071.001", clsClientDetails.createClientDetails("071.001", "La Bouchee dOr", dFormat.parse("2015-08-22"), dFormat.parse("2015-10-23"),"Enterprise",1));
            hmClientDtl.put("072.001", clsClientDetails.createClientDetails("072.001", "MOHANLAL S MITHAIWALA", dFormat.parse("2015-08-25"), dFormat.parse("2017-09-25"),"Enterprise",1));
            hmClientDtl.put("073.001", clsClientDetails.createClientDetails("073.001", "I HOSPITALITY", dFormat.parse("2015-09-01"), dFormat.parse("2016-03-31"),"Enterprise",1));
            hmClientDtl.put("074.001", clsClientDetails.createClientDetails("074.001", "THE POONA CLUB LTD", dFormat.parse("2015-09-02"), dFormat.parse("2017-03-31"),"Enterprise",1));
            hmClientDtl.put("075.001", clsClientDetails.createClientDetails("075.001", "SWEETS INDIA", dFormat.parse("2015-01-20"), dFormat.parse("2016-02-21"),"Enterprise",1));
            hmClientDtl.put("076.001", clsClientDetails.createClientDetails("076.001", "KLOCK KITCHEN", dFormat.parse("2015-09-16"), dFormat.parse("2017-09-16"),"Enterprise",1));//renewd on 03-10-2016
            hmClientDtl.put("077.001", clsClientDetails.createClientDetails("077.001", "JAINSONS SWEETS", dFormat.parse("2015-09-16"), dFormat.parse("2017-10-17"),"Enterprise",1)); // HO Licence // renewed on 19-10-2016 for 1 year old date is 2016-10-17
            hmClientDtl.put("077.002", clsClientDetails.createClientDetails("077.002", "JAINSONS SWEETS", dFormat.parse("2015-09-16"), dFormat.parse("2017-10-17"),"Enterprise",1)); // Outlet 1
            hmClientDtl.put("077.003", clsClientDetails.createClientDetails("077.003", "JAINSONS SWEETS", dFormat.parse("2015-09-16"), dFormat.parse("2017-10-17"),"Enterprise",1)); // Outlet 2
            hmClientDtl.put("078.001", clsClientDetails.createClientDetails("078.001", "THE NORTHERN FRONTIER", dFormat.parse("2015-09-18"), dFormat.parse("2016-10-18"),"Enterprise",1));
            hmClientDtl.put("079.001", clsClientDetails.createClientDetails("079.001", "Baked & Wired", dFormat.parse("2015-09-18"), dFormat.parse("2016-11-02"),"Enterprise",1));//renewed on 27-10-2016
            hmClientDtl.put("080.001", clsClientDetails.createClientDetails("080.001", "KAREEMS", dFormat.parse("2015-10-14"), dFormat.parse("2017-11-15"),"Enterprise",1));//renewed on 16-11-2016 for 1 year
            hmClientDtl.put("081.001", clsClientDetails.createClientDetails("081.001", "THE APPETITE DESTINATION", dFormat.parse("2015-10-14"), dFormat.parse("2016-10-14"),"Enterprise",1));
            hmClientDtl.put("082.001", clsClientDetails.createClientDetails("082.001", "HONEYGUIDES FOOD PARADISE", dFormat.parse("2015-10-14"), dFormat.parse("2016-10-14"),"Enterprise",1));
            hmClientDtl.put("083.001", clsClientDetails.createClientDetails("083.001", "KIMLING EXPRESS", dFormat.parse("2015-10-20"), dFormat.parse("2016-10-20"),"Enterprise",1));
            hmClientDtl.put("084.001", clsClientDetails.createClientDetails("084.001", "CAFE GOA", dFormat.parse("2015-10-20"), dFormat.parse("2016-01-19"),"Enterprise",1));
            hmClientDtl.put("085.001", clsClientDetails.createClientDetails("085.001", "CHOPS AND HOPS HOSPITALITY SERVICES LLP", dFormat.parse("2015-10-22"), dFormat.parse("2017-10-31"),"Enterprise",1));//renewed on 30-10-2016
            hmClientDtl.put("086.001", clsClientDetails.createClientDetails("086.001", "NAUGHTY ANGEL CAFE", dFormat.parse("2015-10-23"), dFormat.parse("2017-11-23"),"Enterprise",1));//renewed on 23-11-2016
            hmClientDtl.put("087.001", clsClientDetails.createClientDetails("087.001", "MADHUKAR RESTAURANT", dFormat.parse("2015-11-18"), dFormat.parse("2017-11-18"),"Enterprise",1));//renewed on 20-11-2016
            hmClientDtl.put("088.001", clsClientDetails.createClientDetails("088.001", "The Bar & Eatery(M/s Next Step Restaurants)",dFormat.parse("2015-11-24"), dFormat.parse("2018-03-04"),"Enterprise",1));//renewed on 03-12-2016
            hmClientDtl.put("089.001", clsClientDetails.createClientDetails("089.001", "JORUP ENTERPRISE LLP", dFormat.parse("2015-12-17"), dFormat.parse("2017-01-19"),"Enterprise",1));//gustozoo  renewed on 19-12-2016 for 1 month
            hmClientDtl.put("090.001", clsClientDetails.createClientDetails("090.001", "Mathura a fusion of pure VEG", dFormat.parse("2015-12-22"), dFormat.parse("2017-12-22"),"Enterprise",1)); //renewed on 22-12-2016 by 1 year
            hmClientDtl.put("091.001", clsClientDetails.createClientDetails("091.001", "Gourmet Gelato Company Pvt. Ltd.", dFormat.parse("2015-12-23"), dFormat.parse("2016-01-23"),"Enterprise",1));    // Payment not received.
            hmClientDtl.put("092.001", clsClientDetails.createClientDetails("092.001", "Shree Sound Pvt Ltd", dFormat.parse("2016-01-08"), dFormat.parse("2017-12-20"),"Enterprise",1));  //HO  Waters  renewed on 20-12-2016 to 1 year
            hmClientDtl.put("092.002", clsClientDetails.createClientDetails("092.002", "Shree Sound Pvt Ltd", dFormat.parse("2016-01-08"), dFormat.parse("2017-12-20"),"Enterprise",1));  //Outlet 1
            hmClientDtl.put("092.003", clsClientDetails.createClientDetails("092.003", "Shree Sound Pvt Ltd", dFormat.parse("2016-01-08"), dFormat.parse("2017-12-20"),"Enterprise",1));  //Outlet 2
            hmClientDtl.put("093.001", clsClientDetails.createClientDetails("093.001", "MAGDALENA", dFormat.parse("2017-09-16"), dFormat.parse("2018-09-16"),"Enterprise",1));
            hmClientDtl.put("094.001", clsClientDetails.createClientDetails("094.001", "MUCH MORE CAKES", dFormat.parse("2016-01-12"), dFormat.parse("2017-01-12"),"Enterprise",1));
            hmClientDtl.put("095.001", clsClientDetails.createClientDetails("095.001", "PIZZA N U", dFormat.parse("2016-01-12"), dFormat.parse("2017-01-18"),"Enterprise",1));
            hmClientDtl.put("096.001", clsClientDetails.createClientDetails("096.001", "Red Consulting Pvt Ltd", dFormat.parse("2016-01-16"), dFormat.parse("2016-04-16"),"Enterprise",1));
            hmClientDtl.put("096.002", clsClientDetails.createClientDetails("096.002", "Red Consulting Pvt Ltd", dFormat.parse("2016-01-16"), dFormat.parse("2016-04-16"),"Enterprise",1));
            hmClientDtl.put("096.003", clsClientDetails.createClientDetails("096.003", "Red Consulting Pvt Ltd", dFormat.parse("2016-01-16"), dFormat.parse("2016-04-16"),"Enterprise",1));
            hmClientDtl.put("097.001", clsClientDetails.createClientDetails("097.001", "GADGIL HOTELS PVT LTD", dFormat.parse("2016-01-23"), dFormat.parse("2016-08-25"),"Enterprise",1));
            hmClientDtl.put("098.001", clsClientDetails.createClientDetails("098.001", "SAUTEED STORIES", dFormat.parse("2016-02-01"), dFormat.parse("2017-02-01"),"Enterprise",1));
            hmClientDtl.put("099.001", clsClientDetails.createClientDetails("099.001", "HOTEL CITI PRIDE", dFormat.parse("2016-02-01"), dFormat.parse("2017-02-01"),"Enterprise",1));
            hmClientDtl.put("100.001", clsClientDetails.createClientDetails("100.001", "KRISH FOODS HOSPITALITY", dFormat.parse("2016-02-06"), dFormat.parse("2018-02-07"),"Enterprise",1));//Marakesh renew on 07/02/2017 for 1 year
            hmClientDtl.put("101.001", clsClientDetails.createClientDetails("101.001", "HUCKLEBERRYS", dFormat.parse("2016-02-23"), dFormat.parse("2017-02-23"),"Enterprise",1));//outlet 1
            hmClientDtl.put("101.002", clsClientDetails.createClientDetails("101.002", "HUCKLEBERRYS", dFormat.parse("2016-02-23"), dFormat.parse("2017-02-23"),"Enterprise",1));//outlet 2
            hmClientDtl.put("102.001", clsClientDetails.createClientDetails("102.001", "RANADE BROTHERS", dFormat.parse("2016-02-24"), dFormat.parse("2017-02-24"),"Enterprise",1));
            hmClientDtl.put("103.001", clsClientDetails.createClientDetails("103.001", "BOMBAY HIGH", dFormat.parse("2016-03-05"), dFormat.parse("2017-03-31"),"Enterprise",1));
            hmClientDtl.put("104.001", clsClientDetails.createClientDetails("104.001", "Bakers Treat", dFormat.parse("2016-03-07"), dFormat.parse("2017-04-07"),"Enterprise",1));// HO Unit
            hmClientDtl.put("104.002", clsClientDetails.createClientDetails("104.002", "Bakers Treat", dFormat.parse("2016-03-07"), dFormat.parse("2017-04-07"),"Enterprise",1));// Outlet 1
            hmClientDtl.put("104.003", clsClientDetails.createClientDetails("104.003", "Bakers Treat", dFormat.parse("2016-03-07"), dFormat.parse("2017-04-07"),"Enterprise",1));// Outlet 2
            hmClientDtl.put("104.004", clsClientDetails.createClientDetails("104.004", "Bakers Treat", dFormat.parse("2016-03-07"), dFormat.parse("2017-04-07"),"Enterprise",1));// Outlet 3
            hmClientDtl.put("104.005", clsClientDetails.createClientDetails("104.005", "Bakers Treat", dFormat.parse("2016-03-07"), dFormat.parse("2017-04-07"),"Enterprise",1));// Outlet 4
            hmClientDtl.put("105.001", clsClientDetails.createClientDetails("105.001", "KASHI BHOJNALAY AT KALHER BHIWANDI", dFormat.parse("2016-03-11"), dFormat.parse("2017-04-17"),"Enterprise",1));
            hmClientDtl.put("106.001", clsClientDetails.createClientDetails("106.001", "Independence Brewing Co. Pvt Ltd", dFormat.parse("2016-03-21"), dFormat.parse("2017-03-21"),"Enterprise",1));
            hmClientDtl.put("107.001", clsClientDetails.createClientDetails("107.001", "APT FOODS AND HOSPITALITY", dFormat.parse("2016-03-23"), dFormat.parse("2017-03-23"),"Enterprise",1));//white rose cafe
            hmClientDtl.put("108.001", clsClientDetails.createClientDetails("108.001", "221 B Baker Street", dFormat.parse("2016-03-07"), dFormat.parse("2017-03-07"),"Enterprise",1));
            hmClientDtl.put("109.001", clsClientDetails.createClientDetails("109.001", "Chemistry 101", dFormat.parse("2016-04-11"), dFormat.parse("2017-04-11"),"Enterprise",1));
            hmClientDtl.put("110.001", clsClientDetails.createClientDetails("110.001", "CAKE SHOP", dFormat.parse("2016-01-23"), dFormat.parse("2016-05-30"),"Enterprise",1));
            // MERWANS CONFECTIONERS PVT LTD
            hmClientDtl.put("111.001", clsClientDetails.createClientDetails("111.001", "MERWANS CONFECTIONERS PVT LTD", dFormat.parse("2016-05-07"), dFormat.parse("2017-12-30"),"Enterprise",1));//HO // renewed on 04-01-2017 for demo
            hmClientDtl.put("111.002", clsClientDetails.createClientDetails("111.002", "MERWANS CONFECTIONERS PVT LTD", dFormat.parse("2016-05-07"), dFormat.parse("2017-12-30"),"Enterprise",1));//Outlet 1
            hmClientDtl.put("111.003", clsClientDetails.createClientDetails("111.003", "MERWANS CONFECTIONERS PVT LTD", dFormat.parse("2016-05-07"), dFormat.parse("2017-12-30"),"Enterprise",1));//Outlet 2
            hmClientDtl.put("111.004", clsClientDetails.createClientDetails("111.004", "MERWANS CONFECTIONERS PVT LTD", dFormat.parse("2016-05-07"), dFormat.parse("2017-12-30"),"Enterprise",1));//Outlet 3
            hmClientDtl.put("111.005", clsClientDetails.createClientDetails("111.005", "MERWANS CONFECTIONERS PVT LTD", dFormat.parse("2016-05-07"), dFormat.parse("2017-12-30"),"Enterprise",1));//Outlet 4
            hmClientDtl.put("111.006", clsClientDetails.createClientDetails("111.006", "MERWANS CONFECTIONERS PVT LTD", dFormat.parse("2016-05-07"), dFormat.parse("2017-12-30"),"Enterprise",1));//Outlet 5
            hmClientDtl.put("111.007", clsClientDetails.createClientDetails("111.007", "MERWANS CONFECTIONERS PVT LTD", dFormat.parse("2016-05-07"), dFormat.parse("2017-12-30"),"Enterprise",1));//Outlet 6
            hmClientDtl.put("111.008", clsClientDetails.createClientDetails("111.008", "MERWANS CONFECTIONERS PVT LTD", dFormat.parse("2016-05-07"), dFormat.parse("2017-12-30"),"Enterprise",1));//Outlet 7
            hmClientDtl.put("111.009", clsClientDetails.createClientDetails("111.009", "MERWANS CONFECTIONERS PVT LTD", dFormat.parse("2016-05-07"), dFormat.parse("2017-12-30"),"Enterprise",1));//Outlet 8
            hmClientDtl.put("111.010", clsClientDetails.createClientDetails("111.010", "MERWANS CONFECTIONERS PVT LTD", dFormat.parse("2016-05-07"), dFormat.parse("2017-12-30"),"Enterprise",1));//Outlet 9
            hmClientDtl.put("111.011", clsClientDetails.createClientDetails("111.011", "MERWANS CONFECTIONERS PVT LTD", dFormat.parse("2016-05-07"), dFormat.parse("2017-12-30"),"Enterprise",1));//Outlet 10
            // GBC MEGA MOTELS(Carnival Group)
            hmClientDtl.put("112.001", clsClientDetails.createClientDetails("112.001", "GBC MEGA MOTELS", dFormat.parse("2016-05-11"), dFormat.parse("2017-05-11"),"Enterprise",1));//HO
            hmClientDtl.put("112.002", clsClientDetails.createClientDetails("112.002", "GBC MEGA MOTELS", dFormat.parse("2016-05-11"), dFormat.parse("2017-05-11"),"Enterprise",1));//outlet 1
            hmClientDtl.put("112.003", clsClientDetails.createClientDetails("112.003", "GBC MEGA MOTELS", dFormat.parse("2016-05-11"), dFormat.parse("2017-05-11"),"Enterprise",1));//outlet 2
            //WOODFIRE HOSPITALITY BY OLENT
            hmClientDtl.put("113.001", clsClientDetails.createClientDetails("113.001", "WOODFIRE HOSPITALITY BY OLENT", dFormat.parse("2016-05-14"), dFormat.parse("2017-05-14"),"Enterprise",1));
            //Dr. Asif Khan Wellness Clinic LLP
            hmClientDtl.put("114.001", clsClientDetails.createClientDetails("114.001", "Dr. Asif Khan Wellness Clinic LLP", dFormat.parse("2016-05-14"), dFormat.parse("2017-05-14"),"Enterprise",1));//HO
            hmClientDtl.put("114.002", clsClientDetails.createClientDetails("114.002", "Dr. Asif Khan Wellness Clinic LLP", dFormat.parse("2016-05-14"), dFormat.parse("2017-05-14"),"Enterprise",1));//outlet 1
            //AHAAN THAI FOOD RESTAURANT
            hmClientDtl.put("115.001", clsClientDetails.createClientDetails("115.001", "AHAAN THAI FOOD RESTAURANT", dFormat.parse("2016-05-19"), dFormat.parse("2017-05-19"),"Enterprise",1));
            //THE WOODSHED INN
            hmClientDtl.put("116.001", clsClientDetails.createClientDetails("116.001", "THE WOODSHED INN", dFormat.parse("2016-05-31"), dFormat.parse("2017-05-31"),"Enterprise",1));
            hmClientDtl.put("117.001", clsClientDetails.createClientDetails("117.001", "THE PREM'S HOTEL", dFormat.parse("2016-06-30"), dFormat.parse("2018-07-31"),"Enterprise",50));

            hmClientDtl.put("118.001", clsClientDetails.createClientDetails("118.001", "PICCADILLY RESTAURANT", dFormat.parse("2016-07-04"), dFormat.parse("2017-08-04"),"Enterprise",1));
            hmClientDtl.put("119.001", clsClientDetails.createClientDetails("119.001", "MALPANI", dFormat.parse("2016-08-10"), dFormat.parse("2016-09-10"),"Enterprise",1));

            hmClientDtl.put("120.001", clsClientDetails.createClientDetails("120.001", "Occasions India", dFormat.parse("2016-08-31"), dFormat.parse("2017-08-31"),"Enterprise",1));

            hmClientDtl.put("121.001", clsClientDetails.createClientDetails("121.001", "MEHTA HOSPITALITY", dFormat.parse("2016-09-21"), dFormat.parse("2016-10-11"),"Enterprise",1));
            hmClientDtl.put("122.001", clsClientDetails.createClientDetails("122.001", "FITNESS FUEL", dFormat.parse("2016-09-29"), dFormat.parse("2017-09-29"),"Enterprise",1));

            hmClientDtl.put("123.001", clsClientDetails.createClientDetails("123.001", "SARKAR COLLECTION", dFormat.parse("2016-10-13"), dFormat.parse("2016-11-12"),"Enterprise",1));
            hmClientDtl.put("124.001", clsClientDetails.createClientDetails("124.001", "ATITHYA DINING LLP", dFormat.parse("2016-10-18"), dFormat.parse("2017-11-04"),"Enterprise",1));// Teddy Boy
            hmClientDtl.put("125.001", clsClientDetails.createClientDetails("125.001", "db", dFormat.parse("2016-10-22"), dFormat.parse("2017-11-24"),"Enterprise",1));// License is Going to change later. renewed on 24-11-2016
            hmClientDtl.put("126.001", createClientDetails("126.001", "BAKERY", dFormat.parse("2016-10-22"), dFormat.parse("2016-11-22"),"Enterprise",1));
            hmClientDtl.put("127.001", createClientDetails("127.001", "Cumin Food & Beverage Pvt Ltd", dFormat.parse("2016-10-26"), dFormat.parse("2017-10-26"),"Enterprise",1));//released on 26-10-2016
            hmClientDtl.put("128.001", createClientDetails("128.001", "Yellow Cup", dFormat.parse("2016-10-26"), dFormat.parse("2017-10-26"),"Enterprise",1));//released on 26-10-2016

            hmClientDtl.put("129.001", createClientDetails("129.001", "GRILL 108 STREET", dFormat.parse("2016-11-01"), dFormat.parse("2017-10-31"),"Enterprise",1));//released on 01-11-2016
            hmClientDtl.put("130.001", createClientDetails("130.001", "WORLD STREET", dFormat.parse("2016-11-15"), dFormat.parse("2017-11-15"),"Enterprise",1));//released on 15-11-2016
            hmClientDtl.put("131.001", createClientDetails("131.001", "KETTLE AND KEG", dFormat.parse("2016-11-23"), dFormat.parse("2017-11-23"),"Enterprise",1));//released on 23-11-2016 renamed on 23-12-2016(KETTLE AND KEG CAFE)

            hmClientDtl.put("132.001", createClientDetails("132.001", "JBDD Hospitality LLP", dFormat.parse("2016-12-05"), dFormat.parse("2018-03-31"),"Enterprise",5));//(Delhi Darbar)extended on 2018-03-31 for HO up to 15th June
            hmClientDtl.put("132.002", createClientDetails("132.002", "JBDD Hospitality LLP", dFormat.parse("2016-12-05"), dFormat.parse("2018-03-31"),"Enterprise",5));//(Delhi Darbar)extended on 2018-03-31 for Outlet 1 up to 15th Sept
            hmClientDtl.put("132.003", createClientDetails("132.003", "JBDD Hospitality LLP", dFormat.parse("2016-12-05"), dFormat.parse("2018-03-31"),"Enterprise",5));//(Delhi Darbar)extended on 2018-03-31 for Outlet 2 up to 15th Sept
            hmClientDtl.put("132.004", createClientDetails("132.004", "JBDD Hospitality LLP", dFormat.parse("2016-12-05"), dFormat.parse("2018-03-31"),"Enterprise",5));//(Delhi Darbar)extended on 2018-03-31 for Outlet 3 up to 15th Sept
            hmClientDtl.put("132.005", createClientDetails("132.005", "JBDD Hospitality LLP", dFormat.parse("2016-12-05"), dFormat.parse("2018-03-31"),"Enterprise",5));//(Delhi Darbar)extended on 2018-03-31 for Outlet 4 up to 15th Sept


            hmClientDtl.put("133.001", createClientDetails("133.001","CAFE EDESIA", dFormat.parse("2016-12-10"), dFormat.parse("2017-12-10"),"Enterprise",1));//released on 10-12-2016 for 1 year
            hmClientDtl.put("134.001", createClientDetails("134.001","Aman Hospitality", dFormat.parse("2016-12-28"), dFormat.parse("2017-12-28"),"Enterprise",1));//released on 28-12-2016 for 1 year
            hmClientDtl.put("135.001", createClientDetails("135.001","QBAA TERRACE", dFormat.parse("2016-12-29"), dFormat.parse("2017-01-31"),"Enterprise",1));//released on 29-12-2016 for 1 month

            hmClientDtl.put("136.001", createClientDetails("136.001","KINKI", dFormat.parse("2017-01-05"), dFormat.parse("2018-01-05"),"Enterprise",1));//released on 05-01-2017 for 1 year
            hmClientDtl.put("141.001", createClientDetails("141.001","SANGUINE SOFTWARE SOLUTIONS PVT LTD", dFormat.parse("2017-01-05"), dFormat.parse("2025-01-05"),"Enterprise",1000));//released on 05-01-2017 for 1 year
            hmClientDtl.put("148.001", createClientDetails("148.001","MURPHIES", dFormat.parse("2017-02-16"), dFormat.parse("2018-02-16"),"Enterprise",1));//release on 16-02-2017 for 1 year
            hmClientDtl.put("151.001", createClientDetails("151.001","Bottle Street Restaurant & Lounge", dFormat.parse("2017-02-23"), dFormat.parse("2018-02-23"),"Enterprise",7));//  release on 23-02-2017 for 1 year for 7 tabs given on 16sept 2017
            hmClientDtl.put("154.001", createClientDetails("154.001","KP RESTAURANTS", dFormat.parse("2017-03-11"), dFormat.parse("2018-05-08"),"Enterprise",2));//released on 08-05-2017 for 1 year
            hmClientDtl.put("155.001", createClientDetails("155.001","CAVALLI THE LOUNGE", dFormat.parse("2017-03-24"), dFormat.parse("2018-03-24"),"Enterprise",7));//released on 24-03-2017 for 1 year  Enterprise for 7 machines
            hmClientDtl.put("157.001", createClientDetails("157.001","Shah & Sanghvi Hospitality LLP", dFormat.parse("2017-03-28"), dFormat.parse("2018-03-28"),"Enterprise",3));//released on 28-03-2017 for 1 year  Enterprise for 3 machines
            hmClientDtl.put("159.001", createClientDetails("159.001","BIG PLATE CUISINES LLP", dFormat.parse("2017-04-14"), dFormat.parse("2018-04-14"),"Lite",1));//released on 14-04-2017 for 1 year reporting 1 tab
            hmClientDtl.put("161.001", createClientDetails("161.001","HOTEL GRAND CENTRAL", dFormat.parse("2017-04-18"), dFormat.parse("2017-10-18"),"Enterprise",6));//released on 18-04-2017 for 6 month reporting 1 tab
            hmClientDtl.put("163.001", createClientDetails("163.001","KADAR KHAN'S SHEESHA", dFormat.parse("2017-04-22"), dFormat.parse("2018-04-22"),"Enterprise",8)); // released on 22-04-2017 for 1 year  Enterprise for 8 tabs
            hmClientDtl.put("164.001", createClientDetails("164.001","GLOBAL FOODS & BEVERAGES PVT. LTD.", dFormat.parse("2017-04-26"), dFormat.parse("2018-04-26"),"Enterprise",6)); // released on 26-04-2017 for 1 year  Enterprise for 6 tabs
            hmClientDtl.put("166.001", createClientDetails("166.001","SUNNYS WORLD", dFormat.parse("2017-04-29"), dFormat.parse("2017-06-29"),"Enterprise",6)); // released on 29-04-2017 for 2 month  Enterprise for 6 tabs
            hmClientDtl.put("167.001", createClientDetails("167.001","LAJMI RESTAURANT", dFormat.parse("2017-05-02"), dFormat.parse("2018-05-01"),"Enterprise",3)); // released on 02-05-2017 for 1 Year  Enterprise for 3 tabs
            hmClientDtl.put("168.001", createClientDetails("168.001","LAJMI RESTAURANT N LOUNGE", dFormat.parse("2017-05-03"), dFormat.parse("2018-05-02"),"Enterprise",3)); // released on 03-05-2017 for 1 Year  Enterprise for 3 tab
            hmClientDtl.put("170.001", createClientDetails("170.001","A1 HEIGHTS N HOSPITALITY PVT LTD", dFormat.parse("2017-05-04"), dFormat.parse("2018-05-02"),"Enterprise",6)); // released on 04-05-2017 for 1 Year  Enterprise for 6 tabs
            hmClientDtl.put("171.001", createClientDetails("171.001","CHINA GRILL-PIMPRI", dFormat.parse("2017-05-24"), dFormat.parse("2018-05-24"),"Enterprise",2)); // released on 24-05-2017 for 1 Year  Enterprise for 2 terminals on tabs
            hmClientDtl.put("172.001", createClientDetails("172.001","DIOS HOTEL LLP", dFormat.parse("2017-05-26"), dFormat.parse("2017-06-26"),"Enterprise",2)); // released on 26-05-2017 for 1 month  Enterprise for 2 tabs
            hmClientDtl.put("176.001", createClientDetails("176.001", "FRANCOPHONE EUROPEAN BISTRO & PATISSERIE", dFormat.parse("2017-06-07"), dFormat.parse("2018-06-06"), "Enterprise",4)); // released on 07-06-2017 for 1 year  Enterprise for 4 tabs
            hmClientDtl.put("175.001", createClientDetails("175.001", "Tjs brew works", dFormat.parse("2017-06-06"), dFormat.parse("2018-06-06"), "Enterprise", 7)); // released on 06-06-2017 for 1 year  7
            hmClientDtl.put("177.001", createClientDetails("177.001", "RAYYAN", dFormat.parse("2017-06-08"), dFormat.parse("2017-07-07"), "Enterprise",2)); // released on 08-06-2017 for 1 month  Enterprise for 2 tabs
            hmClientDtl.put("178.001", createClientDetails("178.001", "UNWIND", dFormat.parse("2017-07-06"), dFormat.parse("2018-06-07"), "Enterprise",13)); // released on 08-06-2017 for 1 month  Enterprise for 13 tabs
            hmClientDtl.put("180.001", createClientDetails("180.001", "AVIKA GROUP LTD", dFormat.parse("2017-06-15"), dFormat.parse("2018-07-14"), "Enterprise",1)); // released on 14-06-2017 for 1 month  Enterprise for tab
            hmClientDtl.put("181.001", createClientDetails("181.001", "RMV COMMUNICATION PVT LTD", dFormat.parse("2017-06-20"), dFormat.parse("2018-07-20"),"Lite",2)); // released on 20-06-2017 for 1 month  Lite for 2 tabs
            hmClientDtl.put("182.001", createClientDetails("182.001", "OOZO", dFormat.parse("2017-06-21"), dFormat.parse("2018-08-21"), "Enterprise",12)); // released on 21-06-2017 for 1 year  Enterprise for 12 tabs   check pos license and set that date to it.
            hmClientDtl.put("183.001", createClientDetails("183.001", "VD HOSPITALITY LLP", dFormat.parse("2017-06-30"), dFormat.parse("2018-06-30"), "Enterprise",5)); // released on 30-06-2017 for 1 month  Enterprise for 5 tabs
            hmClientDtl.put("186.001", createClientDetails("186.001", "JORDAN HOSPITALITY", dFormat.parse("2017-07-19"), dFormat.parse("2017-08-19"), "Enterprise",2)); // released on 19-07-2017 for 1 month  Enterprise for 2 tabs
            hmClientDtl.put("188.001", createClientDetails("188.001", "MUMBAI CAFE", dFormat.parse("2017-07-20"), dFormat.parse("2018-07-20"), "Enterprise",2)); // released on 21-07-2017 for 1 year  Enterprise for 2 tabs
            hmClientDtl.put("190.001", createClientDetails("190.001", "SQUARE ONE HOSPITALITY LLP", dFormat.parse("2017-08-03"), dFormat.parse("2017-09-03"), "Enterprise",1)); // released on 03-08-2017 for 1 month,  Enterprise for 1 tab
            hmClientDtl.put("191.001", createClientDetails("191.001", "TOYAM INDUSTRIES LTD", dFormat.parse("2017-08-05"), dFormat.parse("2017-09-05"), "Enterprise",3)); // released on 05-08-2017 for 1 month,  Enterprise for 4 tabs
            hmClientDtl.put("194.001", createClientDetails("194.001", "SWIG", dFormat.parse("2017-09-06"), dFormat.parse("2018-09-06"), "Enterprise",5)); // released on 06-09-2017 for 1 month,  Enterprise for 5 tabs
            hmClientDtl.put("195.001", createClientDetails("195.001", "COUNTRY OF ORIGIN", dFormat.parse("2017-09-07"), dFormat.parse("2018-09-06"), "Enterprise",1)); //(Mumbai) released on 07-09-2017 for 1 year,  Enterprise for 1 tab
            hmClientDtl.put("197.001", createClientDetails("197.001", "REZBERRY RHINOCERES", dFormat.parse("2017-09-20"), dFormat.parse("2017-10-20"), "Enterprise",5)); //(MUMBAI) released on 20-09-2017 for 1 month,  Enterprise for 1 server ,3 clients terminals on desktop and 5 APOS,Webstocks,NO SMS Pack
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPosVersion()
    {
        return this.posVersion;
    }

    public void setPosVersion(String posVersion)
    {
        this.posVersion = posVersion;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getClient_Name()
    {
        return Client_Name;
    }

    public void setClient_Name(String Client_Name)
    {
        this.Client_Name = Client_Name;
    }

    public Date getInstallDate()
    {
        return installDate;
    }

    public void setInstallDate(Date installDate)
    {
        this.installDate = installDate;
    }

    public Date getExpiryDate()
    {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    public int getIntMAXTerminal()
    {
        return intMAXTerminal;
    }

    public void setIntMAXTerminal(int intMAXTerminal)
    {
        this.intMAXTerminal = intMAXTerminal;
    }

}
