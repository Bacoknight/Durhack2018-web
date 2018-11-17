import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

class ContactBlackBoard{
    /*
    This class will use the BlackBoard API to get the list of courses to which the user is signed up.
    It then calls the PullDayInfo function to get info for those days.
    */

    main(String username, String password){
        //Get the courses from the Blackboard API, using CIS credentials.
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("https://developer.blackboard.com/learn/api/public/v2/courses");

        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();

        System.out.println("Login form get: " + response.getStatusLine());
        if (entity != null) {
            entity.consumeContent();
        }
        System.out.println("Initial set of cookies:");
        List<Cookie> cookies = httpclient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("- " + cookies.get(i).toString());
            }
        }

        HttpPost httpost = new HttpPost("https://portal.sun.com/amserver/UI/Login?" +
                "org=self_registered_users&" +
                "goto=/portal/dt&" +
                "gotoOnFail=/portal/dt?error=true");

        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        nvps.add(new BasicNameValuePair("IDToken1", "username"));
        nvps.add(new BasicNameValuePair("IDToken2", "password"));

        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        response = httpclient.execute(httpost);
        entity = response.getEntity();

        System.out.println("Login form get: " + response.getStatusLine());
        if (entity != null) {
            entity.consumeContent();
        }

        System.out.println("Post logon cookies:");
        cookies = httpclient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("- " + cookies.get(i).toString());
            }
        }
    }

}