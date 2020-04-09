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
package com.b3dgs.lionengine.example.game.collision;

import com.b3dgs.lionengine.game.DirectionNone;
import com.b3dgs.lionengine.game.FeatureProvider;
import com.b3dgs.lionengine.game.Force;
import com.b3dgs.lionengine.game.feature.FeatureGet;
import com.b3dgs.lionengine.game.feature.FeatureInterface;
import com.b3dgs.lionengine.game.feature.Recyclable;
import com.b3dgs.lionengine.game.feature.Routine;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Setup;
import com.b3dgs.lionengine.game.feature.Transformable;
import com.b3dgs.lionengine.game.feature.body.Body;
import com.b3dgs.lionengine.game.feature.tile.map.collision.Axis;
import com.b3dgs.lionengine.game.feature.tile.map.collision.TileCollidable;
import com.b3dgs.lionengine.helper.EntityModelHelper;

/**
 * Player model implementation.
 */
@FeatureInterface
public class PlayerModel extends EntityModelHelper implements Routine, Recyclable
{
    private static final double SPEED = 1;
    private static final double JUMP = 6.0;
    private static final double GRAVITY = 5.5;

    private final Force movement = new Force();
    private final Force jump = new Force();

    @FeatureGet private Body body;
    @FeatureGet private Transformable transformable;
    @FeatureGet private TileCollidable tileCollidable;

    /**
     * Create model.
     * 
     * @param services The services reference.
     * @param setup The setup reference.
     */
    public PlayerModel(Services services, Setup setup)
    {
        super(services, setup);
    }

    /**
     * Get the movement force.
     * 
     * @return The movement force.
     */
    public Force getMovement()
    {
        return movement;
    }

    /**
     * Get the jump force.
     * 
     * @return THe jump force.
     */
    public Force getJump()
    {
        return jump;
    }

    @Override
    public void prepare(FeatureProvider provider)
    {
        super.prepare(provider);

        jump.setVelocity(0.1);
        jump.setSensibility(0.1);

        movement.setVelocity(1.0);
        movement.setSensibility(1.0);

        body.setDesiredFps(Scene.NATIVE.getRate());
        body.setGravity(GRAVITY);
        body.setGravityMax(GRAVITY);

        tileCollidable.addListener((result, category) ->
        {
            if (Axis.Y == category.getAxis() && transformable.getY() < transformable.getOldY())
            {
                body.resetGravity();
                jump.setDirection(DirectionNone.INSTANCE);
                tileCollidable.apply(result);
            }
        });
    }

    @Override
    public void update(double extrp)
    {
        movement.setDestination(input.getHorizontalDirection() * SPEED, 0.0);
        if (input.isUpButtonOnce())
        {
            jump.setDirection(0.0, JUMP);
        }

        movement.update(extrp);
        jump.update(extrp);
        body.update(extrp);

        if (transformable.getY() < 0)
        {
            jump.setDirection(DirectionNone.INSTANCE);
            transformable.teleportY(0.0);
            body.resetGravity();
        }

        transformable.moveLocation(extrp, body, movement, jump);
    }

    @Override
    public void recycle()
    {
        transformable.teleport(0.0, 0.0);
        movement.zero();
        jump.zero();
    }
}
