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

import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.WorldGame;
import com.b3dgs.lionengine.network.NetworkedWorldModelServer;
import com.b3dgs.lionengine.network.NetworkedWorldServer;

/**
 * World server implementation.
 */
class WorldServer extends World<NetworkedWorldModelServer> implements NetworkedWorldServer
{
    /**
     * @see WorldGame#WorldGame(Services)
     */
    public WorldServer(Services services)
    {
        super(services);

        networkedWorld = new NetworkedWorldModelServer(new MessageDecoder());
        networkedWorld.addListener(this);
        networkedWorld.addListener(chat);
    }

    /*
     * World
     */

    @Override
    public void applyCommand(String command)
    {
        // Nothing to do
    }

    /*
     * NetworkedWorld
     */

    @Override
    public void startServer(String name, int port, String messageOfTheDay)
    {
        networkedWorld.startServer(name, port, messageOfTheDay);
    }
}
