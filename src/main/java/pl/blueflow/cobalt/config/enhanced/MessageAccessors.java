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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

// TODO add javadoc
public interface MessageAccessors<T> {
	
	/**
	 * @see #getMessage(String, Map, String)
	 */
	@Nullable T getMessage(@NotNull String key);
	
	/**
	 * @see #getMessage(String, Map, String)
	 */
	@Nullable T getMessage(@NotNull String key, @Nullable Map<String, Object> context);
	
	/**
	 * Retrieves a string and parses it according to the {@link pl.blueflow.cobalt.message.MessageTranslator} specification
	 * into the implementation's {@link T} component.<br>
	 * The implementation may ignore onEmpty parameter's placeholders.
	 */
	@Contract("_, _, !null -> !null; _, _, null -> _")
	@Nullable T getMessage(@NotNull String key, @Nullable Map<String, Object> context, @Nullable T onEmpty);
	
	/**
	 * Retrieves a string and parses it according to the {@link pl.blueflow.cobalt.message.MessageTranslator} specification
	 * into the implementation's {@link T} component.<br>
	 * The implementation must not ignore onEmpty parameter's placeholders.
	 */
	@Contract("_, _, !null -> !null; _, _, null -> _")
	@Nullable T getMessage(@NotNull String key, @Nullable Map<String, Object> context, @Nullable String onEmpty);
	
	@Nullable List<T> getMessageList(@NotNull String key);
	
	@Nullable List<T> getMessageList(@NotNull String key, @Nullable Map<String, Object> context);
	
	@Contract("_, _, !null -> !null; _, _, null -> _")
	@Nullable List<T> getMessageListPremade(@NotNull String key, @Nullable Map<String, Object> context, @Nullable List<T> onEmpty);
	
	@Contract("_, _, !null -> !null; _, _, null -> _")
	@Nullable List<T> getMessageList(@NotNull String key, @Nullable Map<String, Object> context, @Nullable List<String> onEmpty);
	
	@Nullable Map<String, T> getMessageMap(@NotNull String key);
	
	@Nullable Map<String, T> getMessageMap(@NotNull String key, @Nullable Map<String, Object> context);
	
	@Contract("_, _, !null -> !null; _, _, null -> _")
	@Nullable Map<String, T> getMessageMapPremade(@NotNull String key, @Nullable Map<String, Object> context, @Nullable Map<String, T> onEmpty);
	
	@Contract("_, _, !null -> !null; _, _, null -> _")
	@Nullable Map<String, T> getMessageMap(@NotNull String key, @Nullable Map<String, Object> context, @Nullable Map<String, String> onEmpty);
	
}
