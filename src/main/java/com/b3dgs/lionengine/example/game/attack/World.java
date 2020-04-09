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
package com.b3dgs.lionengine.example.game.attack;

import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.Origin;
import com.b3dgs.lionengine.awt.Mouse;
import com.b3dgs.lionengine.game.feature.Featurable;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Transformable;
import com.b3dgs.lionengine.helper.WorldHelper;

/**
 * World representation.
 */
public class World extends WorldHelper
{
    private final Mouse mouse = getInputDevice(Mouse.class);

    /**
     * Create world.
     * 
     * @param services The services reference.
     */
    public World(Services services)
    {
        super(services);

        camera.setView(source, 0, 0, Origin.TOP_LEFT);

        map.create(Medias.create("level.png"));

        final Featurable grunt1 = factory.create(Grunt.MEDIA);
        grunt1.getFeature(Grunt.class).teleport(4, 10, true);
        handler.add(grunt1);

        final Featurable grunt2 = factory.create(Grunt.MEDIA);
        grunt2.getFeature(Grunt.class).teleport(4, 6, false);
        handler.add(grunt2);

        grunt2.getFeature(Grunt.class).attack(grunt1.getFeature(Transformable.class));
    }

    @Override
    public void update(double extrp)
    {
        mouse.update(extrp);

        super.update(extrp);
    }
}
