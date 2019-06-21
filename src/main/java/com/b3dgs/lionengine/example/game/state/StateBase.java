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
import com.b3dgs.lionengine.Animator;
import com.b3dgs.lionengine.game.Force;
import com.b3dgs.lionengine.game.feature.Mirrorable;
import com.b3dgs.lionengine.game.feature.state.StateAbstract;
import com.b3dgs.lionengine.io.InputDeviceDirectional;

/**
 * Base state.
 */
abstract class StateBase extends StateAbstract
{
    /** Movement force. */
    protected final Force movement;
    /** Jump force. */
    protected final Force jump;
    /** Mirrorable model. */
    protected final Mirrorable mirrorable;
    /** Animator model. */
    protected final Animator animator;
    /** Input device reference. */
    protected final InputDeviceDirectional input;
    /** Associated animation. */
    private final Animation animation;

    /**
     * Create state.
     * 
     * @param mario The mario reference.
     * @param animation The associated animation.
     */
    protected StateBase(MarioModel mario, Animation animation)
    {
        super();

        this.animation = animation;
        mirrorable = mario.getFeature(Mirrorable.class);
        animator = mario.getSurface();
        movement = mario.getMovement();
        jump = mario.getJump();
        input = mario.getInput();
    }

    @Override
    public void enter()
    {
        animator.play(animation);
    }
}
