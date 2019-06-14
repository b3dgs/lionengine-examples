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

import com.b3dgs.lionengine.network.message.NetworkMessage;
import com.b3dgs.lionengine.network.message.NetworkMessageChat;
import com.b3dgs.lionengine.network.message.NetworkMessageDecoder;

/**
 * The decoder implementation.
 */
class MessageDecoder implements NetworkMessageDecoder
{
    /**
     * Constructor.
     */
    public MessageDecoder()
    {
        super();
    }

    /*
     * MessageDecoder
     */

    @Override
    public NetworkMessage getNetworkMessageFromType(int type)
    {
        final TypeMessage message = TypeMessage.fromOrdinal(type);
        switch (message)
        {
            case MESSAGE_CHAT:
                return new NetworkMessageChat();
            default:
                return null;
        }
    }
}
