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
package com.b3dgs.lionengine.example.game.effect;

import com.b3dgs.lionengine.AnimState;
import com.b3dgs.lionengine.Animation;
import com.b3dgs.lionengine.AnimatorStateListener;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.Viewer;
import com.b3dgs.lionengine.game.AnimationConfig;
import com.b3dgs.lionengine.game.FeatureProvider;
import com.b3dgs.lionengine.game.feature.Animatable;
import com.b3dgs.lionengine.game.feature.FeatureGet;
import com.b3dgs.lionengine.game.feature.FeatureInterface;
import com.b3dgs.lionengine.game.feature.FeatureModel;
import com.b3dgs.lionengine.game.feature.Identifiable;
import com.b3dgs.lionengine.game.feature.Routine;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Setup;
import com.b3dgs.lionengine.game.feature.Transformable;
import com.b3dgs.lionengine.game.feature.rasterable.Rasterable;
import com.b3dgs.lionengine.io.InputDevicePointer;

/**
 * Effect implementation.
 */
@FeatureInterface
class Effect extends FeatureModel implements Routine
{
    /** Explode media. */
    public static final Media EXPLODE = Medias.create("Explode.xml");

    private final Viewer viewer = services.get(Viewer.class);
    private final Animation animExplode;

    @FeatureGet private Identifiable identifiable;
    @FeatureGet private Transformable transformable;
    @FeatureGet private Rasterable rasterable;
    @FeatureGet private Animatable animatable;

    /**
     * Constructor.
     * 
     * @param services The services reference.
     * @param setup the setup reference.
     */
    public Effect(Services services, Setup setup)
    {
        super(services, setup);

        final AnimationConfig configAnimations = AnimationConfig.imports(setup);
        animExplode = configAnimations.getAnimation("explode");
    }

    /**
     * Start the effect at the specified location.
     * 
     * @param pointer The pointer reference.
     */
    public void start(InputDevicePointer pointer)
    {
        transformable.teleport(viewer.getViewpointX(pointer.getX()), viewer.getViewpointY(pointer.getY()));
        animatable.play(animExplode);
    }

    @Override
    public void prepare(FeatureProvider provider)
    {
        super.prepare(provider);

        animatable.addListener((AnimatorStateListener) state ->
        {
            if (animatable.is(AnimState.FINISHED))
            {
                identifiable.destroy();
            }
        });
    }
}
