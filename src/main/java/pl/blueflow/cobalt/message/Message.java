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

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A bridge between inconvertible Bungeecord and Bukkit components.
 */
@Getter
@Builder
@EqualsAndHashCode
@ToString
public final class Message {
	
	private final @NotNull String content;
	
	private final @Nullable Color color;
	
	@Singular("decorate")
	private final @NotNull List<Decoration> decorations;
	
	private final @Nullable Click<?> clickEvent;
	private final @Nullable Hover<?> hoverEvent;
	
	public static @NotNull List<Message> empty() {
		return List.of(Message.text(""));
	}
	
	public static @NotNull Message text(@NotNull String text) {
		return Message.builder().content(text).build();
	}
	
	public @NotNull Collection<Decoration> getDecorations() {
		return new HashSet<>(this.decorations);
	}
	
	public static final class MessageBuilder {
		public @NotNull String content() {
			return this.content;
		}
		
		public @NotNull MessageBuilder content(@NotNull String content) {
			this.content = content;
			return this;
		}
	}
	
	@Getter
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	@EqualsAndHashCode
	public static final class Color {
		private final int value;
		
		private static final @NotNull Map<Integer, Color> CACHE = new HashMap<>();
		
		public static final @NotNull Color BLACK        = color(0x000000);
		public static final @NotNull Color DARK_BLUE    = color(0x0000aa);
		public static final @NotNull Color DARK_GREEN   = color(0x00aa00);
		public static final @NotNull Color DARK_AQUA    = color(0x00aaaa);
		public static final @NotNull Color DARK_RED     = color(0xaa0000);
		public static final @NotNull Color DARK_PURPLE  = color(0xaa00aa);
		public static final @NotNull Color GOLD         = color(0xffaa00);
		public static final @NotNull Color GRAY         = color(0xaaaaaa);
		public static final @NotNull Color DARK_GRAY    = color(0x555555);
		public static final @NotNull Color BLUE         = color(0x5555ff);
		public static final @NotNull Color GREEN        = color(0x55ff55);
		public static final @NotNull Color AQUA         = color(0x55ffff);
		public static final @NotNull Color RED          = color(0xff5555);
		public static final @NotNull Color LIGHT_PURPLE = color(0xff55ff);
		public static final @NotNull Color YELLOW       = color(0xffff55);
		public static final @NotNull Color WHITE        = color(0xffffff);
		
		public static @Nullable Message.Color matchColor(@NotNull String color) {
			@Nullable Message.Color hexMatch = fromHexString(color);
			if(hexMatch != null) return hexMatch;
			
			try {
				int decimal = Integer.parseInt(color);
				return Message.Color.color(decimal);
			} catch(NumberFormatException ignored) {
			}
			
			return switch(color.toLowerCase()) {
				case "black" -> Message.Color.BLACK;
				case "dark-blue", "dark_blue" -> Message.Color.DARK_BLUE;
				case "dark-green", "dark_green" -> Message.Color.DARK_GREEN;
				case "dark-aqua", "dark_aqua" -> Message.Color.DARK_AQUA;
				case "dark-red", "dark_red" -> Message.Color.DARK_RED;
				case "dark-purple", "dark_purple" -> Message.Color.DARK_PURPLE;
				case "gold", "dark-yellow", "dark_yellow" -> Message.Color.GOLD;
				case "gray", "grey" -> Message.Color.GRAY;
				case "dark-gray", "dark-grey", "dark_gray", "dark_grey" -> Message.Color.DARK_GRAY;
				case "blue" -> Message.Color.BLUE;
				case "green" -> Message.Color.GREEN;
				case "aqua" -> Message.Color.AQUA;
				case "red" -> Message.Color.RED;
				case "light-purple" -> Message.Color.LIGHT_PURPLE;
				case "yellow" -> Message.Color.YELLOW;
				case "white" -> Message.Color.WHITE;
				default -> null;
			};
		}
		
		public static @Nullable Color fromHexString(@NotNull String hex) {
			if(hex.startsWith("#")) {
				try {
					return color(Integer.parseInt(hex.substring(1), 16));
				} catch(NumberFormatException ex) {
					return null;
				}
			}
			return null;
		}
		
		public static @NotNull Color color(int decimal) {
			return CACHE.computeIfAbsent(decimal, Color::new);
		}
		
		public @NotNull String toString() {
			return String.format("#%s", Integer.toHexString(this.value));
		}
		
	}
	
	public enum Decoration {
		OBFUSCATED,
		BOLD,
		STRIKETHROUGH,
		UNDERLINED,
		ITALIC;
		
		public static @Nullable Message.Decoration matchDecoration(@NotNull String decoration) {
			return switch(decoration.toLowerCase()) {
				case "obfuscate", "obfuscated", "matrix" -> Message.Decoration.OBFUSCATED;
				case "bold" -> Message.Decoration.BOLD;
				case "strikethrough", "strike" -> Message.Decoration.STRIKETHROUGH;
				case "underline", "underlined" -> Message.Decoration.UNDERLINED;
				case "italic", "italics" -> Message.Decoration.ITALIC;
				default -> null;
			};
		}
	}
	
	@Getter
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	@EqualsAndHashCode
	@ToString
	public static final class Click<V> {
		
		private final @NotNull Action action;
		private final @NotNull V      value;
		
		public static @NotNull Click<Integer> changePage(int page) {
			return new Click<>(Action.CHANGE_PAGE, page);
		}
		
		public static @NotNull Click<String> copyToClipboard(@NotNull String clipboard) {
			return new Click<>(Action.COPY_TO_CLIPBOARD, clipboard);
		}
		
		public static @NotNull Click<String> openFile(@NotNull String file) {
			return new Click<>(Action.OPEN_FILE, file);
		}
		
		public static @NotNull Click<String> openUrl(@NotNull String url) {
			return new Click<>(Action.OPEN_URL, url);
		}
		
		public static @NotNull Click<String> runCommand(@NotNull String command) {
			return new Click<>(Action.RUN_COMMAND, command);
		}
		
		public static @NotNull Click<String> suggestCommand(@NotNull String command) {
			return new Click<>(Action.SUGGEST_COMMAND, command);
		}
		
		public enum Action {
			CHANGE_PAGE,
			COPY_TO_CLIPBOARD,
			OPEN_FILE,
			OPEN_URL,
			RUN_COMMAND,
			SUGGEST_COMMAND;
			
			public static @Nullable Message.Click.Action matchClickAction(@NotNull String action) {
				return switch(action.toLowerCase()) {
					case "change-page", "change_page", "page" -> Message.Click.Action.CHANGE_PAGE;
					case "copy-to-clipboard", "copy_to_clipboard", "copy", "clipboard" -> Message.Click.Action.COPY_TO_CLIPBOARD;
					case "open-file", "open_file", "file" -> Message.Click.Action.OPEN_FILE;
					case "open-url", "open_url", "url", "link" -> Message.Click.Action.OPEN_URL;
					case "run-command", "run_command", "command", "run" -> Message.Click.Action.RUN_COMMAND;
					case "suggest-command", "suggest_command", "suggest" -> Message.Click.Action.SUGGEST_COMMAND;
					default -> null;
				};
			}
		}
	}
	
	@Getter
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	@EqualsAndHashCode
	@ToString
	public static final class Hover<V> {
		
		private final @NotNull Action action;
		private final @NotNull V      value;
		
		public static @NotNull Hover<List<Message>> showText(@NotNull List<Message> text) {
			return new Hover<>(Action.SHOW_TEXT, text);
		}
		
		public static @NotNull Hover<?> showEntity(@NotNull String params) {
			throw new UnsupportedOperationException();
		}
		
		public static @NotNull Hover<?> showItem(@NotNull String params) {
			throw new UnsupportedOperationException();
		}
		
		public enum Action {
			SHOW_TEXT,
			SHOW_ENTITY,
			SHOW_ITEM;
			
			public static @Nullable Message.Hover.Action matchHoverAction(@NotNull String action) {
				return switch(action.toLowerCase()) {
					case "text", "show-text", "show_text" -> Message.Hover.Action.SHOW_TEXT;
					case "entity", "show-entity", "show_entity" -> Message.Hover.Action.SHOW_ENTITY;
					case "item", "show-item", "show_item" -> Message.Hover.Action.SHOW_ITEM;
					default -> null;
				};
			}
		}
	}
	
}
