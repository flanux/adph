# Phone Truth - Android Surveillance Detector

A simple Android app that reveals what's really installed on your phone and flags suspicious behavior.

## What it does

- **Lists ALL apps** (including hidden ones with no launcher icon)
- **Flags Device Admin apps** (can control your device)
- **Flags Accessibility Services** (can see/record everything on screen)
- **Shows dangerous permissions** (camera, mic, location, SMS, etc.)
- **Sorts by suspiciousness** (most suspicious apps at top)

## How to install on your phone

### Option 1: Download from GitHub Releases (EASIEST)

1. Go to the "Releases" section on the right side of this GitHub page
2. Download the latest `phone-truth-release.apk` file
3. On your phone, go to Settings → Security → Enable "Install from unknown sources"
4. Open the downloaded APK and install
5. Done!

### Option 2: Build it yourself with GitHub Actions

Every time you push code to this repo, GitHub automatically builds the APK for you:

1. Make any code changes you want
2. Push to GitHub
3. Go to "Actions" tab at the top
4. Click on the latest workflow run
5. Scroll down to "Artifacts" section
6. Download `phone-truth-apk`
7. Extract the APK from the zip file
8. Install on your phone

## What to look for

**🔴 RED FLAGS:**
- Apps with "DEVICE ADMIN ENABLED" - can wipe your phone, lock you out
- Apps with "ACCESSIBILITY SERVICE ACTIVE" - can record everything you do
- Apps with "HIDDEN APP (no launcher icon)" that you don't recognize

**⚠️ YELLOW FLAGS:**
- Many camera/microphone permissions on apps that shouldn't need them
- Unknown apps with location tracking
- Apps you don't remember installing

**Safe examples:**
- WhatsApp with camera/mic (expected for calls)
- Google Maps with location (expected)
- Your bank app with device admin (some require this)

## How to use

1. Open the app
2. Wait a few seconds while it scans
3. Scroll through the list
4. Apps with warnings are at the top
5. If you see something suspicious, Google the package name to research it

## Technical details

- Minimum Android: 7.0 (API 24)
- Target Android: 14 (API 34)
- Language: Kotlin
- No internet permission (runs completely offline)
- No analytics, no tracking, no bullshit

## Privacy

This app does NOT:
- Send any data anywhere
- Store any data
- Connect to the internet
- Track you

It just reads what's already on your phone and shows it to you.

## Limitations

- Cannot detect ALL stalkerware (some are very sophisticated)
- Cannot remove apps (you have to do that manually)
- Cannot detect network traffic (would require root)
- Cannot detect apps that hide even from package manager (rare, requires root)

## For developers

Project structure:
```
app/src/main/
├── java/com/phonetruth/detector/
│   └── MainActivity.kt          # Main scanning logic
├── res/
│   ├── layout/
│   │   ├── activity_main.xml    # Main screen
│   │   └── item_app.xml         # App list item
│   └── values/
│       └── styles.xml           # Dark theme
└── AndroidManifest.xml
```

## Next features (help wanted!)

- [ ] Export results to text file
- [ ] Show when apps were installed
- [ ] Network usage per app
- [ ] Battery usage anomalies
- [ ] Timeline view of permission usage

## License

Do whatever you want with this. No license. Public domain. Copy it, modify it, sell it, I don't care.

The point is to help people stay safe.

---

Built in Nepal 🇳🇵 to fight digital surveillance
