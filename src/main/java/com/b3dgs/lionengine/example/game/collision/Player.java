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
package com.b3dgs.lionengine.example.game.collision;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.game.feature.FeaturableModel;
import com.b3dgs.lionengine.game.feature.LayerableModel;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Setup;
import com.b3dgs.lionengine.game.feature.TransformableModel;
import com.b3dgs.lionengine.game.feature.body.BodyModel;
import com.b3dgs.lionengine.game.feature.tile.map.collision.TileCollidableModel;

/**
 * Player description implementation.
 */
class Player extends FeaturableModel
{
    /** Media reference. */
    public static final Media MEDIA = Medias.create("Player.xml");

    /**
     * Constructor.
     * 
     * @param services The services reference.
     * @param setup The setup reference.
     */
    public Player(Services services, Setup setup)
    {
        super(services, setup);

        addFeatureAndGet(new LayerableModel(1));
        addFeature(new TransformableModel(services, setup));
        addFeature(new BodyModel(services, setup));
        addFeature(new TileCollidableModel(services, setup));

        final PlayerModel model = new PlayerModel(setup);
        addFeature(new PlayerController(services, setup, model));
        addFeature(new PlayerUpdater(services, setup, model));
        addFeature(new PlayerRenderer(services, setup, model));
    }
}
