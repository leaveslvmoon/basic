package io.itman.library.util;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import io.swagger.annotations.Api;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Demo class
 *
 * @author keriezhang
 * @date 2016/10/31
 */
public class ApiSmsUtil {
    /**
     * @param mobile  手机号
     * @param code    验证码
     * @param smsName 短信签名
     * @param apiKey  key
     */
    public static void getSmsCode(String mobile, String code, String smsName, String apiKey) {
        ApiSmsUtil api = new ApiSmsUtil();
        String httpResponse = api.testSend(mobile, code, smsName, apiKey);
        try {
            JSONObject jsonObj = new JSONObject(httpResponse);
            int error_code = jsonObj.getInt("error");
            String error_msg = jsonObj.getString("msg");
            if (error_code == 0) {
                System.out.println("Send message success.");
            } else {
                System.out.println("Send message failed,code is " + error_code + ",msg is " + error_msg);
            }
        } catch (JSONException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
        }

        httpResponse = api.testStatus();
        try {
            JSONObject jsonObj = new JSONObject(httpResponse);
            int error_code = jsonObj.getInt("error");
            if (error_code == 0) {
                int deposit = jsonObj.getInt("deposit");
                System.out.println("Fetch deposit success :" + deposit);
            } else {
                String error_msg = jsonObj.getString("msg");
                System.out.println("Fetch deposit failed,code is " + error_code + ",msg is " + error_msg);
            }
        } catch (JSONException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param mobile  手机号
     * @param code    验证码
     * @param smsName 短信签名
     * @param apiKey  key
     */
    private String testSend(String mobile, String code, String smsName, String apiKey) {
        // just replace key here
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(
                "api", "key-" + apiKey));
        WebResource webResource = client.resource(
                "http://sms-api.luosimao.com/v1/send.json");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("mobile", mobile);
        formData.add("message", "验证码："+code+"【"+smsName+"】");
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
                post(ClientResponse.class, formData);
        String textEntity = response.getEntity(String.class);
        return textEntity;
    }

    private String testStatus() {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(
                "api", "key-d609b769db914a4d959bae3414ed1f7X"));
        WebResource webResource = client.resource("http://sms-api.luosimao.com/v1/status.json");
        ClientResponse response = webResource.get(ClientResponse.class);
        String textEntity = response.getEntity(String.class);
        return textEntity;
    }


}
