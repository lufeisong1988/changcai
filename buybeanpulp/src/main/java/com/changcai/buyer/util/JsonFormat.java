package com.changcai.buyer.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wlv on 15-8-19.
 */
public class JsonFormat {

    private static JsonParser jsonParser = new JsonParser();

    public static JsonObject String2Object(String strJson) {
        return jsonParser.parse(strJson).getAsJsonObject();
    }

    public static JsonArray String2Array(String strJson) {
        return jsonParser.parse(strJson).getAsJsonArray();
    }

    public static Map populate(JsonObject jsonObject, Map map) {
        for (Iterator iterator = jsonObject.entrySet().iterator(); iterator
                .hasNext(); ) {
            String entryStr = String.valueOf(iterator.next());
            String key = entryStr.substring(0, entryStr.indexOf("="));
            String value = entryStr.substring(entryStr.indexOf("=") + 1,
                    entryStr.length());
            if (jsonObject.get(key).getClass().equals(JsonObject.class)) {
                HashMap _map = new HashMap();
                map.put(key, _map);
                populate(jsonObject.get(key).getAsJsonObject(), ((Map) (_map)));
            } else if (jsonObject.get(key).getClass().equals(JsonArray.class)) {
                ArrayList list = new ArrayList();
                map.put(key, list);
                populateArray(jsonObject.get(key).getAsJsonArray(), list);
            } else {
                try {
                    map.put(key, jsonObject.get(key).getAsString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return map;
    }

    private static void populateArray(JsonArray jsonArray, List list) {
        for (int i = 0; i < jsonArray.size(); i++)
            if (jsonArray.get(i).getClass().equals(JsonArray.class)) {
                ArrayList _list = new ArrayList();
                list.add(_list);
                populateArray(jsonArray.get(i).getAsJsonArray(), _list);
            } else if (jsonArray.get(i).getClass().equals(JsonObject.class)) {
                HashMap _map = new HashMap();
                list.add(_map);
                populate(jsonArray.get(i).getAsJsonObject(), _map);
            } else {
                list.add(jsonArray.get(i));
            }
    }
}
