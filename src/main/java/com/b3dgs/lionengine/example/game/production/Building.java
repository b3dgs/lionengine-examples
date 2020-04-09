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
package com.b3dgs.lionengine.example.game.production;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.game.FeatureProvider;
import com.b3dgs.lionengine.game.feature.Animatable;
import com.b3dgs.lionengine.game.feature.FeatureGet;
import com.b3dgs.lionengine.game.feature.FeatureInterface;
import com.b3dgs.lionengine.game.feature.FeatureModel;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Setup;
import com.b3dgs.lionengine.game.feature.producible.Producer;
import com.b3dgs.lionengine.game.feature.producible.Producible;
import com.b3dgs.lionengine.game.feature.producible.ProducibleListener;
import com.b3dgs.lionengine.game.feature.rasterable.Rasterable;
import com.b3dgs.lionengine.game.feature.tile.map.pathfinding.Pathfindable;

/**
 * Building implementation.
 */
@FeatureInterface
class Building extends FeatureModel implements ProducibleListener
{
    /** Farm media reference. */
    public static final Media FARM = Medias.create("Farm.xml");
    /** Barracks media reference. */
    public static final Media BARRACKS = Medias.create("Barracks.xml");

    @FeatureGet private Rasterable rasterable;
    @FeatureGet private Pathfindable pathfindable;
    @FeatureGet private Producible producible;
    @FeatureGet private Animatable animatable;

    /**
     * Create a building.
     * 
     * @param services The services reference.
     * @param setup The setup reference.
     */
    public Building(Services services, Setup setup)
    {
        super(services, setup);
    }

    @Override
    public void prepare(FeatureProvider provider)
    {
        super.prepare(provider);

        rasterable.setVisibility(false);
    }

    @Override
    public void notifyProductionStarted(Producer producer)
    {
        pathfindable.setLocation((int) producible.getX() / 16, (int) producible.getY() / 16);
        rasterable.setVisibility(true);
    }

    @Override
    public void notifyProductionProgress(Producer producer)
    {
        // Nothing to do
    }

    @Override
    public void notifyProductionEnded(Producer producer)
    {
        animatable.setFrame(2);
    }
}
