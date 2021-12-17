package com.zpedroo.customenchants.utils.config;

import com.zpedroo.customenchants.utils.FileUtils;

public class Messages {

    public static final String MAX_LEVEL = FileUtils.get().getColoredString(FileUtils.Files.CONFIG, "Messages.max-level");

    public static final String INCOMPATIBLE_ITEM = FileUtils.get().getColoredString(FileUtils.Files.CONFIG, "Messages.incompatible-item");

    public static final String INSUFFICIENT_EXP = FileUtils.get().getColoredString(FileUtils.Files.CONFIG, "Messages.insufficient-exp");

    public static final String OFFLINE_PLAYER = FileUtils.get().getColoredString(FileUtils.Files.CONFIG, "Messages.offline-player");

    public static final String INVALID_AMOUNT = FileUtils.get().getColoredString(FileUtils.Files.CONFIG, "Messages.invalid-amount");
}