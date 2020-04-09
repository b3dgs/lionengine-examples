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
package com.b3dgs.lionengine.example.game.pathfinding;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.awt.MouseAwt;
import com.b3dgs.lionengine.game.Cursor;
import com.b3dgs.lionengine.game.FeatureProvider;
import com.b3dgs.lionengine.game.feature.FeatureGet;
import com.b3dgs.lionengine.game.feature.FeatureInterface;
import com.b3dgs.lionengine.game.feature.FeatureModel;
import com.b3dgs.lionengine.game.feature.Routine;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Setup;
import com.b3dgs.lionengine.game.feature.tile.map.pathfinding.Pathfindable;

/**
 * Peon entity implementation.
 */
@FeatureInterface
class Peon extends FeatureModel implements Routine
{
    /** Media reference. */
    public static final Media MEDIA = Medias.create("Peon.xml");

    private final Cursor cursor = services.get(Cursor.class);

    @FeatureGet private Pathfindable pathfindable;

    /**
     * Create a peon.
     * 
     * @param services The services reference.
     * @param setup The setup reference.
     */
    public Peon(Services services, Setup setup)
    {
        super(services, setup);
    }

    @Override
    public void prepare(FeatureProvider provider)
    {
        super.prepare(provider);

        pathfindable.setRenderDebug(true);
        pathfindable.setLocation(13, 14);
    }

    @Override
    public void update(double extrp)
    {
        if (cursor.hasClickedOnce(MouseAwt.RIGHT))
        {
            pathfindable.setDestination(cursor);
        }
    }
}
