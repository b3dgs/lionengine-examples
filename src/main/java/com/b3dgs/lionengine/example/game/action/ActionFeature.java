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
package com.b3dgs.lionengine.example.game.action;

import com.b3dgs.lionengine.Verbose;
import com.b3dgs.lionengine.game.Action;
import com.b3dgs.lionengine.game.feature.ActionConfig;
import com.b3dgs.lionengine.game.feature.FeatureInterface;
import com.b3dgs.lionengine.game.feature.FeatureModel;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Setup;

/**
 * Represents an action as a feature.
 */
@FeatureInterface
class ActionFeature extends FeatureModel
{
    /** Action displayed name. */
    private final String name;

    /**
     * Create feature.
     * 
     * @param services The services reference.
     * @param setup The setup reference.
     */
    public ActionFeature(Services services, Setup setup)
    {
        super(services, setup);

        name = ActionConfig.imports(setup).getName();
    }

    /**
     * Create the button action.
     * 
     * @param services The services reference.
     * @return The created button action.
     */
    public Action create(Services services)
    {
        return () -> Verbose.info(name);
    }
}
