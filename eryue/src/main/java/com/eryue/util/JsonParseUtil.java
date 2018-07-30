package com.eryue.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:Json 解析工具类
 * Created by 禽兽先生
 * Created on 2018/2/9
 */

public class JsonParseUtil {
    public static Map<String,EmojiEntity> parseEmojiMap(String json) {
        Map<String,EmojiEntity> emojiMap = new HashMap();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.optJSONArray("emoji_list");
            if (jsonArray != null) {
                String name;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                    if (jsonObject1 != null) {
                        EmojiEntity mEmojiEntity = new EmojiEntity();
                        name = jsonObject1.optString("name", "");
                        mEmojiEntity.setName(name);
                        mEmojiEntity.setUnicode(jsonObject1.optInt("unicode", 0));
                        emojiMap.put(name,mEmojiEntity);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return emojiMap;
    }
}
