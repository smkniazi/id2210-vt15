/*
 * Copyright (C) 2009 Swedish Institute of Computer Science (SICS) Copyright (C)
 * 2009 Royal Institute of Technology (KTH)
 *
 * GVoD is free software; you can redistribute it and/or
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package se.kth.swim.simulation;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Assert;
import se.kth.swim.network.BasicAddress;
import se.kth.swim.network.BasicNattedAddress;
import se.sics.kompics.Kompics;
import se.sics.kompics.simulation.SimulatorScheduler;
import se.sics.p2ptoolbox.simulator.run.LauncherComp;

/**
 *
 * @author Alex Ormenisan <aaor@sics.se>
 */
public class SwimMain {

    public static void main(String[] args) {
        LauncherComp.scheduler = new SimulatorScheduler(); 
        /**
         * the 1234 is the simulation seed. The result of a scenario with the same seed should be deterministic. 
         * Should - unless you create Random() with no seed somewhere in your own classes.
         * If you create your own Random() they should use a seed based on the parents seed.
         * It can be the same seed or can be customized, eg: newSeed = a * oldSeed + b
         * When testing you code, you might want to run the scenario with different seeds.
         */
        LauncherComp.scenario = SwimScenario.simpleBoot(1234L);
        //
        try {
            LauncherComp.simulatorClientAddress = new BasicNattedAddress(new BasicAddress(InetAddress.getByName("127.0.0.1"), 30000, -1));
        } catch (UnknownHostException ex) {
            throw new RuntimeException("cannot create address for localhost");
        }

        Kompics.setScheduler(LauncherComp.scheduler);
        Kompics.createAndStart(LauncherComp.class, 1);
        try {
            Kompics.waitForTermination();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        Assert.assertEquals(null, SwimSimulationResult.failureCause);
    }
}
