package com.smarttoni.assignment.util;

import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Modifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModifierHelper {

    private ModifierHelper() {
    }

    private static ModifierHelper INSTANCE = new ModifierHelper();

    public static ModifierHelper getInstance() {
        return INSTANCE;
    }

//    public String getCommaSeparatedModifiers(Context context,Long selectedRecipeId){
//
//        List<SelectedRecipeModifier> modifiers = DbOpenHelper.Companion
//                .getDaoSession(context)
//                .getSelectedRecipeModifierDao()
//                .queryBuilder()
//                .where(SelectedRecipeModifierDao.Properties.SelectedRecipeId.eq(selectedRecipeId)).list();
//
//        ArrayList<String> modifierIds = new ArrayList<String>();
//        for (SelectedRecipeModifier selectedModifier : modifiers) {
//            modifierIds.add(String.valueOf(selectedModifier.getModifierId()));
//        }
//        Collections.sort(modifierIds);
//        return TextUtils.join(",", modifierIds);
//    }

    public List<Modifier> geModifiersFromCommaSeparated(DaoAdapter daoAdapter, String commaSeparatedModifiers) {
        if (commaSeparatedModifiers == null || commaSeparatedModifiers.equals("")) {
            return null;
        }
        List<Long> ids = new ArrayList<>();
        String[] temp = commaSeparatedModifiers.split(",");
        for (String id : temp) {
            ids.add(Long.parseLong(id));
        }
        return daoAdapter.geModifierByIds(ids);
    }

    public String geModifierNameCommaSeparated(List<Modifier> modifiers) {
        if (modifiers == null) {
            return "";
        }
        final Iterator<?> it = modifiers.iterator();
        if (!it.hasNext()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        Modifier m = (Modifier) it.next();
        sb.append(m.getModifier());
        while (it.hasNext()) {
            Modifier modifier = (Modifier) it.next();
            sb.append(",");
            sb.append(modifier.getModifier());
        }
        return sb.toString();
    }
}
