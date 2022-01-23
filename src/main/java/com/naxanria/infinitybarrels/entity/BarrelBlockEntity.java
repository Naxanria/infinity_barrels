package com.naxanria.infinitybarrels.entity;


import com.naxanria.infinitybarrels.config.Config;
import com.naxanria.infinitybarrels.registry.ModBlockEntities;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
public class BarrelBlockEntity extends BlockEntity
{
  private ItemStack item = new ItemStack(Items.STONE);
  
  private ItemStackHandler inventory;
  private final LazyOptional<ItemStackHandler> inventoryCap = LazyOptional.of(this::getInventory);
  private long lastClicked = 0;
  
  public BarrelBlockEntity(BlockPos pos, BlockState state)
  {
    super(ModBlockEntities.BARREL.get(), pos, state);
  }
  
  @Nonnull
  public ItemStackHandler getInventory()
  {
    if (inventory == null)
    {
      inventory = new ItemStackHandler()
      {
        @Override
        public void setSize(int size)
        {
          super.setSize(1);
        }
  
        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack)
        {
        }
  
        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot)
        {
          return Util.make(item.copy(), stack -> stack.setCount(Config.getInstance().stackSize.get()));
        }
  
        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
        {
          return stack;
        }
  
        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate)
        {
          ItemStack stack = BarrelBlockEntity.this.item.copy();
          stack.setCount(amount);
          return stack;
        }
  
        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack)
        {
          return Config.getInstance().stackSize.get();
        }
  
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
          return false;
        }
      };
    }
    
    return inventory;
  }
  
  @Override
  public void load(CompoundTag tag)
  {
    super.load(tag);
    read(tag);
  }
  
  @Override
  protected void saveAdditional(CompoundTag tag)
  {
    write(tag);
  }
  
  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
  {
    CompoundTag tag = pkt.getTag();
    if (tag != null)
    {
      read(tag);
    }
  }
  
  @Nullable
  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket()
  {
    return ClientboundBlockEntityDataPacket.create(this);
  }
  
  @Override
  public CompoundTag getUpdateTag()
  {
    return write(new CompoundTag());
  }
  
  @Override
  public void handleUpdateTag(CompoundTag tag)
  {
    read(tag);
  }
  
  private void read(CompoundTag tag)
  {
    item = ItemStack.of(tag.getCompound("item"));
    tag.getLong("lastClick");
  }
  
  private CompoundTag write(CompoundTag tag)
  {
    
    tag.put("item", item.serializeNBT());
    tag.putLong("lastClick", lastClicked);
    
    return tag;
  }

  
  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
  {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
    {
      return inventoryCap.cast();
    }
    
    return super.getCapability(cap, side);
  }
  
  public ItemStack getItem()
  {
    return item.copy();
  }
  
  public void onClick(Level level, Player player)
  {
    long now = System.currentTimeMillis();
    if (now - lastClicked > 1000 / 5)
    {
      lastClicked = now;
      ItemStack stack;
      if (!player.getInventory().add(stack = getItem()))
      {
        spawnItemStack(level, getBlockPos(), stack);
      }
    }
  }
  
  public void setItem(ItemStack item)
  {
    this.item = item;
    setChanged();
  }
  
  public static void spawnItemStack(Level level, BlockPos position, ItemStack stack)
  {
    ItemEntity itemEntity = new ItemEntity(level, position.getX() + .5, position.getY() + .5, position.getZ() + .5, stack);
    level.addFreshEntity(itemEntity);
  }
}
