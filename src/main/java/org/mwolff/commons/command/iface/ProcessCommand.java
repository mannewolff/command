/**
    Simple Command Framework.

    Framework for easy building software that fits the SOLID principles.
    @author Manfred Wolff <m.wolff@neusta.de>
    Download: https://github.com/simplecommand/SimpleCommandFramework


    Copyright (C) 2015 neusta software development

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
    USA
 */
package org.mwolff.commons.command.iface;

import java.util.ArrayList;
import java.util.List;

/**
 * Command interface for the command framework. Commands may act with generic
 * command contexts.
 */
public interface ProcessCommand<T extends ParameterObject> extends Command<T> {

    default void addTransition(final Transition transition) {
    }

    /**
     * Execute a command as a process. The result is the decision which process
     * step should be executed next.
     *
     * @return The next process step to execute. Null stops the process.
     */
    String executeAsProcess(String startCommand, T context);

    default String findNext(final String next) {
        return next;
    }

    /**
     * Gets the process ID of the command.
     *
     * @return The process ID
     */
    String getProcessID();

    default List<Transition> getTransitionList() {
        return new ArrayList<>();
    }

    /**
     * Sets the process ID of the command
     *
     * @param processID
     */
    void setProcessID(String processID);
}