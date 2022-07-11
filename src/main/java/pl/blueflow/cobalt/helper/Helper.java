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

package pl.blueflow.cobalt.helper;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class Helper {
	
	private Helper() {
	}
	
	/**
	 * Returns a resource file.
	 * @return if null is passed in then returns null, otherwise the result of {@link ClassLoader#getResourceAsStream(String)}
	 */
	@Contract("_, null -> null; _, !null -> _")
	public static @Nullable InputStream getResource(@NotNull Class<?> clazz, @Nullable String name) {
		if(name == null) return null;
		return clazz.getClassLoader().getResourceAsStream(name);
	}
	
	/**
	 * Saves the default stream to the specified file, possibly overriding if replaceExisting is set to true.
	 */
	@SneakyThrows
	public static void saveDefault(@Nullable InputStream defaults, @NotNull File file, boolean replaceExisting) {
		if(defaults != null) {
			if(FileUtils.readFileToString(file, StandardCharsets.UTF_8).isBlank() || replaceExisting) {
				FileUtils.copyInputStreamToFile(defaults, file);
			}
		}
	}
	
}
