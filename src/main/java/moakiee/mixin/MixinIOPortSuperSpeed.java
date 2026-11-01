/*
 * This file includes code adapted from MakeAE2Better by QiuYe.
 * Licensed under the MIT License.
 * Original Source: https://github.com/qiuye2024github/MaekAE2Better
 * Full license text: src/main/resources/LICENSE_MakeAE2Better.txt
 */
package moakiee.mixin;

import appeng.api.upgrades.IUpgradeableObject;
import appeng.blockentity.storage.IOPortBlockEntity;
import moakiee.ModItems;
import moakiee.support.SuperSpeedNumberUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Adds super speed without replacing AE2's storage-cell lookup or transfer loop. */
@Mixin(IOPortBlockEntity.class)
public abstract class MixinIOPortSuperSpeed implements IUpgradeableObject {

    // AE2 15.4.12 has calculated the normal speed-card budget at this point,
    // before entering the cell loop. Boost once per tick, not once per cell.
    @ModifyVariable(
        method = "tickingRequest",
        at = @At(
            value = "INVOKE",
            target = "Lappeng/api/networking/IManagedGridNode;getGrid()Lappeng/api/networking/IGrid;"
        ),
        ordinal = 0,
        require = 1,
        remap = false
    )
    private long ae2oc_boostTransferBudget(long itemsToMove) {
        if (getUpgrades().getInstalledUpgrades(ModItems.SUPER_SPEED_CARD.get()) <= 0) {
            return itemsToMove;
        }
        return SuperSpeedNumberUtil.boostLongSaturating(itemsToMove);
    }
}
