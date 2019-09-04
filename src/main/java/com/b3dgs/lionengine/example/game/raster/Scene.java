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
package com.b3dgs.lionengine.example.game.raster;

import com.b3dgs.lionengine.Animation;
import com.b3dgs.lionengine.Context;
import com.b3dgs.lionengine.Engine;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.Origin;
import com.b3dgs.lionengine.Resolution;
import com.b3dgs.lionengine.UtilMath;
import com.b3dgs.lionengine.awt.Keyboard;
import com.b3dgs.lionengine.awt.KeyboardAwt;
import com.b3dgs.lionengine.awt.Mouse;
import com.b3dgs.lionengine.awt.MouseAwt;
import com.b3dgs.lionengine.game.feature.AnimatableModel;
import com.b3dgs.lionengine.game.feature.Camera;
import com.b3dgs.lionengine.game.feature.ComponentDisplayable;
import com.b3dgs.lionengine.game.feature.ComponentRefreshable;
import com.b3dgs.lionengine.game.feature.DisplayableModel;
import com.b3dgs.lionengine.game.feature.Featurable;
import com.b3dgs.lionengine.game.feature.FeaturableModel;
import com.b3dgs.lionengine.game.feature.Handler;
import com.b3dgs.lionengine.game.feature.LayerableModel;
import com.b3dgs.lionengine.game.feature.MirrorableModel;
import com.b3dgs.lionengine.game.feature.RefreshableModel;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Transformable;
import com.b3dgs.lionengine.game.feature.TransformableModel;
import com.b3dgs.lionengine.game.feature.rasterable.Rasterable;
import com.b3dgs.lionengine.game.feature.rasterable.RasterableModel;
import com.b3dgs.lionengine.game.feature.rasterable.SetupSurfaceRastered;
import com.b3dgs.lionengine.game.feature.tile.map.MapTile;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileGame;
import com.b3dgs.lionengine.game.feature.tile.map.raster.MapTileRastered;
import com.b3dgs.lionengine.game.feature.tile.map.raster.MapTileRasteredModel;
import com.b3dgs.lionengine.game.feature.tile.map.viewer.MapTileViewer;
import com.b3dgs.lionengine.game.feature.tile.map.viewer.MapTileViewerModel;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.drawable.Drawable;
import com.b3dgs.lionengine.graphic.drawable.SpriteAnimated;
import com.b3dgs.lionengine.graphic.engine.Sequence;

/**
 * Game loop designed to handle our world.
 */
class Scene extends Sequence
{
    /** Native resolution. */
    static final Resolution NATIVE = new Resolution(320, 240, 60);

    private final Services services = new Services();
    private final Camera camera = services.create(Camera.class);
    private final Handler handler = services.create(Handler.class);
    private final MapTile map = services.create(MapTileGame.class);
    private final MapTileViewer mapViewer = map.addFeatureAndGet(new MapTileViewerModel(services));
    private final MapTileRastered mapRaster = map.addFeatureAndGet(new MapTileRasteredModel(services));
    private final Mouse mouse;

    private boolean useRaster = true;
    private int count;

    /**
     * Constructor.
     * 
     * @param context The context reference.
     */
    public Scene(Context context)
    {
        super(context, NATIVE);

        getInputDevice(Keyboard.class).addActionPressed(KeyboardAwt.ESCAPE, this::end);
        mouse = getInputDevice(Mouse.class);

        handler.addComponent(new ComponentRefreshable());
        handler.addComponent(new ComponentDisplayable());
    }

    @Override
    public void load()
    {
        handler.add(map);
        map.create(Medias.create("level.png"), 16, 16, 16);
        mapRaster.loadSheets(Medias.create("raster.xml"), false);
        mapViewer.addRenderer(mapRaster);

        camera.setView(0, 0, getWidth(), getHeight(), getHeight());
        camera.setLimits(map);

        final SetupSurfaceRastered setup = new SetupSurfaceRastered(Medias.create("object.xml"),
                                                                    Medias.create("raster.xml"));
        final SpriteAnimated surface = Drawable.loadSpriteAnimated(setup.getSurface(), 4, 4);
        surface.setOrigin(Origin.MIDDLE);

        final Featurable featurable = new FeaturableModel(services, setup);
        featurable.addFeatureAndGet(new LayerableModel(1));
        featurable.addFeature(new MirrorableModel(services, setup));
        featurable.addFeature(new AnimatableModel(services, setup, surface));
        surface.play(new Animation(Animation.DEFAULT_NAME, 1, 10, 0.2, false, true));

        final Transformable transformable = featurable.addFeatureAndGet(new TransformableModel(services, setup));
        final Rasterable rasterable = featurable.addFeatureAndGet(new RasterableModel(services, setup));
        rasterable.setOrigin(Origin.MIDDLE);
        featurable.addFeature(new RefreshableModel(extrp ->
        {
            transformable.setLocationY(UtilMath.sin(count) * 100 + 130);
            rasterable.update(extrp);
            surface.update(extrp);
            surface.setLocation(camera, transformable);
        }));
        featurable.addFeature(new DisplayableModel(g ->
        {
            if (useRaster)
            {
                rasterable.render(g);
            }
            else
            {
                surface.render(g);
            }
        }));

        transformable.setLocationX(120);
        handler.add(featurable);
    }

    @Override
    public void update(double extrp)
    {
        handler.update(extrp);
        count++;
        if (mouse.hasClickedOnce(MouseAwt.LEFT))
        {
            useRaster = !useRaster;
            if (useRaster)
            {
                mapViewer.addRenderer(mapRaster);
            }
            else
            {
                mapViewer.removeRenderer(mapRaster);
            }
        }
    }

    @Override
    public void render(Graphic g)
    {
        g.clear(0, 0, getWidth(), getHeight());
        handler.render(g);
    }

    @Override
    public void onTerminated(boolean hasNextSequence)
    {
        Engine.terminate();
    }
}
