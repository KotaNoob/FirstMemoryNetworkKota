package dev.moru3.minepie.customgui.inventory

import dev.moru3.minepie.customgui.ActionItem
import dev.moru3.minepie.utils.IgnoreRunnable.Companion.ignoreException
import dev.moru3.minepie.utils.Utils.Companion.isNull
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

/**
 * @param plugin JavaPluginを入れてください。
 * @param size インベントリの縦の列のサイズです。
 * @param title インベントリのタイトルを設定してください。
 * @param startX addItemをした際に(ry
 * @param startY addItemをした際(ry
 * @param endX addItemをした(ry
 * @param endY addItemをし(ry
 * @param runnable 任意:処理を記述してください
 */
open class CustomContentsGui(plugin: JavaPlugin, size: Int, title: String, private val startX: Int, private val startY: Int, private val endX: Int, private val endY: Int, private val runnable: CustomContentsGui.() -> Unit = {}): CustomGui(plugin, title, size) {
    private val contents = mutableListOf<ActionItem>()

    var page = 1
    private set

    fun addContents(itemStack: ItemStack, runnable: ActionItem.() -> Unit = {}): CustomContentsGui {
        ActionItem(itemStack.clone()).also {
            contents.add(it)
            runnable.invoke(it)
        }
        return this
    }

    open fun addContents(actionItem: ActionItem, runnable: ActionItem.() -> Unit = {}): CustomContentsGui {
        addContents(actionItem.itemStack.clone()) {
            actionItem.getActions().forEach(this::addAction)
            runnable.invoke(this)
        }
        return this
    }

    fun replaceContents(old: ItemStack, new: ItemStack, runnable: ActionItem.() -> Unit = {}): CustomContentsGui {
        repeat(removeContents(old)) { addContents(new, runnable::invoke) }
        return this
    }

    fun removeContents(actionItem: ActionItem): Int {
        val contentsAmount = contents.count { it==actionItem }
        contents.remove(actionItem)
        return contentsAmount
    }

    fun removeContents(itemStack: ItemStack): Int {
        val contentsAmount: Int
        contents.filter { itemStack==it.itemStack }.apply {
            contentsAmount = this.size
            forEach(contents::remove)
        }
        return contentsAmount
    }

    fun clearContents(): CustomContentsGui {
        contents.clear()
        return this
    }

    override fun open(player: Player): Inventory {
        return open(player, page)
    }

    fun open(player: Player, page: Int): Inventory {
        val customSyncGui = super.clone()
        val slots = mutableListOf<Int>()
        for(x in startX..endX) { for(y in startY..endY) { customSyncGui.getItem(x, y).isNull { slots.add(x+(y*9)) } } }
        if(slots.isEmpty()) {
            return customSyncGui.open(player)
        } else {
            contents.size/slots.size
            slots.forEachIndexed { index, slot -> customSyncGui.setItem(slot%9, slot/9, contents[index+(page-1)]) }
            return customSyncGui.open(player)
        }
    }

    override fun clone(): CustomContentsGui {
        val customContentsGui = CustomContentsGui(plugin, size, title, startX, startY, endX, endY, runnable)
        for(x in 0..8) { for(y in 0..size) {
            customContentsGui.setItem(x, y, this.getItem(x, y)?.clone()?:continue)
        } }
        contents.forEach(customContentsGui::addContents)
        return customContentsGui
    }

    /**
     * initは最後に置いておいてね
     */
    init {
        Runnable { runnable.invoke(this) }.ignoreException()
    }

    companion object {
        fun JavaPlugin.createCustomContentsGui(size: Int, title: String, startX: Int, startY: Int, endX: Int, endY: Int, runnable: CustomContentsGui.() -> Unit = {}): CustomContentsGui {
            return CustomContentsGui(this, size, title, startX, startY, endX, endY, runnable)
        }
    }
}