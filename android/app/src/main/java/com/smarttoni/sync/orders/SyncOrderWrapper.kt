package com.smarttoni.sync.orders

import com.smarttoni.entities.ExternalAvailableQuantity
import com.smarttoni.entities.ExternalOrderRequest

class SyncOrderWrapper {
    var orders: List<SyncOrder>? = null
    var availableQuantity: List<ExternalAvailableQuantity>? = null
    var requestedQuantity: List<ExternalOrderRequest>? = null
}