//
// Created by BONNe
// Copyright - 2024
//


package lv.id.bonne.vaulthunters.junkcontroller.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.HashMap;
import java.util.UUID;

import iskallia.vault.world.data.VaultCharmData;


@Mixin(value = VaultCharmData.class, remap = false)
public interface VaultCharmDataAccessor
{
    @Accessor("whitelistedItems")
    HashMap<UUID, VaultCharmData.VaultCharmInventory> getWhitelistedItems();
}
