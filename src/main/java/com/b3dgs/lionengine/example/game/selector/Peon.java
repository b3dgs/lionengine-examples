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
package com.b3dgs.lionengine.example.game.selector;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.Origin;
import com.b3dgs.lionengine.Viewer;
import com.b3dgs.lionengine.game.FramesConfig;
import com.b3dgs.lionengine.game.feature.DisplayableModel;
import com.b3dgs.lionengine.game.feature.FeaturableModel;
import com.b3dgs.lionengine.game.feature.LayerableModel;
import com.b3dgs.lionengine.game.feature.RefreshableModel;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Setup;
import com.b3dgs.lionengine.game.feature.Transformable;
import com.b3dgs.lionengine.game.feature.TransformableModel;
import com.b3dgs.lionengine.game.feature.collidable.Collidable;
import com.b3dgs.lionengine.game.feature.collidable.CollidableModel;
import com.b3dgs.lionengine.game.feature.collidable.Collision;
import com.b3dgs.lionengine.game.feature.collidable.selector.Selectable;
import com.b3dgs.lionengine.game.feature.collidable.selector.SelectableModel;
import com.b3dgs.lionengine.graphic.ColorRgba;
import com.b3dgs.lionengine.graphic.drawable.Drawable;
import com.b3dgs.lionengine.graphic.drawable.SpriteAnimated;

/**
 * Peon entity implementation.
 */
class Peon extends FeaturableModel
{
    /** Media reference. */
    public static final Media MEDIA = Medias.create("Peon.xml");

    /**
     * Create a peon.
     * 
     * @param services The services reference.
     * @param setup The setup reference.
     */
    public Peon(Services services, Setup setup)
    {
        super(services, setup);

        addFeature(new LayerableModel(services, setup));

        final Transformable transformable = addFeatureAndGet(new TransformableModel(services, setup));
        final Collidable collidable = addFeatureAndGet(new CollidableModel(services, setup));

        final FramesConfig config = FramesConfig.imports(setup);
        final SpriteAnimated surface = Drawable.loadSpriteAnimated(setup.getSurface(),
                                                                   config.getHorizontal(),
                                                                   config.getVertical());
        surface.setOrigin(Origin.BOTTOM_LEFT);
        surface.setFrameOffsets(config.getOffsetX(), config.getOffsetY());

        transformable.teleport(432, 272);
        collidable.addCollision(Collision.AUTOMATIC);
        collidable.setOrigin(Origin.BOTTOM_LEFT);
        collidable.setGroup(Integer.valueOf(0));

        final Viewer viewer = services.get(Viewer.class);

        addFeature(new RefreshableModel(extrp ->
        {
            surface.setLocation(viewer, transformable);
        }));

        final Selectable selectable = addFeatureAndGet(new SelectableModel(services, setup));

        addFeature(new DisplayableModel(g ->
        {
            surface.render(g);
            if (selectable.isSelected())
            {
                g.setColor(ColorRgba.GREEN);
                g.drawRect(viewer, Origin.BOTTOM_LEFT, transformable, false);
            }
        }));
    }
}
