package com.smarttoni.guestgroup.entities

import androidx.room.Entity

@Entity
class GuestGroup(var name: Int,var room: String,var station: String,var table: String)