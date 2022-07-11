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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

interface PrimitiveAccessors {
	
	@Nullable Boolean getBoolean(@NotNull String key);
	
	boolean getBoolean(@NotNull String key, boolean onEmpty);
	
	@Nullable Integer getInt(@NotNull String key);
	
	int getInt(@NotNull String key, int onEmpty);
	
	@Nullable Long getLong(@NotNull String key);
	
	long getLong(@NotNull String key, long onEmpty);
	
	@Nullable Float getFloat(@NotNull String key);
	
	float getFloat(@NotNull String key, float onEmpty);
	
	@Nullable Double getDouble(@NotNull String key);
	
	double getDouble(@NotNull String key, double onEmpty);
	
	void setBoolean(@NotNull String key, @Nullable Boolean value);
	
	void setInt(@NotNull String key, @Nullable Integer value);
	
	void setLong(@NotNull String key, @Nullable Long value);
	
	void setFloat(@NotNull String key, @Nullable Float value);
	
	void setDouble(@NotNull String key, @Nullable Double value);
	
}
