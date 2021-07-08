package me.kota.firstmemorynetworkkota

import dev.moru3.minepie.events.EventRegister.Companion.registerEvent
import me.kota.firstmemorynetworkkota.Item.ItemManager
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    val itemManager = ItemManager()

    override fun onEnable() {
        // Plugin startup logic
        this.registerEvent<PlayerJoinEvent>() {
            player.inventory.addItem(itemManager.items[0])
            player.inventory.addItem(itemManager.items[1])
        }
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}