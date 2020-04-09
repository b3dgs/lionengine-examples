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

import com.b3dgs.lionengine.Animation;
import com.b3dgs.lionengine.game.Force;
import com.b3dgs.lionengine.game.feature.tile.map.collision.Axis;
import com.b3dgs.lionengine.game.feature.tile.map.collision.CollisionCategory;
import com.b3dgs.lionengine.game.feature.tile.map.collision.CollisionResult;
import com.b3dgs.lionengine.helper.StateHelper;

/**
 * Walk state implementation.
 */
class StateWalk extends StateHelper<EntityModel>
{
    private final Force movement;

    /**
     * Create the state.
     * 
     * @param model The model reference.
     * @param animation The associated animation.
     */
    StateWalk(EntityModel model, Animation animation)
    {
        super(model, animation);

        movement = model.getMovement();

        addTransition(StateIdle.class, () -> isGoNone());
        addTransition(StateTurn.class, () -> isChangingDirectionHorizontal(movement));
        addTransition(StateJump.class, this::isGoUpOnce);
        addTransition(StateFall.class, () -> transformable.getY() > 32);
    }

    @Override
    protected void onCollided(CollisionResult result, CollisionCategory category)
    {
        super.onCollided(result, category);

        if (category.getAxis() == Axis.X)
        {
            movement.zero();
        }
    }

    @Override
    public void update(double extrp)
    {
        super.update(extrp);

        movement.setDestination(input.getHorizontalDirection() * EntityModel.SPEED_X, 0.0);
        animatable.setAnimSpeed(Math.abs(movement.getDirectionHorizontal()) / 12.0);
    }
}
