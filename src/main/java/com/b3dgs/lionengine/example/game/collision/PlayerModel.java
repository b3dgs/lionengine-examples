/*
 * Copyright (C) 2013-2017 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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

import com.b3dgs.lionengine.Origin;
import com.b3dgs.lionengine.game.Force;
import com.b3dgs.lionengine.game.feature.FeatureInterface;
import com.b3dgs.lionengine.game.feature.Setup;
import com.b3dgs.lionengine.graphic.drawable.Drawable;
import com.b3dgs.lionengine.graphic.drawable.Sprite;

/**
 * Player model implementation.
 */
@FeatureInterface
public class PlayerModel
{
    private final Force movement = new Force();
    private final Force jump = new Force();
    private final Sprite surface;

    /**
     * Create model.
     * 
     * @param setup The setup reference.
     */
    public PlayerModel(Setup setup)
    {
        super();

        surface = Drawable.loadSprite(setup.getSurface());
        surface.setOrigin(Origin.CENTER_BOTTOM);
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

    /**
     * Get the surface.
     * 
     * @return The surface.
     */
    public Sprite getSurface()
    {
        return surface;
    }
}
