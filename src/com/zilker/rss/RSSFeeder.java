package com.zilker.rss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Servlet implementation class RSSFeeder
 */
@WebServlet("/news")
public class RSSFeeder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	private static String getUrlContents(String theUrl)
    {
      StringBuilder content = new StringBuilder();

      // many of these calls can throw exceptions, so i've just
      // wrapped them all in one try/catch statement.
      try
      {
        // create a url object
        URL url = new URL(theUrl);

        // create a urlconnection object
        URLConnection urlConnection = url.openConnection();

        // wrap the urlconnection in a bufferedreader
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        String line;

        // read from the urlconnection via the bufferedreader
        while ((line = bufferedReader.readLine()) != null)
        {
          content.append(line + "\n");
        }
        bufferedReader.close();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
      return content.toString();
    }
 
	
    public RSSFeeder() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out =response.getWriter();
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String content = request.getParameter("content");
		//int factor = 4;
	    String testString = getUrlContents("https://news.google.com/news/rss/headlines/section/topic/"+content+".en_in");
	    JSONObject xmlJSONObj = XML.toJSONObject(testString);
	    JSONArray items = xmlJSONObj.getJSONObject("rss").getJSONObject("channel").getJSONArray("item");
	    for(int i=0;i<items.length();i++) {
	    	String description = items.getJSONObject(i).getString("description");
	    	Document table = Jsoup.parse(description);
	    	Elements links =table.getElementsByTag("li");
	    	Element ul = new Element("ul");
	    	ul.addClass("news-link-list");
	    	for(int iter=0;iter<links.size();iter++) {
	    		links.get(iter).addClass("news-link");
	    		ul.appendChild(links.get(iter));
	    	}
	    	Elements img = table.getElementsByTag("img");
	    	for(int iter=0;iter<img.size();iter++) {
	    		img.get(iter).addClass("img-link");
	    	}
	    	Element article = new Element("article");
	    	article.addClass("news-article");
	    	if(img.size()>0) {
	    		article.appendChild(img.get(0));
	    	}
	    	article.appendChild(ul);
	    	out.println(article);
	    }
	}

}
