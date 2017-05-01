package com.example.tr.greenfuel.util;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tr.greenfuel.R;

public class MyTalk extends AppCompatActivity {
    TTSController ttsController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_talk);
        ttsController = new TTSController(this);
        ttsController.init();
        ttsController.startSpeaking("我们的项目是基于vsp分布的 油耗和尾气排放监测以及多交叉口路网上绿色路径规划和速度优化建议信息系统"+
                "  系统由 监测硬件 与手机客户端和后台管理系统组成"+
                "我们的监测硬件 由三轴加速度传感器 三轴方向传感器以及无线传输模块组成  获取到机动车的速度加速度以及运动方向等数据"+
                "  通过蓝牙4.0与智能手机建立数据传输通信"+
                "  再通过我们相关的模型 便可以计算出对应的油耗值和排放率"+
                "  除此之外  我们还可以通过汽车的OBD接口  来获取相关的数据  也可以通过蓝牙与智能手机建立数据通信  这种方式的精度比较高 对应的成本也比较高"+
                "    接下来介绍我们的手机APP部分，首先打开蓝牙，选择obd建立与监测硬件的通信   注册用户  输入车型相关数据  "+
                "登录"+"  点击路径   选择起点   我们在地图上任意选择一个地点  然后再选择一个终点"+
                "通过我们的路径规划算法，得到了 油耗最少 和 时间最短 以及怠速最少 三种出行方案"+
                "选择油耗最少方案"+"  我们在导航界面设置了两种策略 "+"  一种是有明确建议速度的导航策略  "+"  另一种是根据司机的驾驶速度给出速度分析的导航策略"+
                "   让司机根据自己的实际情况 变化驾驶方式"+"" +
                "  结束导航可查看本次导航的油耗和排放等信息  在查看详情界面"+
                "   用户可以查看历史总耗油量 排放量 以及总里程信息"+
                "   点击查看历史  用户可以按日 按月 按年 查看 历史油耗情况 以及 排放情况"+
                "    用户还可以对路径进行管理  和收藏相关的地点");
    }
}
