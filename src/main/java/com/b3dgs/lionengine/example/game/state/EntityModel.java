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
package com.b3dgs.lionengine.example.game.state;

import com.b3dgs.lionengine.Mirror;
import com.b3dgs.lionengine.game.FeatureProvider;
import com.b3dgs.lionengine.game.Force;
import com.b3dgs.lionengine.game.feature.FeatureGet;
import com.b3dgs.lionengine.game.feature.FeatureInterface;
import com.b3dgs.lionengine.game.feature.Mirrorable;
import com.b3dgs.lionengine.game.feature.Routine;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Setup;
import com.b3dgs.lionengine.game.feature.Transformable;
import com.b3dgs.lionengine.game.feature.body.Body;
import com.b3dgs.lionengine.graphic.engine.SourceResolutionProvider;
import com.b3dgs.lionengine.helper.EntityModelHelper;

/**
 * Entity model implementation.
 */
@FeatureInterface
class EntityModel extends EntityModelHelper implements Routine
{
    /** Horizontal speed. */
    static final double SPEED_X = 3.0;
    private static final double GRAVITY = 10.0;

    private final Force movement = new Force();
    private final Force jump = new Force();

    private final SourceResolutionProvider source = services.get(SourceResolutionProvider.class);

    @FeatureGet private Transformable transformable;
    @FeatureGet private Mirrorable mirrorable;
    @FeatureGet private Body body;

    /**
     * Constructor.
     * 
     * @param services The services reference.
     * @param setup The setup reference.
     */
    EntityModel(Services services, Setup setup)
    {
        super(services, setup);
    }

    @Override
    public void prepare(FeatureProvider provider)
    {
        super.prepare(provider);

        movement.setVelocity(1.0);

        jump.setSensibility(0.1);
        jump.setVelocity(0.5);

        body.setGravity(GRAVITY);
        body.setGravityMax(GRAVITY);
        body.setDesiredFps(source.getRate());
    }

    @Override
    public void update(double extrp)
    {
        movement.update(extrp);
        jump.update(extrp);

        transformable.moveLocation(extrp, body, movement, jump);

        if (transformable.getY() < 32)
        {
            transformable.teleportY(32);
            body.resetGravity();
        }

        final double side = input.getHorizontalDirection();
        if (side < 0 && movement.getDirectionHorizontal() < 0)
        {
            mirrorable.mirror(Mirror.HORIZONTAL);
        }
        else if (side > 0 && movement.getDirectionHorizontal() > 0)
        {
            mirrorable.mirror(Mirror.NONE);
        }
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
     * @return The jump force.
     */
    public Force getJump()
    {
        return jump;
    }
}
