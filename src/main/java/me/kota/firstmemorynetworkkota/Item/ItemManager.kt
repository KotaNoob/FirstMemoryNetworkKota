package me.kota.firstmemorynetworkkota.Item

import me.kota.rpgitemapi.*
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe




class ItemManager {
    val items = mutableListOf<ItemStack>()

    init {
        items.add(Sword("テスト剣", Material.IRON_SWORD, listOf("テスト", "テスト"), Ability("テスト", mapOf(Action.LEFT_CLICK_AIR to "左クリック"),
                listOf("テスト能力の説明"), 100, 2.5, true),
                Stats(10,10,10,10,10,10, 0), Rarity.LEGENDARY).itemStack)
        items.add(createItem(ItemType.Gun, "テスト銃", Material.IRON_HOE, listOf("テスト銃", "テスト銃"), Ability("撃つ", mapOf(Action.RIGHT_CLICK_AIR to "右クリック"),
                listOf("テスト銃の能力"), 10,10.0,false), Stats(10,10,10,10,10,10,10), Rarity.COMMON).itemStack)
    }

}