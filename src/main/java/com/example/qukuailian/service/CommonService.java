package com.example.qukuailian.service;

import com.example.qukuailian.util.SMA;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service
public class CommonService {
    public Method getEncryptMethod(String algtype) throws ClassNotFoundException, NoSuchMethodException {
        Class<?> clazz = null;
        if(algtype.equals("2")){
            clazz = Class.forName(SMA.SM2.getClassName());
        }else if(algtype.equals("4")){
            clazz = Class.forName(SMA.SM4.getClassName());
        }
        Method method = clazz.getMethod("encrypt", new Class[]{String.class, String.class});
        return method;
    }

    public Method getDecryptMethod(String algtype) throws ClassNotFoundException, NoSuchMethodException {
        Class<?> clazz = null;
        if(algtype.equals("2")){
            clazz = Class.forName(SMA.SM2.getClassName());
        }else if(algtype.equals("4")){
            clazz = Class.forName(SMA.SM4.getClassName());
        }
        Method method = clazz.getMethod("decrypt", new Class[]{String.class, String.class});
        return method;
    }
}
