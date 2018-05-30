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
package com.b3dgs.lionengine.example.pong;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.Origin;
import com.b3dgs.lionengine.UtilRandom;
import com.b3dgs.lionengine.Viewer;
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
 * Racket implementation.
 */
class Racket extends FeaturableModel implements CollidableListener
{
    /** Racket media. */
    public static final Media MEDIA = Medias.create("Racket.xml");
    /** Racket color. */
    private static final ColorRgba COLOR = ColorRgba.YELLOW;

    private final Transformable transformable;
    private Transformable target;

    /**
     * Create an object.
     * 
     * @param services The services reference.
     * @param setup The setup reference.
     */
    public Racket(Services services, Setup setup)
    {
        super();

        final Viewer viewer = services.get(Viewer.class);

        transformable = addFeatureAndGet(new TransformableModel(setup));
        final Collidable collidable = addFeatureAndGet(new CollidableModel(services, setup));
        collidable.addCollision(Collision.AUTOMATIC);
        collidable.setOrigin(Origin.MIDDLE);
        collidable.addAccept(1);
        collidable.setGroup(0);

        transformable.teleportY(Scene.NATIVE.getHeight() / 2
                                - transformable.getHeight() / 2
                                + UtilRandom.getRandomInteger(10));
        final double speed = UtilRandom.getRandomDouble() + 0.5;

        addFeature(new RefreshableModel(extrp ->
        {
            final double diffY = target.getY() - transformable.getY();
            if (diffY < -transformable.getHeight() / 4)
            {
                transformable.moveLocation(extrp, 0.0, -speed);
            }
            else if (diffY > transformable.getHeight() / 4)
            {
                transformable.moveLocation(extrp, 0.0, speed);
            }
        }));

        addFeature(new DisplayableModel(g ->
        {
            g.setColor(COLOR);
            g.drawRect(viewer,
                       Origin.MIDDLE,
                       (int) transformable.getX(),
                       (int) transformable.getY(),
                       transformable.getWidth(),
                       transformable.getHeight(),
                       true);
            collidable.render(g);
        }));
    }

    /**
     * Set the racket side on screen.
     * 
     * @param left <code>true</code> for left side, <code>false</code> for right side.
     */
    public void setSide(boolean left)
    {
        if (left)
        {
            transformable.teleportX(transformable.getWidth() / 2);
        }
        else
        {
            transformable.teleportX(320 - transformable.getWidth() / 2);
        }
    }

    /**
     * Set the ball reference.
     * 
     * @param ball The ball reference.
     */
    public void setBall(Ball ball)
    {
        target = ball.getFeature(Transformable.class);
    }

    /*
     * CollidableListener
     */

    @Override
    public void notifyCollided(Collidable collidable)
    {
        // Nothing to do
    }
}
