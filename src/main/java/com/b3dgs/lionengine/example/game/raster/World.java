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
package com.b3dgs.lionengine.example.game.raster;

import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.Origin;
import com.b3dgs.lionengine.awt.Mouse;
import com.b3dgs.lionengine.awt.MouseAwt;
import com.b3dgs.lionengine.game.feature.Featurable;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.rasterable.Rasterable;
import com.b3dgs.lionengine.game.feature.tile.map.raster.MapTileRastered;
import com.b3dgs.lionengine.game.feature.tile.map.viewer.MapTileViewer;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.helper.WorldHelper;

/**
 * World representation.
 */
public class World extends WorldHelper
{
    private final MapTileViewer mapViewer = map.getFeature(MapTileViewer.class);
    private final MapTileRastered mapRaster = map.getFeature(MapTileRastered.class);
    private final Mouse mouse = getInputDevice(Mouse.class);

    private final Featurable featurable;
    private boolean useRaster = true;

    /**
     * Create world.
     * 
     * @param services The services reference.
     */
    public World(Services services)
    {
        super(services);

        map.create(Medias.create("level.png"), 16, 16, 16);

        camera.setView(source, 0, 0, Origin.TOP_LEFT);
        camera.setLimits(map);

        featurable = factory.create(Medias.create("Dino.xml"));
        handler.add(featurable);
    }

    @Override
    public void update(double extrp)
    {
        super.update(extrp);

        if (mouse.hasClickedOnce(MouseAwt.LEFT))
        {
            useRaster = !useRaster;
            mapViewer.clear();
            if (useRaster)
            {
                mapViewer.addRenderer(mapRaster);
            }
            else
            {
                mapViewer.removeRenderer(mapRaster);
                mapViewer.addRenderer(mapViewer);
            }
            featurable.getFeature(Rasterable.class).setEnabled(useRaster);
        }
    }

    @Override
    public void render(Graphic g)
    {
        g.clear(0, 0, source.getWidth(), source.getHeight());
        super.render(g);
    }
}
