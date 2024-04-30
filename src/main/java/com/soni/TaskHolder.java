package com.soni;

import lombok.Getter;

import java.util.LinkedList;
import java.util.Queue;

public class TaskHolder
{
    @Getter
    private static TaskHolder instance;
    private Queue<Task> tasks;

    public TaskHolder()
    {
        instance = this;
        tasks = new LinkedList<>();
    }

    public void addTask(Task task)
    {
        tasks.add(task);
    }

    public Task getAndRemoveTask()
    {
        return tasks.poll();
    }

    public Task peekTask()
    {
        return tasks.peek();
    }

    public void popTask(){
        tasks.poll();
    }
}
