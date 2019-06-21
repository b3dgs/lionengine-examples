/*
 * Copyright (C) 2013-2019 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.b3dgs.lionengine.example.game.factory;

import com.b3dgs.lionengine.Engine;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.Verbose;
import com.b3dgs.lionengine.Version;
import com.b3dgs.lionengine.awt.graphic.EngineAwt;
import com.b3dgs.lionengine.game.feature.Factory;
import com.b3dgs.lionengine.game.feature.Services;

/**
 * Main class.
 */
public class AppFactory
{
    /**
     * Main.
     * 
     * @param args The arguments.
     */
    public static void main(String[] args)
    {
        EngineAwt.start(AppFactory.class.getSimpleName(), Version.create(1, 0, 0), AppFactory.class);

        final Services services = new Services();
        final Factory factory = new Factory(services);
        final Object param = new Object();

        // Define the context and add the parameter as a service
        services.add(param);

        // Create types
        final BaseType flyMachine = factory.create(Medias.create("FlyMachine.xml"));
        final BaseType groundTruck = factory.create(Medias.create("GroundTruck.xml"));

        Verbose.info(flyMachine.toString());
        Verbose.info(groundTruck.toString());

        // Parameters are the same
        Verbose.info(flyMachine.getParam().toString());
        Verbose.info(groundTruck.getParam().toString());

        Engine.terminate();
    }
}
