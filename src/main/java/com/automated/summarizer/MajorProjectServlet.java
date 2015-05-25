package com.automated.summarizer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;

@SuppressWarnings("serial")
public class MajorProjectServlet extends HttpServlet implements Serializable
{
URL url1=null,url2=null,url3=null,url4=null,sent_url=null;
String url_list=null,page=null,page_1=null,page_2=null,page_3=null,page_4=null,output=null,output_1=null,output_2=null,output_3=null,output_4=null;
String[] split_url=null;
int i=0;
    
//////////////////////////////////////List Url///////////////////////////////////////////////////////////////////      

	 Gson gson = new GsonBuilder().create(); 
	

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
        {

/////////////////////////////////////////////////////////////////////////////////            
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try 
		{ 
                
			String varname="";
			varname=request.getParameter("txtsearch");
			String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
			//String address ="https://www.googleapis.com/customsearch/v1?key=AIzaSyA_Zwfw2lE8efHDoT1RUQrr7pU2vZjEz4Y&cx=003019280336334658413:pzkoltdmafk&q=";
			//String qry = varname;
			String query = varname.replaceAll(" ","+");
			String charset = "UTF-8";

			//out.println(query);
			
			URL url = new URL(address + URLEncoder.encode(query, charset));
			Reader reader = new InputStreamReader(url.openStream(), charset);
			GoogleResults results = gson.fromJson(reader, GoogleResults.class);

			int total = results.getResponseData().getResults().size();
			System.out.println("total: "+total);
                        
                        
                        ArrayList temp = new ArrayList();
                        
                        //List temp = new ArrayList<String>();
			
                        // Show title and URL of each results
			for(int i=0; i<=total-1; i++)
                           {
				//out.println("Title: " + results.getResponseData().getResults().get(i).getTitle());
				//out.println("URL: " + results.getResponseData().getResults().get(i).getUrl() + "\n");
                               temp.add(results.getResponseData().getResults().get(i).getUrl());
                            //  }
                               
                           }
                        
                          StringBuffer sb = new StringBuffer("");
                          
                                Iterator itr=temp.iterator();//getting Iterator from arraylist to traverse elements  
                                 while(itr.hasNext())
                              {  
                                sb.append((String)(itr.next()));
                               sb.append("!");
                                //out.println(sb);  
                              }  
                                /////////////////////
                                url_list =sb.toString();
                                
                               split_url = url_list.split("!");
                           
	                        for(int i=0;i<split_url.length;i++)
                               {
	    	
                                out.println(split_url[i]);  
                                out.println("<br>");
			
                               }
                 
                                       
                             url1=new URL(split_url[0]);
                             url2=new URL(split_url[1]);
                             url3=new URL(split_url[2]);
                             url4=new URL(split_url[3]);
                            
                           sent_url=url1;
                           page_1=read_URL(sent_url);
                          //out.println(page_1);
                           sent_url=url2;
                           page_2=read_URL(sent_url);
                           
                           sent_url=url3;
                           page_3=read_URL(sent_url);
                           
                           sent_url=url4;
                           page_4=read_URL(sent_url);
                        
                              
                }
		 catch(Exception ex)
		{
			out.println(ex.getMessage());
		}
     
           out.println("///////////////////////////////////////////////////////////////////////////////////////////////");    
            out.println("<br>");
             output_1 = read_url_content(page_1);
              //out.println(output_1);
               out.println("<br>");
                //out.println("///////////////////////////////////////////////////////////////////////////////////////////////");
                 out.println("<br>");
            output_2 = read_url_content(page_2);
             //out.println(output_2);
             // out.println("<br>");
               // out.println("///////////////////////////////////////////////////////////////////////////////////////////////");
               //out.println("<br>");
             output_3 = read_url_content(page_3);
             //out.println(output_3);
             // out.println("<br>");
               // out.println("///////////////////////////////////////////////////////////////////////////////////////////////");
                 out.println("<br>");
             output_4 = read_url_content(page_4);
              //out.println(output_4);
               //out.println("<br>");
               // out.println("///////////////////////////////////////////////////////////////////////////////////////////////");

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Displaying Summary</title>");            
            out.println("</head>");
            out.println("<body>");
   out.println("<iframe srcdoc='<html>");
            out.println("<textarea>");
            out.println(output_1);
            out.println("</textarea>");  
            out.println("</html>'>");
            out.println("width='1000' height='800' scrolling='yes'>");
            out.println(" <p>Your browser does not support iframes.</p>");
            out.println("</iframe>");
   out.println("<iframe srcdoc='<html>");
            out.println("<textarea>");  
            out.println(output_2);
            out.println("</textarea>");  
            out.println("</html>'>");
            out.println("width='1000' height='800' scrolling='yes'>");
            out.println(" <p>Your browser does not support iframes.</p>");
            out.println("</iframe>");
   out.println("<iframe srcdoc='<html>");
            out.println("<textarea>");  
            out.println(output_3);
            out.println("</textarea>");  
            out.println("</html>'>");
            out.println("width='1000' height='800' scrolling='yes'>");
            out.println(" <p>Your browser does not support iframes.</p>");
            out.println("</iframe>");
               out.println("<iframe srcdoc='<html>");
            out.println("<textarea>");  
   out.println(output_4);
            out.println("</textarea>");  
            out.println("</html>'>");
            out.println("width='1000' height='800' scrolling='yes'>");
            out.println(" <p>Your browser does not support iframes.</p>");
            out.println("</iframe>");
            out.println("</body>");
            out.println("</html>");
            

/////////////////////////////////Filtering Non-Ascii Characters/////////////////////////////////////////////////////////////	
            FilterNonAscii Fn = new FilterNonAscii(); //Creating object for FilterNonAscii Class
            String in_ascii;
            
            in_ascii = output_1;
            output_1 = Fn.ReplaceNonAscii(in_ascii);
                        
            in_ascii = output_2;
            output_2 = Fn.ReplaceNonAscii(in_ascii);
                        
            in_ascii = output_3;
            output_3 = Fn.ReplaceNonAscii(in_ascii);
            
            in_ascii = output_4;
            output_4 = Fn.ReplaceNonAscii(in_ascii);
            
            in_ascii=null;
////////////////////////////////////////////Summarizing/////////////////////////////////////////////////////////////////////////////	
            
            
         
            String Sum_per = request.getParameter("SlidVal");
            int Summar_percent = 0;
           if (Sum_per != null && !Sum_per.isEmpty()) 
            {
                Summar_percent = Integer.parseInt(Sum_per.trim());
            }
          
            //out.println(Summar_percent);
          
            Util.summaryPercent =Summar_percent;
             
            out.println("<br>");
            Util.combinedSummary=null;
           // Util.summaryPercent=40;
            Util.file_no = 4;
            Util.isParallel = true;
            Util.url1 = output_1;
            Util.url2 = output_2;
            Util.url3 = output_3;
            Util.url4 = output_4;
out.println("///////////////////////////////////////////////////////////////////////////////////////////////");            
      CollectionSample.invoke();
      out.println("<br>");
      out.println("----------------------------Combined_Summary-------------------------");
      out.println("<br>");
      out.println("The Summary Percentage is"+" "+Util.summaryPercent);  
      out.println("<br>");
      out.println("Total time taken for Summarization is"+" "+Util.totalTime+" "+"Milli Seconds");
      out.println("<br>");
      out.println("---------------------------------------------------------------------");
      ///*out.println("<br>");
      out.println(Util.combinedSummary);
      //
      out.println("<br>");
    
                String Nonbulleted=Util.combinedSummary;          
      
                String[] split_summary=null;
		//String Temp_Summary="Applet.Banana.Cherry.Doughnut.";
		String Temp_Summary=Util.combinedSummary;
		split_summary=Temp_Summary.split("\\.");
		int i = 0;
		StringBuffer sb = new StringBuffer(""); 
		ArrayList temp = new ArrayList();
		for(i = 0; i<=(split_summary.length)-1; i++)
		{
			//System.out.println(split_summary[i]);
			temp.add(split_summary[i]);
			
		}
		Iterator itr=temp.iterator();
		sb.append("<ul>");
		while(itr.hasNext())
		{  
			//System.out.println(itr.next().toString());
			sb.append("<li>");
			sb.append((String)(itr.next()));
			sb.append("</li>");
		}  
		sb.append("</ul>");
                String bulleted=sb.toString();
//request.setAttribute("bulleted",bulleted);
//request.getRequestDispatcher("/Bulleted_Summary.jsp").forward(request, response);
	    out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Displaying Summary</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<iframe srcdoc='<html>");
            //out.println(bulleted);
            out.println(Nonbulleted);
            out.println("</html>'>");
            //out.println(bulleted);
            out.println("width='1000' height='800' scrolling='yes'>");
            out.println(" <p>Your browser does not support iframes.</p>");
            out.println("</iframe>");
          //out.println(sb.toString());
            out.println("</body>");
            out.println("</html>");
            


     ///////////////////////
      Util.combinedSummary=null;
      /*out.println("Below should be blank");
       out.println(Util.combinedSummary);
      out.println("<br>");
      */
      URL url1,url2,url3,url4,sent_url;
String url_list,page,page_1,page_2,page_3,page_4,output,output_1,output_2,output_3,output_4;
String[] split_url;
i=0;

}




	static class GoogleResults implements Serializable
	{
            
		GoogleResults(){}
		private ResponseData responseData;
		public ResponseData getResponseData() { return responseData; }
		public void setResponseData(ResponseData responseData) { this.responseData = responseData; }
		public String toString() { return "ResponseData[" + responseData + "]"; }

		class ResponseData implements Serializable
		{
			ResponseData()
			{}
			private List<Result> results;
			public List<Result> getResults() { return results; }
			public void setResults(List<Result> results) { this.results = results; }
			public String toString() { return "Results[" + results + "]"; }
		}

		class Result implements Serializable
		{
			 Result()
			{
				
			}
			private String url;
			private String title;
			public String getUrl() { return url; }
			public String getTitle() { return title; }
			public void setUrl(String url) { this.url = url; }
			public void setTitle(String title) { this.title = title; }
			public String toString() { return "Result[url:" + url +",title:" + title + "]"; }
		}


	}
////////////////////////////////////Exctracting text from url///////////////////////////////////////////////////////////////////////
       String  read_URL(URL sent_url)
        {
        String String_Url=sent_url.toString();
        URL received_url;
         String html;
		{
            
		try {
                    received_url=new URL(String_Url);
                    html=(received_url.toString());
			html=Jsoup.connect(html).get().toString();
			 page = Jsoup.parseBodyFragment(html).toString();//doc.body().text();
			//
		  
                } 
         catch (IOException e) {
			e.printStackTrace();
		}
                //out.println(page);
         return page;      
        }
}
 /////////////////////////////////////Extract text from HTML/////////////////////////////////////////////////////////////////////////    
  String read_url_content(String html_page)throws IOException
      {     
         String inputline=html_page;
         String output=null; 
        /*
         try {
			output = HTMLUtils.extractText(inputline);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         */
         String textOnly;
         textOnly = Jsoup.parse(inputline).text();
         return textOnly;
    }
       

}