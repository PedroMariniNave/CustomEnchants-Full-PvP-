package com.zpedroo.customenchants.cache;

import com.zpedroo.customenchants.objects.EnchantProperties;

import java.util.*;

public class DataCache {

    private Map<String, EnchantProperties> enchants;

    public DataCache() {
        this.enchants = new HashMap<>(8);
    }

    public Map<String, EnchantProperties> getEnchants() {
        return enchants;
    }
}