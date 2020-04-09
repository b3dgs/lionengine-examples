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
package com.b3dgs.lionengine.example.game.extraction;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.UtilMath;
import com.b3dgs.lionengine.Verbose;
import com.b3dgs.lionengine.game.FeatureProvider;
import com.b3dgs.lionengine.game.Tiled;
import com.b3dgs.lionengine.game.feature.FeatureGet;
import com.b3dgs.lionengine.game.feature.FeatureInterface;
import com.b3dgs.lionengine.game.feature.FeatureModel;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Setup;
import com.b3dgs.lionengine.game.feature.rasterable.Rasterable;
import com.b3dgs.lionengine.game.feature.tile.map.extractable.Extractor;
import com.b3dgs.lionengine.game.feature.tile.map.extractable.ExtractorChecker;
import com.b3dgs.lionengine.game.feature.tile.map.extractable.ExtractorListener;
import com.b3dgs.lionengine.game.feature.tile.map.pathfinding.Pathfindable;

/**
 * Peon entity implementation.
 */
@FeatureInterface
class Peon extends FeatureModel implements ExtractorListener
{
    /** Media reference. */
    public static final Media MEDIA = Medias.create("Peon.xml");

    @FeatureGet private Rasterable rasterable;
    @FeatureGet private Extractor extractor;
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

        extractor.setChecker(new ExtractorChecker()
        {
            @Override
            public boolean canExtract()
            {
                return UtilMath.getDistance(pathfindable.getInTileX(),
                                            pathfindable.getInTileY(),
                                            extractor.getResourceLocation().getInTileX(),
                                            extractor.getResourceLocation().getInTileY()) < 2;
            }

            @Override
            public boolean canCarry()
            {
                return true;
            }
        });
    }

    @Override
    public void notifyStartGoToRessources(String type, Tiled resourceLocation)
    {
        pathfindable.setDestination(resourceLocation);
    }

    @Override
    public void notifyStartExtraction(String type, Tiled resourceLocation)
    {
        Verbose.info("Started !");
        rasterable.setVisibility(false);
    }

    @Override
    public void notifyExtracted(String type, int currentQuantity)
    {
        Verbose.info("Extracted ! " + currentQuantity);
    }

    @Override
    public void notifyStartCarry(String type, int totalQuantity)
    {
        rasterable.setVisibility(true);
    }

    @Override
    public void notifyStartDropOff(String type, int totalQuantity)
    {
        // Nothing to do
    }

    @Override
    public void notifyDroppedOff(String type, int droppedQuantity)
    {
        Verbose.info("done !" + droppedQuantity);
    }

    @Override
    public void notifyStopped()
    {
        // Nothing to do
    }
}
