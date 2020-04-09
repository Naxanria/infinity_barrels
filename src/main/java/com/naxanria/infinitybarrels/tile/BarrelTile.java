package com.naxanria.infinitybarrels.tile;

import com.naxanria.infinitybarrels.config.Config;
import com.naxanria.infinitybarrels.init.ModTiles;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.lib.util.nbt.Manager;
import com.wtbw.mods.lib.util.nbt.NBTManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
public class BarrelTile extends TileEntity
{
  private ItemStack item = new ItemStack(Items.STONE);
  private NBTManager manager = new NBTManager();
  private ItemStackHandler handler;
  private LazyOptional<ItemStackHandler> handlerLazyOptional = LazyOptional.of(this::getHandler);
  private long lastClicked = 0;
  
  public BarrelTile()
  {
    super(ModTiles.INFINITY_BARREL);
    
    manager
      .register("item", new Manager()
      {
        @Override
        public void read(String name, CompoundNBT nbt)
        {
          item = ItemStack.read(nbt.getCompound(name));
        }
  
        @Override
        public void write(String name, CompoundNBT nbt)
        {
          nbt.put(name, getItem().serializeNBT());
        }
      })
      .register("inventory", handler)
      .registerLong("last", () -> lastClicked, integer -> lastClicked = integer);
  }
  
  @Nonnull
  public ItemStackHandler getHandler()
  {
    if (handler == null)
    {
      handler = new ItemStackHandler()
      {
        @Override
        public void setSize(int size)
        {
          super.setSize(1);
        }
  
        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack)
        { }
  
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
          ItemStack stack = BarrelTile.this.item.copy();
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
    
    return handler;
  }
  
  @Override
  public void read(CompoundNBT compound)
  {
    super.read(compound);
    manager.read(compound);
  }
  
  @Nonnull
  @Override
  public CompoundNBT write(CompoundNBT compound)
  {
    super.write(compound);
    manager.write(compound);
    return compound;
  }
  
  @Nullable
  @Override
  public SUpdateTileEntityPacket getUpdatePacket()
  {
    CompoundNBT tag = new CompoundNBT();
    tag.put("item", getItem().serializeNBT());
    return new SUpdateTileEntityPacket(pos, 0, tag);
  }
  
  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
  {
    setItem(ItemStack.read(pkt.getNbtCompound().getCompound("item")));
  }
  
  @Override
  public void handleUpdateTag(CompoundNBT tag)
  {
    read(tag);
  }
  
  @Override
  public CompoundNBT getUpdateTag()
  {
    return write(new CompoundNBT());
  }
  
  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
  {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
    {
      return handlerLazyOptional.cast();
    }
    
    return super.getCapability(cap, side);
  }
  
  public ItemStack getItem()
  {
    return item.copy();
  }
  
  public void onClick(World world, PlayerEntity player)
  {
    long now = System.currentTimeMillis();
    if (now - lastClicked > 1000 / 5)
    {
      lastClicked = now;
      ItemStack stack;
      if (!player.inventory.addItemStackToInventory(stack = getItem()))
      {
        Vec3d dropPos = Utilities.getVec3d(pos).add(0.5, 1.2, 0.5);
        InventoryHelper.spawnItemStack(world, dropPos.x, dropPos.y, dropPos.z, stack);
      }
    }
  }
  
  public void setItem(ItemStack item)
  {
    this.item = item;
    markDirty();
  }
}
