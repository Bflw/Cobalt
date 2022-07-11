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

package pl.blueflow.cobalt;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.blueflow.cobalt.config.enhanced.BukkitEnhancedConfiguration;
import pl.blueflow.cobalt.helper.Helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Default {@link CobaltPlugin} implementation for Bukkit.
 */
public abstract class BukkitCobaltPlugin extends JavaPlugin implements CobaltPlugin {
	
	/**
	 * Name of the resource that contains default configuration values.
	 */
	@Setter(AccessLevel.PROTECTED)
	private @Nullable String configurationDefaults = null;
	
	/**
	 * Configuration file location, all necessary directories will be automatically created,<br>
	 * defaults to <code>new File(this.getDataFolder(), "config.yml")</code>
	 */
	@Setter(AccessLevel.PROTECTED)
	private @NotNull File configurationSource = new File(this.getDataFolder(), "config.yml");
	
	/**
	 * If set to true and any configuration's input stream is null,
	 * will output a warning into the console during loading,<br>
	 * defaults to true.
	 */
	@Setter(AccessLevel.PROTECTED)
	private boolean warnOnMissingDefaultConfig = true;
	
	/**
	 * If set to true, any call to {@link #getConfig()} will throw an exception,<br>
	 * if set to false, it'll return the super class' {@link FileConfiguration},<br>
	 * defaults to true, may be changed dynamically.
	 */
	@Setter(AccessLevel.PROTECTED)
	private boolean throwOnNativeConfigAccess = true;
	
	/**
	 * If set to true, any existing configuration from {@link #configurationSource} will be replaced with {@link #configurationDefaults}.<br>
	 * If {@link #configurationDefaults} is null or doesn't exist, nothing will be replaced.<br>
	 * Defaults to false.
	 */
	@Setter(AccessLevel.PROTECTED)
	private boolean replaceExistingConfig = false;
	
	@Getter
	private BukkitEnhancedConfiguration configuration;
	
	@Override
	public final void onLoad() {
		this.configure();
	}
	
	@Override
	@SneakyThrows
	public final void onEnable() {
		// Warn on missing configurations.
		if(this.warnOnMissingDefaultConfig && this.configurationDefaults == null) {
			this.getSLF4JLogger().warn("Plugin didn't provide necessary default configuration sources.");
		}
		
		FileUtils.touch(this.configurationSource);
		Helper.saveDefault(Helper.getResource(this.getClass(), this.configurationDefaults), this.configurationSource, this.replaceExistingConfig);
		
		this.configuration = new BukkitEnhancedConfiguration(
			this.configurationSource,
			Helper.getResource(this.getClass(), this.configurationDefaults),
			new FileInputStream(this.configurationSource)
		);
		
		this.start();
	}
	
	@Override
	public final void onDisable() {
		this.stop();
	}
	
	/**
	 * @deprecated Use {@link CobaltPlugin#getConfiguration()} instead for Cobalt-based plugins.
	 */
	@Override
	@Deprecated(forRemoval = true)
	public final @NotNull FileConfiguration getConfig() {
		if(this.throwOnNativeConfigAccess) throw new UnsupportedOperationException("native configuration access denied by Cobalt API");
		return super.getConfig();
	}
	
}
