package com.bpanalytics.tasksmanagerdaemon.email;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import com.bpanalytics.tasksmanager.business.TasksBusiness;
import com.bpanalytics.tasksmanager.persistence.model.Task;

public class UnfinishedTasks implements Job {
	
	private TasksBusiness tasksBusiness;
	
	public void sendNotifications() {
		List<Task> unfinishedTasks = tasksBusiness.getUnfinishedTasks();
		
		for (Task task : unfinishedTasks) {
			Thread thread = new Thread(new EmailTask(task));
			thread.start();
		}
	}

	private static final String APPLICATION_CONTEXT_KEY = "applicationContext";

    public void execute(JobExecutionContext context) throws JobExecutionException {
        ApplicationContext appCtx = null;
		try {
			appCtx = getApplicationContext(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
        TasksBusiness tasksBusiness = (TasksBusiness) appCtx.getBean("tasksBusiness");
        List<Task> unfinishedTasks = tasksBusiness.getUnfinishedTasks();
		
		for (Task task : unfinishedTasks) {
			Thread thread = new Thread(new EmailTask(task));
			thread.start();
		}
    }

    private ApplicationContext getApplicationContext(JobExecutionContext context ) throws Exception {
        ApplicationContext appCtx = null;
        appCtx = (ApplicationContext)context.getScheduler().getContext().get(APPLICATION_CONTEXT_KEY);
        if (appCtx == null) {
            throw new JobExecutionException(
                    "No application context available in scheduler context for key \"" + APPLICATION_CONTEXT_KEY + "\"");
        }
        return appCtx;
    }
}