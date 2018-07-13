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
package com.b3dgs.lionengine.example.game.state;

import com.b3dgs.lionengine.Animation;
import com.b3dgs.lionengine.Animator;
import com.b3dgs.lionengine.game.Force;
import com.b3dgs.lionengine.game.feature.state.StateAbstract;
import com.b3dgs.lionengine.io.InputDeviceDirectional;

/**
 * Idle state implementation.
 */
class StateIdle extends StateAbstract
{
    private final Animation animation;
    private final Animator animator;
    private final Force movement;

    /**
     * Create the state.
     * 
     * @param mario The mario reference.
     * @param animation The associated animation.
     */
    public StateIdle(MarioModel mario, Animation animation)
    {
        super();

        this.animation = animation;
        animator = mario.getSurface();
        movement = mario.getMovement();

        final InputDeviceDirectional input = mario.getInput();
        addTransition(StateWalk.class, () -> input.getHorizontalDirection() != 0);
        addTransition(StateJump.class, () -> input.getVerticalDirection() > 0);
    }

    @Override
    public void enter()
    {
        movement.setDestination(0.0, 0.0);
        movement.setVelocity(0.3);
        movement.setSensibility(0.01);
    }

    @Override
    public void update(double extrp)
    {
        if (movement.getDirectionHorizontal() != 0.0)
        {
            animator.setAnimSpeed(Math.abs(movement.getDirectionHorizontal()) / 12.0);
        }
        else
        {
            animator.play(animation);
        }
    }
}
