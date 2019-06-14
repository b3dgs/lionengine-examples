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

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import com.b3dgs.lionengine.Align;
import com.b3dgs.lionengine.Constant;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.WorldGame;
import com.b3dgs.lionengine.graphic.ColorRgba;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.Graphics;
import com.b3dgs.lionengine.graphic.Text;
import com.b3dgs.lionengine.graphic.TextStyle;
import com.b3dgs.lionengine.io.FileReading;
import com.b3dgs.lionengine.io.FileWriting;
import com.b3dgs.lionengine.network.NetworkedWorld;
import com.b3dgs.lionengine.network.message.NetworkMessage;
import com.b3dgs.lionengine.network.purview.Networkable;
import com.b3dgs.lionengine.network.purview.NetworkableModel;

/**
 * World base implementation.
 * 
 * @param <N> The network type.
 */
abstract class World<N extends NetworkedWorld> extends WorldGame implements NetworkedWorld
{
    /** Text. */
    protected final Text text;
    /** Chat. */
    protected final Chat chat;
    /** Networkable model. */
    protected final Networkable networkableModel;
    /** Clients list. */
    protected final HashMap<Byte, Client> clients;
    /** Networkable world reference. */
    protected N networkedWorld;

    /**
     * @see WorldGame#WorldGame(Services)
     */
    public World(Services services)
    {
        super(services);

        text = Graphics.createText(Constant.FONT_SANS_SERIF, 10, TextStyle.NORMAL);
        chat = new Chat(this);
        networkableModel = new NetworkableModel();
        clients = new HashMap<>(2);
    }

    /**
     * Apply the specified command.
     * 
     * @param command The command.
     */
    public abstract void applyCommand(String command);

    /**
     * Get the client from its id.
     * 
     * @param id The client id.
     * @return The client name
     */
    public String getClientName(byte id)
    {
        return clients.get(Byte.valueOf(id)).getName();
    }

    /*
     * WorldGame
     */

    @Override
    public void render(Graphic g)
    {
        fill(g, ColorRgba.BLACK);
        text.draw(g, Scene.NATIVE.getWidth(), 0, Align.RIGHT, "Bandwidth=" + getBandwidth() + "byte/sec");
        text.draw(g, 0, 0, "Number of connected clients: " + clients.size());
        text.draw(g, 0, 12, "Clients:");
        int i = 0;
        for (final Client client : clients.values())
        {
            text.draw(g, 0, 24 + i * 12, " - " + client.getName());
            i++;
        }
        chat.render(g);
    }

    @Override
    protected void saving(FileWriting file) throws IOException
    {
        // Nothing to do
    }

    @Override
    protected void loading(FileReading file) throws IOException
    {
        // Nothing to do
    }

    /*
     * NetworkedWorld
     */

    @Override
    public void disconnect()
    {
        networkedWorld.disconnect();
    }

    @Override
    public void addNetworkable(Networkable networkable)
    {
        networkedWorld.addNetworkable(networkable);
    }

    @Override
    public void removeNetworkable(Networkable networkable)
    {
        networkedWorld.removeNetworkable(networkable);
    }

    @Override
    public void addMessage(NetworkMessage message)
    {
        networkedWorld.addMessage(message);
    }

    @Override
    public void addMessages(Collection<NetworkMessage> messages)
    {
        networkedWorld.addMessages(messages);
    }

    @Override
    public void sendMessages()
    {
        networkedWorld.sendMessages();
    }

    @Override
    public void receiveMessages()
    {
        networkedWorld.receiveMessages();
    }

    @Override
    public int getBandwidth()
    {
        return networkedWorld.getBandwidth();
    }

    /*
     * ClientConnectedListener
     */

    @Override
    public void notifyClientConnected(Byte id, String name)
    {
        final Client client = new Client();
        client.setName(name);
        client.setClientId(id);
        addNetworkable(client);
        addNetworkable(chat);
        clients.put(id, client);
        sendMessages();
    }

    @Override
    public void notifyClientDisconnected(Byte id, String name)
    {
        final Client client = clients.get(id);
        removeNetworkable(client);
        clients.remove(id);
    }

    @Override
    public void notifyClientNameChanged(Byte id, String name)
    {
        final Client client = clients.get(id);
        client.setName(name);
    }
}
