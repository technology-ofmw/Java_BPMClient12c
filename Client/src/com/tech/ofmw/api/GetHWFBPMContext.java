package com.tech.ofmw.api;

import com.tech.ofmw.util.ListQueryColumnConstant;
import com.tech.ofmw.util.ProcessUtils;
import com.tech.ofmw.util.SearchCriteria;

import java.io.InputStream;

import java.util.List;

import oracle.bpel.services.bpm.common.IBPMContext;
import oracle.bpel.services.workflow.StaleObjectException;
import oracle.bpel.services.workflow.WorkflowException;
import oracle.bpel.services.workflow.client.util.WorkflowAttachmentUtil;
import oracle.bpel.services.workflow.query.ITaskQueryService;
import oracle.bpel.services.workflow.repos.Ordering;
import oracle.bpel.services.workflow.repos.Predicate;
import oracle.bpel.services.workflow.repos.TableConstants;
import oracle.bpel.services.workflow.task.model.AttachmentType;
import oracle.bpel.services.workflow.task.model.Task;
import oracle.bpel.services.workflow.verification.IWorkflowContext;

import oracle.bpm.services.common.exception.BPMException;


public class GetHWFBPMContext {
    
    private static ProcessUtils processUtils;

    public GetHWFBPMContext() {
        super();
    }

    public static void main(String[] args) throws WorkflowException, BPMException, Exception {
        processUtils = new ProcessUtils();
        
        IWorkflowContext wrkflwCtx = processUtils.getWrkflowCtx("weblogic", "welcome1");
        IBPMContext bpmCtx = processUtils.getBpmCtx(wrkflwCtx);
        
        System.out.println("Human Workflow service client ::" + processUtils.getHWFServiceClient());
        System.out.println("BPM service client ::" + processUtils.getBPMServiceClient());        
        System.out.println("IWorkflowContext :: "+wrkflwCtx);
        System.out.println("IBPMContext :: "+bpmCtx);       
        System.out.println("Task Query Service" + processUtils.getHWFServiceClient().getTaskQueryService());
        
    }

    /**
     * @Description list of 'Task' based on search criteria.
     * @param wrkflowCtx
     * @param searchCriteria 'ProcessName' and 'TaskName' is mandatory
     * @return List of 'Task' based on search criteria.
     * @throws WorkflowException
     *
     */
    public static List<Task> listTask(IWorkflowContext wrkflowCtx,
                                      SearchCriteria searchCriteria) throws WorkflowException {
        List<Task> tasks = null;

        if (searchCriteria != null) {
            String processName = searchCriteria.getProcessName();
            String taskName = searchCriteria.getTaskName();
            String state = searchCriteria.getTaskState();
            String processId = searchCriteria.getProcessInstanceId();

            Predicate predicate = processUtils.buildPredicate(processName, taskName, processId, state);
            ITaskQueryService.AssignmentFilter assignmentFilter = ITaskQueryService.AssignmentFilter.MY_AND_GROUP;
            List<String> columns = ListQueryColumnConstant.getBaseTaskColumnList();
            Ordering ordering = new Ordering(TableConstants.WFTASK_TITLE_COLUMN, true, true);
            tasks = processUtils.listTask(wrkflowCtx, columns, assignmentFilter, predicate, ordering);
        }

        return tasks;
    }

    /**
     * @param wrkFlwCtx
     * @param taskId
     * @return
     * @throws WorkflowException
     * @throws StaleObjectException
     * @throws Exception
     */
    public Task updateTask(IWorkflowContext wrkFlwCtx, String taskId) throws WorkflowException, StaleObjectException,
                                                                             Exception {
        Task documentamTask = null;
        Task task = processUtils.getTaskByTaskId(wrkFlwCtx, taskId);
        System.out.println("task" + task);

        if (task != null) {
            if (task.getAttachment().size() > 0) {
                List attachments = task.getAttachment();
                for (Object o : attachments) {
                    AttachmentType a = (AttachmentType) o;
                    String mime = a.getMimeType();
                    String name = a.getName();
                    String updatedBy = a.getUpdatedBy();
                    String taskID = a.getTaskId();
                    int version = a.getVersion();
                    java.util.Calendar c = a.getUpdatedDate();
                    String cid = a.getCorrelationId();
                    long size = a.getSize();
                    String attScope = a.getAttachmentScope();
                    String updatedByDispName = a.getUpdatedByDisplayName();


                    System.out.println(mime + " :: " + name + " :: " + updatedBy + " :: " + taskID + " :: " + version +
                                       " :: " + c.getInstance().getTime() + " :: " + cid + " :: " + size + " :: " +
                                       attScope + " :: " + updatedByDispName);


                    System.out.println("Context Token >> " + wrkFlwCtx.getToken());
                    System.out.println("SOA URL >> " +
                                       processUtils.getHWFServiceClient().getRuntimeConfigService().getServerURLFromFabricConfig());
                    System.out.println("taskId >> " + taskId);
                    System.out.println("Version >> " + a.getVersion());
                    System.out.println("name" + name);
                    InputStream is =
                        WorkflowAttachmentUtil.getAttachment(wrkFlwCtx,
                                                             processUtils.getHWFServiceClient().getRuntimeConfigService().getServerURLFromFabricConfig(),
                                                             taskID, a.getVersion(), a.getName(), null);


                }
            }
        }
        documentamTask = task;
        return documentamTask;
    }

    /**
     * @param wrkFlwCtx_
     * @param taskId
     * @param outcome
     * @return
     * @throws StaleObjectException
     * @throws WorkflowException
     * @throws Exception
     */
    public Task submitTask(IWorkflowContext wrkFlwCtx_, String taskId,
                           final String outcome) throws StaleObjectException, WorkflowException, Exception {
        Task riCompilationSubmitedTask = null;
        Task task = processUtils.updateTaskOutcome(wrkFlwCtx_, taskId, outcome);
        if (task != null) {
            riCompilationSubmitedTask = task;
        }

        return riCompilationSubmitedTask;
    }
}
