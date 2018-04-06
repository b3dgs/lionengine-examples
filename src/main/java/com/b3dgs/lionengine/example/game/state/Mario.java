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

import com.b3dgs.lionengine.Context;
import com.b3dgs.lionengine.InputDevice;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.Origin;
import com.b3dgs.lionengine.game.DirectionNone;
import com.b3dgs.lionengine.game.Force;
import com.b3dgs.lionengine.game.feature.Camera;
import com.b3dgs.lionengine.game.feature.DisplayableModel;
import com.b3dgs.lionengine.game.feature.FeaturableModel;
import com.b3dgs.lionengine.game.feature.Mirrorable;
import com.b3dgs.lionengine.game.feature.MirrorableModel;
import com.b3dgs.lionengine.game.feature.RefreshableModel;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Setup;
import com.b3dgs.lionengine.game.feature.Transformable;
import com.b3dgs.lionengine.game.feature.TransformableModel;
import com.b3dgs.lionengine.game.feature.body.Body;
import com.b3dgs.lionengine.game.feature.body.BodyModel;
import com.b3dgs.lionengine.game.state.StateAnimationUtil;
import com.b3dgs.lionengine.game.state.StateFactory;
import com.b3dgs.lionengine.game.state.StateHandler;
import com.b3dgs.lionengine.graphic.drawable.Drawable;
import com.b3dgs.lionengine.graphic.drawable.SpriteAnimated;

/**
 * Implementation of our controllable entity.
 */
class Mario extends FeaturableModel
{
    /** Media reference. */
    public static final Media MEDIA = Medias.create("Mario.xml");
    /** Ground location y. */
    static final int GROUND = 32;
    private static final double GRAVITY = 6.0;

    private final StateFactory factory = new StateFactory();
    private final StateHandler handler = new StateHandler(factory);
    private final Force movement = new Force();
    private final Force jump = new Force();
    private final SpriteAnimated surface;

    /**
     * Constructor.
     * 
     * @param services The services reference.
     * @param setup The setup reference.
     */
    public Mario(Services services, Setup setup)
    {
        super();

        final Transformable transformable = addFeatureAndGet(new TransformableModel());
        transformable.teleport(160, GROUND);

        final Mirrorable mirrorable = addFeatureAndGet(new MirrorableModel());

        surface = Drawable.loadSpriteAnimated(setup.getSurface(), 7, 1);
        surface.setOrigin(Origin.CENTER_BOTTOM);
        surface.setFrameOffsets(-1, 0);

        StateAnimationUtil.loadStates(MarioState.values(), factory, this, setup);
        handler.changeState(MarioState.IDLE);

        final Context context = services.get(Context.class);
        final Camera camera = services.get(Camera.class);

        final Body body = addFeatureAndGet(new BodyModel());
        body.setVectors(movement, jump);
        body.setGravity(GRAVITY);
        body.setDesiredFps(context.getConfig().getSource().getRate());

        addFeature(new RefreshableModel(extrp ->
        {
            handler.update(extrp);
            mirrorable.update(extrp);
            movement.update(extrp);
            jump.update(extrp);
            body.update(extrp);
            if (transformable.getY() < GROUND)
            {
                transformable.teleportY(GROUND);
                jump.setDirection(DirectionNone.INSTANCE);
                body.resetGravity();
            }
            surface.setMirror(mirrorable.getMirror());
            surface.update(extrp);
            surface.setLocation(camera, transformable);
        }));

        addFeature(new DisplayableModel(surface::render));
    }

    /**
     * Add an input controller.
     * 
     * @param input The input reference.
     */
    public void addInput(InputDevice input)
    {
        handler.addInput(input);
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

    /**
     * Get the surface.
     * 
     * @return The surface reference.
     */
    public SpriteAnimated getSurface()
    {
        return surface;
    }
}
