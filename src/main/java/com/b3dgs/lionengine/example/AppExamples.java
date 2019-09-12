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
package com.b3dgs.lionengine.example;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.b3dgs.lionengine.Constant;
import com.b3dgs.lionengine.Engine;
import com.b3dgs.lionengine.UtilReflection;
import com.b3dgs.lionengine.Verbose;
import com.b3dgs.lionengine.example.core.drawable.AppDrawable;
import com.b3dgs.lionengine.example.game.action.AppAction;
import com.b3dgs.lionengine.example.game.assign.AppAssign;
import com.b3dgs.lionengine.example.game.attack.AppAttack;
import com.b3dgs.lionengine.example.game.background.AppBackground;
import com.b3dgs.lionengine.example.game.collision.AppCollision;
import com.b3dgs.lionengine.example.game.cursor.AppCursor;
import com.b3dgs.lionengine.example.game.effect.AppEffect;
import com.b3dgs.lionengine.example.game.extraction.AppExtraction;
import com.b3dgs.lionengine.example.game.fog.AppFog;
import com.b3dgs.lionengine.example.game.map.AppMap;
import com.b3dgs.lionengine.example.game.pathfinding.AppPathfinding;
import com.b3dgs.lionengine.example.game.production.AppProduction;
import com.b3dgs.lionengine.example.game.projectile.AppProjectile;
import com.b3dgs.lionengine.example.game.raster.AppRaster;
import com.b3dgs.lionengine.example.game.selector.AppSelector;
import com.b3dgs.lionengine.example.game.state.AppState;
import com.b3dgs.lionengine.example.game.transition.AppTransition;
import com.b3dgs.lionengine.example.helloworld.AppHelloWorld;
import com.b3dgs.lionengine.example.pong.AppPong;

/**
 * Program starts here.
 */
public class AppExamples
{
    /** Application name. */
    public static final String NAME = Constant.ENGINE_NAME + " Examples";
    /** Main function name. */
    private static final String MAIN = "main";
    /** Exit button. */
    private static final String EXIT = "Exit";
    /** BUtton width. */
    private static final int BUTTON_WIDTH = 96;
    /** Button height. */
    private static final int BUTTON_HEIGHT = 64;
    /** Horizontal buttons. */
    private static final int HORIZONTALS = 3;
    /** Vertical buttons. */
    private static final int VERTICALS = 7;
    /** Executor. */
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    /** Example classes. */
    private static final Class<?>[] EXAMPLES = new Class<?>[]
    {
        AppHelloWorld.class, AppDrawable.class, AppAction.class, AppAssign.class, AppAttack.class, AppBackground.class,
        AppCollision.class, AppCursor.class, AppEffect.class, AppFog.class, AppMap.class, AppPathfinding.class,
        AppProduction.class, AppExtraction.class, AppProjectile.class, AppRaster.class, AppSelector.class,
        AppTransition.class, AppState.class, AppPong.class
    };

    /**
     * Main function.
     * 
     * @param args The arguments (none).
     */
    public static void main(String[] args)
    {
        setThemeSystem();

        final JFrame frame = new JFrame(NAME);
        frame.setPreferredSize(new Dimension(HORIZONTALS * BUTTON_WIDTH, VERTICALS * BUTTON_HEIGHT));

        final JPanel panel = new JPanel(true);
        panel.setLayout(new GridLayout(VERTICALS, HORIZONTALS));

        for (final Class<?> example : EXAMPLES)
        {
            addExample(panel, example);
        }

        final JButton exit = new JButton(EXIT);
        exit.addActionListener(event -> terminate(frame));
        panel.add(exit);

        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent event)
            {
                terminate(frame);
            }
        });

        run(frame, panel);
    }

    /**
     * Use system theme for UI.
     */
    private static void setThemeSystem()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ReflectiveOperationException | UnsupportedLookAndFeelException exception)
        {
            Verbose.exception(exception);
        }
    }

    /**
     * Run frame.
     * 
     * @param frame The frame reference.
     * @param panel The panel reference.
     */
    private static void run(JFrame frame, JPanel panel)
    {
        SwingUtilities.invokeLater(() ->
        {
            frame.add(panel);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    /**
     * Terminate example.
     * 
     * @param frame The main frame reference.
     */
    private static void terminate(JFrame frame)
    {
        EXECUTOR.shutdownNow();
        frame.dispose();
    }

    /**
     * Set the enabled state of a components set.
     * 
     * @param components The components.
     * @param enabled The enabled state.
     */
    private static void setEnabled(Component[] components, boolean enabled)
    {
        for (final Component component : components)
        {
            component.setEnabled(enabled);
            if (component instanceof Container)
            {
                final Container container = (Container) component;
                setEnabled(container.getComponents(), enabled);
            }
        }
    }

    /**
     * Add an example with its button and action.
     * 
     * @param panel The panel reference.
     * @param example The example class.
     */
    private static void addExample(JPanel panel, Class<?> example)
    {
        final JButton button = new JButton(example.getSimpleName().substring(3));
        button.addActionListener(event ->
        {
            setEnabled(panel.getComponents(), false);
            try
            {
                UtilReflection.getMethod(example, MAIN, new Object[]
                {
                    new String[0]
                });
                EXECUTOR.execute(() -> waitClose(panel));
            }
            catch (final Exception exception)
            {
                Verbose.exception(exception);
                SwingUtilities.invokeLater(() -> setEnabled(panel.getComponents(), true));
            }
        });
        panel.add(button);
    }

    /**
     * Wait for engine end. Restore panel buttons.
     * 
     * @param panel The panel reference.
     */
    private static void waitClose(JPanel panel)
    {
        while (Engine.isStarted())
        {
            try
            {
                Thread.sleep(Constant.HUNDRED);
            }
            catch (final InterruptedException exception)
            {
                Thread.currentThread().interrupt();
                Verbose.exception(exception);
                break;
            }
        }
        SwingUtilities.invokeLater(() -> setEnabled(panel.getComponents(), true));
    }
}
