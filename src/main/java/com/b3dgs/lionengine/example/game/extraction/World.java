/*
 * Copyright (C) 2013-2018 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.example.game.extraction;

import java.io.IOException;

import com.b3dgs.lionengine.Constant;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.awt.Mouse;
import com.b3dgs.lionengine.game.Cursor;
import com.b3dgs.lionengine.game.feature.DisplayableModel;
import com.b3dgs.lionengine.game.feature.Featurable;
import com.b3dgs.lionengine.game.feature.FeaturableModel;
import com.b3dgs.lionengine.game.feature.LayerableModel;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.WorldGame;
import com.b3dgs.lionengine.game.feature.tile.map.MapTile;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileGame;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileGroupModel;
import com.b3dgs.lionengine.game.feature.tile.map.pathfinding.MapTilePathModel;
import com.b3dgs.lionengine.game.feature.tile.map.pathfinding.Pathfindable;
import com.b3dgs.lionengine.game.feature.tile.map.viewer.MapTileViewerModel;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.Graphics;
import com.b3dgs.lionengine.graphic.Text;
import com.b3dgs.lionengine.graphic.drawable.Drawable;
import com.b3dgs.lionengine.graphic.drawable.Image;
import com.b3dgs.lionengine.io.FileReading;
import com.b3dgs.lionengine.io.FileWriting;

/**
 * World implementation.
 */
class World extends WorldGame
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

        camera.setView(72, 12, 240, 176, camera.getHeight());
        camera.setLocation(192, 96);

        final MapTile map = services.create(MapTileGame.class);
        map.addFeature(new MapTileViewerModel(services));
        map.create(Medias.create("map", "level.png"));
        map.addFeatureAndGet(new MapTileGroupModel()).loadGroups(Medias.create("map", "groups.xml"));
        map.addFeatureAndGet(new MapTilePathModel(services)).loadPathfinding(Medias.create("map", "pathfinding.xml"));
        camera.setLimits(map);
        handler.add(map);

        text.setLocation(74, 192);

        cursor.addImage(0, Medias.create("cursor.png"));
        cursor.addImage(1, Medias.create("cursor_order.png"));
        cursor.load();
        cursor.setGrid(map.getTileWidth(), map.getTileHeight());
        cursor.setInputDevice(mouse);
        cursor.setViewer(camera);

        final Image hudImage = Drawable.loadImage(Medias.create("hud.png"));
        hudImage.load();
        hudImage.prepare();

        final Featurable hud = new FeaturableModel();
        hud.addFeature(new LayerableModel(1));
        hud.addFeature(new DisplayableModel(hudImage::render));
        handler.add(hud);

        handler.add(factory.create(Button.EXTRACT));
        handler.add(factory.create(GoldMine.GOLD_MINE));

        final Peon peon = factory.create(Peon.MEDIA);
        peon.getFeature(Pathfindable.class).setLocation(13, 10);
        handler.add(peon);
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
        super.render(g);
        text.render(g);
        cursor.render(g);
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
}
