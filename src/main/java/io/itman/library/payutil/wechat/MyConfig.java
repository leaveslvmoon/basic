package io.itman.library.payutil.wechat;


import io.itman.library.payutil.wechat.base.IWXPayDomain;
import io.itman.library.payutil.wechat.base.WXPayConfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class MyConfig extends WXPayConfig {
    private byte[] certData;

    public MyConfig() throws Exception {
        /*File file = ResourceUtils.getFile("classpath:cert/apiclient_cert.p12");
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();*/

        /*ClassPathResource resource = new ClassPathResource("cert/apiclient_cert.p12");
        InputStream certStream = resource.getInputStream();
        this.certData = new byte[(int) resource.getFile().length()];
        certStream.read(this.certData);
        certStream.close();*/
        InputStream certStream = getClass().getClassLoader().getResourceAsStream("cert/apiclient_cert.p12");
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024*4];
        int n=0;
        while ( (n=certStream.read(buffer)) !=-1) {
            out.write(buffer,0,n);
        }
        this.certData = out.toByteArray();

        certStream.read(this.certData);
        certStream.close();
    }

    @Override
    public String getAppID() {
        return "wx584527de5286857e";
    }

    @Override
    public String getMchID() {
        return "1514793301";
    }

    @Override
    public String getKey() {
        return "xinnongren20181001xunbangkeji123";
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        return WXPayDomainSimpleImpl.instance();
    }
}
