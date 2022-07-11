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

package pl.blueflow.cobalt.message;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class MessageFormattingException extends IllegalArgumentException {
	
	private final @NotNull String source;
	private final int position;
	
	public MessageFormattingException(@NotNull String source, int position, @NotNull String message) {
		this(source, position, message, null);
	}
	
	public MessageFormattingException(@NotNull String source, int position, @NotNull String message, @Nullable Throwable cause) {
		super(String.format("improperly formatted message: %s; source: %s", message, generateSource(source, position)), cause);
		this.source = source;
		this.position = position;
	}
	
	private static @NotNull String generateSource(@NotNull String source, int position) {
		final @NotNull StringBuilder builder = new StringBuilder(source);
		builder.insert(position, "<here>");
		return builder.toString();
	}
	
}
