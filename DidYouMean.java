import java.io.*;
import java.net.*;
import org.jsoup.*;
import java.util.zip.*;
import org.jsoup.nodes.*;
import org.jsoup.examples.HtmlToPlainText;
public class DidYouMean {
	public static String didYouMean(String s){
		String word="";
		String url="http://www.google.co.in/search?hl=en&q="+URLEncoder.encode(s);
		String html=executeGet(url,"www.google.co.in",'i');
		Document content=Jsoup.parse(html);
		Element submitted=null;
		try{
			submitted=content.getElementById("topstuff").clone();
			HtmlToPlainText h=new HtmlToPlainText();
			word=h.getPlainText(submitted);
			int q,p=word.indexOf("Did you mean:");
			if(p>=0){
				word=word.substring(p+"Did you mean:".length());
				p=word.indexOf("<>");
				if(p>0) word=word.substring(0,p);
				word=word.trim();
			}
			else{
				p=word.indexOf("Showing results for");
				if(p>=0){
					word=word.substring(p+"Showing results for".length());
					p=word.indexOf("<>");
					if(p>0) word=word.substring(0,p);
					word=word.trim();
				}
				else return "No results";
			}
		}catch(Exception e){e.printStackTrace();}	
		return word;
	}
	public static void main(String args[]){
		System.out.println(didYouMean(args[0]));
	}
	public static String executeGet(String targetURL,String host,char ch){
		URL url;
	    HttpURLConnection connection=null;  
	    try{
	      url=new URL(targetURL);
	      connection=(HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("GET");
		  connection.setRequestProperty("Host",host);
	      connection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
	      connection.setRequestProperty("Accept-Language","en-US,en;q=0.8");
	      if(ch=='c') connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.52 Safari/536.5");
	      if(ch=='i') connection.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.0.3705; .NET CLR 1.1.4322; Media Center PC 4.0; InfoPath.2; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; ShopperReports 3.1.22.0; SRS_IT_E879047EB0765B5336AF90)");
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);
	      GZIPInputStream gzis=new GZIPInputStream(connection.getInputStream());
	      InputStreamReader reader=new InputStreamReader(gzis);
	      BufferedReader in=new BufferedReader(reader);
	      String line;
	      StringBuffer response=new StringBuffer(); 
	      while((line=in.readLine())!=null) {
	    	  response.append(line);
	    	  response.append('\r');
	      }
	      in.close();
	      return response.toString();
	    } catch (Exception e) {e.printStackTrace();return null;}
	}
}