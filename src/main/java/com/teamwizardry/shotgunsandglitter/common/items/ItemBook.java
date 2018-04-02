package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemModBook;
import com.teamwizardry.librarianlib.features.gui.provided.book.hierarchy.book.Book;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemBook extends ItemModBook {

	public static Book book = new Book("book");

	public ItemBook() {
		super("book");
		setMaxStackSize(1);
	}

	@NotNull
	@Override
	public Book getBook(@NotNull EntityPlayer player, @Nullable World world, @NotNull ItemStack stack) {
		return book;
	}
}
