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

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

/**
 *
 */
public class MessageTranslator {
	
	// Language specification
	public static final @NotNull String  PLACEHOLDER_PREFIX             = "${";
	public static final @NotNull String  PLACEHOLDER_SUFFIX             = "}";
	public static final @NotNull String  PLACEHOLDER_VALUE_DELIMITER    = ":";
	public static final          char    PLACEHOLDER_ESCAPE             = '$';
	public static final          boolean THROW_ON_UNDEFINED_PLACEHOLDER = true;
	
	public static final @NotNull String STYLE_PREFIX    = "@{";
	public static final @NotNull String STYLE_SUFFIX    = "}";
	public static final @NotNull String STYLE_SEPARATOR = ":";
	public static final          char   STYLE_ESCAPE    = '@';
	
	public static final @NotNull String ACTION_PREFIX     = "#{";
	public static final @NotNull String ACTION_SUFFIX     = "}";
	public static final @NotNull String ACTION_END        = "{/#}";
	public static final @NotNull String ACTION_SEPARATOR  = ":";
	public static final          char   ACTION_ESCAPE     = '#';
	public static final @NotNull String ACTION_END_ESCAPE = "{//#}";
	
	public static final @NotNull String HOVER_CONTENT_PREFIX        = "(";
	public static final @NotNull String HOVER_CONTENT_SUFFIX        = ")";
	public static final          char   HOVER_CONTENT_SUFFIX_ESCAPE = '\\';
	
	// Shorter usable values
	protected static final @NotNull String ESCAPED_PLACEHOLDER_PREFIX   = PLACEHOLDER_ESCAPE + PLACEHOLDER_PREFIX;
	protected static final @NotNull String ESCAPED_STYLE_PREFIX         = STYLE_ESCAPE + STYLE_PREFIX;
	protected static final @NotNull String ESCAPED_ACTION_PREFIX        = ACTION_ESCAPE + ACTION_PREFIX;
	protected static final @NotNull String ESCAPED_HOVER_CONTENT_SUFFIX = HOVER_CONTENT_SUFFIX_ESCAPE + HOVER_CONTENT_SUFFIX;
	
	protected static final int STYLE_PREFIX_LENGTH         = STYLE_PREFIX.length();
	protected static final int STYLE_SUFFIX_LENGTH         = STYLE_SUFFIX.length();
	protected static final int ACTION_PREFIX_LENGTH        = ACTION_PREFIX.length();
	protected static final int ACTION_SUFFIX_LENGTH        = ACTION_SUFFIX.length();
	protected static final int ACTION_END_LENGTH           = ACTION_END.length();
	protected static final int HOVER_CONTENT_PREFIX_LENGTH = HOVER_CONTENT_PREFIX.length();
	protected static final int HOVER_CONTENT_SUFFIX_LENGTH = HOVER_CONTENT_SUFFIX.length();
	
	/**
	 * Looks for the nearest tag based on index only.
	 */
	protected static int nearestIndex(int a, int b) {
		if(a < 0 && b < 0) return -1;
		if(a < 0) return b;
		if(b < 0) return a;
		return Math.min(a, b);
	}
	
	protected static int nearestStyle(@NotNull String source, int offset) {
		return indexOfUnescaped(source, STYLE_PREFIX, offset, STYLE_ESCAPE);
	}
	
	protected static int nearestAction(@NotNull String source, int offset) {
		return indexOfUnescaped(source, ACTION_PREFIX, offset, ACTION_ESCAPE);
	}
	
	/**
	 * Determines the nearest, unescaped instance of the searched string.
	 */
	protected static int indexOfUnescaped(@NotNull String source, @NotNull String search, int offset, char escape) {
		int index;
		while(true) {
			index = source.indexOf(search, offset);
			if(index > 0 && source.charAt(index - 1) == escape) {
				offset = index + 1;
				continue;
			}
			break;
		}
		return index;
	}
	
	/**
	 * Determines the nearest instance of the search string. Each match of skips will skip a single instance of the search string.
	 */
	protected static int indexOfSkippable(@NotNull String source, @NotNull String search, int offset, @NotNull String skips, char skipsEscape) {
		final int originalOffset = offset;
		
		int index;
		int skip;
		int skipAmount = 0;
		while(true) {
			skip = indexOfUnescaped(source, skips, offset, skipsEscape);
			if(skip != -1) {
				offset = skip + 1;
				skipAmount++;
				continue;
			}
			break;
		}
		offset = originalOffset;
		while(true) {
			index = source.indexOf(search, offset);
			if(index > 0 && skipAmount > 0) {
				offset = index + 1;
				skipAmount--;
				continue;
			}
			break;
		}
		return index;
	}
	
	/**
	 * Strips the string of escape characters.
	 */
	@Contract("null -> null; !null -> !null")
	protected static @Nullable String stripEscapes(@Nullable String content) {
		if(content == null) return null;
		return content
			.replace(ESCAPED_PLACEHOLDER_PREFIX, PLACEHOLDER_PREFIX)
			.replace(ESCAPED_STYLE_PREFIX, STYLE_PREFIX)
			.replace(ESCAPED_ACTION_PREFIX, ACTION_PREFIX)
			.replace(ACTION_END_ESCAPE, ACTION_END);
	}
	
	/**
	 * Strips the string of hover escape character.
	 */
	@Contract("null -> null; !null -> !null")
	protected static @Nullable String stripEscapesHover(@Nullable String content) {
		if(content == null) return null;
		return content
			.replace(ESCAPED_HOVER_CONTENT_SUFFIX, HOVER_CONTENT_SUFFIX);
	}
	
	/**
	 * Escape all tags.
	 */
	public @NotNull String escape(@NotNull String message) {
		return message
			.replace(PLACEHOLDER_PREFIX, ESCAPED_PLACEHOLDER_PREFIX)
			.replace(STYLE_PREFIX, ESCAPED_STYLE_PREFIX)
			.replace(ACTION_PREFIX, ESCAPED_ACTION_PREFIX)
			.replace(ACTION_END, ACTION_END_ESCAPE);
	}
	
	/**
	 * Escape text for inserting as a hover.
	 */
	public @NotNull String escapeHover(@NotNull String hoverText) {
		return hoverText
			.replace(HOVER_CONTENT_SUFFIX, ESCAPED_HOVER_CONTENT_SUFFIX);
	}
	
	protected @NotNull List<Message> parse(@NotNull String message) {
		return this.parse(message, Collections.emptyMap());
	}
	
	protected @NotNull List<Message> parse(@NotNull String message, @NotNull Map<String, Object> context) {
		return this.parse(message, context, null);
	}
	
	/**
	 * Parse a string to a list of messages according to the language specifications.
	 *
	 * @param message   the message to parse
	 * @param context   placeholders
	 * @param with      a consumer to accept for all builders that the parser will create, the consumer is accepted after the parser completed its builder setup
	 * @param flagArray flags to modify the parser's behavior, note this method exists only for internal handling and the flags passed won't be passed to sub-parsers
	 */
	protected @NotNull List<Message> parse(@NotNull String message, @NotNull Map<String, Object> context, @Nullable Consumer<Message.MessageBuilder> with, @NotNull Flag... flagArray) {
		final @NotNull String source = this.getSubstitutor(context).replace(message.trim());
		if(source.isBlank()) return Message.empty();
		final @NotNull Set<Flag> flags = Flag.toSet(flagArray);
		
		final @NotNull List<Message> messages = new ArrayList<>();
		
		final boolean stylesDisabled  = flags.contains(Flag.DISABLE_STYLES);
		final boolean actionsDisabled = flags.contains(Flag.DISABLE_ACTIONS);
		
		int nextStyle  = stylesDisabled ? -1 : nearestStyle(source, 0);
		int nextAction = actionsDisabled ? -1 : nearestAction(source, 0);
		
		final int nearestTag = nearestIndex(nextStyle, nextAction);
		if(nearestTag == -1) {
			final @NotNull Message.MessageBuilder builder = Message
				.builder()
				.content(stripEscapes(source));
			if(with != null) with.accept(builder);
			return List.of(builder.build());
		}
		if(nearestTag > 0) {
			final @NotNull Message.MessageBuilder builder = Message
				.builder()
				.content(stripEscapes(source.substring(0, nearestTag)));
			if(with != null) with.accept(builder);
			messages.add(builder.build());
		}
		
		int     nextLookup    = 0;
		boolean lastWasAction = false;
		while(true) {
			if((nextAction == -1 || nextStyle < nextAction) && nextStyle != -1) {
				final int open  = nextStyle;
				final int close = source.indexOf(STYLE_SUFFIX, open + STYLE_PREFIX_LENGTH);
				if(close == -1) throw this.generateMissingStyleSuffixException(source, open);
				final @NotNull String[] data = StringUtils.split(source.substring(open + STYLE_PREFIX_LENGTH, close), STYLE_SEPARATOR);
				
				nextLookup = close + STYLE_SUFFIX_LENGTH;
				nextStyle  = nearestStyle(source, nextLookup);
				nextAction = actionsDisabled ? -1 : nearestAction(source, nextLookup);
				
				final int contentEnd = nearestIndex(nextStyle, nextAction);
				final @NotNull Message.MessageBuilder builder = Message
					.builder()
					.content(stripEscapes(source.substring(nextLookup, contentEnd == -1 ? source.length() : contentEnd)));
				
				if(data.length == 1 && data[0].isBlank()) {
					messages.add(builder.build());
					continue;
				}
				
				for(final @NotNull String modifier : data) {
					final @Nullable Message.Color color = Message.Color.matchColor(modifier);
					if(color != null) {
						builder.color(color);
						continue;
					}
					
					final @Nullable Message.Decoration decoration = Message.Decoration.matchDecoration(modifier);
					if(decoration != null) {
						builder.decorate(decoration);
						continue;
					}
					
					throw this.generateUnknownStyleModifierException(source, open, modifier);
				}
				
				if(with != null) with.accept(builder);
				messages.add(builder.build());
				lastWasAction = false;
				continue;
			}
			
			if((nextStyle == -1 || nextAction < nextStyle) && nextAction != -1) {
				final int open = nextAction;
				
				final int hoverOpen = source.indexOf(HOVER_CONTENT_PREFIX, open + ACTION_PREFIX_LENGTH);
				final int hoverClose =
					hoverOpen == -1 ?
					-1 :
					indexOfUnescaped(source, HOVER_CONTENT_SUFFIX, hoverOpen + HOVER_CONTENT_PREFIX_LENGTH, HOVER_CONTENT_SUFFIX_ESCAPE);
				
				final int close = source.indexOf(ACTION_SUFFIX, hoverClose == -1 ? open + ACTION_PREFIX_LENGTH : hoverClose + HOVER_CONTENT_SUFFIX_LENGTH);
				if(close == -1) throw this.generateMissingActionSuffixException(source, open);
				final int end = indexOfSkippable(source, ACTION_END, close + ACTION_SUFFIX_LENGTH, ACTION_PREFIX, ACTION_ESCAPE);
				if(end == -1) throw this.generateMissingActionEndException(source, close);
				
				final @NotNull String[] data = StringUtils.split(source.substring(open + ACTION_PREFIX_LENGTH, close), ACTION_SEPARATOR, 2);
				if(data.length != 2) throw this.generateMissingActionParametersException(source, open);
				
				nextLookup = end + ACTION_END_LENGTH;
				nextStyle  = stylesDisabled ? -1 : nearestStyle(source, nextLookup);
				nextAction = nearestAction(source, nextLookup);
				
				final @Nullable Message.Click.Action clickAction = Message.Click.Action.matchClickAction(data[0]);
				final @Nullable Message.Click<?> clickEvent =
					clickAction == null ? null :
					switch(clickAction) {
						case CHANGE_PAGE -> {
							try {
								yield Message.Click.changePage(Integer.parseInt(data[1]));
							} catch(NumberFormatException ex) {
								throw this.generateActionPageNumberFormatException(source, open, data[1], ex);
							}
						}
						case COPY_TO_CLIPBOARD -> Message.Click.copyToClipboard(data[1]);
						case OPEN_URL -> Message.Click.openUrl(data[1]);
						case OPEN_FILE -> Message.Click.openFile(data[1]);
						case RUN_COMMAND -> Message.Click.runCommand(data[1]);
						case SUGGEST_COMMAND -> Message.Click.suggestCommand(data[1]);
					};
				
				final @Nullable Message.Hover.Action hoverAction = Message.Hover.Action.matchHoverAction(data[0]);
				final @Nullable Message.Hover<?> hoverEvent =
					hoverAction == null ? null :
					switch(hoverAction) {
						case SHOW_TEXT -> {
							if(hoverOpen == -1) throw this.generateMissingActionHoverPrefix(source, open);
							if(hoverClose == -1) throw this.generateMissingActionHoverSuffix(source, open);
							if(hoverClose + 1 != close) throw this.generateUnknownActionHoverPost(source, hoverClose, close);
							
							yield Message.Hover.showText(
								this.parse(
									source.substring(hoverOpen + HOVER_CONTENT_PREFIX_LENGTH, hoverClose),
									context,
									builder -> builder.content(stripEscapesHover(builder.content())),
									Flag.DISABLE_ACTIONS
								)
							);
						}
						case SHOW_ENTITY, SHOW_ITEM -> throw new UnsupportedOperationException();
					};
				
				messages.addAll(
					this.parse(
						stripEscapes(source.substring(close + ACTION_SUFFIX_LENGTH, end)),
						context,
						(builder) -> {
							if(with != null) with.accept(builder);
							if(clickEvent != null) builder.clickEvent(clickEvent);
							if(hoverEvent != null) builder.hoverEvent(hoverEvent);
						}
					)
				);
				
				lastWasAction = true;
				continue;
			}
			
			break;
		}
		
		if(lastWasAction && nextLookup < source.length()) {
			final @NotNull Message.MessageBuilder builder = Message.builder().content(stripEscapes(source.substring(nextLookup)));
			if(with != null) with.accept(builder);
			messages.add(builder.build());
		}
		
		return Collections.unmodifiableList(messages);
	}
	
	private @NotNull MessageFormattingException generateMissingStyleSuffixException(@NotNull String source, int position) {
		return new MessageFormattingException(source, position + STYLE_PREFIX_LENGTH, "style tag missing a close brace");
	}
	
	private @NotNull MessageFormattingException generateUnknownStyleModifierException(@NotNull String source, int position, @NotNull String modifier) {
		return new MessageFormattingException(source, position + STYLE_PREFIX_LENGTH, String.format("style tag contains an unrecognized modifier '%s'", modifier));
	}
	
	private @NotNull MessageFormattingException generateMissingActionSuffixException(@NotNull String source, int position) {
		return new MessageFormattingException(source, position + ACTION_PREFIX_LENGTH, "action tag missing a close brace");
	}
	
	private @NotNull MessageFormattingException generateMissingActionEndException(@NotNull String source, int position) {
		return new MessageFormattingException(source, position + ACTION_SUFFIX_LENGTH, "action tag is missing an end branch");
	}
	
	private @NotNull MessageFormattingException generateMissingActionParametersException(@NotNull String source, int position) {
		return new MessageFormattingException(source, position + ACTION_PREFIX_LENGTH, "action tag is missing parameters");
	}
	
	private @NotNull MessageFormattingException generateActionPageNumberFormatException(@NotNull String source, int position, @NotNull String tried, @NotNull Throwable cause) {
		return new MessageFormattingException(source, position + ACTION_PREFIX_LENGTH, String.format("unrecognized number in page change '%s'", tried), cause);
	}
	
	private @NotNull MessageFormattingException generateMissingActionHoverPrefix(String source, int position) {
		return new MessageFormattingException(source, position + ACTION_PREFIX_LENGTH, "action tag is missing hover parenthesis open");
	}
	
	private @NotNull MessageFormattingException generateMissingActionHoverSuffix(String source, int position) {
		return new MessageFormattingException(source, position + ACTION_PREFIX_LENGTH, "action tag is missing hover parenthesis close");
	}
	
	private @NotNull MessageFormattingException generateUnknownActionHoverPost(String source, int hoverCloseAt, int closeAt) {
		return new MessageFormattingException(source, hoverCloseAt + HOVER_CONTENT_SUFFIX_LENGTH,
		                                      String.format("action tag must end immediately after hover parenthesis close, expected '%s' got '%s'",
		                                                    ACTION_SUFFIX,
		                                                    source.substring(hoverCloseAt + HOVER_CONTENT_SUFFIX_LENGTH, closeAt)
		                                      ));
	}
	
	public @NotNull StringSubstitutor getSubstitutor(@NotNull Map<String, Object> context) {
		return new StringSubstitutor(Collections.unmodifiableMap(context))
			.setVariablePrefix(PLACEHOLDER_PREFIX)
			.setVariableSuffix(PLACEHOLDER_SUFFIX)
			.setValueDelimiter(PLACEHOLDER_VALUE_DELIMITER)
			.setEscapeChar(PLACEHOLDER_ESCAPE)
			.setEnableUndefinedVariableException(THROW_ON_UNDEFINED_PLACEHOLDER);
	}
	
	public enum Flag {
		DISABLE_STYLES,
		DISABLE_ACTIONS;
		
		public static @NotNull Set<Flag> toSet(@NotNull Flag[] flags) {
			return Sets.newHashSet(flags);
		}
		
	}
	
}
