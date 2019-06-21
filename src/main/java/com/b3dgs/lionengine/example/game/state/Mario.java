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

import java.util.Locale;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
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
import com.b3dgs.lionengine.game.feature.state.State;
import com.b3dgs.lionengine.game.feature.state.StateHandler;
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

    private static final int PREFIX = State.class.getSimpleName().length();
    private static final double GRAVITY = 6.0;

    /**
     * Get animation name from state class.
     * 
     * @param state The state class.
     * @return The animation name.
     */
    private static String getAnimationName(Class<? extends State> state)
    {
        return state.getSimpleName().substring(PREFIX).toLowerCase(Locale.ENGLISH);
    }

    /**
     * Constructor.
     * 
     * @param services The services reference.
     * @param setup The setup reference.
     */
    public Mario(Services services, Setup setup)
    {
        super();

        final MarioModel model = addFeatureAndGet(new MarioModel(services, setup));
        final Mirrorable mirrorable = addFeatureAndGet(new MirrorableModel());
        final Transformable transformable = addFeatureAndGet(new TransformableModel());
        transformable.teleport(160, GROUND);

        final Force movement = model.getMovement();
        final Force jump = model.getJump();

        final Body body = addFeatureAndGet(new BodyModel());
        body.setGravity(GRAVITY);
        body.setDesiredFps(60);

        final SpriteAnimated surface = model.getSurface();
        final Camera camera = services.get(Camera.class);

        final StateHandler state = addFeatureAndGet(new StateHandler(setup, Mario::getAnimationName));
        state.changeState(StateIdle.class);

        addFeature(new RefreshableModel(extrp ->
        {
            state.update(extrp);
            movement.update(extrp);
            jump.update(extrp);
            body.update(extrp);
            state.postUpdate();
            mirrorable.update(extrp);

            transformable.moveLocation(extrp, body, movement, jump);
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
}
