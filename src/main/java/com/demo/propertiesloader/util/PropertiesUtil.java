package com.demo.propertiesloader.util;

import com.demo.propertiesloader.domain.Authinfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * 通过java.util.Properties，将文件中的内容反射到对象中
 * 方式可行但目前使用spring读取的方式
 */
@Slf4j
public class PropertiesUtil {

    public static <T> T readPropertiesToObject(Class<T> clazz,String configPath,String prefix) throws Exception {
        Properties prop=readProperties(configPath);
        Field[] fields = clazz.getDeclaredFields();
        T result=clazz.newInstance();
        for(Field field:fields){
            String fieldName=field.getName();
            String propName= StringUtils.isEmpty(prefix)?fieldName:prefix+"."+fieldName;
            if (prop.get(propName)!=null){
                field.setAccessible(true);
                field.set(result,prop.get(propName));
            }
        }
        return result;
    }


    //将
    public static Properties readProperties(String configPath)throws Exception{
        FileInputStream fis=null;
        try {
            File file=new File(PropertiesUtil.class.getClassLoader().getResource(configPath).getFile());
            if (!file.exists()){
                log.error("file isn't exists. ["+file.getAbsolutePath()+"]");
                throw new FileNotFoundException();
            }
            fis=new FileInputStream(file);
            Properties prop=new Properties();
            prop.load(fis);
            return prop;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("read file exception :"+e);
            throw e;
        }finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("FileInputStream close exception :"+e);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            Authinfo auth=  readPropertiesToObject(Authinfo.class,"authinfo.properties","auth");
            System.out.println(auth);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
