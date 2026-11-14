package com.endralink.app

import android.hardware.usb.UsbConstants
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager

/**
 * Read-only diagnostics for the TI-Nspire CX II CAS. These helpers NEVER claim an
 * interface, reset a device, detach a driver, or attempt an N-Link session.
 *
 * USB deviceName (/dev/bus/usb/001/002 etc.) is an ephemeral enumeration address.
 * Use VID:PID to identify a device, and only use a live UsbDevice after reconnect.
 */
internal object NspireUsbDiagnostics {
    const val VENDOR_ID = 0x0451
    const val PRODUCT_ID = 0xE022

    fun isNspire(vendorId: Int, productId: Int): Boolean =
        vendorId == VENDOR_ID && productId == PRODUCT_ID

    fun isNspire(device: UsbDevice): Boolean = isNspire(device.vendorId, device.productId)

    fun describe(device: UsbDevice, usb: UsbManager): String = buildString {
        append("usbIdentity=%04X:%04X".format(device.vendorId, device.productId))
        append(" address=").append(device.deviceName)
        append(" deviceId=").append(device.deviceId)
        append(" interfaces=").append(device.interfaceCount)
        append(" permission=").append(usb.hasPermission(device))
        for (index in 0 until device.interfaceCount) {
            val intf = device.getInterface(index)
            append(" | if=").append(index)
            append(" class=").append(intf.interfaceClass)
            append(" subclass=").append(intf.interfaceSubclass)
            append(" protocol=").append(intf.interfaceProtocol)
            append(" endpoints=").append(intf.endpointCount)
            for (e in 0 until intf.endpointCount) {
                val ep = intf.getEndpoint(e)
                append(" ep").append(e)
                append("=0x%02X".format(ep.address))
                append(if (ep.direction == UsbConstants.USB_DIR_IN) "(IN)" else "(OUT)")
                append(" type=").append(ep.type)
            }
        }
    }
}
