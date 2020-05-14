package com.demo.propertiesloader;

import com.demo.propertiesloader.domain.Authinfo;
import com.demo.propertiesloader.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户端开发问题，客户端的方法怎么调用？
 * springboot怎么开发客户端？
 *
 */

@Slf4j
@RestController
public class SMSSender {

    @Autowired
    public Authinfo authinfo;


    @RequestMapping(path = "/autoload")
    public String initProperties(){
        //Authinfo authinfo = null;
        try {

            return authinfo.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("sms sender init exception "+e);
            return e.toString();
        }


    }

    @RequestMapping(path = "/initProperties")
    public String initProperties(@RequestParam("path") String configFilePath,@RequestParam("prefix") String prefix){
        //Authinfo authinfo = null;
        try {
            authinfo = PropertiesUtil.readPropertiesToObject(Authinfo.class, configFilePath,prefix);
            log.info("read file to object"+authinfo.toString());
            return authinfo.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("sms sender init exception "+e);
            return e.toString();
        }


    }



}
