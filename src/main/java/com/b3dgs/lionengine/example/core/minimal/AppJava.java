/*
 * Copyright (C) 2013-2017 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.b3dgs.lionengine.example.core.minimal;

import com.b3dgs.lionengine.Config;
import com.b3dgs.lionengine.Version;
import com.b3dgs.lionengine.awt.graphic.EngineAwt;
import com.b3dgs.lionengine.graphic.engine.Loader;

/**
 * Program starts here. When you start the JVM, ensure that this main function is called.
 */
public class AppJava
{
    /**
     * Main function called by the JVM.
     * 
     * @param args The arguments.
     */
    public static void main(String[] args)
    {
        // Start engine EngineAwt.start("AppJava", Version.create(1, 0, 0), "resources");
        // The engine is initialized with our parameters:
        // - The name of our program: "AppJava"
        // - The program version: "1.0.0"
        // - The main resources directory, relative to the execution directory: ./resources/
        // This mean that any resources loaded with Media.get(...) will have this directory as prefix.
        // To load resources from JAR, this alternative is preferred if external folder is not possible:
        EngineAwt.start(AppJava.class.getSimpleName(), Version.create(1, 0, 0), AppJava.class);

        // Final configuration (rendering will be scaled by 2 considering source and output resolution).
        // This is the final configuration container, including color depth and window mode.
        // Program starter, setup with our configuration. It just needs one sequence reference to start.
        Loader.start(Config.windowed(Scene.NATIVE.get2x()), Scene.class);
    }
}
