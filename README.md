# Iranian Hardcore - پلاگین هاردکور فوق سخت برای 1.12.2

پلاگین **IranianHardcore** یک پلاگین Bukkit/Spigot/Paper برای نسخه **1.12.2** است که بازی را در حد سخت‌ترین مودهای ماینکرافت مثل **RLCraft** و **Blood N Bones** چالش‌برانگیز می‌کند، اما به صورت پلاگین و بدون نیاز به مود.

همچنین شامل **سیستم نژاد (Race System)** کامل با 8 نژاد مخصوص بایوم‌ها است.

---

## 📦 ساختار پروژه

```
IranianHardcore/
├── pom.xml
├── src/main/
│   ├── java/ir/iranian/hardcore/
│   │   ├── IranianHardcorePlugin.java         # کلاس اصلی
│   │   ├── config/
│   │   │   └── ConfigManager.java             # مدیریت config.yml + races.yml + data.yml
│   │   ├── race/
│   │   │   ├── RaceType.java                  # Enum تمام نژادها با بایوم‌ها
│   │   │   └── RaceManager.java               # منطق انتخاب و ذخیره نژاد
│   │   ├── hardcore/
│   │   │   └── HardcoreManager.java           # تنظیمات جهانی هاردکور
│   │   ├── listeners/
│   │   │   ├── PlayerHardcoreListener.java    # جان نصف، گرسنگی، ابزار، مرگ
│   │   │   ├── MobHardcoreListener.java       # ماب‌های قوی‌تر
│   │   │   ├── SurvivalListener.java          # تخت، آب/لاوا
│   │   │   └── RaceListener.java              # افکت‌های نژاد بر اساس بایوم
│   │   ├── gui/
│   │   │   └── RaceGUI.java                   # منوی GUI انتخاب نژاد
│   │   ├── commands/
│   │   │   ├── RaceCommand.java               # /race
│   │   │   └── HardcoreCommand.java           # /hardcore
│   │   └── utils/
│   │       └── MessageUtils.java              # رنگ و پیام فارسی
│   └── resources/
│       ├── plugin.yml
│       └── config.yml
└── README.md
```

---

## ⚙️ ویژگی‌های هاردکور

### 1. سلامت و گرسنگی
- **جان نصف**: `GENERIC_MAX_HEALTH = 10.0` (5 قلب)
- **گرسنگی سریع**: ضریب `2.0` در `FoodLevelChangeEvent` + `setExhaustion`
- **رژن طبیعی غیرفعال**: `naturalRegeneration = false` + کنسل `EntityRegainHealthEvent`
- **غذای خام مسموم**: `RAW_BEEF`, `RAW_CHICKEN`, `PORK`, `MUTTON`, `RABBIT`, `RAW_FISH` → Poison + Nausea + Hunger

### 2. مصرف منابع
- **خرابی ابزار 2 برابر**: `PlayerItemDamageEvent.setDamage(damage * 2)`
- **سنگ فقط با پیک‌اکس**: `BlockBreakEvent` چک ابزار
- **برگ بدون قیچی هیچی نده**: اگر ابزار `SHEARS` نباشد، بلوک هوا می‌شود بدون دراپ

### 3. دشمنان
- **جان 2 برابر و دمیج 1.5 برابر**: via `Attribute.GENERIC_MAX_HEALTH` و `GENERIC_ATTACK_DAMAGE`
- **اسپاون 2 برابر در شب**: در `CreatureSpawnEvent` اگر `time 13000-23000` باشد 50% ماب اضافه اسپاون
- **زامبی در شکن**: `Zombie.setCanBreakDoors(true)`
- **کریپر قوی‌تر**: `EntityExplodeEvent.setYield(yield * 1.8)` + انفجار دوم
- **عنکبوت مسموم**: `EntityDamageByEntityEvent` → 40% شانس Poison
- **اسکلت دقیق‌تر**: `EntityShootBowEvent` → سرعت تیر `*1.5`

### 4. بقا
- **خواب فقط شب + کولدان 3 روز**: `PlayerBedEnterEvent` چک `world.getFullTime()` ذخیره در `data.yml`
- **هیل خواب فقط 2 قلب**: `PlayerBedLeaveEvent` → `healAmount = 4.0`
- **ندر خطرناک‌تر**: تسک هر 3 ثانیه → Wither + آتش
- **اند خطرناک‌تر**: Levitation + Weakness + Void دمیج 2 برابر
- **آب سخت‌تر**: هوا سریع‌تر تمام می‌شود + کندی
- **لاوا سخت‌تر**: دمیج 1.5 برابر + FireTicks بیشتر + کندی

### 5. مرگ
- **نابودی اینونتوری**: `PlayerDeathEvent.getDrops().clear()` + `setDroppedExp(0)`
- **دیباف اسپاون**: Weakness 60s + Slow 60s + Hunger 30s

---

## 🧬 سیستم نژادها

| نژاد | بایوم خانه | بایوم دشمن | قدرت | ضعف |
|------|-----------|-----------|------|-----|
| **Mountainborn** کوهستان | Extreme Hills, Mesa | Ocean, River | 70% کاهش سقوط، مقاومت | کندی در آب |
| **Forestborn** جنگل | Forest, Taiga, Jungle | Desert | سرعت و عجله در جنگل | ضعف در بیابان |
| **Desertborn** بیابان | Desert, Savanna | Ice Plains, Cold | گرسنگی کمتر، مقاومت آتش | کندی در سرما |
| **Oceanborn** اقیانوس | Ocean, Beach, River | Desert, Nether | نفس 3 برابر، شنای سریع | ضعف در خشکی |
| **Frostborn** برفی | Ice Plains, Cold Taiga | Desert, Nether | مقاومت، سرعت در برف | آتش در بیابان |
| **Swampborn** مرداب | Swamp, Mushroom | Nether, Desert | مصونیت Poison، تنفس | ضعف آتش |
| **Netherborn** نتر | Nether (HELL) | Ocean, Ice | مصونیت آتش، قدرت در ندر | دمیج در آب |
| **Endborn** پایان | End (SKY) | Plains, Desert | مقاومت سقوط، پرش | ضعف نور خورشید |

- **قدرت کامل فقط در بایوم خانه**
- **ضعف در بایوم دشمن**
- ذخیره در `plugins/IranianHardcore/races.yml`
- کولدان تغییر نژاد: 7 روز بازی (`168000` تیک)

### دستورات نژاد
```
/race choose          # باز کردن GUI انتخاب
/race info [race]     # اطلاعات نژاد فعلی یا یک نژاد خاص
/race change          # تغییر نژاد (با کولدان 7 روز)
/race list            # لیست نژادها
/race admin set <player> <race>   # ادمین: تنظیم نژاد
/race admin clear <player>        # ادمین: پاک کردن نژاد
```

### دستورات هاردکور
```
/hardcore reload                  # ریلود کانفیگ
/hardcore info                    # اطلاعات پلاگین
/hardcore setrace <player> <race> # تنظیم نژاد
/hardcore reset <player>          # ریست داده‌ها
```

### پرمیشن‌ها
```yaml
iranianhardcore.race.choose: true
iranianhardcore.race.info: true
iranianhardcore.race.change: true
iranianhardcore.race.admin: op
iranianhardcore.hardcore.reload: op
iranianhardcore.bypass.bedcooldown: op
iranianhardcore.bypass.racecooldown: op
iranianhardcore.bypass.hardcore: op
iranianhardcore.admin: op
```

---

## 🛠️ نحوه کامپایل

### پیش‌نیازها
- Java 8 (JDK 1.8)
- Maven 3.x
- اینترنت برای دانلود Spigot API

### مراحل
```bash
# کلون پروژه
git clone <repo-url>
cd plugin-1.12.2-minecraft-iranian

# کامپایل
mvn clean package

# خروجی در target/
# target/iranian-hardcore-1.0.0.jar
```

فایل `target/iranian-hardcore-1.0.0.jar` را در پوشه `plugins/` سرور Spigot/Paper 1.12.2 قرار دهید و سرور را ریستارت کنید.

---

## 📥 نصب روی سرور

1. سرور 1.12.2 Spigot یا Paper دانلود کنید (https://getbukkit.org/download/spigot)
2. فایل jar کامپایل شده را در `plugins/` بگذارید
3. سرور را یک بار اجرا کنید تا `config.yml` ساخته شود
4. تنظیمات را در `plugins/IranianHardcore/config.yml` ویرایش کنید
5. `/hardcore reload` یا ریستارت سرور

---

## ⚙️ کانفیگ نمونه

تمام ویژگی‌ها در `config.yml` قابل خاموش/روشن و تنظیم هستند. مثلاً:

```yaml
hardcore:
  health:
    max-health: 10.0
    faster-hunger: true
  mobs:
    double-health: true
    damage-multiplier: 1.5
race:
  change-cooldown-days: 7
```

---

## 🧩 بدون NMS

این پلاگین **تا حد امکان از NMS استفاده نمی‌کند** و فقط از API خود Spigot استفاده می‌کند تا با تمام نسخه‌های Paper/Spigot 1.12.2 سازگار باشد.

---

## 📜 لایسنس

MIT - آزاد برای استفاده و ویرایش

---

## 👨‍💻 توسعه‌دهنده

IranianTeam - برای جامعه ماینکرافت ایران

> اگر باگ یا پیشنهادی دارید، Issue باز کنید یا Pull Request بفرستید!
```

