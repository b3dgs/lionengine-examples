/*
 * Copyright (C) 2013-2020 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.example.game.chat;

import com.b3dgs.lionengine.Context;
import com.b3dgs.lionengine.Engine;
import com.b3dgs.lionengine.Resolution;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.engine.Sequence;
import com.b3dgs.lionengine.graphic.engine.SourceResolutionProvider;

/**
 * Game loop designed to handle our little world.
 */
class Scene extends Sequence
{
    /** Native resolution. */
    static final Resolution NATIVE = new Resolution(320, 240, 60);

    /** World reference. */
    private final World<?> world;

    /**
     * Constructor.
     * 
     * @param context The context reference.
     */
    public Scene(Context context)
    {
        super(context, NATIVE);

        final Services services = new Services();
        services.add(new SourceResolutionProvider()
        {
            @Override
            public int getWidth()
            {
                return Scene.this.getWidth();
            }

            @Override
            public int getHeight()
            {
                return Scene.this.getHeight();
            }

            @Override
            public int getRate()
            {
                return Scene.this.getRate();
            }
        });
        final boolean server = true;
        if (server)
        {
            final WorldServer worldServer = new WorldServer(services);
            worldServer.startServer("Test", 7777, "Welcome !");
            world = worldServer;
        }
        else
        {
            final WorldClient worldClient = new WorldClient(services);
            addKeyListener(worldClient.getChat());
            worldClient.setName("Unnamed");
            worldClient.connect("127.0.0.1", 7777);
            world = worldClient;
        }
    }

    /*
     * Sequence
     */

    @Override
    public void load()
    {
        // Nothing to do
    }

    @Override
    public void update(double extrp)
    {
        world.receiveMessages();
        world.update(extrp);
        world.sendMessages();
    }

    @Override
    public void render(Graphic g)
    {
        world.render(g);
    }

    @Override
    public void onTerminated(boolean hasNextSequence)
    {
        world.disconnect();
        Engine.terminate();
    }
}
