package ir.pearly.assistant;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 1/22/2018.
 */

public class AlaviJson  {

    public static ArrayList string_to_json(String sdata) {
        ArrayList list = new ArrayList();
        JSONArray array = null;
        try {
            array = new JSONArray(sdata);
            list = toList(array);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList toList(JSONArray array) throws JSONException {
        ArrayList list = new ArrayList();
        for (int i = 0; i < array.length(); i++) {
            list.add(fromJson_p(array.get(i)));
        }
        return list;
    }

    public static Map<String, Object> toMap(Object jsonobject) {
        JSONObject object = (JSONObject) jsonobject;
        Map<String, Object> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            try {
                map.put(key, fromJson_p(object.get(key)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return map;
    }


    private static Object fromJson_p(Object json) throws JSONException {
        if (json == JSONObject.NULL) {
            return null;
        } else if (json instanceof JSONObject) {
            return toMap((JSONObject) json);
        } else if (json instanceof JSONArray) {
            return toList((JSONArray) json);
        } else {
            return json;
        }
    }


}
