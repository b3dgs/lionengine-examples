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
package com.b3dgs.lionengine.example.game.state;

import com.b3dgs.lionengine.Animation;
import com.b3dgs.lionengine.Mirror;

/**
 * Walk state implementation.
 */
class StateWalk extends StateBase
{
    /**
     * Create the walk state.
     * 
     * @param mario The mario reference.
     * @param animation The associated animation.
     */
    public StateWalk(MarioModel mario, Animation animation)
    {
        super(mario, animation);

        addTransition(StateIdle.class, () -> Double.compare(movement.getDirectionHorizontal(), 0.0) == 0);
        addTransition(StateTurn.class,
                      () -> input.getHorizontalDirection() < 0 && movement.getDirectionHorizontal() > 0
                            || input.getHorizontalDirection() > 0 && movement.getDirectionHorizontal() < 0);
        addTransition(StateJump.class, () -> input.getVerticalDirection() > 0);
    }

    @Override
    public void enter()
    {
        super.enter();

        movement.setVelocity(0.5);
        movement.setSensibility(0.1);
    }

    @Override
    public void update(double extrp)
    {
        final double side = input.getHorizontalDirection();
        movement.setDestination(side * 3.0, 0);
        animator.setAnimSpeed(Math.abs(movement.getDirectionHorizontal()) / 12.0);

        if (side < 0 && movement.getDirectionHorizontal() < 0)
        {
            mirrorable.mirror(Mirror.HORIZONTAL);
        }
        else if (side > 0 && movement.getDirectionHorizontal() > 0)
        {
            mirrorable.mirror(Mirror.NONE);
        }
    }
}
