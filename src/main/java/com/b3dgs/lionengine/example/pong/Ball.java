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
package com.b3dgs.lionengine.example.pong;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.Origin;
import com.b3dgs.lionengine.UtilMath;
import com.b3dgs.lionengine.Viewer;
import com.b3dgs.lionengine.game.Force;
import com.b3dgs.lionengine.game.feature.DisplayableModel;
import com.b3dgs.lionengine.game.feature.FeaturableModel;
import com.b3dgs.lionengine.game.feature.RefreshableModel;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Setup;
import com.b3dgs.lionengine.game.feature.Transformable;
import com.b3dgs.lionengine.game.feature.TransformableModel;
import com.b3dgs.lionengine.game.feature.collidable.Collidable;
import com.b3dgs.lionengine.game.feature.collidable.CollidableListener;
import com.b3dgs.lionengine.game.feature.collidable.CollidableModel;
import com.b3dgs.lionengine.game.feature.collidable.Collision;
import com.b3dgs.lionengine.graphic.ColorRgba;

/**
 * Ball implementation.
 */
class Ball extends FeaturableModel implements CollidableListener
{
    /** Racket media. */
    public static final Media MEDIA = Medias.create("Ball.xml");

    /** Transformable reference. */
    private final Transformable transformable;
    /** Collidable reference. */
    private final Collidable collidable;
    /** Speed. */
    private final double speed = 3.0;
    /** Current force. */
    private final Force force = new Force(-speed, 0.0);

    /**
     * Create an object.
     * 
     * @param services The services reference.
     * @param setup The setup reference.
     */
    public Ball(Services services, Setup setup)
    {
        super();

        final Viewer viewer = services.get(Viewer.class);

        transformable = addFeatureAndGet(new TransformableModel(setup));
        collidable = addFeatureAndGet(new CollidableModel(services, setup));
        collidable.addCollision(Collision.AUTOMATIC);
        collidable.setOrigin(Origin.MIDDLE);
        collidable.setGroup(Integer.valueOf(1));
        collidable.addAccept(Integer.valueOf(0));
        collidable.setCollisionVisibility(false);

        force.setDestination(-speed, 0.0);
        force.setVelocity(speed);

        transformable.teleport(320.0 / 2.0, 220.0 / 2.0);

        addFeature(new RefreshableModel(extrp ->
        {
            force.update(extrp);
            transformable.moveLocation(extrp, force);
            if (transformable.getY() < transformable.getHeight() / 2)
            {
                transformable.teleportY(transformable.getHeight() / 2);
                force.setDestination(force.getDirectionHorizontal(), -force.getDirectionVertical());
            }
            if (transformable.getY() > 240 - transformable.getHeight() / 2)
            {
                transformable.teleportY(240.0 - transformable.getHeight() / 2);
                force.setDestination(force.getDirectionHorizontal(), -force.getDirectionVertical());
            }
        }));

        addFeature(new DisplayableModel(g ->
        {
            g.setColor(ColorRgba.GRAY);
            g.drawOval(viewer,
                       Origin.MIDDLE,
                       (int) transformable.getX(),
                       (int) transformable.getY(),
                       transformable.getWidth(),
                       transformable.getHeight(),
                       true);
            collidable.render(g);
        }));
    }

    @Override
    public void notifyCollided(Collidable other, Collision with, Collision by)
    {
        final Transformable racket = other.getFeature(Transformable.class);

        int side = 0;
        if (transformable.getX() < transformable.getOldX())
        {
            transformable.teleportX(racket.getX() + racket.getWidth() / 2 + transformable.getWidth() / 2 + 1);
            side = 1;
        }
        if (transformable.getX() > transformable.getOldX())
        {
            transformable.teleportX(racket.getX() - transformable.getWidth());
            side = -1;
        }

        final double diff = transformable.getY() - racket.getY();
        final int angle = (int) Math.round(diff * 2);
        force.setDestination(speed * UtilMath.cos(angle) * side, speed * UtilMath.sin(angle));
    }
}
