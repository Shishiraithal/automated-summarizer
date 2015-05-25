package com.automated.summarizer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Vishwesh
 */
public class Mind_map_servlet extends HttpServlet {
           

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        {
            String varname = "";
            varname = request.getParameter("txtsearch");
            varname= varname.replaceAll(" ","+");
            StringBuffer sb = new StringBuffer("");
            String query;


/////////////////////////////////////Place Map//////////////////////////////////////////////            
            sb.append((String) varname);
            sb.append((String) "+map");
            query = sb.toString();
            out.println(query);
            Element sub1 = getmap(query);
            out.println(sub1);
            

/////////////////////////////////////Person//////////////////////////////////////////////            
            sb.setLength(0);
            sb.append((String) varname);
            sb.append((String)"+celebrity");
            query = sb.toString();

            Element sub2  = getperson(query);
            out.println(sub2);


/////////////////////////////////////Meaning of////////////////////////////////////////////
            sb.setLength(0);
            sb.append((String) varname);
            sb.append((String)"+meaning");
            query = sb.toString();

            Element sub3 = getmeaning(query);
            out.println(sub3);

/////////////////////////////////////Stock value///////////////////////////////////////////
            sb.setLength(0);
            sb.append((String) varname);
            sb.append((String) "+stock+value");
            query = sb.toString();

            Element sub4 = getstock(query);
            out.println(sub4);
///////////////////////////////////////////////////////////////////////////////////
request.setAttribute("sub1",sub1); // This will be available as ${sub1}
request.setAttribute("sub2",sub2); // This will be available as ${sub2}
request.setAttribute("sub3",sub3); // This will be available as ${sub3}
request.setAttribute("sub4",sub4); // This will be available as ${sub4}
request.setAttribute("varname",varname); // This will be available as ${varname}
request.getRequestDispatcher("/display_map.jsp").forward(request, response);

            
        }
        
}
    
    public Element getmap(String query)
    {
        String qry = query;
        Element sub = null;
        Document doc = null;
        try
        {
            doc = Jsoup.connect("http://www.bing.com/search?q="+qry).userAgent("Mozilla").ignoreHttpErrors(true).timeout(30 * 1000).get();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Mind_map_servlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        sub = doc.select("div.b_rich").first();
        //System.out.println(sub);
        return sub;
    } 
    public Element getperson(String query)
    {
        String qry = query;
        Element sub = null;
        Document doc = null;
        try
        {
            doc = Jsoup.connect("http://www.bing.com/search?q="+qry).userAgent("Mozilla").ignoreHttpErrors(true).timeout(30 * 1000).get();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Mind_map_servlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        sub = doc.select("div.b_subModule").first();
        //System.out.println(sub);
        return sub;
    }
    public Element getmeaning(String query)
        {
        String qry = query;
        Element sub = null;
        Document doc = null;
        try
        {
            doc = Jsoup.connect("http://www.bing.com/search?q="+qry).userAgent("Mozilla").ignoreHttpErrors(true).timeout(30 * 1000).get();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Mind_map_servlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        sub = doc.select("div.b_vPanel").first();
        //System.out.println(sub);
        return sub;
    }
        public Element getstock(String query)
    {
        String qry = query;
        Element sub = null;
        Document doc = null;
        try
        {
            doc = Jsoup.connect("http://www.bing.com/search?q="+qry).userAgent("Mozilla").ignoreHttpErrors(true).timeout(30 * 1000).get();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Mind_map_servlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        sub = doc.select("div.b_imagePair.wide_finGraph").first();
        //System.out.println(sub);
        return sub;
    } 
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}



