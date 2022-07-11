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

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.bukkit.Material;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@ToString(onlyExplicitlyIncluded = true)
public class BlueflowCobaltConfiguration implements CobaltConfiguration {
	
	
	protected static final @NotNull Yaml                  YAML           = new Yaml();
	protected static final @NotNull Map<String, Material> MATERIAL_CACHE = new HashMap<>();
	
	private final @NotNull File file;
	
	@Getter
	@ToString.Include
	private final @NotNull Map<String, Object> defaults;
	
	@Getter
	@ToString.Include
	private final @NotNull Map<String, Object> source;
	
	@SneakyThrows
	public BlueflowCobaltConfiguration(@NotNull File file, @Nullable InputStream defaultsStream, @NotNull InputStream sourceStream) {
		this.file = file;
		
		this.defaults = Collections.unmodifiableMap(defaultsStream != null ? YAML.load(defaultsStream) : new HashMap<>());
		final @Nullable Map<String, Object> sourceResult = YAML.load(sourceStream);
		if(sourceResult == null) throw new IllegalStateException("empty configuration file");
		this.source = Collections.unmodifiableMap(sourceResult);
		
		if(defaultsStream != null) defaultsStream.close();
		sourceStream.close();
	}
	
	protected static @NotNull Map<String, Object> resolveParentMap(@NotNull Map<String, Object> sourceMap, @NotNull String key) {
		final @NotNull String[] splitKey  = key.split("\\.");
		final @NotNull String[] splitPath = new String[splitKey.length - 1];
		System.arraycopy(splitKey, 0, splitPath, 0, splitPath.length);
		return resolveMap(sourceMap, splitPath);
	}
	
	protected static @NotNull Map<String, Object> resolveMap(@NotNull Map<String, Object> sourceMap, @NotNull String[] splitPath) {
		@Nullable Map<String, Object> resolved = sourceMap;
		for(final @NotNull String key : splitPath) {
			//noinspection unchecked
			resolved = (Map<String, Object>) resolved.get(key);
			if(resolved == null) throw new IllegalArgumentException(String.format("unable to resolve map at %s", key));
		}
		
		return resolved;
	}
	
	protected static @NotNull String resolveLastKey(@NotNull String key) {
		final @NotNull String[] splitKey = key.split("\\.");
		return splitKey[splitKey.length - 1];
	}
	
	@Contract("!null -> !null; null -> null")
	protected static <T> @Nullable List<T> newList(@Nullable List<T> contents) {
		if(contents == null) return null;
		return new LinkedList<>(contents);
	}
	
	@Contract("!null -> !null; null -> null")
	protected static <K, V> @Nullable Map<K, V> newMap(@Nullable Map<K, V> contents) {
		if(contents == null) return null;
		return new LinkedHashMap<>(contents);
	}
	
	@Override
	public @NotNull Set<String> keys() {
		return this.source.keySet();
	}
	
	@Override
	public @NotNull Set<String> keys(@NotNull String path) {
		return resolveMap(this.source, path.split("\\.")).keySet();
	}
	
	@Override
	@Contract("_, !null -> !null; _, null -> _")
	public <T> @Nullable T get(@NotNull String key, @Nullable T onEmpty) {
		final @NotNull String lastKey = resolveLastKey(key);
		
		try {
			final @NotNull Map<String, Object> source = resolveParentMap(this.source, key);
			@Nullable Object                   result = null;
			
			if(source.containsKey(lastKey)) {
				result = source.get(lastKey);
			}
			
			if(result == null && !this.defaults.isEmpty()) {
				final @NotNull Map<String, Object> defaults = resolveParentMap(this.defaults, key);
				
				if(defaults.containsKey(lastKey)) {
					result = defaults.get(lastKey);
				}
			}
			
			//noinspection unchecked
			return result != null ? (T) result : onEmpty;
		} catch(IllegalArgumentException ex) {
			return onEmpty;
		}
	}
	
	@Override
	public void set(@NotNull String key, @Nullable Object value) {
		resolveParentMap(this.source, key).put(key, value);
	}
	
	@Override
	@SneakyThrows
	public void save() {
		YAML.dump(this.source, new FileWriter(this.file));
	}
	
	@Override
	public @Nullable List<Boolean> getBooleanList(@NotNull String key) {
		return this.getBooleanList(key, null);
	}
	
	@Override
	public @Nullable List<Boolean> getBooleanList(@NotNull String key, @Nullable List<Boolean> onEmpty) {
		return newList(this.get(key, onEmpty));
	}
	
	@Override
	public @Nullable List<Integer> getIntList(@NotNull String key) {
		return this.getIntList(key, null);
	}
	
	@Override
	public @Nullable List<Integer> getIntList(@NotNull String key, @Nullable List<Integer> onEmpty) {
		final @Nullable List<? extends Number> list = this.get(key, onEmpty);
		return list == null ? null : list.stream().map(Number::intValue).collect(Collectors.toList());
	}
	
	@Override
	public @Nullable List<Long> getLongList(@NotNull String key) {
		return this.getLongList(key, null);
	}
	
	@Override
	public @Nullable List<Long> getLongList(@NotNull String key, @Nullable List<Long> onEmpty) {
		final @Nullable List<? extends Number> list = this.get(key, onEmpty);
		return list == null ? null : list.stream().map(Number::longValue).collect(Collectors.toList());
	}
	
	@Override
	public @Nullable List<Float> getFloatList(@NotNull String key) {
		return this.getFloatList(key, null);
	}
	
	@Override
	public @Nullable List<Float> getFloatList(@NotNull String key, @Nullable List<Float> onEmpty) {
		final @Nullable List<? extends Number> list = this.get(key, onEmpty);
		return list == null ? null : list.stream().map(Number::floatValue).collect(Collectors.toList());
	}
	
	@Override
	public @Nullable List<Double> getDoubleList(@NotNull String key) {
		return this.getDoubleList(key, null);
	}
	
	@Override
	public @Nullable List<Double> getDoubleList(@NotNull String key, @Nullable List<Double> onEmpty) {
		final @Nullable List<? extends Number> list = this.get(key, onEmpty);
		return list == null ? null : list.stream().map(Number::doubleValue).collect(Collectors.toList());
	}
	
	@Override
	public @Nullable List<String> getStringList(@NotNull String key) {
		return this.getStringList(key, null);
	}
	
	@Override
	public @Nullable List<String> getStringList(@NotNull String key, @Nullable List<String> onEmpty) {
		return newList(this.get(key, onEmpty));
	}
	
	@Override
	public void setBooleanList(@NotNull String key, @Nullable List<Boolean> value) {
		this.set(key, value);
	}
	
	@Override
	public void setIntList(@NotNull String key, @Nullable List<Integer> value) {
		this.set(key, value);
	}
	
	@Override
	public void setLongList(@NotNull String key, @Nullable List<Long> value) {
		this.set(key, value);
	}
	
	@Override
	public void setFloatList(@NotNull String key, @Nullable List<Float> value) {
		this.set(key, value);
	}
	
	@Override
	public void setDoubleList(@NotNull String key, @Nullable List<Double> value) {
		this.set(key, value);
	}
	
	@Override
	public void setStringList(@NotNull String key, @Nullable List<String> value) {
		this.set(key, value);
	}
	
	@Override
	public @Nullable Map<String, Boolean> getBooleanMap(@NotNull String key) {
		return this.getBooleanMap(key, null);
	}
	
	@Override
	public @Nullable Map<String, Boolean> getBooleanMap(@NotNull String key, @Nullable Map<String, Boolean> onEmpty) {
		return newMap(this.get(key, onEmpty));
	}
	
	@Override
	public @Nullable Map<String, Integer> getIntMap(@NotNull String key) {
		return this.getIntMap(key, null);
	}
	
	@Override
	public @Nullable Map<String, Integer> getIntMap(@NotNull String key, @Nullable Map<String, Integer> onEmpty) {
		final @Nullable Map<String, ? extends Number> map = this.get(key, onEmpty);
		return map == null ? null : map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().intValue()));
	}
	
	@Override
	public @Nullable Map<String, Long> getLongMap(@NotNull String key) {
		return this.getLongMap(key, null);
	}
	
	@Override
	public @Nullable Map<String, Long> getLongMap(@NotNull String key, @Nullable Map<String, Long> onEmpty) {
		final @Nullable Map<String, ? extends Number> map = this.get(key, onEmpty);
		return map == null ? null : map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().longValue()));
	}
	
	@Override
	public @Nullable Map<String, Float> getFloatMap(@NotNull String key) {
		return this.getFloatMap(key, null);
	}
	
	@Override
	public @Nullable Map<String, Float> getFloatMap(@NotNull String key, @Nullable Map<String, Float> onEmpty) {
		final @Nullable Map<String, ? extends Number> map = this.get(key, onEmpty);
		return map == null ? null : map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().floatValue()));
	}
	
	@Override
	public @Nullable Map<String, Double> getDoubleMap(@NotNull String key) {
		return this.getDoubleMap(key, null);
	}
	
	@Override
	public @Nullable Map<String, Double> getDoubleMap(@NotNull String key, @Nullable Map<String, Double> onEmpty) {
		final @Nullable Map<String, ? extends Number> map = this.get(key, onEmpty);
		return map == null ? null : map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().doubleValue()));
	}
	
	@Override
	public @Nullable Map<String, String> getStringMap(@NotNull String key) {
		return this.getStringMap(key, null);
	}
	
	@Override
	public @Nullable Map<String, String> getStringMap(@NotNull String key, @Nullable Map<String, String> onEmpty) {
		return newMap(this.get(key, onEmpty));
	}
	
	@Override
	public void setBooleanMap(@NotNull String key, @Nullable Map<String, Boolean> value) {
		this.set(key, value);
	}
	
	@Override
	public void setIntMap(@NotNull String key, @Nullable Map<String, Integer> value) {
		this.set(key, value);
	}
	
	@Override
	public void setLongMap(@NotNull String key, @Nullable Map<String, Long> value) {
		this.set(key, value);
	}
	
	@Override
	public void setFloatMap(@NotNull String key, @Nullable Map<String, Float> value) {
		this.set(key, value);
	}
	
	@Override
	public void setDoubleMap(@NotNull String key, @Nullable Map<String, Double> value) {
		this.set(key, value);
	}
	
	@Override
	public void setStringMap(@NotNull String key, @Nullable Map<String, String> value) {
		this.set(key, value);
	}
	
	@Override
	public @Nullable Boolean getBoolean(@NotNull String key) {
		return this.get(key, null);
	}
	
	@Override
	public boolean getBoolean(@NotNull String key, boolean onEmpty) {
		return this.get(key, onEmpty);
	}
	
	@Override
	public @Nullable Integer getInt(@NotNull String key) {
		return this.get(key, null);
	}
	
	@Override
	public int getInt(@NotNull String key, int onEmpty) {
		return this.get(key, onEmpty);
	}
	
	@Override
	public @Nullable Long getLong(@NotNull String key) {
		return this.get(key, null);
	}
	
	@Override
	public long getLong(@NotNull String key, long onEmpty) {
		return this.get(key, onEmpty);
	}
	
	@Override
	public @Nullable Float getFloat(@NotNull String key) {
		return this.get(key, null);
	}
	
	@Override
	public float getFloat(@NotNull String key, float onEmpty) {
		return this.get(key, onEmpty);
	}
	
	@Override
	public @Nullable Double getDouble(@NotNull String key) {
		return this.get(key, null);
	}
	
	@Override
	public double getDouble(@NotNull String key, double onEmpty) {
		return this.get(key, onEmpty);
	}
	
	@Override
	public void setBoolean(@NotNull String key, @Nullable Boolean value) {
		this.set(key, value);
	}
	
	@Override
	public void setInt(@NotNull String key, @Nullable Integer value) {
		this.set(key, value);
	}
	
	@Override
	public void setLong(@NotNull String key, @Nullable Long value) {
		this.set(key, value);
	}
	
	@Override
	public void setFloat(@NotNull String key, @Nullable Float value) {
		this.set(key, value);
	}
	
	@Override
	public void setDouble(@NotNull String key, @Nullable Double value) {
		this.set(key, value);
	}
	
	@Override
	public @Nullable String getString(@NotNull String key) {
		return this.getString(key, null);
	}
	
	@Override
	public @Nullable String getString(@NotNull String key, @Nullable String onEmpty) {
		return this.get(key, onEmpty);
	}
	
	@Override
	public void setString(@NotNull String key, @Nullable String value) {
		this.set(key, value);
	}
	
}
