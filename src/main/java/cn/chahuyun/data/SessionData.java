package cn.chahuyun.data;

import cn.chahuyun.HuYanSession;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.console.data.Value;
import net.mamoe.mirai.console.data.java.JavaAutoSavePluginData;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.MiraiLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * SessionData
 * 对话消息数据
 *
 * @author Zhangjiaxing
 * @description
 * @date 2022/6/16 14:25
 */
public class SessionData extends JavaAutoSavePluginData {

    /**
     * 唯一构造
     */
    public static final SessionData INSTANCE = new SessionData();

    private MiraiLogger l = HuYanSession.INSTANCE.getLogger();

    /**
     * 文件名
     */
    public SessionData() {
        super("SessionData");
    }

    /**
     * list<SessionDataBase> 对话数据集合
     */
    private final Value<Map<String,String>> sessionMap = typedValue("sessionMap",createKType(Map.class,createKType(String .class),createKType(String.class)));

    public final Value<Map<String, Map<String, Object>>> mapValue = typedValue("mapvalue", createKType(
            Map.class,createKType(String.class), createKType(Map.class, createKType(String.class), createKType(Object.class))
    ));


    /**
     * @description 获取是sessiondatabase的map
     * @author zhangjiaxing
     * @date 2022/6/17 16:37
     * @return java.util.Map<java.lang.String,cn.chahuyun.data.SessionDataBase>
     */
    public Map<String, SessionDataBase> getSessionMap() {
        //从data中获取map
        Map<String, String> stringStringMap = INSTANCE.sessionMap.get();
        //创建一个用来返回的map
        Map<String, SessionDataBase> stringSessionDataBaseMap = new HashMap<String, SessionDataBase>();
        //进行遍历
        for (String s : stringStringMap.values()) {
            //序列化对象
            SessionDataBase dataBase = JSONObject.parseObject(s, SessionDataBase.class);
            //添加到返回map中
            stringSessionDataBaseMap.put(dataBase.getKey(), dataBase);
        }
        return stringSessionDataBaseMap;
    }


    /**
     * @description 用于修改本地数据的操作方法
     * @author zhangjiaxing
     * @param s + or -
     * @param base 基本对话
     * @date 2022/6/20 8:35
     * @return net.mamoe.mirai.message.data.MessageChain
     */
    public MessageChain setSessionMap(String s ,SessionDataBase base) {
        Map<String, String> stringStringMap = this.sessionMap.get();
        if (s.equals("+")) {
            String jsonString = JSONObject.toJSONString(base);
            stringStringMap.put(base.getKey(), jsonString);
            return new MessageChainBuilder().append("学习成功! ")
                    .append(MiraiCode.deserializeMiraiCode(base.getKey()))
                    .append(" -> ")
                    .append(MiraiCode.deserializeMiraiCode(base.getValue()))
                    .build();
        } else {
            if (stringStringMap.containsKey(base.getKey())) {
                stringStringMap.remove(base.getKey());
                return new MessageChainBuilder().append("删除成功! ")
                        .append(MiraiCode.deserializeMiraiCode(base.getKey()))
                        .build();
            }else {
                return new MessageChainBuilder().append("没有找到能够删除的东西啦~")
                        .build();
            }
        }
    }


}