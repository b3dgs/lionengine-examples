/*
 * Copyright (C) 2013-2017 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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

import com.b3dgs.lionengine.network.purview.NetworkableModel;

/**
 * Network client.
 */
class Client extends NetworkableModel
{
    /** Client name. */
    private String name;

    /**
     * Constructor.
     */
    public Client()
    {
        super();
    }

    /**
     * Set the client name.
     * 
     * @param name The client name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get the client name.
     * 
     * @return The client name.
     */
    public String getName()
    {
        return name;
    }
}
