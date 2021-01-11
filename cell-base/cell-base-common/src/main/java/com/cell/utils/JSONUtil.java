package com.cell.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author joker
 * @date 创建时间：2018年5月14日 上午10:54:40
 */
public class JSONUtil
{
    private static Gson gson = null;

    static
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>()
        {
            private SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException
            {
                try
                {
                    return new Date(json.getAsJsonPrimitive().getAsLong());

                } catch (NumberFormatException e)
                {
                    // Get the json element as a String and parse it to get a Date
                    try
                    {
                        return dtf.parse(json.getAsString());
                    } catch (ParseException e2)
                    {
                        // Throw a JsonParseException in case of a parsing error
                        throw new JsonParseException(e);
                    }
                }

            }
        });
        gsonBuilder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
//		gsonBuilder.registerTypeAdapter(Date.class, new JsonSerializer<Date>()
//		{
//
//			@Override
//			public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException
//			{
//				
//			}
//
//		});
        gson = gsonBuilder.create();
    }

    public static <K, V> Map<K, V> json2Map(String json, Type type)
    {
        return gson.fromJson(json, type);
    }

    public static void main(String[] args)
    {
        String[] arr = new String[]
                {"1", "2"};
        System.out.println(arr);
    }

    public static String obj2Json(Object object)
    {
        String json = gson.toJson(object);

        return json;
    }

    public static <T> List<T> json2List(String json, Type type)
    {
        return gson.fromJson(json, type);
    }

//    public static <T> T json2Object(String json, Class<T> c)
//    {
//        T t = gson.fromJson(json, c);
//        return t;
//    }

    /**
     * 将对象转为格式化的json格式
     *
     * @param obj 要转json的对象
     * @return
     */
    public static String toFormattedJson(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.WriteNonStringKeyAsString, SerializerFeature.SkipTransientField,
                SerializerFeature.PrettyFormat);
    }

    /**
     * 将对象转为json格式的文件
     *
     * @param obj 要转json的对象
     * @return true if success.
     */
    public static boolean toFile(Object obj, String fileName) {
        String jsonString = toFormattedJson(obj);
        try {
            FileUtils.setFileText(fileName, jsonString);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 将对象转为json格式的文件
     *
     * @param obj 要转json的对象
     * @return true if success.
     */
    public static boolean jsonObjectToFile(JSONObject obj, String fileName) {
        SerializeWriter out = new SerializeWriter(new SerializerFeature[]{SerializerFeature.PrettyFormat, SerializerFeature.QuoteFieldNames});
        try {
            new JSONSerializer(out).write(obj);
            String jsonString = out.toString();
            try {
                FileUtils.setFileText(fileName, jsonString);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            }
        } finally {
            out.close();
        }
        return true;
    }

    /**
     * @param jsonData
     * @param t
     * @return
     * @Desc 描述：把一个json串的数组转成对象数组,这里的T 只能是一个对象，不能是基本类型
     * @author 王广帅
     * @Date 2017年3月24日 下午1:47:57
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> jsonToListObject(String jsonData, Class<T> t) {
        if (jsonData == null || jsonData.isEmpty()) {
            return new ArrayList<>();
        }
        List<T> objList = new ArrayList<>();
        JSONArray jsonArray = JSONArray.parseArray(jsonData);
        int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            if (t == String.class) {
                objList.add((T) jsonArray.get(i));
            } else {
                JSONObject jsonobj = (JSONObject) jsonArray.get(i);
                T obj = jsonobj.toJavaObject(t);
                objList.add(obj);
            }
        }
        return objList;

    }

    /**
     * 将json串转化为相应的对象
     *
     * @param json 要转化为对象的json串
     * @param t    对象的class类型
     * @return
     */
    public static <T> T json2Obj(String json, Class<T> t) {
        return JSON.parseObject(json, t);
    }
    public static <T> T json2Object(String json, Class<T> t) {
        return JSON.parseObject(json, t);
    }
    /**
     * 将json串转化为相应的对象
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static JSONObject jsonFileToJsonObj(String jsonFile)
            throws FileNotFoundException, UnsupportedEncodingException
    {
        return (JSONObject) JSON.parse(FileUtils.getFileText(jsonFile));
    }

    public static String toJsonString(Map<String, Object> map){
        return JSON.toJSONString(map,
                SerializerFeature.MapSortField,
                SerializerFeature.WriteMapNullValue
//                SerializerFeature.WriteNullStringAsEmpty,
//                SerializerFeature.WriteNullListAsEmpty
        );
    }
    public static String toJsonString(Object obj){
        if (obj != null && obj instanceof String) {
            return (String) obj;
        }
        return JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
    }


}
