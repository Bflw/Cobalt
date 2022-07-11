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

package pl.blueflow.cobalt.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class BukkitPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		this.getServer().getConsoleSender().sendMessage(Component.text("Thank you for using the Cobalt API! See https://cobalt.blueflow.pl for more. Licensed under the GNU General Public License.").color(TextColor.color(0x506AAD)));
	}
	
}
