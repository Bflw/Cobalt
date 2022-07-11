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

package pl.blueflow.cobalt.config;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * An interface for the Cobalt configuration.<br>
 * If you modify the configuration with setters and wish to save the changes, you must persist them using {@link #save()}.
 */
public interface CobaltConfiguration extends
                                     PrimitiveAccessors,
                                     StringAccessors,
                                     ListAccessors,
                                     MapAccessors {
	
	/**
	 * Return the map keys.<br>
	 * The implementation may return only the keys in the sources.
	 */
	@NotNull Set<String> keys();
	
	/**
	 * Returns the map keys inside the path.<br>
	 * The implementation may return only the keys in the sources.
	 */
	@NotNull Set<String> keys(@NotNull String path);
	
	/**
	 * Generic method to get configuration values.
	 */
	@Contract("_, !null -> !null; _, null -> _")
	<T> @Nullable T get(@NotNull String key, @Nullable T onEmpty);
	
	/**
	 * Generic method to set configuration values.
	 */
	void set(@NotNull String key, @Nullable Object value);
	
	/**
	 * Saves the current configuration to the file. The implementation can choose whether to save default values or not.
	 * Warning: Most implementations (including the default one) will discard any comments and formatting that are currently in the file.
	 */
	void save();
	
}
