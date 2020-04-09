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
package com.b3dgs.lionengine.example.game.action;

import com.b3dgs.lionengine.Constant;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.awt.Mouse;
import com.b3dgs.lionengine.game.Cursor;
import com.b3dgs.lionengine.game.feature.Factory;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.Graphics;
import com.b3dgs.lionengine.graphic.Text;
import com.b3dgs.lionengine.helper.WorldHelper;

/**
 * World representation.
 */
public class World extends WorldHelper
{
    private final Text text = services.add(Graphics.createText(9));
    private final Cursor cursor = services.create(Cursor.class);
    private final Mouse mouse = getInputDevice(Mouse.class);

    /**
     * Create world.
     * 
     * @param services The services reference.
     */
    public World(Services services)
    {
        super(services);

        text.setLocation(74, 192);

        cursor.addImage(0, Medias.create("cursor.png"));
        cursor.load();
        cursor.setInputDevice(mouse);

        final Factory factory = services.create(Factory.class);
        handler.add(factory.create(Medias.create("Hud.xml")));
    }

    @Override
    public void update(double extrp)
    {
        text.setText(Constant.EMPTY_STRING);
        mouse.update(extrp);
        cursor.update(extrp);

        super.update(extrp);
    }

    @Override
    public void render(Graphic g)
    {
        g.clear(0, 0, source.getWidth(), source.getHeight());
        super.render(g);

        text.render(g);
        cursor.render(g);
    }
}
