package com.smarttoni.utils;

import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.UnitConversion;
import com.smarttoni.entities.Units;

import java.util.List;


public class UnitHelper {

    private static final int DIGITS_3 = 1;
    private static final int DIGITS_2 = 2;
    private static final int DIGITS_1 = 3;
    private static final int DIGITS_4Plus = 4;
    private static final int DIGITS_1Less = 5;


    public static float convertToRecipeUnit(String from, Recipe recipe, float required) {
        if (Strings.isEmpty(from)) {
            from = recipe.getOutputUnit().getId();
        }
        String to = recipe.getOutputUnit().getId();
        if (from.equals(to)) {
            return required;
        }
        DaoAdapter daoAdapter = ServiceLocator
                .getInstance()
                .getDatabaseAdapter();
        List<UnitConversion> conversions = daoAdapter
                .listUnitConversions(from);

        for (UnitConversion unitConversion : conversions) {
            if (unitConversion.getFrom().equals(from) && unitConversion.getTo().equals(to)) {
                return required * unitConversion.getFactor();
            }
        }

        return required;
    }

    public static int getRecipeQty(String from, Recipe recipe, float required) {
        if (Strings.isEmpty(from)) {
            from = recipe.getOutputUnit().getId();
        }
        String to = recipe.getOutputUnit().getId();
        if (from.equals(to)) {
            return StMath.roundQuantity(required,recipe.getOutputQuantity());
        }
        DaoAdapter daoAdapter = ServiceLocator
                .getInstance()
                .getDatabaseAdapter();
        List<UnitConversion> conversions = daoAdapter
                .listUnitConversions(from);

        for (UnitConversion unitConversion : conversions) {
            if (unitConversion.getFrom().equals(from) && unitConversion.getTo().equals(to)) {
                return StMath.roundQuantity(required * unitConversion.getFactor(),recipe.getOutputQuantity());
            }
        }

        return StMath.roundQuantity(required,recipe.getOutputQuantity());
    }

    public static float convertToUnit(String from, String to, float fromQty) {
        if (Strings.isNotEmpty(from) && Strings.isNotEmpty(to) && !from.equals(to)) {
            DaoAdapter daoAdapter = ServiceLocator
                    .getInstance()
                    .getDatabaseAdapter();
            List<UnitConversion> conversions = daoAdapter
                    .listUnitConversions(from);
            for (UnitConversion unitConversion : conversions) {
                if (unitConversion.getTo().equals(to)) {
                    return fromQty * unitConversion.getFactor();
                }
            }
        }

        return fromQty;
    }

    public static String convertToString(float qty, Units unit) {

        DaoAdapter daoAdapter = ServiceLocator
                .getInstance()
                .getDatabaseAdapter();
        List<UnitConversion> conversions = daoAdapter
                .listUnitConversions(unit.getId());

        UnitConversion best = null;
        int score = bestScore(qty);
        for (UnitConversion unitConversion : conversions) {
            float q = unitConversion.getFactor() * qty;
            if (best != null) {
                if (score == DIGITS_4Plus) {
                    if (unitConversion.getFactor() > best.getFactor()) {
                        continue;
                    }
                } else if (score == DIGITS_1Less) {
                    if (unitConversion.getFactor() < best.getFactor()) {
                        continue;
                    }
                }
            }
            int newScore = bestScore(q);
            if (newScore < score) {
                score = bestScore(q);
                best = unitConversion;
            } else if (best != null) {
                if ((newScore == DIGITS_4Plus && score == DIGITS_4Plus) ||
                        (newScore == DIGITS_1Less && score == DIGITS_1Less)) {
                    best = unitConversion;
                }
            }
            if (score == DIGITS_3) {
                break;
            }
        }
        if (best != null) {
            return Strings.formatUnitString(qty * best.getFactor(), daoAdapter.getUnitById(best.getTo()).getSymbol());
        }
        return Strings.formatUnitString(qty, unit.getSymbol());
    }

    private static int bestScore(float qty) {
        qty = (float) Math.ceil(qty);
        if (qty >= 1000) {
            return DIGITS_4Plus;
        } else if (qty >= 100) {
            return DIGITS_3;
        } else if (qty >= 10) {
            return DIGITS_2;
        } else if (qty >= 1) {
            return DIGITS_1;
        } else {
            return DIGITS_1Less;
        }
    }

    private static double round(float qty) {
        return Math.round(qty * 100.0) / 100.0;
    }
}
