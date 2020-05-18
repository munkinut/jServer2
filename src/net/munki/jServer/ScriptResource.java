/*
 * ScriptResource.java - provides a packaged set of resources and commands
 * for use by JavaBot's BeanShell scripts
 *
 * Copyright (C) 2002 by Warren Milburn
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
*/

package net.munki.jServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;


public class ScriptResource {

    final Logger log = Logger.getLogger(this.getClass().getName());

    private String name;
    private String description;
    private InputStream is;
    private OutputStream os;
    private PrintStream ps;

    /** Creates new ScriptResource */
    public ScriptResource(String name, String description, InputStream is, OutputStream os, PrintStream ps) {
        this.name = name;
        this.description = description;
        this.is = is;
        this.os = os;
        this.ps = ps;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public InputStream getIs() {
        return is;
    }

    public OutputStream getOs() {
        return os;
    }

    public PrintStream getPs() {
        return ps;
    }

    public void write(String message) {
        try {
            byte[] bytes = message.getBytes();
            os.write(bytes);
        }
        catch (IOException ioe){
            log.warning("IOException: " + ioe);
        }
    }


}
