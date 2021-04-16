package com.smarttoni.guestgroup.entities

import androidx.room.Entity
import com.smarttoni.entities.Recipe
import org.greenrobot.greendao.annotation.Transient

@Entity
class Test {
    var mealId: String? = null

    @Transient
    var reicpes: List<Recipe>? = null

}