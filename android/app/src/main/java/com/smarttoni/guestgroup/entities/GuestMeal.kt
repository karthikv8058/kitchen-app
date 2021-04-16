package com.smarttoni.guestgroup.entities

import androidx.room.Entity


@Entity
class GuestMeal(var mealId: String,var recipe: String,var amount:Float,var guest:String,var guestName:String,var paymnet:Int)