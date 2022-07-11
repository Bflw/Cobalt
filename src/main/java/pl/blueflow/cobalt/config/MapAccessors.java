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

import java.util.Map;

public interface MapAccessors {
	
	@Nullable Map<String, Boolean> getBooleanMap(@NotNull String key);
	
	@Contract("_, !null -> !null; _, null -> _")
	@Nullable Map<String, Boolean> getBooleanMap(@NotNull String key, @Nullable Map<String, Boolean> onEmpty);
	
	@Nullable Map<String, Integer> getIntMap(@NotNull String key);
	
	@Contract("_, !null -> !null; _, null -> _")
	@Nullable Map<String, Integer> getIntMap(@NotNull String key, @Nullable Map<String, Integer> onEmpty);
	
	@Nullable Map<String, Long> getLongMap(@NotNull String key);
	
	@Contract("_, !null -> !null; _, null -> _")
	@Nullable Map<String, Long> getLongMap(@NotNull String key, @Nullable Map<String, Long> onEmpty);
	
	@Nullable Map<String, Float> getFloatMap(@NotNull String key);
	
	@Contract("_, !null -> !null; _, null -> _")
	@Nullable Map<String, Float> getFloatMap(@NotNull String key, @Nullable Map<String, Float> onEmpty);
	
	@Nullable Map<String, Double> getDoubleMap(@NotNull String key);
	
	@Contract("_, !null -> !null; _, null -> _")
	@Nullable Map<String, Double> getDoubleMap(@NotNull String key, @Nullable Map<String, Double> onEmpty);
	
	@Nullable Map<String, String> getStringMap(@NotNull String key);
	
	@Contract("_, !null -> !null; _, null -> _")
	@Nullable Map<String, String> getStringMap(@NotNull String key, @Nullable Map<String, String> onEmpty);
	
	
	void setBooleanMap(@NotNull String key, @Nullable Map<String, Boolean> value);
	
	void setIntMap(@NotNull String key, @Nullable Map<String, Integer> value);
	
	void setLongMap(@NotNull String key, @Nullable Map<String, Long> value);
	
	void setFloatMap(@NotNull String key, @Nullable Map<String, Float> value);
	
	void setDoubleMap(@NotNull String key, @Nullable Map<String, Double> value);
	
	void setStringMap(@NotNull String key, @Nullable Map<String, String> value);
	
	
//	@Contract("_, !null -> !null; _, null -> _")
//	<V> @Nullable Map<String, V> getMap(@NotNull String key, @Nullable Map<String, V> onEmpty);
	
}
