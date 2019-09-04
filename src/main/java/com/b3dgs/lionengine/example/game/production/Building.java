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
package com.b3dgs.lionengine.example.game.production;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.Origin;
import com.b3dgs.lionengine.Viewer;
import com.b3dgs.lionengine.game.feature.DisplayableModel;
import com.b3dgs.lionengine.game.feature.FeaturableModel;
import com.b3dgs.lionengine.game.feature.LayerableModel;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Setup;
import com.b3dgs.lionengine.game.feature.Transformable;
import com.b3dgs.lionengine.game.feature.TransformableModel;
import com.b3dgs.lionengine.game.feature.producible.Producer;
import com.b3dgs.lionengine.game.feature.producible.ProducibleListener;
import com.b3dgs.lionengine.game.feature.producible.ProducibleModel;
import com.b3dgs.lionengine.graphic.drawable.Drawable;
import com.b3dgs.lionengine.graphic.drawable.SpriteAnimated;

/**
 * Building implementation.
 */
class Building extends FeaturableModel implements ProducibleListener
{
    /** Farm media reference. */
    public static final Media FARM = Medias.create("Farm.xml");
    /** Barracks media reference. */
    public static final Media BARRACKS = Medias.create("Barracks.xml");

    private final Transformable transformable;
    private final SpriteAnimated surface;
    private final Viewer viewer;

    private boolean visible;

    /**
     * Create a building.
     * 
     * @param services The services reference.
     * @param setup The setup reference.
     */
    public Building(Services services, Setup setup)
    {
        super(services, setup);

        viewer = services.get(Viewer.class);

        transformable = addFeatureAndGet(new TransformableModel(services, setup));

        surface = Drawable.loadSpriteAnimated(setup.getSurface(), 2, 1);
        surface.setOrigin(Origin.TOP_LEFT);

        addFeatureAndGet(new LayerableModel(1));
        addFeature(new ProducibleModel(services, setup));
        addFeature(new DisplayableModel(g ->
        {
            if (visible)
            {
                surface.render(g);
            }
        }));
    }

    @Override
    public void notifyProductionStarted(Producer producer)
    {
        surface.setLocation(viewer, transformable);
        visible = true;
    }

    @Override
    public void notifyProductionProgress(Producer producer)
    {
        // Nothing to do
    }

    @Override
    public void notifyProductionEnded(Producer producer)
    {
        surface.setFrame(2);
    }
}
