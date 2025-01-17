package cn.chahuyun.files

import cn.chahuyun.entity.TimingTaskBase
import com.alibaba.fastjson2.JSONObject
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.buildMessageChain

object TimingData : AutoSavePluginData("TimingTask") {

    private val timingList : MutableMap<Int,String> by value()

    private var timingNumber : Int by value()

    /**
     * 添加定时任务
     * @author zhangjiaxing
     * @param base 定时任务实体
     * @date 2022/6/30 19:24
     */
    fun addTimingList(base: TimingTaskBase) {
        val toJSONString = JSONObject.toJSONString(base)
        val index = base.id
        timingList[index] = toJSONString
    }

    /**
     * 取定时任务的map
     * @author zhangjiaxing
     * @date 2022/6/30 19:25
     * @return 定时任务列表map
     */
    fun readTimingList(): MutableMap<Int,TimingTaskBase> {
        val rTimingList:MutableMap<Int,TimingTaskBase> = mutableMapOf()
        for (entry in timingList) {
            val base = JSONObject.parseObject(entry.value, TimingTaskBase::class.java)
            rTimingList[entry.key] = base
        }
        return rTimingList
    }

    /**
     * 删除定时任务
     * @author zhangjiaxing
     * @param index 定时器任务下标
     * @date 2022/6/30 19:26
     * @return 消息
     */
    fun deleteTimingList(index:Int):MessageChain{
        return if (timingList.containsKey(index)) {
            timingList.remove(index)
            buildMessageChain { +"定时器 $index 删除成功！" }
        }else{
            buildMessageChain { +"没有找到该定时器！" }
        }
    }

    /**
     * 获取最新定时任务编号
     * @author zhangjiaxing
     * @date 2022/6/30 19:26
     * @return 定时任务编号
     */
    fun getTimingNum():Int{
        val index = timingNumber
        timingNumber++
        return index
    }

    /**
     * 设置定时器状态
     * @author zhangjiaxing
     * @param key 定时器编号
     * @param boolean 新状态
     * @date 2022/7/1 15:56
     */
    fun setTimingState(key: Int, boolean: Boolean) {
        val body = timingList[key]
        val base = JSONObject.parseObject(body, TimingTaskBase::class.java)
        base.state = boolean
        timingList[key] = JSONObject.toJSONString(base)
    }

}