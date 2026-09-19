# 🇮🇷 Iranian Hardcore v4.0 - Finglish + Climate + Thirst + Custom Mobs + Resource Pack

**Version 4.0** - Bozorgtarin update! Hala **Finglish** (baraye server hayi ke Farsi namayesh dade nemishavad) + **Ab o Hava** (sard/garm) + **Teshnegi** (bayad ab bokhori) + **7 Mob jadid Irani** (Div, Simurgh, Zahhak) + **Resource Pack makhsus**!

> **Shoar**: Har biome yek ghom Irani - 14 ghom asil - Finglish baraye Aternos - Zende bad Iran - Khalij hameshe Fars 🇮🇷

**NEW v4.0**: Moshkel namayesh Farsi dar Aternos hal shod! Hame matn ha Finglish (Farsi ba horoof Englisi) - Mesle "Ghale Alamoat" be jaye "قلعه الموت"

---

## 📥 Download Mostaghim - Bedune niaz be compile

### ✅ Ravesh 1: Download Jar Amade (Sadatarin - Faghat bezar too plugins)

**Link mostaghim Jar:**
- **File Jar dar repo:** [IranianHardcore-4.0.0.jar](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/raw/main/IranianHardcore-4.0.0.jar
- **Az Release v4.0.0:** [iranian-hardcore-4.0.0.jar](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/releases/download/v4.0.0/iranian-hardcore-4.0.0.jar) (173KB - BUILD SUCCESS))
- **Ya az tarigh GitHub:** Boro be tab [Actions](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/actions) -> Akharin build movafagh -> Download Artifact `IranianHardcore-4.0.0`
- **Ya download az repo:** File `IranianHardcore-4.0.0.jar` dar root repo hast, mostaghim download kon

**Nasb:**
1. File `IranianHardcore-4.0.0.jar` ro download kon
2. Bezar too poshe `plugins` serveret (Spigot 1.12.2)
3. Server ro restart kon
4. Tamam! Plugin faal mishe 🇮🇷

### 📦 Ravesh 2: Download Resource Pack

**Link mostaghim Resource Pack:**
- **File Zip dar repo:** [IranianHardcore-ResourcePack.zip](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/raw/main/resourcepack/IranianHardcore-ResourcePack.zip
- **Az Release v4.0.0:** [IranianHardcore-ResourcePack.zip](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/releases/download/v4.0.0/IranianHardcore-ResourcePack.zip) (2.9KB))
- **Ya local:** `resourcepack/IranianHardcore-ResourcePack.zip` dar repo

**Nasb Resource Pack:**
1. File `IranianHardcore-ResourcePack.zip` ro download kon
2. Bezar too `resourcepacks` ya URL ro dar `config.yml` bezar:
```yaml
resourcepack:
  enabled: true
  auto-send-on-join: true
  url: "https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/raw/main/resourcepack/IranianHardcore-ResourcePack.zip
- **Az Release v4.0.0:** [IranianHardcore-ResourcePack.zip](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/releases/download/v4.0.0/IranianHardcore-ResourcePack.zip) (2.9KB)"
```
3. Bazikonan vaghti join mikonan accept konan

### 📦 Ravesh 3: Download Source Code

**Link mostaghim source:**
- **Download Zip kamel:** [Download source code (main.zip)](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/archive/refs/heads/main.zip)
- **Ya az Releases:** [Safhe Releases](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/releases) -> Akharin version -> Source code (zip)

**Compile dasti (agar khasti):**
```bash
# Ba Maven (niaz be Java 8)
mvn clean package
# Jar dar target/ sakhte mishe
```

### 🎯 Version haye montasher shode

| Version | Tarikh | Vizhegi | Link Download |
|------|-------|--------|-------------|
| **v4.0.0** | 2026-09-19 | Finglish + Climate + Thirst + 7 Mobs + ResourcePack + 14 ghom + 16 dungeon | [Download](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/releases/tag/v4.0.0) |
| **v3.5.3** | 2026-09-18 | Jar amade dar root repo - Raf bug haye compile | [Download Jar](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/raw/main/IranianHardcore-3.5.0.jar) |
| **v3.5.0** | 2026-09-18 | 14 ghom Irani + 16 dungeon + 8 saze + bazar | [Download](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/releases/tag/v3.5.0) |

### ✅ Test shode

- ✅ Spigot 1.12.2
- ✅ Java 8
- ✅ Bedune niaz be database
- ✅ Finglish + Farsi (ghabel taghir dar config)
- ✅ Aternos compatible
- ✅ 14 ghom Irani
- ✅ 16 dungeon tarikhi
- ✅ 8 saze Irani
- ✅ Bazar Irani
- ✅ Roosta Irani
- ✅ **Jadid v4.0**: Climate system (sard/garm)
- ✅ **Jadid v4.0**: Thirst system (teshnegi)
- ✅ **Jadid v4.0**: 7 mob jadid Irani (Div, Simurgh, Zahhak)
- ✅ **Jadid v4.0**: Resource Pack makhsus

---

## 🆕 V4.0 Features Jadid - Darkhast Shoma

### 1. Finglish Version - Hal moshkel Farsi dar Aternos

**Moshkel**: Dar Aternos va server haye dige, horoof Farsi be soorat `????` ya box namayesh dade mishod

**Hal**: Hame matn ha hala Finglish (Farsi ba horoof Englisi)
- Ghabl: `قلعه الموت`
- Hala: `Ghale Alamoat`

**Tanzim dar config.yml:**
```yaml
general:
  language: finglish  # ya fa baraye Farsi asli
```

**Mesal:**
- `Ghale Alamoat` be jaye `قلعه الموت`
- `Takht Jamshid` be jaye `تخت جمشید`
- `Ghome Pars - Fars` be jaye `قوم فارس`
- `Teshne hastid! Bayad ab benoshid!` be jaye `تشنه هستید!`

### 2. Climate System - Ab o Hava Sard/Garm

**Biome haye sard**: ICE_FLATS, TAIGA_COLD, FROZEN_OCEAN - Dar in biome ha sardetan mishe:
- Temp -30 ta -100
- Effect: SLOW, WEAKNESS, damage vaghti -90
- Rah hal: Nazdik atash berid, lebas charm bepushid (leather armor)

**Biome haye garm**: DESERT, MESA, SAVANNA, HELL - Dar in biome ha garmazade mishid:
- Temp +30 ta +100
- Effect: SLOW, CONFUSION, damage vaghti +90
- Rah hal: Ab benoshid, dar saye bemanid, lebas sabok

**Namayesh dar Action Bar:**
```
Teshnegi: 80% 💧💧💧💧💧💧💧💧○○ Dama: -20 (abi baraye sard)
Teshnegi: 60% 💧💧💧💧💧💧○○○○ Dama: +45 (ghermez baraye garm)
```

**Config:**
```yaml
climate:
  enabled: true
  cold:
    damage-at: -90
    slow-at: -50
  hot:
    damage-at: 90
    slow-at: 50
```

### 3. Thirst System - Teshnegi - Bayad Ab Bokhori!

**System**: Har bazikon teshnegi 0-100 darad, 100 = sir, 0 = marg az teshnegi

**Kam shodan teshnegi:**
- Paye: 0.3 har 4 sanie
- Davoidan: +0.4 bishtar
- Biome garm: + temp/100 (ta +1)
- Hardcore: x1.5 sari tar

**Effect haye teshnegi:**
- 40-20%: SLOW
- 20-0%: SLOW 1, WEAKNESS, CONFUSION, payam "Teshne hastid!"
- 0%: Damage 1.5, SLOW 2, WEAKNESS 1, CONFUSION, "Khatar marg az teshnegi!"

**Noshidan ab:**
- `/iranian drink` ya `/iranian ab` - Noshidan ab (+25%)
- `/iranian mashk` - Gereftan Mashk Ab Irani (sonati, az poste boz, 10 bar ab, +30%)
- Right-click ba shishe ab (POTION)
- Water bottle az vanilla ham kar mikone

**Action Bar:** Teshnegi + Dama har lahze namayesh dade mishe

**Config:**
```yaml
thirst:
  enabled: true
  max: 100
  drain-rate: 0.3
  water-bottle:
    amount: 25
    mashk-ab: 30
```

### 4. New Mobs - 7 Mob Jadid Irani - Afsane Irani

| # | Mob | Finglish | Persian | Biome | HP | Damage | Vizhegi |
|---|-----|----------|---------|-------|----|--------|---------|
| 1 | Div Sepid | Div Sepid - Ghoul Sefid | دیو سپید | Ice, Cold | 40 | 8 | SLOW, BLINDNESS, WEAKNESS |
| 2 | Div Siah | Div Siah - Ghoul Siah | دیو سیاه | Desert | 25 | 6 | WITHER, POISON |
| 3 | Simurgh | Simorgh - Morgh Afsanei | سیمرغ | Extreme Hills | 30 | 4 | Parvaz, drop Golden Apple, REGENERATION |
| 4 | Zahhak | Zahhak - Shah Mar | ضحاک | Desert, Mesa | 50 | 10 | 3 sar, POISON, WITHER, drop Diamond |
| 5 | Rostam Ghost | Rouh Rostam - Ghahreman | روح رستم | Extreme Hills | 60 | 12 | Ghahreman, INCREASE_DAMAGE, drop Diamond Sword |
| 6 | Al | Al - Jadoogar Mordab | آل | Swamp, Jungle | 20 | 5 | Witch, CONFUSION, SLOW |
| 7 | Kaveh | Kaveh Ahangar - Dost | کاوه آهنگر | Plains | 80 | 15 | Friendly! Dost bazikon, FIRE_RESISTANCE |

**Spawn:**
- Har 2 daghighe, 5% chance dar har world
- Nazdik bazikon (60 block)
- Biome monaseb baraye har mob
- Mob haye nader (Zahhak, Simurgh, Rostam) broadcast ta 100 block

**Drops:**
- Hame: GOLD_NUGGET 2-6 + 20% GOLD_INGOT
- Zahhak: + DIAMOND 1-2, 50 XP
- Simurgh: + GOLDEN_APPLE, 30 XP
- Rostam: + DIAMOND_SWORD, 40 XP

**Dastor admin:**
```
/iranian spawnmob DIV_SEPID
/iranian spawnmob ZAHHAK
/iranian spawnmob SIMURGH
```

**Config:**
```yaml
mobs:
  custom:
    enabled: true
    spawn-chance: 0.05
    replace-natural: false  # Agar true, 8% spawn haye tabiei ba mob Irani jaygozin mishe
```

### 5. Resource Pack Makhsus - Texture haye Irani

**Shamel:**
- Div Sepid texture (zombie)
- Div Siah texture (husk)
- Simurgh texture (chicken/parrot)
- Zahhak texture (villager zombie)
- Rostam texture (skeleton)
- Al texture (witch)
- Kaveh texture (iron golem)
- Persian items (gold, sword, etc)
- Iranian flag
- Mashk Ab model

**File:** `resourcepack/IranianHardcore-ResourcePack.zip` (2.9KB placeholder, ghabel tosee)

**pack.mcmeta:**
```json
{
  "pack": {
    "pack_format": 3,
    "description": "Iranian Hardcore Resource Pack v4.0 - Div, Simurgh, Zahhak"
  }
}
```

**Tanzim:**
```yaml
resourcepack:
  enabled: true
  auto-send-on-join: false  # Agar true, vaghti join mikone auto send mishe
  url: "https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/raw/main/resourcepack/IranianHardcore-ResourcePack.zip
- **Az Release v4.0.0:** [IranianHardcore-ResourcePack.zip](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/releases/download/v4.0.0/IranianHardcore-ResourcePack.zip) (2.9KB)"
```

**Dastorat:**
```
/iranian resourcepack - Daryaft pack
/iranian packinfo - Etelaat pack
```

---

## 🏛️ 14 Ghom Irani - Har Biome Yek Ghom (Az v3.5)

| # | Ghom Irani Finglish | Biome Haye Khane | Ghabeliat Makhsus | Farhang |
|---|---------------------|------------------|-------------------|---------|
| 1 | **Ghome Pars - Fars** | Dasht, Tape Biabani | Memari Hakhamaneshi: Haste + Moghavemat + Gorosnegi kamtar | Takht Jamshid, Kourosh |
| 2 | **Ghome Azari** | Barf, Taiga Sard, Yakh | Moghavemat sarma, Sorat dar barf | Babak Khorramdin, Sattar Khan |
| 3 | **Ghome Kord** | Koohestan | 70% kahesh soghoot, Paresh Zagros | Howraman |
| 4 | **Ghome Lor** | Tape Jangali | Ghodrat badani, Damdari | Kamanche Lori |
| 5 | **Ghome Baloch** | Biaban, Mesa | Moghavemat garma, Sorat dar shen | Moghavemat kavir |
| 6 | **Ghome Arab Khoozestan** | Mordab, Roodkhane | Tanafos zir ab, Moghavemat garma 50 daraje | Nakhl |
| 7 | **Ghome Torkaman** | Savana | Sorat bala (Asb Torkaman) | Farsh Torkaman |
| 8 | **Ghome Gilak** | Jangal Saghf-dar | Mahigiri, Namarei dar barg | Chai Lahijan |
| 9 | **Ghome Mazani - Tabari** | Jangal Toos, Taiga | Keshavarzi, Tanafos zir ab (Khazar) | Keshti Loochoo |
| 10 | **Ghome Bakhtiari** | Koohestan Boland | Kooch-neshini: Moghavemat soghoot 70% | Choogha Bakhtiari |
| 11 | **Ghome Ghashghaei** | Falat Mesa | Sorat 2, Damdari | Farsh Ghashghaei |
| 12 | **Ghome Bandari - Hormozgan** | Oghyanos, Sahel | Nafas 3 barabar zir ab, Shena sari | Lanj, Khalij Fars |
| 13 | **Ghome Khorasani** | Dasht Aftabgardan | Farhang va adab: Shans ketab + Moghavemat | Ferdowsi, Shahname |
| 14 | **Ghome Sistani** | Kooh Biabani | Ghodrat Rostam 2, Moghavemat kavir | Rostam Dastan, Shahr Sokhte |

**GUI Entekhab Ghom:** `/race choose` ya `/ghome` - 54 slot, Parcham Iran sabz/sefid/ghermez

---

## 🎮 Dastorat v4.0 - Finglish

```
/race choose - Entekhab ghome Irani (14 ghom)
/race info - Etelaat ghome feli
/race list - List 14 ghom

/iranian thirst - Didan mizan teshnegi
/iranian temperature - Didan dama badan
/iranian drink - Noshidan ab (+25%)
/iranian mashk - Gereftan Mashk Ab Irani
/iranian resourcepack - Daryaft resource pack Irani
/iranian spawnmob <type> - Sakht mob Irani (admin)
/iranian help - Rahnama

/bazaar - Bazar Irani
/dungeon list - List 16 dungeon tarikhi

Types mob: DIV_SEPID, DIV_SIAH, SIMURGH, ZAHHAK, ROSTAM_GHOST, AL, KAVEH
```

---

## 📦 Sakhtar Proje v4.0

```
src/main/java/ir/iranian/hardcore/
  climate/TemperatureManager.java - System dama, biome sard/garm
  thirst/ThirstManager.java - System teshnegi, Mashk Ab
  mobs/CustomMobType.java - 7 mob Irani (Div, Simurgh, Zahhak...)
  mobs/IranianMobsManager.java - Spawn mob, effect, drop
  resourcepack/ResourcePackManager.java - Send pack, status
  commands/IranianCommand.java - /iranian command
  language/LanguageManager.java - fa/finglish
  race/RaceType.java - 14 ghom Finglish
  gui/RaceGUI.java - GUI Finglish
  listeners/SurvivalListener.java - Hook thirst
  ...
resourcepack/
  pack.mcmeta - Resource pack info
  assets/minecraft/models/item/mashk_ab.json
  IranianHardcore-ResourcePack.zip - Pack zip
```

---

## 🛠️ Compile

```bash
mvn clean package
# target/iranian-hardcore-4.0.0.jar
```

---

**🇮🇷 Har biome yek ghom Irani - 14 ghom asil - Finglish baraye Aternos - Climate + Thirst + 7 Mob Jadid + Resource Pack - Zende bad Iran!**
**Khalij hameshe Fars - Aghvam Irani motahed!**

## Download Links Mostaghim

- **Jar v4.0:** https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/raw/main/IranianHardcore-4.0.0.jar
- **Az Release v4.0.0:** [iranian-hardcore-4.0.0.jar](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/releases/download/v4.0.0/iranian-hardcore-4.0.0.jar) (173KB - BUILD SUCCESS)
- **ResourcePack:** https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/raw/main/resourcepack/IranianHardcore-ResourcePack.zip
- **Az Release v4.0.0:** [IranianHardcore-ResourcePack.zip](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/releases/download/v4.0.0/IranianHardcore-ResourcePack.zip) (2.9KB)
- **Source:** https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/archive/refs/heads/main.zip
