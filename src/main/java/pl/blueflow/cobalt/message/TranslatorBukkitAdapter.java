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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public final class TranslatorBukkitAdapter extends MessageTranslator {
	
	private static final @NotNull Map<TextDecoration, TextDecoration.State> DEFAULT_DECORATION_STATE = Arrays
		.stream(TextDecoration.values())
		.collect(Collectors.toUnmodifiableMap(d -> d, d -> TextDecoration.State.FALSE));
	
	private static final @NotNull Map<Message.Decoration, TextDecoration> DECORATION_CACHE = new HashMap<>();
	
	private static @NotNull Component toComponent(@NotNull List<Message> messages) {
		final @NotNull TextComponent.Builder builder = Component.text();
		for(final @NotNull Message message : messages) {
			builder.append(toComponent(message));
		}
		return builder.build();
	}
	
	private static @NotNull Component toComponent(@NotNull Message message) {
		return Component
			.text(message.getContent())
			.color(convertColor(message.getColor()))
			.decorations(convertDecorations(message.getDecorations()))
			.clickEvent(convertClickEvent(message.getClickEvent()))
			.hoverEvent(convertHoverEvent(message.getHoverEvent()));
	}
	
	@Contract("null -> null; !null -> !null")
	private static @Nullable TextColor convertColor(@Nullable Message.Color color) {
		if(color == null) return null;
		return TextColor.color(color.getValue());
	}
	
	private static @NotNull Map<TextDecoration, TextDecoration.State> convertDecorations(@NotNull Collection<Message.Decoration> decorations) {
		final @NotNull Map<TextDecoration, TextDecoration.State> map = decorations
			.stream()
			.map(decoration -> DECORATION_CACHE.computeIfAbsent(decoration, d -> TextDecoration.valueOf(d.name())))
			.collect(Collectors.toMap(d -> d, d -> TextDecoration.State.TRUE)
			);
		DEFAULT_DECORATION_STATE.forEach(map::putIfAbsent);
		
		return map;
	}
	
	@Contract("null -> null; !null -> !null")
	private static @Nullable ClickEvent convertClickEvent(@Nullable Message.Click<?> click) {
		if(click == null) return null;
		return switch(click.getAction()) {
			case CHANGE_PAGE -> ClickEvent.changePage((int) click.getValue());
			case COPY_TO_CLIPBOARD -> ClickEvent.copyToClipboard((String) click.getValue());
			case OPEN_FILE -> ClickEvent.openFile((String) click.getValue());
			case OPEN_URL -> ClickEvent.openUrl((String) click.getValue());
			case RUN_COMMAND -> ClickEvent.runCommand((String) click.getValue());
			case SUGGEST_COMMAND -> ClickEvent.suggestCommand((String) click.getValue());
		};
	}
	
	@Contract("null -> null; !null -> !null")
	private static @Nullable HoverEventSource<?> convertHoverEvent(@Nullable Message.Hover<?> hover) {
		if(hover == null) return null;
		return switch(hover.getAction()) {
			case SHOW_TEXT -> //noinspection unchecked
				HoverEvent.showText(toComponent((List<Message>) hover.getValue()));
			case SHOW_ENTITY, SHOW_ITEM -> throw new UnsupportedOperationException();
		};
	}
	
	public @NotNull Component translate(@NotNull String source) {
		return this.translate(source, null);
	}
	
	public @NotNull Component translate(@NotNull String source, @Nullable Map<String, Object> context) {
		return toComponent(this.parse(source, context != null ? context : Collections.emptyMap(), null));
	}
	
}
