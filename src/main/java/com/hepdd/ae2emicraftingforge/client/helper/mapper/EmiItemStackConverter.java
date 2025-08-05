package com.hepdd.ae2emicraftingforge.client.helper.mapper;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import com.hepdd.ae2emicraftingforge.client.helper.interfaces.EmiStackConverter;
import dev.emi.emi.api.stack.EmiStack;
import org.jetbrains.annotations.Nullable;

public class EmiItemStackConverter implements EmiStackConverter {

    @Override
    public Class<?> getKeyType() {
        return Item.class;
    }

    @Override
    public @Nullable EmiStack toEmiStack(GenericStack stack) {
        if (stack.what() instanceof AEItemKey itemKey) {
            return EmiStack.of(itemKey.getItem(), stack.amount());
        }

        return null;
    }

    @Override
    public @Nullable GenericStack toGenericStack(EmiStack stack) {
        var item = stack.getKeyOfType(Item.class);

        if (item != null && item != Items.AIR) {
            var itemKey = AEItemKey.of(item, stack.getNbt());
            return new GenericStack(itemKey, stack.getAmount());
        }

        return null;
    }
}
