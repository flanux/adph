# SETUP GUIDE - How to get this running

## Step 1: Create GitHub Account (if you don't have one)
1. Go to github.com
2. Sign up (free)

## Step 2: Create New Repository
1. Click the "+" in top right → "New repository"
2. Name it: `phone-truth`
3. Make it Public (required for free Actions)
4. DON'T initialize with README (we already have one)
5. Click "Create repository"

## Step 3: Upload the code

### Option A: Using GitHub website (easier for you)
1. On your new empty repo page, click "uploading an existing file"
2. Drag ALL the folders/files from the phone-truth folder I gave you
3. Make sure you upload:
   - `.github` folder (contains workflows)
   - `app` folder (contains code)
   - `gradle` folder
   - `build.gradle`
   - `settings.gradle.kts`
   - `gradlew`
   - `README.md`
   - `.gitignore`
4. Scroll down, click "Commit changes"

### Option B: Using git command line (if you know git)
```bash
cd /path/to/phone-truth
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/phone-truth.git
git push -u origin main
```

## Step 4: Enable GitHub Actions
1. Go to your repo on GitHub
2. Click "Actions" tab at top
3. If it asks, click "I understand my workflows, go ahead and enable them"

## Step 5: Trigger a build
GitHub Actions should automatically start building. If not:
1. Go to Actions tab
2. Click "Build APK" on the left
3. Click "Run workflow" button on the right
4. Click the green "Run workflow" button

## Step 6: Wait for build (takes ~5 minutes first time)
1. You'll see a yellow circle (building)
2. Wait until it turns green (success) or red (failed)

## Step 7: Download the APK
1. Click on the completed workflow run
2. Scroll to bottom
3. Under "Artifacts", click "phone-truth-apk"
4. It downloads as a zip file
5. Unzip it to get the APK

## Step 8: Install on your phone
1. Send the APK to your phone (email, USB, whatever)
2. On phone: Settings → Security → Enable "Install unknown apps" for your file manager
3. Open the APK file
4. Click "Install"
5. Open "Phone Truth" app

## That's it!

## Troubleshooting

**Build fails:**
- Check the error in Actions tab
- Make sure ALL files were uploaded correctly
- Ensure `.github/workflows/build.yml` is present

**Can't install APK:**
- Enable "Unknown sources" in phone settings
- Make sure it's a .apk file, not a .zip

**Need help:**
- DM me the error message
- Check the Actions logs for details

## Making changes

To modify the code:
1. Edit the files on GitHub (click file → edit button)
2. Commit changes
3. GitHub automatically builds new APK
4. Download from Actions → Artifacts

No Android Studio needed!
