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

import com.b3dgs.lionengine.Updatable;
import com.b3dgs.lionengine.awt.Keyboard;
import com.b3dgs.lionengine.awt.KeyboardAwt;
import com.b3dgs.lionengine.game.DirectionNone;
import com.b3dgs.lionengine.game.Force;
import com.b3dgs.lionengine.game.feature.FeatureInterface;
import com.b3dgs.lionengine.game.feature.FeatureModel;
import com.b3dgs.lionengine.game.feature.Services;

/**
 * Player controlling implementation.
 */
@FeatureInterface
class PlayerController extends FeatureModel implements Updatable
{
    private static final double SPEED = 1;
    private static final double JUMP = 6.0;

    private final Force movement;
    private final Force jump;
    private final Keyboard keyboard;

    /**
     * Create updater.
     * 
     * @param services The services reference.
     * @param model The model reference.
     */
    public PlayerController(Services services, PlayerModel model)
    {
        super();

        movement = model.getMovement();
        jump = model.getJump();
        keyboard = services.get(Keyboard.class);
    }

    @Override
    public void update(double extrp)
    {
        movement.setDirection(DirectionNone.INSTANCE);
        if (keyboard.isPressed(KeyboardAwt.LEFT))
        {
            movement.setDirection(-SPEED, 0);
        }
        if (keyboard.isPressed(KeyboardAwt.RIGHT))
        {
            movement.setDirection(SPEED, 0);
        }
        if (keyboard.isPressedOnce(KeyboardAwt.UP))
        {
            jump.setDirection(0.0, JUMP);
        }
    }
}
