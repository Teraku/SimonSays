package com.hugo.simonsays;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo on 15-1-2017.
 */

/**
 * A queue containing Runnables. The next runnable will only run when the previous one is finished.
 */
public class RunQueue implements Runnable
{
    private List list = new ArrayList();

    public void queue(Runnable task)
    {
        list.add(task);
    }

    public void run()
    {
        while(list.size() > 0)
        {
            Runnable task = (Runnable)list.get(0);

            list.remove(0);
            task.run();
        }
    }
}