package org.rutusoft.camunda;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.community.process_test_coverage.spring_test.platform7.ProcessEngineCoverageConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * Test case starting an in-memory database-backed Process Engine.
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@Import(ProcessEngineCoverageConfiguration.class)
public class ProcessUnitTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(ProcessUnitTest.class);

  @Autowired
  private ProcessEngine processEngine;
  @Autowired
  private RepositoryService repositoryService;
  @Autowired
  private TaskService taskService;
  @Autowired
  private HistoryService historyService;
  @Autowired
  private RuntimeService runtimeService;
  public static String processInstanceId = "";

  @BeforeEach
  public void setup() {
    init(processEngine);
  }

  @Order(1)
  @Test
  @Deployment(resources = "process.bpmn") // only required for process test coverage
  public void test_1_startProcess() {
    LOGGER.info("Executing test case to start workflow");
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.WORKFLOW_KEY);
    LOGGER.info("Process started with process instance id : ", processInstance.getProcessInstanceId());
    assertNotNull(processInstance);

    processInstanceId = processInstance.getProcessInstanceId();
    assertNotNull(processInstance.getId());

    Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
    String taskId = task.getId();

  }

  @Test
  @Order(2)
  public void test_2_CompleteUserTask_CIBIL_Score() {
    LOGGER.info("Executing test case to complete CIBIL score task : "+processInstanceId);
    LOGGER.info("Executing test cases to verify intake review and building review tasks process varables");
    //ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

    Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
    String taskId = task.getId();
    assertEquals(Constants.TASK_NAME_CIBIL_SCORE, task.getName());

    Map taskVriables = new HashMap();
    taskService.complete(taskId, taskVriables);

    Long taskCount = taskService.createTaskQuery().taskName(Constants.TASK_NAME_CIBIL_SCORE).processInstanceId(processInstanceId).count();
    assertEquals(0, taskCount);

  }

  @Test
  @Order(3)
  public void test_3_CompleteUserTask_CIBIL_Score() {
    LOGGER.info("Executing test case to complete CIBIL score task : "+processInstanceId);
    LOGGER.info("Executing test cases to verify intake review and building review tasks process varables");
    //ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

    Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
    String taskId = task.getId();
    assertEquals(Constants.TASK_NAME_EMPLOYEE_STATUS, task.getName());

    Map taskVriables = new HashMap();
    taskService.complete(taskId, taskVriables);

    Long taskCount = taskService.createTaskQuery().taskName(Constants.TASK_NAME_EMPLOYEE_STATUS).processInstanceId(processInstanceId).count();
    assertEquals(0, taskCount);

  }

}
