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

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTranslatorTest {
	
	private final @NotNull MessageTranslator translator = new MessageTranslator();
	
	@Test
	public void placeholder() {
		assertEquals(
			List.of(Message.builder().content("Hello, World!").build()),
			this.translator.parse("Hello, ${hello}!", Map.of("hello", "World"))
		);
	}
	
	@Test
	public void placeholderEscape() {
		assertEquals(
			List.of(Message.builder().content("Hello, ${hello}!").build()),
			this.translator.parse("Hello, $${hello}!", Map.of("hello", "World"))
		);
	}
	
	@Test
	public void missingPlaceholder() {
		assertThrows(IllegalArgumentException.class, () -> this.translator.parse("Hello, ${hello}!"));
	}
	
	@Test
	public void messageEscape() {
		assertEquals(
			List.of(Message.builder().content("${variable} #{action} @{red}").build()),
			this.translator.parse(this.translator.escape("${variable} #{action} @{red}"))
		);
	}
	
	@Test
	public void hoverEscape() {
		assertEquals(
			List.of(
				Message
					.builder()
					.content("Hover")
					.hoverEvent(
						Message.Hover.showText(List.of(
							Message
								.builder()
								.content("Hello (World)!")
								.build()
						))
					)
					.build()
			),
			this.translator.parse("#{text:(${hoverText})}Hover{/#}", Map.of("hoverText", this.translator.escapeHover("Hello (World)!")))
		);
	}
	
	@Test
	public void namedColor() {
		assertEquals(
			List.of(
				Message.builder().content("Hello, World!").color(Message.Color.RED).build()
			),
			this.translator.parse("@{red}Hello, World!")
		);
	}
	
	@Test
	public void hexColor() {
		assertEquals(
			List.of(
				Message.builder().content("Hello, World!").color(Message.Color.fromHexString("#51d51d")).build()
			),
			this.translator.parse("@{#51d51d}Hello, World!")
		);
	}
	
	@Test
	public void decimalColor() {
		assertEquals(
			List.of(
				Message.builder().content("Hello, World!").color(Message.Color.color(5105015)).build()
			),
			this.translator.parse("@{5105015}Hello, World!")
		);
	}
	
	@Test
	public void colorCache() {
		assertSame(Message.Color.fromHexString("#abcdef"), Message.Color.fromHexString("#abcdef"));
	}
	
	@Test
	public void decoration() {
		assertEquals(
			List.of(
				Message.builder().content("Hello, World!").decorate(Message.Decoration.BOLD).build()
			),
			this.translator.parse("@{bold}Hello, World!")
		);
	}
	
	@Test
	public void manyStyling() {
		assertEquals(
			List.of(
				Message.builder().content("Hello").build(),
				Message.builder().content(", ").color(Message.Color.fromHexString("#51d51d")).build(),
				Message.builder().content("World!").decorate(Message.Decoration.BOLD).decorate(Message.Decoration.UNDERLINED).build()
			),
			this.translator.parse("Hello@{#51d51d}, @{bold:underline}World!")
		);
	}
	
	@Test
	public void resetStyle() {
		assertEquals(
			List.of(
				Message.builder().content("Hello, ").color(Message.Color.RED).build(),
				Message.builder().content("World!").build()
			),
			this.translator.parse("@{red}Hello, @{}World!")
		);
	}
	
	@Test
	public void link() {
		assertEquals(
			List.of(
				Message.builder().content("Hello, World!").clickEvent(Message.Click.openUrl("https://example.com")).build()
			),
			this.translator.parse("#{link:https://example.com}Hello, World!{/#}")
		);
	}
	
	@Test
	public void coloredLink() {
		assertEquals(
			List.of(
				Message.builder().content("Hello, World!").color(Message.Color.RED).clickEvent(Message.Click.openUrl("https://example.com")).build()
			),
			this.translator.parse("#{link:https://example.com}@{red}Hello, World!{/#}")
		);
	}
	
	@Test
	public void hover() {
		assertEquals(
			List.of(
				Message
					.builder()
					.content("Hello, World!")
					.hoverEvent(Message.Hover.showText(
						List.of(
							Message.builder().content("Hi there!").color(Message.Color.RED).build()
						)
					))
					.build()
			),
			this.translator.parse("#{text:(@{red}Hi there!)}Hello, World!{/#}")
		);
	}
	
	@Test
	public void linkWithHover() {
		final @NotNull Message.Hover<List<Message>> hoverEvent = Message.Hover.showText(List.of(
			Message.builder().content("Hello, World!").color(Message.Color.RED).build()
		));
		final @NotNull Message.Click<String> clickEvent = Message.Click.openUrl("https://example.com");
		assertEquals(
			List.of(
				Message.builder().hoverEvent(hoverEvent).clickEvent(clickEvent).content("Click me with hover!").build()
			),
			this.translator.parse("#{text:(@{red}Hello, World!)}#{link:https://example.com}Click me with hover!{/#}{/#}")
		);
	}
	
	@Test
	public void forgottenAfterAction() {
		assertEquals(
			List.of(
				Message
					.builder()
					.content("Click me!")
					.clickEvent(Message.Click.openUrl("https://example.com"))
					.build(),
				Message
					.builder()
					.content(" Click left!")
					.build()
			),
			this.translator.parse("#{link:https://example.com}Click me!{/#} Click left!")
		);
	}
	
	@Test
	public void rawEscape() {
		assertEquals("$${variable} @@{color} ##{action} (parenthesis)", this.translator.escape("${variable} @{color} #{action} (parenthesis)"));
		assertEquals("${variable} @{color} #{action} (parenthesis\\)", this.translator.escapeHover("${variable} @{color} #{action} (parenthesis)"));
	}
	
	@Test
	public void rawStripEscape() {
		assertEquals("${variable} @{color} #{action} (parenthesis)", MessageTranslator.stripEscapes("$${variable} @@{color} ##{action} (parenthesis)"));
		assertEquals("${variable} @{color} #{action} (parenthesis)", MessageTranslator.stripEscapesHover("${variable} @{color} #{action} (parenthesis\\)"));
	}
	
}
