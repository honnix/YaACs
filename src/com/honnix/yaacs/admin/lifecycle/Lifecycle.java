/**
 * LifeCycle.java
 * 
 * Copyright : (C) 2008 by Honnix
 * Email     : hxliang1982@gmail.com
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.honnix.yaacs.admin.lifecycle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.honnix.yaacs.adapter.AdapterServer;
import com.honnix.yaacs.constant.ACPropertiesConstant;
import com.honnix.yaacs.core.server.ACServer;
import com.honnix.yaacs.core.server.ACServerImpl;
import com.honnix.yaacs.exception.ACException;
import com.honnix.yaacs.util.PropertiesLoader;

/**
 *
 */
public final class Lifecycle
{

    class LifecycleChecker
        extends TimerTask
    {

        public LifecycleChecker()
        {
            super();
        }

        @Override
        public void run()
        {
            File file = new File(ACPropertiesConstant.RUN_FILE_PATH);

            if (!file.exists())
            {
                stop();
            }
        }

    }

    private static final Log LOG = LogFactory.getLog(Lifecycle.class);

    private static final long PERIOD = 1000;

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        Lifecycle mainEntrance = null;

        try
        {
            mainEntrance = new Lifecycle();
        }
        catch (ACException e)
        {
            LOG.fatal("Error starting YaACs.", e);

            System.exit(-1);
        }

        mainEntrance.start();

        LOG.warn("YaACs starts successfully.");

        while (true)
        {
            try
            {
                Thread.sleep(PERIOD * 10);
            }
            catch (InterruptedException e)
            {
                LOG.warn("YaACs stops successfully.");

                break;
            }
        }
    }

    private ACServer acServer;

    private Map<String, AdapterServer> adapterServerMap;

    private LifecycleChecker lifeCycleChecker;

    private Timer timer;

    private Thread mainThread;

    private Lifecycle()
        throws ACException
    {
        super();

        acServer = new ACServerImpl();
        lifeCycleChecker = new LifecycleChecker();
        timer = new Timer("LifeCycleChecker", true);
        mainThread = Thread.currentThread();
    }

    private void createAdapterServers()
    {
        Properties props =
                PropertiesLoader
                        .loadProperties(ACPropertiesConstant.ADAPTER_PROPERTIES_FILE_NAME);

        adapterServerMap = new HashMap<String, AdapterServer>(props.size());

        Enumeration<?> propEnum = props.propertyNames();

        while (propEnum.hasMoreElements())
        {
            String key = (String) propEnum.nextElement();
            String value = props.getProperty(key);

            try
            {
                AdapterServer adapterServer =
                        (AdapterServer) Class.forName(value).getConstructor(
                                ACServer.class).newInstance(acServer);

                adapterServerMap.put(key, adapterServer);
            }
            catch (Exception e)
            {
                StringBuilder sb =
                        new StringBuilder("Error create adapter server \"")
                                .append(value).append("\".");

                LOG.error(sb.toString(), e);
            }
        }
    }

    private void createRunFile()
    {
        File file = new File(ACPropertiesConstant.RUN_FILE_PATH);

        if (file.exists())
        {
            file.delete();

            StringBuilder sb =
                    new StringBuilder("Strange! yaacs.run file exists.")
                            .append(" Anyway, we will start YaACs.");

            LOG.warn(sb.toString());
        }
        try
        {
            PrintWriter pw = new PrintWriter(file);

            pw.println("Delete this file to stop YaACs.");
            pw.flush();
        }
        catch (FileNotFoundException e)
        {
            LOG.fatal("Error starting YaACs.", e);

            System.exit(1);
        }
    }

    private void start()
    {
        createRunFile();

        acServer.start();
        createAdapterServers();
        startAdapterServers();
        timer.schedule(lifeCycleChecker, 0, PERIOD);
    }

    private void startAdapterServers()
    {
        for (Iterator<AdapterServer> iterator =
                adapterServerMap.values().iterator(); iterator.hasNext();)
        {
            iterator.next().start();
        }
    }

    private void stop()
    {
        timer.cancel();
        stopAdapterServers();
        acServer.stop();

        mainThread.interrupt();
    }

    private void stopAdapterServers()
    {
        for (Iterator<AdapterServer> iterator =
                adapterServerMap.values().iterator(); iterator.hasNext();)
        {
            iterator.next().stop();
        }
    }

}
