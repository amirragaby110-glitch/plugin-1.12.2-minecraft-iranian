# 🇮🇷 Iranian Hardcore v4.0 - Finglish + Climate + Thirst + Custom Mobs + Resource Pack

**Version 4.0** - Bozorgtarin update! Hala **Finglish** (baraye server hayi ke Farsi namayesh dade nemishavad) + **Ab o Hava** (sard/garm) + **Teshnegi** (bayad ab bokhori) + **7 Mob jadid Irani** (Div, Simurgh, Zahhak) + **Resource Pack makhsus**!

> **Shoar**: Har biome yek ghom Irani - 14 ghom asil - Finglish baraye Aternos - Zende bad Iran - Khalij hameshe Fars 🇮🇷

**NEW v4.0**: Moshkel namayesh Farsi dar Aternos hal shod! Hame matn ha Finglish (Farsi ba horoof Englisi) - Mesle "Ghale Alamoat" be jaye "قلعه الموت"

---

## 📥 Download Mostaghim - Bedune niaz be compile

### ✅ Ravesh 1: Download Jar Amade (Sadatarin - Faghat bezar too plugins)

**Link mostaghim Jar:**
- **File Jar dar repo (raw):** [IranianHardcore-4.0.0.jar](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/raw/main/IranianHardcore-4.0.0.jar)
- **Az Release v4.0.0 (recommended):** [iranian-hardcore-4.0.0.jar](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/releases/download/v4.0.0/iranian-hardcore-4.0.0.jar) - 173KB BUILD SUCCESS
- **Ya az tarigh GitHub Actions:** Boro be tab [Actions](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/actions) -> Akharin build movafagh -> Download Artifact `IranianHardcore-4.0.0`

**Nasb:**
1. File `IranianHardcore-4.0.0.jar` ro download kon
2. Bezar too poshe `plugins` serveret (Spigot 1.12.2)
3. Server ro restart kon
4. Tamam! Plugin faal mishe 🇮🇷

### 📦 Ravesh 2: Download Resource Pack

**Link mostaghim Resource Pack:**
- **File Zip dar repo (raw):** [IranianHardcore-ResourcePack.zip](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/raw/main/resourcepack/IranianHardcore-ResourcePack.zip)
- **Az Release v4.0.0 (recommended):** [IranianHardcore-ResourcePack.zip](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/releases/download/v4.0.0/IranianHardcore-ResourcePack.zip) - 2.9KB

**Nasb Resource Pack:**
1. File `IranianHardcore-ResourcePack.zip` ro download kon
2. URL ro dar `config.yml` bezar:
```yaml
resourcepack:
  enabled: true
  auto-send-on-join: true
  url: "https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/releases/download/v4.0.0/IranianHardcore-ResourcePack.zip"
```
3. Bazikonan vaghti join mikonan Accept konan

### 📦 Ravesh 3: Download Source Code

- **Download Zip kamel:** [main.zip](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/archive/refs/heads/main.zip)
- **Releases:** [Releases page](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/releases)

**Compile dasti:**
```bash
mvn clean package
# target/iranian-hardcore-4.0.0.jar - 173KB
```

### 🎯 Version haye montasher shode

| Version | Tarikh | Vizhegi | Link Download |
|------|-------|--------|-------------|
| **v4.0.0** | 2026-09-19 | Finglish + Climate + Thirst + 7 Mobs + ResourcePack + 14 ghom + 16 dungeon - BUILD SUCCESS 173KB | [Download Release](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/releases/tag/v4.0.0) |
| **v3.5.3** | 2026-09-18 | Jar amade dar root repo | [Download](https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/releases/tag/v3.5.3) |

### ✅ Test shode

- ✅ Spigot 1.12.2
- ✅ Java 8
- ✅ Aternos compatible - Finglish
- ✅ 14 ghom Irani
- ✅ 16 dungeon tarikhi
- ✅ 8 saze Irani + Bazar + Roosta
- ✅ Climate system (sard/garm)
- ✅ Thirst system (teshnegi)
- ✅ 7 mob jadid Irani
- ✅ Resource Pack makhsus

---

## 🆕 V4.0 Features Jadid

### 1. Finglish Version - Hal moshkel Farsi dar Aternos
- Hame matn ha Finglish: `Ghale Alamoat` be jaye `قلعه الموت`
- Config: `general.language: finglish` (default) ya `fa`
- RaceType: finglishName + loreFinglish
- DungeonType: Finglish names
- RaceGUI: Finglish support

### 2. Climate System - Ab o Hava
- Sard: ICE_FLATS, TAIGA_COLD - temp -100, SLOW, WEAKNESS, damage -90
- Garm: DESERT, MESA, SAVANNA, HELL - temp +100, SLOW, CONFUSION, damage +90
- Leather garme, iron sard, atash garm, ab sard
- Action bar: Dama + Teshnegi

### 3. Thirst System - Teshnegi
- 0-100 thirst, drain 0.3 +0.4 sprint + temp/100, hardcore x1.5
- Effects: SLOW, WEAKNESS, CONFUSION, damage
- Action bar: 💧 bar
- `/iranian drink` (+25%), `/iranian mashk` (Mashk Ab +30%)
- Right-click water bottle

### 4. Custom Mobs - 7 Mob Jadid
| Mob | HP | Damage | Biome | Drop |
|-----|----|--------|-------|------|
| Div Sepid | 40 | 8 | Ice | Bone, Gold |
| Div Siah | 25 | 6 | Desert | Coal, Gold Nugget |
| Simurgh | 30 | 4 | Extreme Hills | Feather, Golden Apple |
| Zahhak | 50 | 10 | Desert | Diamond, Gold Block |
| Rostam Ghost | 60 | 12 | Extreme Hills | Diamond Sword |
| Al | 20 | 5 | Swamp | Redstone, Potion |
| Kaveh | 80 | 15 | Plains | Friendly! |

- Spawn har 2min 5% chance, biome-based, broadcast rare
- `/iranian spawnmob <type>`

### 5. Resource Pack
- `resourcepack/pack.mcmeta` + `IranianHardcore-ResourcePack.zip`
- Textures: Div, Simurgh, Zahhak, etc
- `ResourcePackManager` auto-send
- `/iranian resourcepack`, `/iranian packinfo`

---

## 🎮 Dastorat v4.0

```
/race choose - Entekhab ghome Irani
/iranian thirst - Teshnegi
/iranian temperature - Dama
/iranian drink - Noshidan ab
/iranian mashk - Mashk Ab
/iranian resourcepack - Pack
/iranian spawnmob <type> - Mob Irani (admin)
/bazaar - Bazar Irani
/dungeon list - 16 dungeon
```

---

## 📦 Sakhtar Proje v4.0

```
climate/TemperatureManager.java - Dama system
thirst/ThirstManager.java - Teshnegi + Mashk Ab
mobs/CustomMobType.java - 7 mob
mobs/IranianMobsManager.java - Spawn + effects
resourcepack/ResourcePackManager.java - Pack send
commands/IranianCommand.java - /iranian
language/LanguageManager.java - fa/finglish
race/RaceType.java - 14 ghom Finglish
gui/RaceGUI.java - GUI Finglish
resourcepack/ - pack.mcmeta + zip
```

---

## 🛠️ Compile

```bash
mvn clean package
# target/iranian-hardcore-4.0.0.jar - 173KB BUILD SUCCESS
```

---

**🇮🇷 Zende bad Iran! Khalij hameshe Fars!**

**Download:**
- Jar: https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/releases/download/v4.0.0/iranian-hardcore-4.0.0.jar
- ResourcePack: https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/releases/download/v4.0.0/IranianHardcore-ResourcePack.zip
- Raw Jar: https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/raw/main/IranianHardcore-4.0.0.jar
- Raw Pack: https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/raw/main/resourcepack/IranianHardcore-ResourcePack.zip
