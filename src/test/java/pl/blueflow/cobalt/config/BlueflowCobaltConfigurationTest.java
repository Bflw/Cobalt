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

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public final class BlueflowCobaltConfigurationTest {
	
	private final @NotNull CobaltConfiguration config;
	
	public BlueflowCobaltConfigurationTest() {
		//noinspection ConstantConditions
		this.config = new BlueflowCobaltConfiguration(
			null,
			this.getClass().getClassLoader().getResourceAsStream("default.config.yml"),
			Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("test.config.yml"))
		);
	}
	
	@Test
	public void testNonExistent() {
		assertNull(this.config.getString("null-string"));
		assertNull(this.config.getBooleanList("null-bool-list"));
		assertNull(this.config.getFloatList("null-float-list"));
		assertNull(this.config.getLongMap("null-long-map"));
	}
	
	@Test
	public void testStrings() {
		assertEquals("value-1", this.config.getString("string-1"));
		assertEquals("value-2-nonexistent", this.config.getString("string-2-nonexistent", "value-2-nonexistent"));
	}
	
	@Test
	public void testStringList() {
		assertEquals(
			List.of("list-value-1", "list-value-2", "list-value-3"),
			this.config.getStringList("list-1")
		);
	}
	
	@Test
	public void testBooleanList() {
		assertEquals(
			List.of(true, true, false, true),
			this.config.getBooleanList("bool-list")
		);
	}
	
	@Test
	public void testNumberLists() {
		assertEquals(
			List.of(1, 1, -129863679, -5),
			// -129863679 is the result of long to int conversion
			this.config.getIntList("number-list")
		);
		assertEquals(
			List.of(1.45f, 1f, 9.8406057E17f, -5f),
			this.config.getFloatList("number-list")
		);
		assertEquals(
			List.of(1L, 1L, 984060604627185665L, -5L),
			this.config.getLongList("number-list")
		);
		assertEquals(
			List.of(1.45D, 1D, 984060604627185665D, -5D),
			this.config.getDoubleList("number-list")
		);
	}
	
	@Test
	public void testStringMap() {
		assertEquals(
			Map.of("key-1", "v-1", "key-2", "v-2", "key-3", "v-3"),
			this.config.getStringMap("string-map")
		);
	}
	
	@Test
	public void testBooleanMap() {
		assertEquals(
			Map.of("bool-1", true, "bool-2", false),
			this.config.getBooleanMap("bool-map")
		);
	}
	
	@Test
	public void testNumberMaps() {
		assertEquals(
			Map.of("float", 1, "int", 1,  "long", -129863679, "negative-int", -5),
			this.config.getIntMap("number-map")
		);
		assertEquals(
			Map.of("float", 1.45f, "int", 1f,  "long", 9.8406057E17f, "negative-int", -5f),
			this.config.getFloatMap("number-map")
		);
		assertEquals(
			Map.of("float", 1L, "int", 1L,  "long", 984060604627185665L, "negative-int", -5L),
			this.config.getLongMap("number-map")
		);
		assertEquals(
			Map.of("float", 1.45D, "int", 1D,  "long", 984060604627185665D, "negative-int", -5D),
			this.config.getDoubleMap("number-map")
		);
	}
	
	@Test
	public void testDeepMap() {
		assertEquals("v-1", this.config.getString("deep.string-1"));
	}
	
}
