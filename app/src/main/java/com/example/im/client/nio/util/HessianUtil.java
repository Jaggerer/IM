package com.example.im.client.nio.util;

import java.io.*;

import com.caucho.hessian.io.*;


public class HessianUtil {

    public static byte[] serialize(Object obj) throws Exception {
        if (obj == null) throw new NullPointerException();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(os);
        out.writeObject(obj);
        return os.toByteArray();
    }

    public static Object deserialize(byte[] by) throws Exception {
        if (by == null) throw new NullPointerException();

        ByteArrayInputStream is = new ByteArrayInputStream(by);
        ObjectInputStream in = new ObjectInputStream(is);
        return in.readObject();
    }
}