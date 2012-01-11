/**
 * ACHttpServer.java
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
package com.honnix.yaacs.adapter.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Server;

import com.honnix.yaacs.adapter.AdapterServer;
import com.honnix.yaacs.adapter.http.constant.ACHttpPropertiesConstant;
import com.honnix.yaacs.core.server.ACServer;

/**
 *
 */
public class ACHttpServer
    implements AdapterServer
{

    private static final Log LOG = LogFactory.getLog(ACHttpServer.class);

    private Server server;

    public ACHttpServer(ACServer acServer)
    {
        super();

        server = new Server(ACHttpPropertiesConstant.HTTP_LISTENING_PORT);
        server.setHandler(new ACRequestHandler(acServer));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.honnix.yaacs.adapter.AdapterServer#start()
     */
    public void start()
    {
        try
        {
            if (server.isStopped())
            {
                server.start();

                while (true)
                {
                    if (server.isStarted())
                    {
                        break;
                    }

                    Thread.sleep(5);
                }
            }
        }
        catch (Exception e)
        {
            LOG.error("Error starting HTTP server.", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.honnix.yaacs.adapter.AdapterServer#stop()
     */
    public void stop()
    {
        try
        {
            if (!server.isStopped())
            {
                server.stop();
                server.join();
            }
        }
        catch (Exception e)
        {
            LOG.error("Error stopping HTTP server.", e);
        }
    }

}
