/*
 * Copyright (C) 2013-2019 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.example.game.transition;

import java.util.Arrays;

import com.b3dgs.lionengine.Constant;
import com.b3dgs.lionengine.Context;
import com.b3dgs.lionengine.Engine;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.Resolution;
import com.b3dgs.lionengine.UtilMath;
import com.b3dgs.lionengine.awt.Keyboard;
import com.b3dgs.lionengine.awt.KeyboardAwt;
import com.b3dgs.lionengine.awt.Mouse;
import com.b3dgs.lionengine.game.Cursor;
import com.b3dgs.lionengine.game.TextGame;
import com.b3dgs.lionengine.game.feature.Camera;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.tile.map.MapTile;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileGame;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileGroup;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileGroupModel;
import com.b3dgs.lionengine.game.feature.tile.map.transition.MapTileTransition;
import com.b3dgs.lionengine.game.feature.tile.map.transition.MapTileTransitionModel;
import com.b3dgs.lionengine.game.feature.tile.map.transition.TransitionsConfig;
import com.b3dgs.lionengine.game.feature.tile.map.viewer.MapTileViewer;
import com.b3dgs.lionengine.game.feature.tile.map.viewer.MapTileViewerModel;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.TextStyle;
import com.b3dgs.lionengine.graphic.engine.Sequence;

/**
 * Game loop designed to handle our little world.
 */
class Scene extends Sequence
{
    private static final Resolution NATIVE = new Resolution(320, 240, 60);
    private static final int TILE_GROUND = 18;
    private static final int TILE_TREE = 141;
    private static final int TILE_WATER = 198;

    private final TextGame text = new TextGame(Constant.FONT_DIALOG, 9, TextStyle.NORMAL);
    private final Services services = new Services();
    private final Camera camera = services.create(Camera.class);
    private final Cursor cursor = services.create(Cursor.class);
    private final MapTile map = services.create(MapTileGame.class);
    private final MapTileViewer mapViewer = map.addFeatureAndGet(new MapTileViewerModel(services));
    private final MapTileGroup mapGroup = map.addFeatureAndGet(new MapTileGroupModel());
    private final MapTileTransition mapTransition = map.addFeatureAndGet(new MapTileTransitionModel(services));
    private final Keyboard keyboard = getInputDevice(Keyboard.class);
    private final Mouse mouse = getInputDevice(Mouse.class);

    /**
     * Constructor.
     * 
     * @param context The context reference.
     */
    public Scene(Context context)
    {
        super(context, NATIVE);

        setSystemCursorVisible(false);
        keyboard.addActionPressed(KeyboardAwt.ESCAPE, this::end);
    }

    @Override
    public void load()
    {
        TransitionsConfig.exports(Medias.create("transitions.xml"),
                                  Arrays.asList(Medias.create("transitions.png")),
                                  Medias.create("sheets.xml"),
                                  Medias.create("groups.xml"));

        map.loadSheets(Medias.create("sheets.xml"));
        mapGroup.loadGroups(Medias.create("groups.xml"));
        mapTransition.loadTransitions(Medias.create("transitions.xml"));

        map.create(16, 16, 32, 32);
        for (int tx = 0; tx < 32; tx++)
        {
            for (int ty = 0; ty < 32; ty++)
            {
                map.setTile(tx, ty, TILE_GROUND);
            }
        }

        cursor.addImage(0, Medias.create("cursor.png"));
        cursor.load();
        cursor.setGrid(map.getTileWidth(), map.getTileHeight());
        cursor.setInputDevice(mouse);
        cursor.setViewer(camera);

        camera.setView(0, 0, getWidth(), getHeight(), getHeight());
        camera.setLimits(map);
    }

    @Override
    public void update(double extrp)
    {
        mouse.update(extrp);

        if (keyboard.isPressedOnce(KeyboardAwt.UP))
        {
            camera.moveLocation(extrp, 0, 64);
        }
        if (keyboard.isPressedOnce(KeyboardAwt.DOWN))
        {
            camera.moveLocation(extrp, 0, -64);
        }
        if (keyboard.isPressedOnce(KeyboardAwt.LEFT))
        {
            camera.moveLocation(extrp, -64, 0);
        }
        if (keyboard.isPressedOnce(KeyboardAwt.RIGHT))
        {
            camera.moveLocation(extrp, 64, 0);
        }

        if (cursor.getClick() > 0)
        {
            final int tx = map.getInTileX(cursor);
            final int ty = map.getInTileY(cursor);
            if (UtilMath.isBetween(tx, 0, map.getInTileWidth() - 1)
                && UtilMath.isBetween(ty, 0, map.getInTileHeight() - 1))
            {
                if (cursor.getClick() == 1)
                {
                    map.setTile(tx, ty, TILE_TREE);
                }
                else if (cursor.getClick() == 2)
                {
                    map.setTile(tx, ty, TILE_GROUND);
                }
                else if (cursor.getClick() == 3)
                {
                    map.setTile(tx, ty, TILE_WATER);
                }
                mapTransition.resolve(map.getTile(tx, ty));
            }
        }

        cursor.update(extrp);
        text.update(camera);
    }

    @Override
    public void render(Graphic g)
    {
        mapViewer.render(g);
        cursor.render(g);
    }

    @Override
    public void onTerminated(boolean hasNextSequence)
    {
        Engine.terminate();
    }
}
