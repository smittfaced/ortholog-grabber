/**
 * Created by Andrew on 11/17/2015.
 */

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OGF {

    static String HGSID;

    public static void main(String[] args) throws Exception {
    }

    /*
     * Gets the form elements from the UCSC website and allows them to be pulled
     * into OG
     */
    public static ArrayList<ArrayList<String>> GetFormElements(String url)
            throws Exception {

        // All FORM Elements
        Elements ORGANISMS, tempHGSID, DATABASE, TYPE;
        // String HGSID;
        // ArrayList<String> ORGANISMS = new ArrayList<String>();
        // ArrayList<String> DATABASE = new ArrayList<String>();
        // ArrayList<String> TYPE = new ArrayList<String>();

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            HttpGet httpGet = new HttpGet(url);
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                public String handleResponse(final HttpResponse response)
                        throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
//                        System.out.println(response.getStatusLine());
                        return entity != null ? EntityUtils.toString(entity)
                                : null;
                    } else {
                        throw new ClientProtocolException(
                                "Unexpected response status: " + status);
                    }
                }
            };

            String responseBody = httpclient.execute(httpGet, responseHandler);

            // Parse the HTML for form values
            Document doc = Jsoup.parse(responseBody, url);

            tempHGSID = doc.getElementsByAttributeValue("name", "hgsid");
            ORGANISMS = doc.getElementsByAttributeValue("name", "org").select(
                    "select > option");
            DATABASE = doc.getElementsByAttributeValue("name", "db").select(
                    "select > option");
            TYPE = doc.getElementsByAttributeValue("name", "type").select(
                    "select > option");

            // ArrayList<Elements> result = new ArrayList<Elements>();
            ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
            // result.add(HGSID);
            // HGSID
            ArrayList<String> sHGSID = new ArrayList<String>();
            sHGSID.add(tempHGSID.get(0).val());
            result.add(sHGSID);

            // result.add(ORGANISMS);
            // Organisms
            Iterator<Element> it = ORGANISMS.iterator();

            ArrayList<String> sORGANISMS = new ArrayList<String>();
            while (it.hasNext()) {
                sORGANISMS.add(it.next().val());
            }
            result.add(sORGANISMS);

            // result.add(DATABASE);
            // Database
            it = DATABASE.iterator();

            ArrayList<String> sDATABASE = new ArrayList<String>();
            while (it.hasNext()) {
                // sDATABASE.add(it.next().val());
                sDATABASE.add(it.next().text());
            }
            result.add(sDATABASE);

            // result.add(TYPE);
            // Database
            it = TYPE.iterator();

            ArrayList<String> sTYPE = new ArrayList<String>();
            while (it.hasNext()) {
                sTYPE.add(it.next().text());
            }
            result.add(sTYPE);

            // result.add(SORT);
            // result.add(OUTPUT);
            HGSID = sHGSID.get(0);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            httpclient.close();
        }

    }

    /*
     * Returns the list of database options for each Organism
     *
     * Requires: Organism<String>, URL<String> Returns:
     * ArrayList<ArrayList<String>>
     */
    public static ArrayList<ArrayList<String>> ChangeOrganism(String hgSID, String org, String url) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                public String handleResponse(final HttpResponse response)
                        throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
//                        System.out.println(response.getStatusLine());
                        return entity != null ? EntityUtils.toString(entity)
                                : null;
                    } else {
                        throw new ClientProtocolException(
                                "Unexpected response status: " + status);
                    }
                }

            };

            MultipartEntityBuilder a = MultipartEntityBuilder.create();
            a.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            a.addTextBody("hgsid", hgSID);
            a.addTextBody("org", org);

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(a.build());
            String responseBody = httpclient.execute(httpPost, responseHandler);

            Document doc = Jsoup.parse(responseBody, url);

            Elements DATABASE = doc.getElementsByAttributeValue("name", "db")
                    .select("select > option");
            Iterator<Element> db = DATABASE.iterator();
            ArrayList<String> dbs = new ArrayList<String>();
            ArrayList<String> displayText = new ArrayList<String>();
            ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

            while (db.hasNext()) {
                Element y = db.next();
                dbs.add(y.attr("VALUE"));
                displayText.add(y.text());
            }

            result.add(dbs);
            result.add(displayText);

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            httpclient.close();
        }
    }

    /*
     * Returns an ArrayList<ArrayList<String>> of length 2 containing the links
     * and information returned from the BLAT search
     *
     * Requires DNA<String>, Organism<String>, Genome<String>, and URL<String>
     * Returns ArrayList<ArrayList<String>>
     */
    public static ArrayList<Object[][]> getBlatResults(String hgSID, String seq, String org, String genome, String url) throws IOException {

//        System.out.println("\nSpecies: " + org + "\tGenome: " + genome);

        ArrayList<Object> GB_Links_Info; // The % Same, size, and location of
        // the BLAT results
        ArrayList<Object> GB_Links; // The hyperlinks to the corresponding BLAT
        // results

        CloseableHttpClient httpclient = HttpClients.createDefault();

        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

            public String handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
//                    System.out.println(response.getStatusLine());
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException(
                            "Unexpected response status: " + status);
                }
            }

        };

        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

            MultipartEntityBuilder a = MultipartEntityBuilder.create();
            a.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            a.addTextBody("hgsid", hgSID);
            a.addTextBody("org", org);
            a.addTextBody("db", genome);
            a.addTextBody("type", "Blat's guess");
            a.addTextBody("userSeq", seq);

            httpPost.setEntity(a.build());
            String responseBody = httpclient.execute(httpPost, responseHandler);

            Document doc = Jsoup.parse(responseBody, url);

			/*
             * Getting the URL Links for the Results
			 */
            Elements results = doc.getElementsByTag("pre");

            Object[] result = results.select("pre > a").toArray();
            GB_Links = new ArrayList<Object>(result.length / 2);

            int index = 0;
            for (Object s : result) {
                if (index % 2 == 0) {

                    String temp = s.toString().substring(
                            s.toString().indexOf("<a href=\"..") + 12,
                            s.toString().indexOf("hgsid") + 6);

                    GB_Links.add(temp.substring(0, temp.indexOf("position"))
                            + temp.substring(temp.indexOf("db="),
                            temp.indexOf("&amp;ss=.."))
                            + "&"
                            + temp.substring(temp.indexOf("position"),
                            temp.indexOf("&amp;db")).replaceFirst(":",
                            "%3A") + "&hgsid=");
                }
                index++;
            }

            Object[] links = new String[GB_Links.size()];
            for (int i = 0; i < GB_Links.size(); i++)
                links[i] = GB_Links.get(i).toString();

			/*
             * Getting the Percent Identity info about the Results
			 */
            GB_Links_Info = new ArrayList<Object>();

            String info = results.first().ownText();
            String[] resultRows = info.split("\n");

            Object[][] rowString = new Object[resultRows.length - 2][11];
            Object[][] rows = new Object[resultRows.length - 2][11];

            for (int i = 2; i < rowString.length + 2; i++) {
                String s = resultRows[i].replaceFirst("YourSeq", links[i - 2].toString());
                rowString[i - 2] = s.trim().split("\\s+");
            }

            for (int i = 0; i < rowString.length; i++) {
                for (int j = 0; j < rowString[i].length; j++) {
                    try {
                        rows[i][j] = Integer.parseInt((String) rowString[i][j]);
                    } catch (Exception e) {
                        String s = (String) rowString[i][j];
                        rows[i][j] = s;
                    }
                }
            }

            Object[] colHeaders = new Object[]{"LINK", "SCORE", "START", "END", "QSIZE", "IDENTITY", "CHROMOSOME", "STRAND", "START", "END", "SPAN"};
            Object[][] cols = new Object[1][colHeaders.length];
            for (int i = 0; i < colHeaders.length; i++) {
                cols[0][i] = colHeaders[i];
            }

            ArrayList<Object[][]> resRet = new ArrayList<Object[][]>();
            resRet.add(null);
            resRet.add(cols);
            resRet.add(rows);

            httpclient.close();
            return resRet;
        } finally

        {
            httpclient.close();
        }

    }

    public static String getDNAQuick(String hgSID, String strand, String fivePad, String threePad, String url) throws IOException {

//        Get the GenomeBrowser Page using result of BLAT Search
        String baseUrl = "http://genome.ucsc.edu/";
        url = baseUrl.concat(url);

        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

            public String handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
//                    System.out.println(response.getStatusLine());
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException(
                            "Unexpected response status: " + status);
                }
            }

        };

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

            String responseBody = httpclient.execute(httpPost, responseHandler);

//            Find the DNA Link And Submit a HTTP Request
            Document doc = Jsoup.parse(responseBody, url);
            String dnaLink = doc.getElementById("dnaLink").attr("href").substring(3);
//            System.out.println("\ndnaLink: " + dnaLink);

//            Submit HTTP Request for Getting DNA
            httpPost = new HttpPost(baseUrl.concat(dnaLink));
            httpPost.addHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

            responseBody = httpclient.execute(httpPost, responseHandler);

//            Submit Request for DNA Page
            doc = Jsoup.parse(responseBody, baseUrl.concat(dnaLink));

            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            String dnaUrl = baseUrl.concat("cgi-bin/hgc?");
//            System.out.println(dnaUrl);

//            Element hgsidValue = doc.select("input[name=hgsid]").first();
//            System.out.println("HGSID: " + hgsidValue.attr("value"));
//            urlParameters.add(new BasicNameValuePair("hgsid", hgsidValue.attr("value")));

            Element gValue = doc.select("input[name=g]").first();
//            System.out.println("gValue: " + gValue.attr("value"));
            urlParameters.add(new BasicNameValuePair("g", gValue.attr("value")));

//            Element tableValue = doc.select("input[name=table]").first();
//            System.out.println("tableValue: " + tableValue.attr("value"));
//            urlParameters.add(new BasicNameValuePair("table", tableValue.attr("value")));

//            Element iValue = doc.select("input[name=i]").first();
//            System.out.println("iValue: " + iValue.attr("value"));
//            urlParameters.add(new BasicNameValuePair("i", iValue.attr("value")));

//            Element oValue = doc.select("input[name=o]").first();
//            System.out.println("oValue: " + oValue.attr("value"));
//            urlParameters.add(new BasicNameValuePair("o", oValue.attr("value")));

//            Element lValue = doc.select("input[name=l]").first();
//            System.out.println("lValue: " + lValue.attr("value"));
//            urlParameters.add(new BasicNameValuePair("l", lValue.attr("value")));

//            Element rValue = doc.select("input[name=r]").first();
//            System.out.println("rValue: " + rValue.attr("value"));
//            urlParameters.add(new BasicNameValuePair("r", rValue.attr("value")));

//            Element getDnaPosValue = doc.select("input[name=getDnaPos]").first();
//            System.out.println("getDnaPosValue: " + getDnaPosValue.attr("value"));
//            urlParameters.add(new BasicNameValuePair("getDnaPos", getDnaPosValue.attr("value")));

//            Element dbValue = doc.select("input[name=db]").first();
//            System.out.println("db: " + dbValue.attr("value"));
//            urlParameters.add(new BasicNameValuePair("db", dbValue.attr("value")));

//            Element hgSeqcdsExonValue = doc.select("input[name=hgSeq.cdsExon]").first();
//            System.out.println("hgSeq.cdsExon: " + hgSeqcdsExonValue.attr("value"));
//            urlParameters.add(new BasicNameValuePair("hgSeq.cdsExon", hgSeqcdsExonValue.attr("value")));

            Element hgSeqpadding5Value = doc.select("input[name=hgSeq.padding5]").first();
//            System.out.println("hgSeq.padding5: " + hgSeqpadding5Value.attr("value"));
            urlParameters.add(new BasicNameValuePair("hgSeq.padding5", fivePad));

            Element hgSeqpadding3Value = doc.select("input[name=hgSeq.padding3]").first();
//            System.out.println("hgSeq.padding3: " + hgSeqpadding3Value.attr("value"));
            urlParameters.add(new BasicNameValuePair("hgSeq.padding3", threePad));

            Element hgSeqcasingValue = doc.select("input[name=hgSeq.casing]").first();
//            System.out.println("hgSeq.casing: " + hgSeqcasingValue.attr("value"));
            urlParameters.add(new BasicNameValuePair("hgSeq.casing", hgSeqcasingValue.attr("value")));

            Element hgSeqrepMaskingValue = doc.select("input[name=hgSeq.repMasking]").first();
//            System.out.println("hgSeq.repMasking: " + hgSeqrepMaskingValue.attr("value"));
            urlParameters.add(new BasicNameValuePair("hgSeq.repMasking", hgSeqrepMaskingValue.attr("value")));

            Element submitValue = doc.select("input[name=submit]").first();
//            System.out.println("submitValue: " + submitValue.attr("value"));
            urlParameters.add(new BasicNameValuePair("submit", submitValue.attr("value")));

            if (strand.equals("-")) {
//                Reverse Compliment Name-Value Pair
                Element hgSeqrevCompValue = doc.select("input[name=hgSeq.revComp]").first();
//                System.out.println("hgSeq.revComp: " + hgSeqrevCompValue.attr("value"));
                urlParameters.add(new BasicNameValuePair("hgSeq.revComp", hgSeqrevCompValue.attr("value")));
            }

            httpPost = new HttpPost(dnaUrl);
            httpPost.addHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

            responseBody = httpclient.execute(httpPost, responseHandler);
            Document doc2 = Jsoup.parse(responseBody, dnaUrl);

            System.out.println(doc.select("pre").text());

            String temp = doc2.select("pre").text();
//            String strandResult = temp.substring((temp.indexOf("strand=") + 7), temp.indexOf(" repeatMasking"));

////            Ignore strandedness on initial BLAT
//            if (strand.equals("0")) {
//                return doc2.select("pre").text();
//            }
//            if (!strandResult.equals(strand)) {
////                Reverse Compliment Name-Value Pair
//                Element hgSeqrevCompValue = doc.select("input[name=hgSeq.revComp]").first();
////                System.out.println("hgSeq.revComp: " + hgSeqrevCompValue.attr("value"));
//                urlParameters.add(new BasicNameValuePair("hgSeq.revComp", hgSeqrevCompValue.attr("value")));
//
//                httpPost = new HttpPost(dnaUrl);
//                httpPost.addHeader(
//                        "User-Agent",
//                        "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
//                httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
//
//                responseBody = httpclient.execute(httpPost, responseHandler);
//                doc = Jsoup.parse(responseBody, dnaUrl);
//
//                return doc.select("pre").text();
//            }

            return doc2.select("pre").text();

        }
    }

    /*
     * Reads a single FASTA file and returns the DNA as a String
     */
    public static String readFASTA(File file) {

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuffer sb = new StringBuffer();
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.contains(">")) {

                } else {
                    sb.append(sCurrentLine);
                }
            }
            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * NOT IMPLEMENTED YET
     *
     * Reads a multiple FASTA file and returns the DNA as an ArrayList<String>
     */
    public static void readMultiFASTA(File file) {

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuffer sb = new StringBuffer();
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.contains(">")) {

                } else {
                    sb.append(sCurrentLine);
                }
            }
            // return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
            // return null;
        }
    }

    public String getHGSID() {
        return HGSID;
    }
}

