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

import com.b3dgs.lionengine.Animation;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.Verbose;
import com.b3dgs.lionengine.game.FeatureProvider;
import com.b3dgs.lionengine.game.feature.Animatable;
import com.b3dgs.lionengine.game.feature.FeatureGet;
import com.b3dgs.lionengine.game.feature.FeatureInterface;
import com.b3dgs.lionengine.game.feature.FeatureModel;
import com.b3dgs.lionengine.game.feature.Layerable;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Setup;
import com.b3dgs.lionengine.game.feature.Transformable;
import com.b3dgs.lionengine.game.feature.attackable.Attacker;
import com.b3dgs.lionengine.game.feature.attackable.AttackerListener;
import com.b3dgs.lionengine.game.feature.tile.map.pathfinding.Pathfindable;

/**
 * Grunt entity implementation.
 */
@FeatureInterface
class Grunt extends FeatureModel implements AttackerListener
{
    /** Media reference. */
    public static final Media MEDIA = Medias.create("Grunt.xml");
    private static final Animation IDLE = new Animation("idle", 1, 1, 1.0, false, false);
    private static final Animation IDLE_SOUTH = new Animation("idle", 5, 5, 1.0, false, false);
    private static final Animation WALK = new Animation("walk", 6, 10, 0.15, true, false);
    private static final Animation ATTACK = new Animation("attack", 31, 34, 0.15, true, false);

    @FeatureGet private Pathfindable pathfindable;
    @FeatureGet private Attacker attacker;
    @FeatureGet private Animatable animatable;
    @FeatureGet private Layerable layerable;

    /**
     * Create grunt.
     * 
     * @param services The services reference.
     * @param setup The setup reference.
     */
    Grunt(Services services, Setup setup)
    {
        super(services, setup);
    }

    @Override
    public void prepare(FeatureProvider provider)
    {
        super.prepare(provider);

        animatable.play(IDLE);
        attacker.setAttackFrame(ATTACK.getLast());
    }

    /***
     * Attack a target.
     * 
     * @param target The target to attack.
     */
    public void attack(Transformable target)
    {
        pathfindable.setDestination(target);
        attacker.attack(target);
        animatable.play(WALK);
    }

    /**
     * Set location in tile.
     * 
     * @param tx The horizontal tile.
     * @param ty The vertical tile.
     * @param mirror The mirror flag.
     */
    public void teleport(int tx, int ty, boolean mirror)
    {
        pathfindable.setLocation(tx, ty);
        if (mirror)
        {
            animatable.play(IDLE_SOUTH);
            layerable.setLayer(Integer.valueOf(1), Integer.valueOf(1));
        }
    }

    @Override
    public void notifyReachingTarget(Transformable target)
    {
        // Nothing to do
    }

    @Override
    public void notifyAttackStarted(Transformable target)
    {
        animatable.play(ATTACK);
    }

    @Override
    public void notifyAttackEnded(Transformable target, int damages)
    {
        Verbose.info("Attack: " + damages);
    }

    @Override
    public void notifyAttackAnimEnded()
    {
        animatable.play(IDLE);
    }

    @Override
    public void notifyPreparingAttack(Transformable target)
    {
        // Nothing to do
    }

    @Override
    public void notifyAttackStopped()
    {
        // Nothing to do
    }
}
