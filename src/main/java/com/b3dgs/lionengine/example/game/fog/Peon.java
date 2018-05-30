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

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.Origin;
import com.b3dgs.lionengine.Tick;
import com.b3dgs.lionengine.UtilRandom;
import com.b3dgs.lionengine.Viewer;
import com.b3dgs.lionengine.game.feature.DisplayableModel;
import com.b3dgs.lionengine.game.feature.FeaturableModel;
import com.b3dgs.lionengine.game.feature.LayerableModel;
import com.b3dgs.lionengine.game.feature.RefreshableModel;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Setup;
import com.b3dgs.lionengine.game.feature.Transformable;
import com.b3dgs.lionengine.game.feature.TransformableModel;
import com.b3dgs.lionengine.game.feature.tile.map.transition.fog.Fovable;
import com.b3dgs.lionengine.game.feature.tile.map.transition.fog.FovableModel;
import com.b3dgs.lionengine.graphic.drawable.Drawable;
import com.b3dgs.lionengine.graphic.drawable.SpriteAnimated;

/**
 * Peon implementation.
 */
class Peon extends FeaturableModel
{
    /** Media reference. */
    public static final Media MEDIA = Medias.create("Peon.xml");

    /**
     * Create a peon.
     * 
     * @param services The services reference.
     * @param setup The setup reference.
     */
    public Peon(Services services, Setup setup)
    {
        super();

        addFeature(new LayerableModel(1));

        final Transformable transformable = addFeatureAndGet(new TransformableModel());
        transformable.teleport(64, 64);

        final Fovable fovable = addFeatureAndGet(new FovableModel(services));
        fovable.setFov(3);

        final SpriteAnimated surface = Drawable.loadSpriteAnimated(setup.getSurface(), 15, 9);
        surface.setOrigin(Origin.BOTTOM_LEFT);
        surface.setFrameOffsets(8, 8);

        final Tick tick = new Tick();
        tick.start();

        final Viewer viewer = services.get(Viewer.class);

        addFeature(new RefreshableModel(extrp ->
        {
            surface.setLocation(viewer, transformable);
            tick.update(extrp);
            if (tick.elapsed(Scene.NATIVE.getRate()))
            {
                transformable.teleport(UtilRandom.getRandomInteger(19) * 16, UtilRandom.getRandomInteger(14) * 16);
                tick.restart();
            }
        }));

        addFeature(new DisplayableModel(surface::render));
    }
}
