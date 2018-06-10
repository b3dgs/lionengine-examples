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
package com.b3dgs.lionengine.example.game.world;

import java.io.IOException;

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

/**
 * World implementation.
 */
class World extends WorldGame
{
    private final Text text = Graphics.createText(Constant.FONT_SERIF, 12, TextStyle.NORMAL);
    private String str;

    /**
     * Create a new world.
     * 
     * @param services The services reference.
     */
    public World(Services services)
    {
        super(services);
    }

    @Override
    public void render(Graphic g)
    {
        fill(g, ColorRgba.BLACK);
        text.draw(g, 10, 10, str);
    }

    @Override
    protected void saving(FileWriting file) throws IOException
    {
        file.writeString("world");
    }

    @Override
    protected void loading(FileReading file) throws IOException
    {
        str = file.readString();
    }
}
