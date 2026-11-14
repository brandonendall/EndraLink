# EndraLink 0.1.25 — TI-Nspire USB repair baseline

**Status: investigation / source recovery; NOT an APK build or a completed fix.**

## What happened

On TI-Nspire CX II CAS using installed EndraLink **0.1.24**:

- Android detects VID:PID `0451:E022` and grants permission.
- FD wrapping succeeds; interface 0, IN endpoint `0x81`, OUT endpoint `0x01` are reported.
- Native initialization takes ~10–11 seconds and either returns `LibUSB error` or succeeds.
- If initialization succeeds, root listing takes another ~11–12 seconds and returns `Directory read failed: Busy`.
- On one error sequence the device subsequently disappears from Android enumeration.

The calculator had recently run CPUID Nspire and hidden diagnostics, and temporarily stopped charging before a hard reset; **no causal link or persistent modification has been established**. Its Ndless startup folder was empty and `ndless.cfg.tns` contained image-viewer extension associations only.

## Verified working USB control case

Termux with `nlink-termux 0.1.0` accesses and downloads calculator files through the Android USB FD launcher:

```sh
clear
USB=$(termux-usb -l | grep -o '/dev/bus/usb/[0-9]*/[0-9]*' | head -n 1)
test -n "$USB" || { echo 'No USB device'; exit 1; }
termux-usb -r -E -e 'nlink ls /' "$USB"
termux-usb -r -E -e 'nlink download EndraHome_Apps.cfg.tns .' "$USB"
```

The download returned a 553-byte file. Plain `nlink ls /` without the Termux FD wrapper fails. Paths such as `/dev/bus/usb/001/002`, `/001/009`, `/001/011` are ephemeral enumeration addresses, NOT USB VID/PID changes. For full reference see [TERMUX_USB_NLINK.md](TERMUX_USB_NLINK.md).

## Critical source baseline mismatch

The GitHub `main` branch currently declares `versionName = "0.1.20"`, `versionCode = 21` in `app/build.gradle.kts`. Inspected alternate `phone-responsive-0.1.21` branch declares 0.1.21. The source of the installed EndraLink 0.1.24 N-Link native bridge has NOT been located in inspected branches. This branch therefore must **not** be released as 0.1.25 until complete 0.1.24 source (including native code and build dependencies) is recovered. Keep all existing file manager features; don't overwrite the release with an older codebase.

## Implementation acceptance criteria

1. Find the source revision that produced 0.1.24 (including JNI source and libusb/N-Link implementation); verify existing app behavior and version.
2. Trace and log each USB stage: Android descriptor IDs, FD open/ownership, libusb wrap result, interface claim, endpoint discovery, handshake, directory listing; include **numeric libusb error and human-readable name**, not just `LibUSB error`.
3. Time operations with monotonic timestamps, and report timeout vs busy vs no-device separately.
4. Handle connect/disconnect and USB re-enumeration without relying on a cached path; prevent concurrent session commands and close handles on all errors.
5. Avoid automatic device resets, hook deletions, OS reinstallations, forced interface detachment, or writes to the calculator as diagnostics.
6. Test on real TI-Nspire (list root, download known file, disconnect and reconnect) and verify fx-CG50 workflows are unchanged.
7. Only then bump the version and publish a **separately validated** APK.

Tracking: [GitHub issue #4](https://github.com/brandonendall/EndraLink/issues/4).
