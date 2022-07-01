package cn.chahuyun.eventManager;

import cn.chahuyun.HuYanSession;
import cn.chahuyun.groupManager.ScopeGroupManager;
import cn.chahuyun.timingManager.TimingManager;
import cn.chahuyun.utils.MessageUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.utils.MiraiLogger;

import java.util.regex.Pattern;

/**
 * FriendMessageEventManager
 *
 * @author Zhangjiaxing
 * @description 好友消息处理
 * @date 2022/6/28 17:31
 */
public class FriendMessageEventManager {

    public static final FriendMessageEventManager INSTANCE = new FriendMessageEventManager();
    private MiraiLogger l = HuYanSession.INSTANCE.getLogger();

    /**
     * 接收到所有好友发送的消息处理
     * @author zhangjiaxing
     * @param event 消息事件
     * @date 2022/6/29 8:40
     */
    public void isMessageType(MessageEvent event) {
        Contact subject = event.getSubject();
        String code = event.getMessage().contentToString();

        l.info("好友消息");

        //定时任务
        if (Pattern.matches("添加定时任务|%ds", code)) {
            MessageUtil.INSTANCE.addTiming(event,0);
        }else if (Pattern.matches("[+-]ds[:：]\\d+", code)) {
            TimingManager.INSTANCE.operateTiming(event);
        } else if (Pattern.matches("ds[:：](\\d+)?", code)) {
            TimingManager.INSTANCE.checkTiming(event);
        }

        l.info(code);
        l.info("pattern-"+Pattern.matches("^[+-]group[:：](\\d+)( \\d+)*", code));
        //群组
        if (Pattern.matches("[+-]group[:：](\\d+)( \\d+)*", code)) {
            l.info("code1->"+code.startsWith("+"));
            if (code.startsWith("+")) {
                ScopeGroupManager.INSTANCE.addScopeGroup(event);
            } else {
                ScopeGroupManager.INSTANCE.delScopeGroup(event);
            }
        }else if (Pattern.matches("group[:：]", code)) {
            ScopeGroupManager.INSTANCE.checkScopeGroup(event);
        }




    }





}