package com.hepdd.ae2emicraftingforge.client.recipes.locale;

import appeng.core.localization.LocalizationEnum;

import java.util.Locale;

public enum TranslationKeys implements LocalizationEnum {

    CATEGORY_CHARGER("Charger"),
    CATEGORY_CONDENSER("Condenser"),
    CATEGORY_ENTROPY_MANIPULATOR("Entropy Manipulator"),
    CATEGORY_INSCRIBER("Inscriber"),
    CATEGORY_ATTUNEMENT("P2P Tunnel Attunement"),
    CATEGORY_TRANSFORM("In-World Transformation"),

    ;

    private final String englishText;

    TranslationKeys(String englishText) {
        this.englishText = englishText;
    }

    @Override
    public String getEnglishText() {
        return englishText;
    }

    @Override
    public String getTranslationKey() {
        return "ae2-emi-crafting-forge." + name().toLowerCase(Locale.ROOT);
    }
}
