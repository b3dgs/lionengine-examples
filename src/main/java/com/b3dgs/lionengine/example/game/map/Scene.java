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
package com.b3dgs.lionengine.example.game.map;

import com.b3dgs.lionengine.Context;
import com.b3dgs.lionengine.Resolution;
import com.b3dgs.lionengine.core.Engine;
import com.b3dgs.lionengine.core.Medias;
import com.b3dgs.lionengine.core.awt.KeyboardAwt;
import com.b3dgs.lionengine.core.sequence.Sequence;
import com.b3dgs.lionengine.game.feature.Camera;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.tile.map.MapTile;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileGame;
import com.b3dgs.lionengine.game.feature.tile.map.Minimap;
import com.b3dgs.lionengine.game.feature.tile.map.viewer.MapTileViewer;
import com.b3dgs.lionengine.game.feature.tile.map.viewer.MapTileViewerModel;
import com.b3dgs.lionengine.graphic.ColorRgba;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.io.awt.Keyboard;

/**
 * Game loop designed to handle our world.
 * 
 * @see com.b3dgs.lionengine.example.core.minimal
 */
class Scene extends Sequence
{
    private static final Resolution NATIVE = new Resolution(320, 240, 60);

    private final Services services = new Services();
    private final Camera camera = services.create(Camera.class);
    private final MapTile map = services.create(MapTileGame.class);
    private final MapTileViewer mapViewer = map.addFeatureAndGet(new MapTileViewerModel(services));
    private final Minimap minimap = new Minimap(map);

    private double speed;
    private int size;

    /**
     * Constructor.
     * 
     * @param context The context reference.
     */
    public Scene(Context context)
    {
        super(context, NATIVE);

        getInputDevice(Keyboard.class).addActionPressed(KeyboardAwt.ESCAPE, () -> end());
    }

    @Override
    public void load()
    {
        map.create(Medias.create("level.png"), 16, 16, 16);

        minimap.load();
        minimap.automaticColor();
        minimap.prepare();

        camera.setView(0, 0, getWidth(), getHeight(), getHeight());
        camera.setLimits(map);

        size = map.getWidth() - camera.getWidth();
        speed = 3;
    }

    @Override
    public void update(double extrp)
    {
        if (camera.getX() >= size)
        {
            camera.setLocation(size, 0);
            speed *= -1;
        }
        if (camera.getX() <= 0)
        {
            camera.setLocation(0, 0);
            speed *= -1;
        }
        camera.moveLocation(extrp, speed, 0.0);
    }

    @Override
    public void render(Graphic g)
    {
        g.clear(0, 0, getWidth(), getHeight());
        mapViewer.render(g);
        minimap.render(g);
        g.setColor(ColorRgba.RED);
        g.drawRect((int) (camera.getX() / map.getTileWidth()),
                   (int) (camera.getY() / map.getTileHeight()),
                   camera.getWidth() / map.getTileWidth(),
                   camera.getHeight() / map.getTileWidth(),
                   false);
    }

    @Override
    public void onTerminated(boolean hasNextSequence)
    {
        Engine.terminate();
    }
}
