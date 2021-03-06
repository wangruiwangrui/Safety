package com.safety.android.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class PersistentCookieStore {


    private static final String COOKIE_PREFS = "Cookies_Prefs";

    //根据各自的业务形态进行定制，可以使用hashMap，甚至也可以选用其他数据结构存储Cookie。例子中使用了HashMap实现，key作为一级域名；value则是以cookieToken为key的Cookie映射，cookieToken的获取见下述方法。
    private final Map<String, ConcurrentHashMap<String, Cookie>> cookies;


    private final SharedPreferences cookiePrefs;

    public PersistentCookieStore(Context context) {
        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
        cookies = new ConcurrentHashMap<String, ConcurrentHashMap<String, Cookie>>();

        //将持久化的cookies缓存到内存中 即map cookies
        Map<String, ?> prefsMap = cookiePrefs.getAll();
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            String[] cookieNames = TextUtils.split((String) entry.getValue(), ",");
            for (String name : cookieNames) {
                String encodedCookie = cookiePrefs.getString(name, null);
                if (encodedCookie != null) {
                    Cookie decodedCookie = decodeCookie(encodedCookie);
                    if (decodedCookie != null) {
                        if (!cookies.containsKey(entry.getKey())) {
                            cookies.put(entry.getKey(), new ConcurrentHashMap<String, Cookie>());
                        }
                        cookies.get(entry.getKey()).put(name, decodedCookie);
                    }
                }
            }
        }
    }


    /**
     *cookieToken的获取
     */
    protected String getCookieToken(Cookie cookie) {
        return cookie.name() + "@" + cookie.domain();
    }

    /**
     *cookie的存储
     */
    public void add(Cookie cookie) {
        String name = getCookieToken(cookie);
        if (!cookies.containsKey("XX.com")) {
            cookies.put("XX.com", new ConcurrentHashMap<String, Cookie>());
        }
        cookies.get("XX.com").put(name, cookie);

        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        if (cookies.containsKey("XX.com")) {
            prefsWriter.putString("XX.com", TextUtils.join(",", cookies.get("XX.com").keySet()));
            prefsWriter.putString(name, encodeCookie(new SerializableHttpCookie(cookie)));
            prefsWriter.apply();
        }
    }

    public void add(HttpUrl url, Cookie cookie) {
        String name = getCookieToken(cookie);

        if (!cookies.containsKey(url.host())) {
            cookies.put(url.host(), new ConcurrentHashMap<String, Cookie>());
        }
        cookies.get(url.host()).put(name, cookie);

        //讲cookies持久化到本地
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        if (cookies.containsKey(url.host())) {
            prefsWriter.putString(url.host(), TextUtils.join(",", cookies.get(url.host()).keySet()));
            prefsWriter.putString(name, encodeCookie(new SerializableHttpCookie(cookie)));
            prefsWriter.apply();
        }
    }

    public List<Cookie> get(HttpUrl url) {
        ArrayList<Cookie> ret = new ArrayList<Cookie>();
        if (cookies.containsKey(url.host()))
            ret.addAll(cookies.get(url.host()).values());

        return ret;
    }


    public List<Cookie> get() {
        ArrayList<Cookie> ret = new ArrayList<Cookie>();
        if (cookies.containsKey("XX.com"))
            ret.addAll(cookies.get("XX.com").values());
        return ret;
    }


    public boolean removeAll() {
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.clear();
        prefsWriter.apply();

        cookies.clear();
        return true;
    }

    public boolean remove() {
        if (cookies.containsKey("XX.com")) {
            SharedPreferences.Editor prefsWriter = cookiePrefs.edit();

            for (Cookie cookie : cookies.get("XX.com").values()) {
                String name = getCookieToken(cookie);
                if (cookiePrefs.contains(name)) {
                    prefsWriter.remove(name);
                }
            }
            prefsWriter.remove("XX.com");
            prefsWriter.apply();
            cookies.get("XX.com").clear();
            cookies.remove("XX.com");
            return true;
        } else {
            return false;
        }
    }

    public boolean remove(HttpUrl url, Cookie cookie) {
        String name = getCookieToken(cookie);

        if (cookies.containsKey(url.host()) && cookies.get(url.host()).containsKey(name)) {

            cookies.get(url.host()).remove(name);

            SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
            if (cookiePrefs.contains(name)) {
                prefsWriter.remove(name);
            }
            prefsWriter.putString(url.host(), TextUtils.join(",", cookies.get(url.host()).keySet()));
            prefsWriter.apply();

            return true;
        } else {
            return false;
        }
    }


    /**
     * cookies 序列化成 string
     *
     * @param cookie 要序列化的cookie
     * @return 序列化之后的string
     */
    protected String encodeCookie(SerializableHttpCookie cookie) {
        if (cookie == null)
            return null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (IOException e) {
//            Log.d(LOG_TAG, "IOException in encodeCookie", e);
            return null;
        }

        return byteArrayToHexString(os.toByteArray());
    }

    /**
     * 将字符串反序列化成cookies
     *
     * @param cookieString cookies string
     * @return cookie object
     */
    protected Cookie decodeCookie(String cookieString) {
        byte[] bytes = hexStringToByteArray(cookieString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableHttpCookie) objectInputStream.readObject()).getCookies();
        } catch (IOException e) {
//            Log.d(LOG_TAG, "IOException in decodeCookie", e);
        } catch (ClassNotFoundException e) {
//            Log.d(LOG_TAG, "ClassNotFoundException in decodeCookie", e);
        }

        return cookie;
    }

    /**
     * 二进制数组转十六进制字符串
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    protected String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    /**
     * 十六进制字符串转二进制数组
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    protected byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}