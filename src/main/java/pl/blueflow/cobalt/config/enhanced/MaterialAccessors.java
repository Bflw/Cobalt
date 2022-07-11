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

package pl.blueflow.cobalt.config.enhanced;

import org.bukkit.Material;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface MaterialAccessors {
	
	@Nullable Material getMaterial(@NotNull String key);
	
	@Contract("_, !null -> !null; _, null -> _")
	@Nullable Material getMaterial(@NotNull String key, @Nullable Material onEmpty);
	
	@Nullable List<Material> getMaterialList(@NotNull String key);
	
	@Contract("_, !null -> !null; _, null -> _")
	@Nullable List<Material> getMaterialList(@NotNull String key, @Nullable List<Material> onEmpty);
	
	@Nullable Map<String, Material> getMaterialMap(@NotNull String key);
	
	@Contract("_, !null -> !null; _, null -> _")
	@Nullable Map<String, Material> getMaterialMap(@NotNull String key, @Nullable Map<String, Material> onEmpty);
	
	void setMaterial(@NotNull String key, @Nullable Material value);
	
	void setMaterialList(@NotNull String key, @Nullable List<Material> value);
	
	void setMaterialMap(@NotNull String key, @Nullable Map<String, Material> value);
	
}
