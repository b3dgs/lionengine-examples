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
package com.b3dgs.lionengine.example.game.collision;

import com.b3dgs.lionengine.game.DirectionNone;
import com.b3dgs.lionengine.game.FeatureProvider;
import com.b3dgs.lionengine.game.Force;
import com.b3dgs.lionengine.game.feature.FeatureGet;
import com.b3dgs.lionengine.game.feature.FeatureInterface;
import com.b3dgs.lionengine.game.feature.FeatureModel;
import com.b3dgs.lionengine.game.feature.Refreshable;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Transformable;
import com.b3dgs.lionengine.game.feature.body.Body;
import com.b3dgs.lionengine.game.feature.tile.Tile;
import com.b3dgs.lionengine.game.feature.tile.map.collision.Axis;
import com.b3dgs.lionengine.game.feature.tile.map.collision.CollisionCategory;
import com.b3dgs.lionengine.game.feature.tile.map.collision.TileCollidable;
import com.b3dgs.lionengine.game.feature.tile.map.collision.TileCollidableListener;

/**
 * Player updating implementation.
 */
@FeatureInterface
class PlayerUpdater extends FeatureModel implements Refreshable, TileCollidableListener
{
    private static final double GRAVITY = 5.5;

    private final Force movement;
    private final Force jump;

    @FeatureGet private Body body;
    @FeatureGet private Transformable transformable;
    @FeatureGet private PlayerController controller;
    @FeatureGet private TileCollidable tileCollidable;

    /**
     * Create updater.
     * 
     * @param services The services reference.
     * @param model The model reference.
     */
    public PlayerUpdater(Services services, PlayerModel model)
    {
        movement = model.getMovement();
        jump = model.getJump();
    }

    @Override
    public void prepare(FeatureProvider provider)
    {
        super.prepare(provider);

        transformable.teleport(190, 0);

        body.setDesiredFps(Scene.NATIVE.getRate());
        body.setGravity(GRAVITY);
        body.setGravityMax(6.5);
        body.setVectors(movement, jump);
    }

    @Override
    public void update(double extrp)
    {
        controller.update(extrp);
        jump.update(extrp);
        movement.update(extrp);
        body.update(extrp);
        tileCollidable.update(extrp);

        if (transformable.getY() < 0)
        {
            transformable.teleportY(100);
            body.resetGravity();
        }
    }

    @Override
    public void notifyTileCollided(Tile tile, CollisionCategory category)
    {
        if (Axis.Y == category.getAxis() && transformable.getY() < transformable.getOldY())
        {
            body.resetGravity();
            jump.setDirection(DirectionNone.INSTANCE);
        }
    }
}
