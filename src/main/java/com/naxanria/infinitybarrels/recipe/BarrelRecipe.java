package com.naxanria.infinitybarrels.recipe;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import com.naxanria.infinitybarrels.InfinityBarrels;
import com.naxanria.infinitybarrels.registry.ModBlocks;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

/*
  @author: Naxanria
*/
public class BarrelRecipe extends ShapedRecipe
{
  public static final BarrelSerializer SERIALIZER = new BarrelSerializer();
  public static final String NAME = "barrel";
  private static final int MAX_HEIGHT = 3;
  private static final int MAX_WIDTH = 3;
  
  public BarrelRecipe(ResourceLocation id, String group, int recipeWidth, int recipeHeight, NonNullList<Ingredient> recipeItems, ItemStack recipeOutput)
  {
    super(id, group, recipeWidth, recipeHeight, recipeItems,
      Util.make(new ItemStack(ModBlocks.BARREL.get()),
        stack -> {
          stack.getOrCreateTagElement("BlockEntityTag").put("item", recipeOutput.serializeNBT());
//          stack.getOrCreateTagElement("BlockEntityTag").put("Items", new ListNBT());
//
//          InfinityBarrels.LOGGER.info("Recipe for {} loading", recipeOutput.getItem().getRegistryName().toString());
//          CompoundNBT itemNbt = recipeOutput.serializeNBT();
//          InfinityBarrels.LOGGER.info("\titem nbt {}", itemNbt.toString());
//          if (stack.hasTag())
//          {
//            InfinityBarrels.LOGGER.info("\tTag present");
//            if (stack.getTag().contains("BlockEntityTag"))
//            {
//              InfinityBarrels.LOGGER.info("BlockTag: {}", stack.getTag().get("BlockEntityTag"));
//            }
//            else
//            {
//              InfinityBarrels.LOGGER.info("No BlockEntityTag");
//            }
//          }
//
//          stack.getOrCreateTag().put("BlockEntityTag", new CompoundNBT().put("item", itemNbt));
//          InfinityBarrels.LOGGER.info("BlockTag: {}", stack.getTag().get("BlockEntityTag"));
        }
      )
    );
  }
  
  private static NonNullList<Ingredient> deserializeIngredients(String[] pattern, Map<String, Ingredient> keys, int patternWidth, int patternHeight)
  {
    NonNullList<Ingredient> nonnulllist = NonNullList.withSize(patternWidth * patternHeight, Ingredient.EMPTY);
    Set<String> set = Sets.newHashSet(keys.keySet());
    set.remove(" ");
    
    for (int i = 0; i < pattern.length; ++i)
    {
      for (int j = 0; j < pattern[i].length(); ++j)
      {
        String s = pattern[i].substring(j, j + 1);
        Ingredient ingredient = keys.get(s);
        if (ingredient == null)
        {
          throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
        }
        
        set.remove(s);
        nonnulllist.set(j + patternWidth * i, ingredient);
      }
    }
    
    if (!set.isEmpty())
    {
      throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
    }
    else
    {
      return nonnulllist;
    }
  }
  
  @VisibleForTesting
  static String[] shrink(String... toShrink)
  {
    int i = Integer.MAX_VALUE;
    int j = 0;
    int k = 0;
    int l = 0;
    
    for (int i1 = 0; i1 < toShrink.length; ++i1)
    {
      String s = toShrink[i1];
      i = Math.min(i, firstNonSpace(s));
      int j1 = lastNonSpace(s);
      j = Math.max(j, j1);
      if (j1 < 0)
      {
        if (k == i1)
        {
          ++k;
        }
        
        ++l;
      }
      else
      {
        l = 0;
      }
    }
    
    if (toShrink.length == l)
    {
      return new String[0];
    }
    else
    {
      String[] astring = new String[toShrink.length - l - k];
      
      for (int k1 = 0; k1 < astring.length; ++k1)
      {
        astring[k1] = toShrink[k1 + k].substring(i, j + 1);
      }
      
      return astring;
    }
  }
  
  private static int firstNonSpace(String str)
  {
    int i;
    for (i = 0; i < str.length() && str.charAt(i) == ' '; ++i)
    {
      ;
    }
    
    return i;
  }
  
  private static int lastNonSpace(String str)
  {
    int i;
    for (i = str.length() - 1; i >= 0 && str.charAt(i) == ' '; --i)
    {
      ;
    }
    
    return i;
  }
  
  private static String[] patternFromJson(JsonArray jsonArr)
  {
    String[] astring = new String[jsonArr.size()];
    if (astring.length > MAX_HEIGHT)
    {
      throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
    }
    else if (astring.length == 0)
    {
      throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
    }
    else
    {
      for (int i = 0; i < astring.length; ++i)
      {
        String s = GsonHelper.getAsString(jsonArr.get(i).getAsJsonObject(), "pattern[" + i + "]");
        if (s.length() > MAX_WIDTH)
        {
          throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
        }
        
        if (i > 0 && astring[0].length() != s.length())
        {
          throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
        }
        
        astring[i] = s;
      }
      
      return astring;
    }
  }
  
  /**
   * Returns a key json object as a Java HashMap.
   */
  private static Map<String, Ingredient> deserializeKey(JsonObject json)
  {
    Map<String, Ingredient> map = Maps.newHashMap();
    
    for (Map.Entry<String, JsonElement> entry : json.entrySet())
    {
      if (entry.getKey().length() != 1)
      {
        throw new JsonSyntaxException("Invalid key entry: '" + (String) entry.getKey() + "' is an invalid symbol (must be 1 character only).");
      }
      
      if (" ".equals(entry.getKey()))
      {
        throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
      }
      
      map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
    }
    
    map.put(" ", Ingredient.EMPTY);
    return map;
  }
  
  public static ItemStack deserializeItem(JsonObject json)
  {
    String s = GsonHelper.getAsString(json, "item");
    
    Item item = Registry.ITEM.getOptional(new ResourceLocation(s)).orElseThrow(() ->
    {
      return new JsonSyntaxException("Unknown item '" + s + "'");
    });
    if (json.has("data"))
    {
      throw new JsonParseException("Disallowed data tag found");
    }
    else
    {
      int i = GsonHelper.getAsInt(json, "count", 1);
      return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(json, true);
    }
  }
  
  public static class BarrelSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<BarrelRecipe>
  {
    public static final ResourceLocation NAME = new ResourceLocation(InfinityBarrels.MODID, "barrel");
  
    @Override
    public BarrelRecipe fromJson(ResourceLocation recipeId, JsonObject json)
    {
      String s = GsonHelper.getAsString(json, "group", "");
      Map<String, Ingredient> map = BarrelRecipe.deserializeKey(GsonHelper.getAsJsonObject(json, "key"));
      String[] astring = BarrelRecipe.shrink(BarrelRecipe.patternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
      int i = astring[0].length();
      int j = astring.length;
      NonNullList<Ingredient> nonnulllist = BarrelRecipe.deserializeIngredients(astring, map, i, j);
      ItemStack itemstack = BarrelRecipe.deserializeItem(GsonHelper.getAsJsonObject(json, "result"));
      return new BarrelRecipe(recipeId, s, i, j, nonnulllist, itemstack);
    }
  
    @Nullable
    @Override
    public BarrelRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
    {
      int i = buffer.readVarInt();
      int j = buffer.readVarInt();
      String s = buffer.readUtf();
      NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);
  
      for (int k = 0; k < nonnulllist.size(); ++k)
      {
        nonnulllist.set(k, Ingredient.fromNetwork(buffer));
      }
  
      ItemStack itemstack = buffer.readItem();
      return new BarrelRecipe(recipeId, s, i, j, nonnulllist, itemstack);
    }
  
    @Override
    public void toNetwork(FriendlyByteBuf buffer, BarrelRecipe recipe)
    {
      buffer.writeVarInt(recipe.getWidth());
      buffer.writeVarInt(recipe.getHeight());
      buffer.writeUtf(recipe.getGroup());
  
      for (Ingredient ingredient : recipe.getIngredients())
      {
        ingredient.toNetwork(buffer);
      }
  
      buffer.writeItemStack(recipe.getResultItem(), false);
    }
  }
}
