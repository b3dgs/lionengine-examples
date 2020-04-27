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
package com.b3dgs.lionengine.example.game.background;

import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.UtilMath;
import com.b3dgs.lionengine.game.background.BackgroundAbstract;
import com.b3dgs.lionengine.game.background.BackgroundComponent;
import com.b3dgs.lionengine.game.background.BackgroundElement;
import com.b3dgs.lionengine.game.background.BackgroundElementRastered;
import com.b3dgs.lionengine.game.background.Parallax;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.drawable.Sprite;
import com.b3dgs.lionengine.graphic.engine.SourceResolutionProvider;

/**
 * Swamp background implementation.
 */
class Swamp extends BackgroundAbstract
{
    private static final int MOON_RASTERS = 20;
    private static final int PARALLAX_LINES = 96;

    private final Backdrop backdrop;
    private final Clouds clouds;
    private final Parallax parallax;
    private final double scaleH;

    /**
     * Constructor.
     * 
     * @param source The resolution source reference.
     * @param scaleH The horizontal factor.
     * @param scaleV The horizontal factor.
     */
    Swamp(SourceResolutionProvider source, double scaleH, double scaleV)
    {
        super(null, 0, 344);

        this.scaleH = scaleH;
        totalHeight = 82;

        final int width = source.getWidth();
        final int halfScreen = (int) (source.getWidth() / 3.5);

        backdrop = new Backdrop(width);
        clouds = new Clouds(Medias.create("cloud.png"), width, 4);
        parallax = new Parallax(source, Medias.create("parallax.png"), PARALLAX_LINES, halfScreen, 124, 50, 100);
        add(backdrop);
        add(clouds);
        add(parallax);
    }

    /**
     * Backdrop represents the back background plus top background elements.
     */
    private final class Backdrop implements BackgroundComponent
    {
        private final BackgroundElement backcolor;
        private final BackgroundElement mountain;
        private final BackgroundElementRastered moon;
        private final Sprite mountainSprite;
        private final int moonOffset;
        private final int w;
        private final int screenWidth;

        /**
         * Constructor.
         * 
         * @param screenWidth The screen width.
         */
        Backdrop(int screenWidth)
        {
            super();

            backcolor = createElement("backcolor.png", 0, 0);
            mountain = createElement("mountain.png", 0, 124);
            final int x = (int) (224 * scaleH);
            moonOffset = 50;
            moon = new BackgroundElementRastered(x,
                                                 moonOffset,
                                                 Medias.create("moon.png"),
                                                 Medias.create("moon.xml"),
                                                 MOON_RASTERS);
            mountainSprite = (Sprite) mountain.getRenderable();
            this.screenWidth = screenWidth;
            w = (int) Math.ceil(screenWidth / (double) ((Sprite) mountain.getRenderable()).getWidth()) + 1;
        }

        /**
         * Render backdrop element.
         * 
         * @param g The graphic output.
         */
        private void renderBackdrop(Graphic g)
        {
            final Sprite sprite = (Sprite) backcolor.getRenderable();

            for (int i = 0; i < Math.ceil(screenWidth / (double) sprite.getWidth()); i++)
            {
                final int x = backcolor.getMainX() + i * sprite.getWidth();
                final double y = backcolor.getOffsetY() + backcolor.getMainY();
                sprite.setLocation(x, y);
                sprite.render(g);
            }
        }

        /**
         * Render moon element.
         * 
         * @param g The graphic output.
         */
        private void renderMoon(Graphic g)
        {
            final int id = (int) (mountain.getOffsetY() + (totalHeight - getOffsetY())) / 6;
            final Sprite spriteMoon = moon.getRaster(id);
            spriteMoon.setLocation(moon.getMainX(), moon.getOffsetY() + moon.getMainY());
            spriteMoon.render(g);
        }

        /**
         * Render mountains element.
         * 
         * @param g The graphic output.
         */
        private void renderMountains(Graphic g)
        {
            final int oy = (int) (mountain.getOffsetY() + mountain.getMainY());
            final int ox = (int) (-mountain.getOffsetX() + mountain.getMainX());
            final int sx = mountainSprite.getWidth();
            for (int j = 0; j < w; j++)
            {
                mountainSprite.setLocation(ox + sx * j, oy);
                mountainSprite.render(g);
            }
        }

        @Override
        public void update(double extrp, int x, int y, double speed)
        {
            backcolor.setOffsetY(y);
            moon.setOffsetY(moonOffset - totalHeight + getOffsetY());
            final double mx = mountain.getOffsetX() + speed * 0.24;
            mountain.setOffsetX(UtilMath.wrapDouble(mx, 0.0, mountainSprite.getWidth()));
            mountain.setOffsetY(y);
        }

        @Override
        public void render(Graphic g)
        {
            renderBackdrop(g);
            renderMoon(g);
            renderMountains(g);
        }
    }
}
