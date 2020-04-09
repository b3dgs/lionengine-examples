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
package com.b3dgs.lionengine.example.game.selector;

import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.Origin;
import com.b3dgs.lionengine.awt.Keyboard;
import com.b3dgs.lionengine.awt.KeyboardAwt;
import com.b3dgs.lionengine.awt.Mouse;
import com.b3dgs.lionengine.awt.MouseAwt;
import com.b3dgs.lionengine.game.Cursor;
import com.b3dgs.lionengine.game.feature.LayerableModel;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.collidable.selector.Selector;
import com.b3dgs.lionengine.graphic.ColorRgba;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.helper.WorldHelper;

/**
 * World representation.
 */
public class World extends WorldHelper
{
    private final Keyboard keyboard = getInputDevice(Keyboard.class);
    private final Mouse mouse = getInputDevice(Mouse.class);
    private final Cursor cursor = services.create(Cursor.class);

    /**
     * Create world.
     * 
     * @param services The services reference.
     */
    public World(Services services)
    {
        super(services);

        camera.setView(source, 0, 0, Origin.TOP_LEFT);
        camera.setLocation(276, 172);

        map.create(Medias.create("level.png"), 16, 16, 16);
        camera.setLimits(map);

        cursor.addImage(0, Medias.create("cursor.png"));
        cursor.load();
        cursor.setGrid(map.getTileWidth(), map.getTileHeight());
        cursor.setInputDevice(mouse);
        cursor.setViewer(camera);

        spawn(Peon.MEDIA, 432, 272);

        final Selector selector = new Selector(services);
        selector.addFeatureAndGet(new LayerableModel(3));
        selector.setClickableArea(camera);
        selector.setSelectionColor(ColorRgba.GREEN);
        selector.setClickSelection(MouseAwt.LEFT);
        handler.add(selector);
    }

    @Override
    public void update(double extrp)
    {
        mouse.update(extrp);
        cursor.update(extrp);

        super.update(extrp);

        if (keyboard.isPressed(KeyboardAwt.UP))
        {
            camera.moveLocation(extrp, 0, 16);
        }
        if (keyboard.isPressed(KeyboardAwt.DOWN))
        {
            camera.moveLocation(extrp, 0, -16);
        }
        if (keyboard.isPressed(KeyboardAwt.LEFT))
        {
            camera.moveLocation(extrp, -16, 0);
        }
        if (keyboard.isPressed(KeyboardAwt.RIGHT))
        {
            camera.moveLocation(extrp, 16, 0);
        }
    }

    @Override
    public void render(Graphic g)
    {
        super.render(g);

        cursor.render(g);
    }
}
