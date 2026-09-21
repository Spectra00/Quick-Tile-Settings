package com.rbn.qtsettings.utils

object Constants {
    const val PRIVATE_DNS_MODE = "private_dns_mode"
    const val PRIVATE_DNS_SPECIFIER = "private_dns_specifier" // Hostname for DNS_MODE_ON
    const val ADB_ENABLED = "adb_enabled" // USB Debugging (0 or 1)
    const val ADB_WIFI_ENABLED = "adb_wifi_enabled" // Wireless Debugging (0 or 1)
    const val DEVELOPMENT_SETTINGS_ENABLED =
        "development_settings_enabled" // Developer Options (0 or 1)

    const val DNS_MODE_OFF = "off"
    const val DNS_MODE_AUTO = "opportunistic"
    const val DNS_MODE_ON = "hostname"

    const val TILE_ONLY_DETECTION = "tile_only"
    const val BACKGROUND_DETECTION = "background"

    // Per-network DNS actions
    const val WIFI_DNS_ACTION_DEFAULT = "default"

    // Network type constants
    const val NETWORK_TYPE_WIFI = "wifi"
    const val NETWORK_TYPE_MOBILE = "mobile"
    const val NETWORK_TYPE_NONE = "none"

    // Shortcut actions
    const val ACTION_DNS_OFF = "com.rbn.qtsettings.action.DNS_OFF"
    const val ACTION_DNS_AUTO = "com.rbn.qtsettings.action.DNS_AUTO"
    const val ACTION_DNS_ADGUARD = "com.rbn.qtsettings.action.DNS_ADGUARD"
    const val ACTION_DNS_CLOUDFLARE = "com.rbn.qtsettings.action.DNS_CLOUDFLARE"
    const val ACTION_DNS_QUAD9 = "com.rbn.qtsettings.action.DNS_QUAD9"
    const val ACTION_DNS_CUSTOM = "com.rbn.qtsettings.action.DNS_CUSTOM"
    const val ACTION_USB_ON = "com.rbn.qtsettings.action.USB_ON"
    const val ACTION_USB_OFF = "com.rbn.qtsettings.action.USB_OFF"

    // Shortcut intent extras
    const val EXTRA_DNS_ENTRY_ID = "extra_dns_entry_id"

    // Hotspot Tile (root-based NAT tethering)
    // Interface names are device-specific; verify with `ip link` / `dumpsys wifi` if the
    // tile stops working after a firmware or kernel update.
    const val HOTSPOT_WIFI_INTERFACE = "wlan2"
    const val HOTSPOT_UPSTREAM_INTERFACE = "rmnet_data0"
    const val HOTSPOT_SUBNET = "192.168.43.0/24"

    // `cmd wifi start-softap` requires an SSID and security type; bare "start-softap" is
    // invalid. Security type is one of: open, owe, owe_transition, wpa2, wpa3,
    // wpa3_transition. A passphrase is required (8-63 chars) for wpa2/wpa3/wpa3_transition
    // and ignored otherwise. CHANGE HOTSPOT_PASSPHRASE below before building - this default
    // is a placeholder, not a real secret.
    const val HOTSPOT_SSID = "OnePlus15-Hotspot"
    const val HOTSPOT_SECURITY_TYPE = "wpa2"
    const val HOTSPOT_PASSPHRASE = "ChangeMe123"

    const val HOTSPOT_START_COMMAND =
        "cmd wifi start-softap \"$HOTSPOT_SSID\" $HOTSPOT_SECURITY_TYPE \"$HOTSPOT_PASSPHRASE\" && ndc nat enable $HOTSPOT_WIFI_INTERFACE $HOTSPOT_UPSTREAM_INTERFACE 1 $HOTSPOT_SUBNET"
    const val HOTSPOT_STOP_COMMAND =
        "ndc nat disable $HOTSPOT_WIFI_INTERFACE $HOTSPOT_UPSTREAM_INTERFACE 1 $HOTSPOT_SUBNET ; cmd wifi stop-softap"

    // Shared between HotspotToggleTileService and QuickActionDialogActivity so both agree on
    // where the tile's last-known on/off state lives.
    const val HOTSPOT_PREFS_NAME = "hotspot_tile_service_state"
    const val HOTSPOT_PREFS_KEY_ON = "hotspot_on"
}
