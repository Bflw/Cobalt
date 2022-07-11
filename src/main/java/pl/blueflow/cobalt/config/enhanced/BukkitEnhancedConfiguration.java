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

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.blueflow.cobalt.config.BlueflowCobaltConfiguration;
import pl.blueflow.cobalt.message.TranslatorBukkitAdapter;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BukkitEnhancedConfiguration extends BlueflowCobaltConfiguration implements MaterialAccessors, MessageAccessors<Component> {
	
	private static final @NotNull TranslatorBukkitAdapter TRANSLATOR_ADAPTER = new TranslatorBukkitAdapter();
	
	@Getter
	private final @NotNull Map<String, Object> defaultContext = new HashMap<>();
	
	public BukkitEnhancedConfiguration(@NotNull File file, @Nullable InputStream defaultsStream, @NotNull InputStream sourceStream) {
		super(file, defaultsStream, sourceStream);
	}
	
	protected static @NotNull Material mapEntryToMaterial(@NotNull Map.Entry<?, String> entry) {
		if(entry.getValue() == null) throw new IllegalArgumentException("cannot put a null material");
		final @Nullable Material matched = Material.matchMaterial(entry.getValue());
		if(matched == null) throw new IllegalArgumentException("cannot put a null material");
		return matched;
	}
	
	protected static <K, V> @NotNull Map<K, V> joinMaps(@NotNull Map<K, V> defaults, @Nullable Map<K, V> put) {
		if(put == null) return defaults;
		final @NotNull Map<K, V> result = new HashMap<>(put);
		defaults.forEach(result::putIfAbsent);
		return result;
	}
	
	@Override
	public @Nullable Material getMaterial(@NotNull String key) {
		return this.getMaterial(key, null);
	}
	
	@Override
	public @Nullable Material getMaterial(@NotNull String key, @Nullable Material onEmpty) {
		final @Nullable String   material = this.getString(key);
		final @Nullable Material matched  = material != null ? MATERIAL_CACHE.computeIfAbsent(material, Material::matchMaterial) : null;
		return matched != null ? matched : onEmpty;
	}
	
	@Override
	public @Nullable List<Material> getMaterialList(@NotNull String key) {
		return this.getMaterialList(key, null);
	}
	
	@Override
	public @Nullable List<Material> getMaterialList(@NotNull String key, @Nullable List<Material> onEmpty) {
		final @Nullable List<String> list = this.getStringList(key);
		if(list == null) return onEmpty;
		final @NotNull List<Material> result = list.stream().map(Material::matchMaterial).collect(Collectors.toList());
		return result;
	}
	
	@Override
	public @Nullable Map<String, Material> getMaterialMap(@NotNull String key) {
		return this.getMaterialMap(key, null);
	}
	
	@Override
	public @Nullable Map<String, Material> getMaterialMap(@NotNull String key, @Nullable Map<String, Material> onEmpty) {
		final @Nullable Map<String, String> map = this.getStringMap(key);
		if(map == null) return onEmpty;
		final @NotNull Map<String, Material> result = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, BukkitEnhancedConfiguration::mapEntryToMaterial));
		return result;
	}
	
	@Override
	public void setMaterial(@NotNull String key, @Nullable Material value) {
		this.setString(key, value != null ? value.name() : null);
	}
	
	@Override
	public void setMaterialList(@NotNull String key, @Nullable List<Material> value) {
		this.setStringList(key, value != null ? value.stream().map(Material::name).collect(Collectors.toList()) : null);
	}
	
	@Override
	public void setMaterialMap(@NotNull String key, @Nullable Map<String, Material> value) {
		this.setStringMap(key, value != null ? value.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().name())) : null);
	}
	
	@Override
	public @Nullable Component getMessage(@NotNull String key) {
		return this.getMessage(key, null);
	}
	
	public @Nullable Component getMessage(@NotNull String key, @Nullable Map<String, Object> context) {
		return this.getMessage(key, context, (String) null);
	}
	
	@Override
	public @Nullable Component getMessage(@NotNull String key, @Nullable Map<String, Object> context, @Nullable Component onEmpty) {
		final @Nullable String message = this.getString(key);
		if(message == null) return onEmpty;
		return TRANSLATOR_ADAPTER.translate(message, joinMaps(this.defaultContext, context));
	}
	
	@Override
	public @Nullable Component getMessage(@NotNull String key, @Nullable Map<String, Object> context, @Nullable String onEmpty) {
		final @Nullable String message = this.getString(key, onEmpty);
		if(message == null) return null;
		return TRANSLATOR_ADAPTER.translate(message, joinMaps(this.defaultContext, context));
	}
	
	@Override
	public @Nullable List<Component> getMessageList(@NotNull String key) {
		return this.getMessageList(key, null);
	}
	
	@Override
	public @Nullable List<Component> getMessageList(@NotNull String key, @Nullable Map<String, Object> context) {
		return this.getMessageList(key, context, null);
	}
	
	@Override
	public @Nullable List<Component> getMessageListPremade(@NotNull String key, @Nullable Map<String, Object> context, @Nullable List<Component> onEmpty) {
		final @Nullable List<String> list = this.getStringList(key);
		if(list == null) return onEmpty;
		final @NotNull Map<String, Object> joinedContext = joinMaps(this.defaultContext, context);
		return list.stream().map(s -> TRANSLATOR_ADAPTER.translate(s, joinedContext)).collect(Collectors.toList());
	}
	
	@Override
	public @Nullable List<Component> getMessageList(@NotNull String key, @Nullable Map<String, Object> context, @Nullable List<String> onEmpty) {
		final @Nullable List<String> list = this.getStringList(key, onEmpty);
		if(list == null) return null;
		final @NotNull Map<String, Object> joinedContext = joinMaps(this.defaultContext, context);
		return list.stream().map(s -> TRANSLATOR_ADAPTER.translate(s, joinedContext)).collect(Collectors.toList());
	}
	
	@Override
	public @Nullable Map<String, Component> getMessageMap(@NotNull String key) {
		return this.getMessageMap(key, null);
	}
	
	@Override
	public @Nullable Map<String, Component> getMessageMap(@NotNull String key, @Nullable Map<String, Object> context) {
		return this.getMessageMap(key, context, null);
	}
	
	@Override
	public @Nullable Map<String, Component> getMessageMapPremade(@NotNull String key, @Nullable Map<String, Object> context, @Nullable Map<String, Component> onEmpty) {
		final @Nullable Map<String, String> map = this.getStringMap(key);
		if(map == null) return onEmpty;
		final @NotNull Map<String, Object> joinedContext = joinMaps(this.defaultContext, context);
		return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> TRANSLATOR_ADAPTER.translate(e.getValue(), joinedContext)));
	}
	
	@Override
	public @Nullable Map<String, Component> getMessageMap(@NotNull String key, @Nullable Map<String, Object> context, @Nullable Map<String, String> onEmpty) {
		final @Nullable Map<String, String> map = this.getStringMap(key, onEmpty);
		if(map == null) return null;
		final @NotNull Map<String, Object> joinedContext = joinMaps(this.defaultContext, context);
		return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> TRANSLATOR_ADAPTER.translate(e.getValue(), joinedContext)));
	}
	
}
