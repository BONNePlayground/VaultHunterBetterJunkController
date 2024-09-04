//
// Created by BONNe
// Copyright - 2024
//


package lv.id.bonne.vaulthunters.junkcontroller.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.deposit.DepositUpgradeItem;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.deposit.DepositUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;


@Mixin(value = DepositUpgradeItem.class, remap = false)
public interface DepositUpgradeItemAccessor
{
    @Accessor("TYPE")
    static UpgradeType<DepositUpgradeWrapper> getTYPE()
    {
        throw new UnsupportedOperationException();
    }
}
