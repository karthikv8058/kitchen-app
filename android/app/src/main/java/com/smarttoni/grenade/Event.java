package com.smarttoni.grenade;


/**
 * __
 * ____ _________  ____  ____ _____/ /__
 * / __ `/ ___/ _ \/ __ \/ __ `/ __  / _ \
 * / /_/ / /  /  __/ / / / /_/ / /_/ /  __/
 * \__, /_/   \___/_/ /_/\__,_/\__,_/\___/
 * /____/
 * <p>
 * <p>
 * The Event Emitter for SmartTONi
 */
public enum Event {
    SERVER_START,
    SERVER_STOP,
    REACT_PUSH,

    SEND_PEGION,

    ORDER_RECEIVED,
    ORDER_PROCESSED,
    ORDER_COMPLETED,
    ORDER_REMOVED
}
