/*
 * Cobalt - a Minecraft Bungeecord and Bukkit library.
 * Copyright (c) 2022.  Oliwier Miodun  <naczs@n-mind.pl>
 * Copyright (c) 2022.  Blueflow        <support@blueflow.pl>
 *
 * This file is part of Cobalt.
 *
 * Cobalt is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Cobalt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Cobalt.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package pl.blueflow.cobalt;

import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import pl.blueflow.cobalt.config.CobaltConfiguration;

/**
 * Basic interface that's used in shared classes,
 * if you wish to create your own implementation, extend this class.
 */
public interface CobaltPlugin {
	
	/**
	 * The plugin's loading configuration is to be set here, the default implementation calls this in
	 * {@link org.bukkit.plugin.java.JavaPlugin#onLoad()}/{@link net.md_5.bungee.api.plugin.Plugin#onLoad()}.<br>
	 * Some configuration values may be changed while the plugin is already running, an example of that is the {@link BukkitCobaltPlugin#throwOnNativeConfigAccess}.
	 */
	void configure();
	
	/**
	 * Same as {@link org.bukkit.plugin.java.JavaPlugin#onEnable()}/{@link net.md_5.bungee.api.plugin.Plugin#onEnable()},
	 * implementations may define the aforementioned methods as final, if so, the implementation must call this method inside them.
	 */
	void start();
	
	/**
	 * Same as {@link org.bukkit.plugin.java.JavaPlugin#onDisable()}/{@link net.md_5.bungee.api.plugin.Plugin#onDisable()},
	 * implementations may define the aforementioned methods as final, if so, the implementation must call this method inside them.
	 */
	void stop();
	
	/**
	 * Returns the plugin's configuration.<br>
	 * The implementation should make this method be available during {@link #start()}
	 * and load it only once during {@link org.bukkit.plugin.java.JavaPlugin#onEnable()}/{@link net.md_5.bungee.api.plugin.Plugin#onEnable()}.
	 */
	default @NotNull CobaltConfiguration getConfiguration() {
		throw new NotImplementedException();
	}
	
}
