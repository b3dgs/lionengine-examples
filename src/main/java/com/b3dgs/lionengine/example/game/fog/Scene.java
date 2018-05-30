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
package com.b3dgs.lionengine.example.game.fog;

import java.util.ArrayList;
import java.util.Collection;

import com.b3dgs.lionengine.Context;
import com.b3dgs.lionengine.Engine;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.Resolution;
import com.b3dgs.lionengine.awt.Keyboard;
import com.b3dgs.lionengine.awt.KeyboardAwt;
import com.b3dgs.lionengine.awt.Mouse;
import com.b3dgs.lionengine.game.feature.Camera;
import com.b3dgs.lionengine.game.feature.ComponentDisplayable;
import com.b3dgs.lionengine.game.feature.ComponentRefreshable;
import com.b3dgs.lionengine.game.feature.Factory;
import com.b3dgs.lionengine.game.feature.Handler;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.tile.map.MapTile;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileGame;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileRendererModel;
import com.b3dgs.lionengine.game.feature.tile.map.transition.fog.FogOfWar;
import com.b3dgs.lionengine.game.feature.tile.map.transition.fog.Fovable;
import com.b3dgs.lionengine.game.feature.tile.map.viewer.MapTileViewer;
import com.b3dgs.lionengine.game.feature.tile.map.viewer.MapTileViewerModel;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.drawable.Drawable;
import com.b3dgs.lionengine.graphic.drawable.SpriteTiled;
import com.b3dgs.lionengine.graphic.engine.Sequence;

/**
 * Game loop designed to handle our little world.
 * 
 * @see com.b3dgs.lionengine.example.core.minimal
 */
class Scene extends Sequence
{
    /** Native resoltion. */
    static final Resolution NATIVE = new Resolution(320, 240, 60);

    private final Collection<Fovable> fovables = new ArrayList<>();
    private final FogOfWar fogOfWar = new FogOfWar();
    private final Services services = new Services();
    private final Handler handler = services.create(Handler.class);
    private final Mouse mouse = getInputDevice(Mouse.class);

    /**
     * Constructor.
     * 
     * @param context The context reference.
     */
    public Scene(Context context)
    {
        super(context, NATIVE);

        handler.addComponent(new ComponentRefreshable());
        handler.addComponent(new ComponentDisplayable());

        setSystemCursorVisible(false);
        getInputDevice(Keyboard.class).addActionPressed(KeyboardAwt.ESCAPE, this::end);
    }

    @Override
    public void load()
    {
        final Camera camera = services.create(Camera.class);
        camera.setView(0, 0, getWidth(), getHeight(), getHeight());
        camera.setLocation(0, 0);

        final MapTile map = services.create(MapTileGame.class);
        map.create(Medias.create("level.png"), 16, 16, 16);
        camera.setLimits(map);
        handler.add(map);

        final MapTileViewer mapViewer = new MapTileViewerModel(services);
        map.addFeature(mapViewer);
        mapViewer.addRenderer(new MapTileRendererModel());
        mapViewer.addRenderer(fogOfWar);

        final SpriteTiled hide = Drawable.loadSpriteTiled(Medias.create("hide.png"), 16, 16);
        hide.load();
        hide.prepare();

        final SpriteTiled fog = Drawable.loadSpriteTiled(Medias.create("fog.png"), 16, 16);
        fog.load();
        fog.prepare();

        fogOfWar.setTilesheet(hide, fog);
        fogOfWar.setEnabled(true, true);
        fogOfWar.create(map, Medias.create("fog.xml"));

        final Factory factory = services.create(Factory.class);
        final Peon peon = factory.create(Peon.MEDIA);
        handler.add(peon);
        fovables.add(peon.getFeature(Fovable.class));
    }

    @Override
    public void update(double extrp)
    {
        mouse.update(extrp);
        handler.update(extrp);
        fogOfWar.update(fovables);
    }

    @Override
    public void render(Graphic g)
    {
        handler.render(g);
    }

    @Override
    public void onTerminated(boolean hasNextSequence)
    {
        Engine.terminate();
    }
}
