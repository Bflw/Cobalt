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

import java.util.List;

public interface ListAccessors {
	
	@Nullable List<Boolean> getBooleanList(@NotNull String key);
	
	@Contract("_, !null -> !null; _, null -> _")
	@Nullable List<Boolean> getBooleanList(@NotNull String key, @Nullable List<Boolean> onEmpty);
	
	@Nullable List<Integer> getIntList(@NotNull String key);
	
	@Contract("_, !null -> !null; _, null -> _")
	@Nullable List<Integer> getIntList(@NotNull String key, @Nullable List<Integer> onEmpty);
	
	@Nullable List<Long> getLongList(@NotNull String key);
	
	@Contract("_, !null -> !null; _, null -> _")
	@Nullable List<Long> getLongList(@NotNull String key, @Nullable List<Long> onEmpty);
	
	@Nullable List<Float> getFloatList(@NotNull String key);
	
	@Contract("_, !null -> !null; _, null -> _")
	@Nullable List<Float> getFloatList(@NotNull String key, @Nullable List<Float> onEmpty);
	
	@Nullable List<Double> getDoubleList(@NotNull String key);
	
	@Contract("_, !null -> !null; _, null -> _")
	@Nullable List<Double> getDoubleList(@NotNull String key, @Nullable List<Double> onEmpty);
	
	@Nullable List<String> getStringList(@NotNull String key);
	
	@Contract("_, !null -> !null; _, null -> _")
	@Nullable List<String> getStringList(@NotNull String key, @Nullable List<String> onEmpty);
	
	
	void setBooleanList(@NotNull String key, @Nullable List<Boolean> value);
	
	void setIntList(@NotNull String key, @Nullable List<Integer> value);
	
	void setLongList(@NotNull String key, @Nullable List<Long> value);
	
	void setFloatList(@NotNull String key, @Nullable List<Float> value);
	
	void setDoubleList(@NotNull String key, @Nullable List<Double> value);
	
	void setStringList(@NotNull String key, @Nullable List<String> value);
	
	
//	@Contract("_, !null -> !null; _, null -> _")
//	<T> @Nullable List<T> getList(@NotNull String key, @Nullable List<T> onEmpty);
	
}
