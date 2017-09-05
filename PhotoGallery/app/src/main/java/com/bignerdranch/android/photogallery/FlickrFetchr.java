package com.bignerdranch.android.photogallery;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/31.
 * 网络连接类
 */

public class FlickrFetchr {

    private static final String TAG = "FlickrFetchr";

    private static final String API_KEY = "1e11934c7cb99e10d66cc10822e736a7";

    /**
     * 重指定URL获取原始数据并返回一个字节流数组
     * @param urlSpec
     * @return
     * @throws IOException
     */
    public byte[] getUrlBytes(String urlSpec) throws IOException{
        // 创建url对象
        URL url = new URL(urlSpec);
        // 调用 openConnection() 方法创建一个指向要访问URL的连接对象
        // URL.openConnection() 方法默认返回的是URLConnection对象,但要连接的是http URL,
        // 因此需将其强制类型转换为 HttpURLConnection 对象
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try{

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 如果是 POST 请求,调用 getOutputStream() 方法
            InputStream in = connection.getInputStream();

            // 判断是否连接成功
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage()+": with "+urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0){
                out.write(buffer,0,bytesRead);
            }
            out.close();
            return out.toByteArray();

        }finally {

            connection.disconnect();

        }
    }

    /**
     * 将 getUrlBytes(String)方法返回的结果转换为String
     * @param urlSpec
     * @return
     * @throws IOException
     */
    public String getUrlString(String urlSpec) throws IOException{
        return new String(getUrlBytes(urlSpec));
    }

    /**
     * 使用Uri.Builder构建了完整的Flickr API请求URL
     */
    public List<GalleryItem> fetchItems(){
        List<GalleryItem> items = new ArrayList<>();
//        try{
//            String url = Uri.parse("https://api.flickr.com/services/rest/")
//                    .buildUpon()
//                    .appendQueryParameter("method","flickr.photos.getRecent")
//                    .appendQueryParameter("api_key",API_KEY)
//                    .appendQueryParameter("format","json")
//                    .appendQueryParameter("jsoncallback","1")
//                    .appendQueryParameter("extras","url_s")
//                    .build().toString();
//            String jsonString = getUrlString(url);
//            Log.i(TAG,"Received JSON: "+jsonString);
//
//        } catch (IOException ioe){
//            Log.i(TAG,"Failed to fetch items",ioe);
//        }
        String result = "{\"photos\":{\"page\":1,\"pages\":10,\"perpage\":100,\"total\":1000,\"photo\":[{\"id\":\"36175320264\",\"owner\":\"149058190@N04\",\"secret\":\"e697f96901\",\"server\":\"4351\",\"farm\":5,\"title\":\"sUV2G3\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36175320864\",\"owner\":\"157846255@N07\",\"secret\":\"9cd0c12f21\",\"server\":\"4360\",\"farm\":5,\"title\":\"2017-09-03_08-36-31\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36175324844\",\"owner\":\"148372083@N04\",\"secret\":\"4b63354eb0\",\"server\":\"4403\",\"farm\":5,\"title\":\"pondering\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36175326774\",\"owner\":\"150519650@N07\",\"secret\":\"fc8f8d973f\",\"server\":\"4398\",\"farm\":5,\"title\":\"Welcome to today\\u2019s innovative urban mobility concept.\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36175327554\",\"owner\":\"144585328@N08\",\"secret\":\"1fe2c85ecd\",\"server\":\"4343\",\"farm\":5,\"title\":\"IMG_3005\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36175330024\",\"owner\":\"44434117@N05\",\"secret\":\"e4a56af40a\",\"server\":\"4374\",\"farm\":5,\"title\":\"P3270911\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36175331164\",\"owner\":\"150117772@N02\",\"secret\":\"f88d6730a5\",\"server\":\"4404\",\"farm\":5,\"title\":\"54_dendra_dasos_skaron\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36175332444\",\"owner\":\"67307473@N00\",\"secret\":\"9182b3ebd8\",\"server\":\"4389\",\"farm\":5,\"title\":\"_DSC5746.jpg\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36175332484\",\"owner\":\"66712620@N00\",\"secret\":\"94dc3bdf73\",\"server\":\"4348\",\"farm\":5,\"title\":\"Photo added to \\\"All Photos\\\"\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36175333174\",\"owner\":\"62602226@N08\",\"secret\":\"3d770b2763\",\"server\":\"4364\",\"farm\":5,\"title\":\"EC Baptism\\/Growth Track 9-3-17\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36175333794\",\"owner\":\"73871769@N07\",\"secret\":\"9c0ba37a08\",\"server\":\"4373\",\"farm\":5,\"title\":\"Barcelona April 2017\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202680853\",\"owner\":\"125420270@N05\",\"secret\":\"a5f95ca7e2\",\"server\":\"4389\",\"farm\":5,\"title\":\"Great White Egret Flying Over the Water\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202682673\",\"owner\":\"49238979@N02\",\"secret\":\"388d6c62b9\",\"server\":\"4433\",\"farm\":5,\"title\":\"Our cannabis flowers are blooming!\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202684463\",\"owner\":\"51763924@N06\",\"secret\":\"63512d9e43\",\"server\":\"4349\",\"farm\":5,\"title\":\"Bodega Azul - Mendoza\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202685683\",\"owner\":\"121723564@N07\",\"secret\":\"2bed520bc9\",\"server\":\"4405\",\"farm\":5,\"title\":\"2017-09-04_07-36-38\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202686123\",\"owner\":\"154160672@N02\",\"secret\":\"1d94231302\",\"server\":\"4412\",\"farm\":5,\"title\":\"Avisering fr\\u00e5n Google \\u2013 true love\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202688553\",\"owner\":\"151390901@N04\",\"secret\":\"c2ec18b623\",\"server\":\"4437\",\"farm\":5,\"title\":\"MICHAEL WINSLOW LIVE - MOVIE - 2011\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202689053\",\"owner\":\"125264515@N02\",\"secret\":\"1dea9039f6\",\"server\":\"4434\",\"farm\":5,\"title\":\"Candy & blue hour. #danang #vietnam #streetfood #streetlife #travel #travelandleisure #travelmore #travelphotography #travelphotographer #monocle #monoclemagazine #monoclephotographer #natgeo #natgeotravel #cottoncandy #bluehour #southeastasia http:\\/\\/ift.\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202689673\",\"owner\":\"130510542@N06\",\"secret\":\"86b4b45b0a\",\"server\":\"4363\",\"farm\":5,\"title\":\"Oxford Diecast 1:76 Scania R420 Topline Curtainside Eddie Stobart\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202690283\",\"owner\":\"151800015@N03\",\"secret\":\"16e687a8a6\",\"server\":\"4424\",\"farm\":5,\"title\":\"FB_IMG_1504481816838\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202692543\",\"owner\":\"10948047@N03\",\"secret\":\"6f28913eab\",\"server\":\"4368\",\"farm\":5,\"title\":\"2017-08-31 10.41.40\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202693903\",\"owner\":\"151534369@N02\",\"secret\":\"c500da01cb\",\"server\":\"4368\",\"farm\":5,\"title\":\"Pan\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202696453\",\"owner\":\"90623534@N00\",\"secret\":\"43453d8574\",\"server\":\"4411\",\"farm\":5,\"title\":\"D3 of #writerswhobookstagramsept: Characters who remind me of my characters. This is Frank Black of MILLENNIUM, a teevee show that aired for three seasons in the 1990s. It was tangentially related to THE X-FILES, but not a spinoff. Frank Black was a man w\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202697453\",\"owner\":\"27003603@N00\",\"secret\":\"a19bed7017\",\"server\":\"4438\",\"farm\":5,\"title\":\"Jimmy Yacabonis\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202697503\",\"owner\":\"37405826@N00\",\"secret\":\"a67bd1607b\",\"server\":\"4365\",\"farm\":5,\"title\":\"DSC_9718\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202697803\",\"owner\":\"47294906@N05\",\"secret\":\"f0af126bf0\",\"server\":\"4397\",\"farm\":5,\"title\":\"upload\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202699003\",\"owner\":\"22714285@N05\",\"secret\":\"0b85d11726\",\"server\":\"4340\",\"farm\":5,\"title\":\"DSC01665\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202699143\",\"owner\":\"96055761@N05\",\"secret\":\"f7df83ee8a\",\"server\":\"4409\",\"farm\":5,\"title\":\"Flower Petal Beauty In Nature Springtime Santiagoapie Quilicura\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202699673\",\"owner\":\"7862959@N02\",\"secret\":\"3e920f7de5\",\"server\":\"4384\",\"farm\":5,\"title\":\"\\u7389\\u6cc9\\u9662\\u4e38\\u9f20\\u591a\\u9580\\u767a\\u6398\\u8abf\\u67fb\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202699783\",\"owner\":\"124171259@N06\",\"secret\":\"1853a0aee1\",\"server\":\"4362\",\"farm\":5,\"title\":\"Ba\\u00f1era planetaria.\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202700363\",\"owner\":\"77190416@N02\",\"secret\":\"dd47171976\",\"server\":\"4360\",\"farm\":5,\"title\":\"IMG_1538\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202700493\",\"owner\":\"154354326@N08\",\"secret\":\"95c0c0f609\",\"server\":\"4345\",\"farm\":5,\"title\":\"WS01\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202700773\",\"owner\":\"96196317@N08\",\"secret\":\"4867b2540b\",\"server\":\"4412\",\"farm\":5,\"title\":\"JFRM-2017-07-9372.jpg\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202700793\",\"owner\":\"20003801@N00\",\"secret\":\"23fd1aec90\",\"server\":\"4345\",\"farm\":5,\"title\":\"Marks Wedding Photos\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202700993\",\"owner\":\"151619857@N06\",\"secret\":\"f18bb625c7\",\"server\":\"4419\",\"farm\":5,\"title\":\"_DSC6061.jpg\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202701483\",\"owner\":\"141533342@N04\",\"secret\":\"db56b52e01\",\"server\":\"4358\",\"farm\":5,\"title\":\"DSC08965\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36202701853\",\"owner\":\"154523403@N05\",\"secret\":\"fa133c4725\",\"server\":\"4413\",\"farm\":5,\"title\":\"IMG_20170901_100805616\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36613977080\",\"owner\":\"41440718@N04\",\"secret\":\"9b440ca327\",\"server\":\"4417\",\"farm\":5,\"title\":\"Zachary Bar\\/Mitzvah\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36613977140\",\"owner\":\"150927802@N06\",\"secret\":\"73e9fff519\",\"server\":\"4411\",\"farm\":5,\"title\":\"nxstmp\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36613979570\",\"owner\":\"29148673@N07\",\"secret\":\"d3ee78e7d1\",\"server\":\"4437\",\"farm\":5,\"title\":\"Kept company by fire on drive back to Warmun #eastkimberley\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36613981550\",\"owner\":\"155475088@N08\",\"secret\":\"05c45194f0\",\"server\":\"4387\",\"farm\":5,\"title\":\"P1040380.JPG\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36613982500\",\"owner\":\"151771605@N03\",\"secret\":\"e96a378c1d\",\"server\":\"4364\",\"farm\":5,\"title\":\"Fitness Outfits & Women's Gym Wear : Gym to Street || The Honeybee Blog...\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36613984830\",\"owner\":\"142426414@N06\",\"secret\":\"7f0a9e6572\",\"server\":\"4399\",\"farm\":5,\"title\":\"Tendance Robe du mari\\u00e9e 2017\\/2018 \\u2013 Would You Ever Rent Your Wedding Dress? | Brides.com\\u2026\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36613985620\",\"owner\":\"70223391@N07\",\"secret\":\"41ee4bfc13\",\"server\":\"4375\",\"farm\":5,\"title\":\"Flying into Salt Lake City #utah\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36613986880\",\"owner\":\"49118647@N08\",\"secret\":\"fc3f266ae7\",\"server\":\"4410\",\"farm\":5,\"title\":\"CamGrab [LW2.20170904001250.jpg]\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36613989650\",\"owner\":\"154132296@N08\",\"secret\":\"c26bb892d9\",\"server\":\"4331\",\"farm\":5,\"title\":\"Case 580K Phase 3 Service Manual\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36613990880\",\"owner\":\"158181386@N07\",\"secret\":\"68d215ca55\",\"server\":\"4371\",\"farm\":5,\"title\":\"#sunset #photographer\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36613991660\",\"owner\":\"24931013@N05\",\"secret\":\"2ace27af20\",\"server\":\"4395\",\"farm\":5,\"title\":\"Happy birthday @cheekd\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36613992890\",\"owner\":\"46216979@N05\",\"secret\":\"5d6e8271ed\",\"server\":\"4358\",\"farm\":5,\"title\":\"\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36613993020\",\"owner\":\"20146766@N00\",\"secret\":\"cc62cdabf7\",\"server\":\"4345\",\"farm\":5,\"title\":\"IMG_8941\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36613993780\",\"owner\":\"99273284@N00\",\"secret\":\"2ec8e66dcd\",\"server\":\"4341\",\"farm\":5,\"title\":\"Wiiiiinnnnggggzzzzz\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36822717926\",\"owner\":\"134087031@N04\",\"secret\":\"558484fafe\",\"server\":\"4344\",\"farm\":5,\"title\":\"2017-09-03_08-36-45\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36822718446\",\"owner\":\"22591300@N08\",\"secret\":\"85e31e910b\",\"server\":\"4385\",\"farm\":5,\"title\":\"Photo\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36822719126\",\"owner\":\"146983522@N04\",\"secret\":\"20d58f13d9\",\"server\":\"4405\",\"farm\":5,\"title\":\"Paige\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36822719256\",\"owner\":\"56218749@N07\",\"secret\":\"ef0ee9778c\",\"server\":\"4429\",\"farm\":5,\"title\":\"\\\"Il y'a quelques ann\\u00e9es \\\" \\u00c0 vos avis \\/ Give your feedback !! . just style & good vibes... . . . . #vintagestyle #backintheday #throwback #stylemen #streetstyle #hallway #urban #daylight #ootd #outfitoftheday #menwithstyle #instastyle #styleinspo #styleoft\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36822719366\",\"owner\":\"40441544@N08\",\"secret\":\"ecc98b6355\",\"server\":\"4332\",\"farm\":5,\"title\":\"HP Studio Tour Sept 1st 2017\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36822720696\",\"owner\":\"17856604@N00\",\"secret\":\"d180609e12\",\"server\":\"4368\",\"farm\":5,\"title\":\"Inland sea bike ride - Japan\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36822724706\",\"owner\":\"106589573@N02\",\"secret\":\"dfffd33149\",\"server\":\"4332\",\"farm\":5,\"title\":\"\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36822725466\",\"owner\":\"151301191@N04\",\"secret\":\"362728f35c\",\"server\":\"4353\",\"farm\":5,\"title\":\"Recette Minceur : Filet de poisson blanc \\u00e0 la fondue de poireaux citronn\\u00e9e pour r\\u00e9gime citron: ...\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36822727636\",\"owner\":\"151177520@N07\",\"secret\":\"1dec6eb024\",\"server\":\"4424\",\"farm\":5,\"title\":\"How to Build Your Personal Brand on YouTube\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36822730436\",\"owner\":\"8142312@N04\",\"secret\":\"2f6404548f\",\"server\":\"4411\",\"farm\":5,\"title\":\"L1030928\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36822730936\",\"owner\":\"77928420@N00\",\"secret\":\"e5883de384\",\"server\":\"4348\",\"farm\":5,\"title\":\"Wetlands\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36822731146\",\"owner\":\"93391696@N04\",\"secret\":\"79b843be2d\",\"server\":\"4418\",\"farm\":5,\"title\":\"\\u3059\\u3079\\u3066\\u306e\\u5199\\u771f-333\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36839847702\",\"owner\":\"97926088@N08\",\"secret\":\"6fd69312fb\",\"server\":\"4418\",\"farm\":5,\"title\":\"03\\/09\\/17 secteur Gaschney - Hohneck\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36839853272\",\"owner\":\"7308012@N03\",\"secret\":\"e478939159\",\"server\":\"4357\",\"farm\":5,\"title\":\"GVD_6022.jpg\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36839857952\",\"owner\":\"13265182@N00\",\"secret\":\"9efe4b9e0d\",\"server\":\"4340\",\"farm\":5,\"title\":\"Needed some veggies after Algonquin. And gnocchi.\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36839858282\",\"owner\":\"157785925@N04\",\"secret\":\"d7c548507f\",\"server\":\"4439\",\"farm\":5,\"title\":\"20170903_141652\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36839858912\",\"owner\":\"90134940@N00\",\"secret\":\"5e66245b6e\",\"server\":\"4401\",\"farm\":5,\"title\":\"Storm Trooper Sushi\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36839859252\",\"owner\":\"58626738@N05\",\"secret\":\"f9123ff1df\",\"server\":\"4369\",\"farm\":5,\"title\":\"P1020607\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36839859542\",\"owner\":\"148299483@N08\",\"secret\":\"66f9413913\",\"server\":\"4342\",\"farm\":5,\"title\":\"F1 Uniform Malaysia F1 Uniform Design Oren Sport F1 Uniform F1 Uniform Vector Oren Sport Catalog 2016 Oren Sport Catalog 2017 Oren Sport Catalogue 2017 Pdf Oren Sport Microfibre\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36839859872\",\"owner\":\"46030668@N06\",\"secret\":\"07b6f3ddcc\",\"server\":\"4432\",\"farm\":5,\"title\":\"IMG_0025\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36839860022\",\"owner\":\"152803235@N05\",\"secret\":\"42ed993138\",\"server\":\"4405\",\"farm\":5,\"title\":\"Ras de suelo\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36839862152\",\"owner\":\"74190776@N08\",\"secret\":\"6fc9b6b8ea\",\"server\":\"4373\",\"farm\":5,\"title\":\"DSC_3110\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36839862362\",\"owner\":\"102213268@N06\",\"secret\":\"08dc5c478a\",\"server\":\"4373\",\"farm\":5,\"title\":\"When you don't care where you're at. Nap time is exactly that. Nap time #schlitterbahn #2017\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36839862502\",\"owner\":\"112056345@N07\",\"secret\":\"7df5955401\",\"server\":\"4391\",\"farm\":5,\"title\":\"Tucano!\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36839864412\",\"owner\":\"57255464@N08\",\"secret\":\"e0a7c73730\",\"server\":\"4339\",\"farm\":5,\"title\":\"#Range of this morning\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36839865272\",\"owner\":\"49022745@N02\",\"secret\":\"b6cbf5ce59\",\"server\":\"4363\",\"farm\":5,\"title\":\"2012-09-14 22.20.15 (1).jpg\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36839865502\",\"owner\":\"8319362@N08\",\"secret\":\"c6914773d0\",\"server\":\"4331\",\"farm\":5,\"title\":\"Downtown Disney\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36870289911\",\"owner\":\"54501045@N07\",\"secret\":\"c40bfa26a8\",\"server\":\"4393\",\"farm\":5,\"title\":\"20170903_181138\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36870290911\",\"owner\":\"51671326@N00\",\"secret\":\"fe12a17003\",\"server\":\"4407\",\"farm\":5,\"title\":\"Llandudno Goldwing Light Parade 2017\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36870295401\",\"owner\":\"8250364@N03\",\"secret\":\"7bb444a40f\",\"server\":\"4351\",\"farm\":5,\"title\":\"\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36870297221\",\"owner\":\"101859099@N03\",\"secret\":\"905b824828\",\"server\":\"4382\",\"farm\":5,\"title\":\"Senadora Dolores Padierna reafirma su convicci\\u00f3n y congruencia por un mejor pa\\u00eds en la firma del acuerdo pol\\u00edtico de unidad nacional #LaNuevaEsperanza #PorElBuenVIvir  #MNE\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36870298341\",\"owner\":\"157574055@N07\",\"secret\":\"18f10cdce9\",\"server\":\"4376\",\"farm\":5,\"title\":\"Hailey!\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36870301571\",\"owner\":\"125461655@N08\",\"secret\":\"455f40530e\",\"server\":\"4416\",\"farm\":5,\"title\":\"Ok so, the number one question I get at the moment is this. It's fine, I'm fine, leave me alone unmess.youre offering coffee or chocolate. #Repost @phd.diaries _____________________ #phddiaries #phd #science #undergrad #uoft #microscope #microscopy #l4l #\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"36870301851\",\"owner\":\"95442773@N06\",\"secret\":\"1a4b35df20\",\"server\":\"4370\",\"farm\":5,\"title\":\"the mac shack adventure club. established 2015.\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"37011267255\",\"owner\":\"150446963@N08\",\"secret\":\"ae287ea0e4\",\"server\":\"4422\",\"farm\":5,\"title\":\"Trendy Wedding Dresses  : 10 Designers You Need To Know About, NOW! | Keren Mor Yossef Wedding Dress | Bri... - #Dress\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"37011267985\",\"owner\":\"28117870@N05\",\"secret\":\"ef0160af97\",\"server\":\"4337\",\"farm\":5,\"title\":\"#Rosario #fog #niebla #longexposure #ndfilter #river #Rio #cranes #sony #f22 #iso50\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"37011272475\",\"owner\":\"46055880@N03\",\"secret\":\"3085a81f0b\",\"server\":\"4384\",\"farm\":5,\"title\":\"@ Nasi Kandar Fathima at Menara Weld KL Jalan P.Ramlee\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"37011273545\",\"owner\":\"145026089@N08\",\"secret\":\"6719644df1\",\"server\":\"4333\",\"farm\":5,\"title\":\"2017-09-03_08-37-28\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"37011273575\",\"owner\":\"147193841@N05\",\"secret\":\"a8aac036f6\",\"server\":\"4335\",\"farm\":5,\"title\":\"English Actress Helena Bonham Carter [b. 26-MAY-1966, Golders Green, Islington, London, UK-]; Photo\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"37011273725\",\"owner\":\"116652683@N05\",\"secret\":\"b0847a7826\",\"server\":\"4383\",\"farm\":5,\"title\":\"bandamusicalemontemarciano\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"37011274805\",\"owner\":\"157426759@N08\",\"secret\":\"f2440f0239\",\"server\":\"4343\",\"farm\":5,\"title\":\"Quotes About Love: Whatever you are facing, or will face remember this:...\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"37011275135\",\"owner\":\"49182023@N05\",\"secret\":\"ea0686c8f5\",\"server\":\"4427\",\"farm\":5,\"title\":\"IMG_2187.JPG\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"37011275895\",\"owner\":\"150134066@N03\",\"secret\":\"47bf9bfce2\",\"server\":\"4349\",\"farm\":5,\"title\":\"City of the future. :: #glassbuilding #city #future #skyscrapers #trees #modernarchitecture #xuniting\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"37011276575\",\"owner\":\"129094380@N07\",\"secret\":\"3afe651cdb\",\"server\":\"4409\",\"farm\":5,\"title\":\"IMG_5029.jpg\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"37011276585\",\"owner\":\"151693926@N05\",\"secret\":\"893e5593d2\",\"server\":\"4436\",\"farm\":5,\"title\":\"DSC_0358\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"37011277175\",\"owner\":\"157790806@N07\",\"secret\":\"e3c8fd90d3\",\"server\":\"4396\",\"farm\":5,\"title\":\"0903171607\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"37011278555\",\"owner\":\"43765140@N03\",\"secret\":\"f7733bec50\",\"server\":\"4409\",\"farm\":5,\"title\":\"Daydreams of Trees 36\\\" x 24\\\" Acrylic on Canvas #artbyclint\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"37011278965\",\"owner\":\"145544402@N06\",\"secret\":\"291bf80c7e\",\"server\":\"4345\",\"farm\":5,\"title\":\"WP_20170901_11_01_58_Pro\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0},{\"id\":\"37011279235\",\"owner\":\"97164280@N08\",\"secret\":\"8fc14ff931\",\"server\":\"4435\",\"farm\":5,\"title\":\"DSC_3574.jpg\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}]},\"stat\":\"ok\"}";
        try{

            JSONObject jsonBody = new JSONObject(result);
            parseItems(items,jsonBody);

        } catch (JSONException je){
            Log.i(TAG,"json: "+je);
        } catch (IOException ioe){
            Log.i(TAG,"ioe: "+ioe);
        }
        return items;
    }

    /**
     * 取出每张图片的信息,生成一个个GalleryItem对象。再添加到List中
     * @param items
     * @param jsonBody
     */
    private void parseItems(List<GalleryItem> items, JSONObject jsonBody) throws IOException,JSONException{
        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");
        for(int i = 0; i< photoJsonArray.length();i++){

            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

            GalleryItem item = new GalleryItem();

            item.setId(photoJsonObject.getString("id"));
            item.setCaption(photoJsonObject.getString("title"));

            if(photoJsonObject.has("url_s")){
                item.setUrl(photoJsonObject.getString("url_s"));
            }
            items.add(item);
        }

    }

}
