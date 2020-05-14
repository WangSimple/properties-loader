package com.demo.propertiesloader.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Component
@Data
@PropertySource(value="classpath:authinfo-${spring.profiles.active}.properties")
//@PropertySource("classpath:authinfo.properties")
@ConfigurationProperties(prefix = "auth")
public class Authinfo {
    private String orgName;
    private String userName;
    private String password;
    private String userId;
    private String serverPath;

    public String toString(){
        return orgName+" --- "
                +userName+" --- "
                +password+" --- "
                +userId+" --- "
                +serverPath;
    }
}
