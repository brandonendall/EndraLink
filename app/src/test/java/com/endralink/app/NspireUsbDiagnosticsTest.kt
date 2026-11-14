package com.endralink.app

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NspireUsbDiagnosticsTest {
    @Test fun identifiesTiNspireCxIiCas() {
        assertTrue(NspireUsbDiagnostics.isNspire(0x0451, 0xE022))
    }

    @Test fun doesNotMistakeFxCg50OrOtherTiDevicesForNspire() {
        assertFalse(NspireUsbDiagnostics.isNspire(0x07CF, 0x6102))
        assertFalse(NspireUsbDiagnostics.isNspire(0x0451, 0x0000))
        assertFalse(NspireUsbDiagnostics.isNspire(0x0000, 0xE022))
    }
}
