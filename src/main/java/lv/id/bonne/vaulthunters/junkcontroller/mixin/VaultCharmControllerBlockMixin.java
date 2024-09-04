//
// Created by BONNe
// Copyright - 2024
//


package lv.id.bonne.vaulthunters.junkcontroller.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import java.util.Iterator;

import iskallia.vault.block.entity.VaultCharmControllerTileEntity;
import iskallia.vault.world.data.VaultCharmData;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.p3pp3rf1y.sophisticatedbackpacks.api.CapabilityBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedcore.inventory.ItemStackKey;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeHandler;


@Mixin(BackpackItem.class)
public class VaultCharmControllerBlockMixin
{
    @Inject(method = "useOn",
        at = @At(value = "INVOKE",
            target = "Lnet/p3pp3rf1y/sophisticatedbackpacks/util/InventoryInteractionHelper;tryInventoryInteraction(Lnet/minecraft/world/item/context/UseOnContext;)Z"),
        locals = LocalCapture.CAPTURE_FAILSOFT,
        cancellable = true)
    private void implementContainerClick(UseOnContext context,
        CallbackInfoReturnable<InteractionResult> cir,
        Player player)
    {
        if (!(context.getLevel().getBlockEntity(context.getClickedPos()) instanceof VaultCharmControllerTileEntity))
        {
            // Not a charm tile block.
            return;
        }

        // Only for server player

        context.getItemInHand().getCapability(CapabilityBackpackWrapper.getCapabilityInstance()).ifPresent(wrapper ->
        {
            UpgradeHandler upgradeHandler = wrapper.getUpgradeHandler();

            if (!upgradeHandler.hasUpgrade(DepositUpgradeItemAccessor.getTYPE()))
            {
                return;
            }

            if (player instanceof ServerPlayer serverPlayer)
            {
                VaultCharmData vaultCharmData = VaultCharmData.get(serverPlayer.getLevel());

                if (!((VaultCharmDataAccessor) vaultCharmData).getWhitelistedItems().containsKey(serverPlayer.getUUID()))
                {
                    // Player has not unlocked VaultCharm
                    cir.setReturnValue(InteractionResult.SUCCESS);
                    cir.cancel();

                    return;
                }

                VaultCharmData.VaultCharmInventory data = vaultCharmData.getInventory(serverPlayer);

                Iterator<ItemStackKey> iter = wrapper.getInventoryForInputOutput().getTrackedStacks().iterator();

                int addedItems = 0;
                int emptySpots = data.getSize() - data.getWhitelist().size();

                while (iter.hasNext() && emptySpots > 0)
                {
                    ItemStackKey next = iter.next();

                    if (!data.getWhitelist().contains(next.stack().getItem().getRegistryName()))
                    {
                        data.getWhitelist().add(next.getStack().getItem().getRegistryName());
                        emptySpots--;
                        addedItems++;
                    }
                }

                String translKey = addedItems > 0 ? "gui.sophisticatedbackpacks.status.stacks_deposited" :
                    "gui.sophisticatedbackpacks.status.nothing_to_deposit";
                player.displayClientMessage(new TranslatableComponent(translKey, addedItems), true);
            }

            cir.setReturnValue(InteractionResult.SUCCESS);
            cir.cancel();
        });
    }
}
