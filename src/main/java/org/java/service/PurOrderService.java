package org.java.service;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.java.dao.PurOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PurOrderService {

    @Autowired
    private PurOrderMapper purOrderMapper;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    public void createOrder(Map map) {

        //得到一个UUID值，作为业务表的主键，同时，也是BusinessKey
        String uuid = UUID.randomUUID().toString();

        //作为业务表的主键
        map.put("id", uuid);

        //启动流程实例
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myProcess", uuid);

        //向采购表中，添加一条采购记录
        map.put("createtime", new Date());
        map.put("processInstance_id", instance.getProcessInstanceId());

        purOrderMapper.createOrder(map);//添加业务数据


    }

    /**
     * 处理待办任务
     */
    public List<Map<String, Object>> queryPersonTask(String userId) {

        // 创建查询接口
        TaskQuery taskQuery = taskService.createTaskQuery();

        // 指定查询哪一个人员的任务
        taskQuery.taskAssignee(userId);

        // 查询需要办理的任务-----------------------------------------这里面，只包含工作流的信息，不包含业务数据
        List<Task> list = taskQuery.list();

        // 这里面，不仅要装载工作流的信息，还要包含业务 数据
        List<Map<String, Object>> taskList = new ArrayList<Map<String, Object>>();

        // 对任务循环迭代
        for (Task t : list) {
            // 根据任务id，找到该任务对应的流程实例
            ProcessInstance instance = runtimeService
                    .createProcessInstanceQuery()
                    .processInstanceId(t.getProcessInstanceId()).singleResult();

            // 找到该流程实例对应的buesinessKey
            String buesinessKey = instance.getBusinessKey();

            // 根据业务主键，查询出该主键，对应的业务数据
            Map<String, Object> m = purOrderMapper
                    .findByBusinessKey(buesinessKey);

            // 把工作流的信息，放到m中，一起返回
            m.put("taskId", t.getId());// 任务id
            m.put("taskName", t.getName());// 任务名称
            m.put("taskDef", t.getTaskDefinitionKey());// 任务执行到哪一节点
            m.put("createTime", t.getCreateTime());// 任务的开始时间

            taskList.add(m);
        }

        return taskList;
    }


}
