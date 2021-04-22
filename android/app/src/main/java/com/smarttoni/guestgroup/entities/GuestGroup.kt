package com.smarttoni.guestgroup.entities

import androidx.room.Entity

@Entity
class GuestGroup(var name: Int,var station: String,var room: String,var table: String)