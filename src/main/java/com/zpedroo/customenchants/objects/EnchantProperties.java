package com.zpedroo.customenchants.objects;

public class EnchantProperties {

    private String name;
    private String display;
    private int maxLevel;
    private int expPerLevel;

   public EnchantProperties(String name, String display, int maxLevel, int expPerLevel) {
       this.name = name;
       this.display = display;
       this.maxLevel = maxLevel;
       this.expPerLevel = expPerLevel;
   }

   public String getName() {
       return name;
   }

    public String getDisplay() {
        return display;
    }

    public int getMaxLevel() {
       return maxLevel;
   }

   public int getExpPerLevel() {
       return expPerLevel;
   }
}