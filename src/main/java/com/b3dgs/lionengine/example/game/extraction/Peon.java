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
package com.b3dgs.lionengine.example.game.extraction;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.Origin;
import com.b3dgs.lionengine.UtilMath;
import com.b3dgs.lionengine.Verbose;
import com.b3dgs.lionengine.Viewer;
import com.b3dgs.lionengine.game.Tiled;
import com.b3dgs.lionengine.game.feature.DisplayableModel;
import com.b3dgs.lionengine.game.feature.FeaturableModel;
import com.b3dgs.lionengine.game.feature.LayerableModel;
import com.b3dgs.lionengine.game.feature.RefreshableModel;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Setup;
import com.b3dgs.lionengine.game.feature.Transformable;
import com.b3dgs.lionengine.game.feature.TransformableModel;
import com.b3dgs.lionengine.game.feature.tile.map.extractable.Extractor;
import com.b3dgs.lionengine.game.feature.tile.map.extractable.ExtractorChecker;
import com.b3dgs.lionengine.game.feature.tile.map.extractable.ExtractorListener;
import com.b3dgs.lionengine.game.feature.tile.map.extractable.ExtractorModel;
import com.b3dgs.lionengine.game.feature.tile.map.pathfinding.Pathfindable;
import com.b3dgs.lionengine.game.feature.tile.map.pathfinding.PathfindableModel;
import com.b3dgs.lionengine.graphic.drawable.Drawable;
import com.b3dgs.lionengine.graphic.drawable.SpriteAnimated;

/**
 * Peon entity implementation.
 */
class Peon extends FeaturableModel implements ExtractorListener
{
    /** Media reference. */
    public static final Media MEDIA = Medias.create("Peon.xml");

    private final Pathfindable pathfindable;
    private boolean visible = true;

    /**
     * Create a peon.
     * 
     * @param services The services reference.
     * @param setup The setup reference.
     */
    public Peon(Services services, Setup setup)
    {
        super(services, setup);

        addFeatureAndGet(new LayerableModel(2));

        final SpriteAnimated surface = Drawable.loadSpriteAnimated(setup.getSurface(), 15, 9);
        surface.setOrigin(Origin.BOTTOM_LEFT);
        surface.setFrameOffsets(8, 8);

        final Extractor extractor = addFeatureAndGet(new ExtractorModel(services, setup));
        extractor.setExtractionSpeed(0.05);
        extractor.setDropOffSpeed(0.05);
        extractor.setCapacity(5);
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

        final Transformable transformable = addFeatureAndGet(new TransformableModel(services, setup));
        pathfindable = addFeatureAndGet(new PathfindableModel(services, setup));

        final Viewer viewer = services.get(Viewer.class);

        addFeature(new RefreshableModel(extrp ->
        {
            pathfindable.update(extrp);
            extractor.update(extrp);
            surface.setLocation(viewer, transformable);
        }));

        addFeature(new DisplayableModel(g ->
        {
            if (visible)
            {
                surface.render(g);
            }
        }));
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
        visible = false;
    }

    @Override
    public void notifyExtracted(String type, int currentQuantity)
    {
        Verbose.info("Extracted ! " + currentQuantity);
    }

    @Override
    public void notifyStartCarry(String type, int totalQuantity)
    {
        visible = true;
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
