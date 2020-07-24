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
package com.b3dgs.lionengine.example.prototype.gameplay;

import com.b3dgs.lionengine.game.Alterable;
import com.b3dgs.lionengine.game.FeatureProvider;
import com.b3dgs.lionengine.game.Force;
import com.b3dgs.lionengine.game.feature.FeatureGet;
import com.b3dgs.lionengine.game.feature.FeatureInterface;
import com.b3dgs.lionengine.game.feature.Mirrorable;
import com.b3dgs.lionengine.game.feature.Recyclable;
import com.b3dgs.lionengine.game.feature.Routine;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Setup;
import com.b3dgs.lionengine.game.feature.Transformable;
import com.b3dgs.lionengine.game.feature.body.Body;
import com.b3dgs.lionengine.game.feature.collidable.Collidable;
import com.b3dgs.lionengine.game.feature.state.StateHandler;
import com.b3dgs.lionengine.helper.EntityModelHelper;

/**
 * Entity model implementation.
 */
@FeatureInterface
public final class EntityModel extends EntityModelHelper implements Routine, Recyclable
{
    private static final String ANIM_ATTACK_PREFIX = "attack";

    private final Force movement = new Force();
    private final Force jump = new Force();
    private final Alterable health = new Alterable(1);

    @FeatureGet private Transformable transformable;
    @FeatureGet private Mirrorable mirrorable;
    @FeatureGet private Body body;
    @FeatureGet private StateHandler state;
    @FeatureGet private Collidable collidable;

    /**
     * Create model.
     * 
     * @param services The services reference.
     * @param setup The setup reference.
     */
    public EntityModel(Services services, Setup setup)
    {
        super(services, setup);

        jump.setVelocity(0.25);
        jump.setSensibility(0.1);

        movement.setVelocity(1.0);
        movement.setSensibility(1.0);
    }

    /**
     * Get the movement.
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
     * @return The jump force.
     */
    public Force getJump()
    {
        return jump;
    }

    /**
     * Get the health.
     * 
     * @return The health reference.
     */
    public Alterable getHealth()
    {
        return health;
    }

    @Override
    public void prepare(FeatureProvider provider)
    {
        super.prepare(provider);

        collidable.addListener((other, with, by) ->
        {
            if (by.getName().startsWith(ANIM_ATTACK_PREFIX))
            {
                health.decrease(1);
            }
        });
    }

    @Override
    public void update(double extrp)
    {
        movement.update(extrp);
        jump.update(extrp);

        if (transformable.getY() < 0)
        {
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
        health.fill();
    }
}
