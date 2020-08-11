package de.mgrossmann.fritzdata.fritzbox;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created on 11.08.2020
 *
 * @author mgn
 */
public class FritzConnection {
    private HttpHost targetHost;
    private String user = null;
    private String pwd = null;
    private CloseableHttpClient httpClient;
    private HttpClientContext context;

    public FritzConnection(String host, String user, String pwd) {
        this.targetHost = new HttpHost(host, 80);
        this.user = user;
        this.pwd = pwd;
        context = HttpClientContext.create();
        httpClient = HttpClients.createDefault();
    }

    protected InputStream getXml(String fileName) throws IOException {
        HttpGet httpget = new HttpGet(fileName);
        return httpRequest(this.targetHost, httpget, this.context);

    }

    private InputStream httpRequest(HttpHost target, HttpRequest request, HttpContext context) throws IOException {
        CloseableHttpResponse response = null;
        byte[] content = null;
        try {
            response = httpClient.execute(target, request, context);
            content = EntityUtils.toByteArray(response.getEntity());
        } catch (IOException e) {
            throw e;
        } finally {
            if (response != null) {
                response.close();
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new IOException(response.getStatusLine().toString());
                }
            }

        }
        if (content != null) {
            return new ByteArrayInputStream(content);
        } else {
            return new ByteArrayInputStream(new byte[0]);
        }
    }


}
