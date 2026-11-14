# Termux USB access for TI-Nspire (nlink-termux)

Verified working on 2026-11-14 with `nlink-termux 0.1.0`, a TI-Nspire CX II CAS, and Android Termux.

## Required invocation

Use `termux-usb -r -E -e` to launch `nlink` with the authorized USB device descriptor. Invoking `nlink` directly from the Termux shell may return `Couldn't find any device` even when `termux-usb -l` shows the device and `termux-usb -r` grants permission.

```bash
clear
termux-usb -l
termux-usb -r -E -e 'nlink ls /' /dev/bus/usb/001/002
```

**Successful read-only result:** the calculator root directory was listed, including `MyLib/`, `ndless/`, `GBA/`, and EndraHome files.

Upload example (writes to calculator; run only when intended):

```bash
clear
termux-usb -r -E -e 'nlink upload BaldursGateDarkAlliance.gbz.tns /' /dev/bus/usb/001/002
```

Replace `/dev/bus/usb/001/002` with the **current** path from `termux-usb -l`; the number can change on reconnection. Keep other calculator USB apps (including EndraLink) closed during the test.

## Troubleshooting context

- Android USB enumeration alone does not prove that `nlink` has an accessible file descriptor.
- A separate `termux-usb -r /dev/bus/usb/001/002` can report `Permission granted` while plain `nlink ls /` still fails with `Couldn't find any device`.
- The wrapped `termux-usb -r -E -e 'nlink ls /' ...` successfully retrieved the file list after EndraLink reported `Directory read failed: Busy`.
- This demonstrates that calculator file-transfer access works through wrapped N-Link; it does **not**, by itself, establish the specific root cause of EndraLink's Busy error.
