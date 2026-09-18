package ir.iranian.hardcore.items;

import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * آیتم‌های ایرانی تاریخی - کاملا فارسی
 * هر دانجن لوت مخصوص خودش را دارد
 */
public class PersianItems {

    // ============ شمشیرها ============

    public static ItemStack createShamshirAlamut() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&8&l⚔ شمشیر الموت - تیغه حسن صباح"));
        meta.setLore(MessageUtils.color(Arrays.asList(
                "&7شمشیر افسانه‌ای قلعه الموت",
                "&7متعلق به حسن صباح، رهبر اسماعیلیان",
                "",
                "&c&lویژگی‌ها:",
                "&7• &cقدرت: &f+10",
                "&7• &aتیز در کوهستان",
                "&7• &6افکت: Wither هنگام ضربه",
                "",
                "&8&lتاریخ: &7قرن 11 میلادی - الموت، قزوین"
        )));
        meta.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
        meta.addEnchant(Enchantment.DURABILITY, 3, true);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createShamshirBabak() {
        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&c&l⚔ شمشیر بابک خرمدین - شیر آذربایجان"));
        meta.setLore(MessageUtils.color(Arrays.asList(
                "&7شمشیر قهرمان ملی ایران، بابک خرمدین",
                "&7که 22 سال در برابر اعراب مقاومت کرد",
                "",
                "&c&lویژگی‌ها:",
                "&7• &cقدرت در برف: &f+8",
                "&7• &bمقاومت به سرما",
                "",
                "&8&lتاریخ: &7قرن 9 میلادی - قلعه بابک، کلیبر"
        )));
        meta.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
        meta.addEnchant(Enchantment.KNOCKBACK, 2, true);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createShamshirHakhamaneshi() {
        ItemStack item = new ItemStack(Material.GOLD_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&6&l⚔ آکیناکه هخامنشی - شمشیر جاویدان"));
        meta.setLore(MessageUtils.color(Arrays.asList(
                "&7شمشیر کوتاه سربازان جاویدان هخامنشی",
                "&7ارتش 10 هزار نفری گارد شاهنشاهی",
                "",
                "&c&lویژگی‌ها:",
                "&7• &6قدرت الهی: &f+12",
                "&7• &eشانس کریتیکال بالا",
                "",
                "&8&lتاریخ: &7 550-330 قبل از میلاد - تخت جمشید"
        )));
        meta.addEnchant(Enchantment.DAMAGE_ALL, 6, true);
        meta.addEnchant(Enchantment.SWEEPING_EDGE, 3, true);
        meta.addEnchant(Enchantment.DURABILITY, 5, true);
        item.setItemMeta(meta);
        return item;
    }

    // ============ تاج‌ها و جواهرات ============

    public static ItemStack createTajKourosh() {
        ItemStack item = new ItemStack(Material.GOLD_HELMET);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&6&l👑 تاج کوروش بزرگ - شاه شاهان"));
        meta.setLore(MessageUtils.color(Arrays.asList(
                "&7تاج بنیان‌گذار شاهنشاهی هخامنشی",
                "&7کوروش بزرگ، آزادکننده بابل",
                "",
                "&c&lویژگی‌ها:",
                "&7• &aمقاومت کامل",
                "&7• &bقدرت فرماندهی",
                "&7• &6افکت: Regeneration",
                "",
                "&8&lتاریخ: &7 600-530 قبل از میلاد - پاسارگاد",
                "&8&lشعار: &7من کوروش، شاه جهان..."
        )));
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 6, true);
        meta.addEnchant(Enchantment.PROTECTION_FIRE, 4, true);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createManshurKourosh() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&e&l📜 منشور کوروش - اولین اعلامیه حقوق بشر"));
        meta.setLore(MessageUtils.color(Arrays.asList(
                "&7منشور کوروش بزرگ، اولین اعلامیه حقوق بشر جهان",
                "&7که در موزه بریتانیا نگهداری می‌شود",
                "",
                "&f\"منم کوروش، شاه جهان، شاه بزرگ...\"",
                "&f\"آزادی ادیان را برقرار کردم...\"",
                "",
                "&a&lقدرت: &fباز کردن درهای بسته و صلح",
                "&8&lتاریخ: &7 539 قبل از میلاد - بابل"
        )));
        meta.addEnchant(Enchantment.LUCK, 3, true);
        item.setItemMeta(meta);
        return item;
    }

    // ============ فرش و صنایع دستی ============

    public static ItemStack createFarshKermani() {
        ItemStack item = new ItemStack(Material.CARPET, 1, (short) 14);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&c&l🧶 فرش دستباف کرمانی - نقش شاه عباسی"));
        meta.setLore(MessageUtils.color(Arrays.asList(
                "&7فرش دستباف کرمان با نقش شاه عباسی",
                "&7بافت شده توسط هنرمندان کرمانی",
                "",
                "&a&lویژگی: &fگرما در بیابان و سرما",
                "&8&lقدمت: &7هنر 2500 ساله ایرانی"
        )));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createMorghAmin() {
        ItemStack item = new ItemStack(Material.FEATHER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&b&l🪶 پر سیمرغ - پرنده افسانه‌ای شاهنامه"));
        meta.setLore(MessageUtils.color(Arrays.asList(
                "&7پر سیمرغ، پرنده افسانه‌ای شاهنامه فردوسی",
                "&7که زال را در البرز بزرگ کرد",
                "",
                "&a&lقدرت: &fپرواز کوتاه + مقاومت سقوط",
                "&8&lمنبع: &7شاهنامه فردوسی"
        )));
        meta.addEnchant(Enchantment.LUCK, 5, true);
        item.setItemMeta(meta);
        return item;
    }

    // ============ غذا و نوشیدنی ایرانی ============

    public static ItemStack createChaiIrani() {
        ItemStack item = new ItemStack(Material.POTION);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&6&l☕ چای ایرانی لاهیجان - عطر بهشت"));
        meta.setLore(MessageUtils.color(Arrays.asList(
                "&7چای معطر لاهیجان، بهترین چای ایران",
                "&7کشت شده در دامنه‌های البرز",
                "",
                "&a&lافکت: &fسرعت + مقاومت + گرما",
                "&8&lخاستگاه: &7لاهیجان، گیلان"
        )));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createMorvaridKhalij() {
        ItemStack item = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&b&l🔮 مروارید خلیج فارس - اشک دریا"));
        meta.setLore(MessageUtils.color(Arrays.asList(
                "&7مروارید ناب خلیج همیشه فارس",
                "&7صید شده توسط غواصان بوشهری",
                "",
                "&a&lقدرت: &fتنفس زیر آب طولانی",
                "&8&lمکان: &7بندر سیراف، بوشهر - خلیج فارس"
        )));
        meta.addEnchant(Enchantment.LUCK, 4, true);
        item.setItemMeta(meta);
        return item;
    }

    // ============ لوح‌ها و آثار باستانی ============

    public static ItemStack createLohIlami() {
        ItemStack item = new ItemStack(Material.CLAY_BRICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&6&l🧱 خشت ایلامی - خط میخی"));
        meta.setLore(MessageUtils.color(Arrays.asList(
                "&7خشت نوشته‌دار ایلامی از زیگورات چغازنبیل",
                "&7با خط میخی ایلامی",
                "",
                "&f\"این معبد را اونتاش-گال برای اینشوشیناک ساخت\"",
                "",
                "&8&lقدمت: &7 1250 قبل از میلاد - شوش، خوزستان"
        )));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createAtashMoghadas() {
        ItemStack item = new ItemStack(Material.FIREBALL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&6&l🔥 آتش مقدس زرتشتی - آذرگشسب"));
        meta.setLore(MessageUtils.color(Arrays.asList(
                "&7آتش مقدس زرتشتیان، یکی از سه آتش بزرگ",
                "&7که 700 سال در آذرگشسب روشن بود",
                "",
                "&c&lقدرت: &fمصونیت کامل به آتش و لاوا",
                "&8&lمکان: &7آتشکده آذرگشسب، تخت سلیمان"
        )));
        meta.addEnchant(Enchantment.FIRE_ASPECT, 3, true);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createSekkeHakhamaneshi() {
        ItemStack item = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&e&l🪙 سکه دریک هخامنشی - اولین سکه جهان"));
        meta.setLore(MessageUtils.color(Arrays.asList(
                "&7سکه طلای داریوش بزرگ، اولین سکه بین‌المللی جهان",
                "&7با نقش کماندار هخامنشی",
                "",
                "&a&lارزش: &fقابل معامله با روستاییان با قیمت بالا",
                "&8&lتاریخ: &7 515 قبل از میلاد - تخت جمشید"
        )));
        item.setItemMeta(meta);
        return item;
    }

    // ============ سلاح‌های ویژه ============

    public static ItemStack createKamanArash() {
        ItemStack item = new ItemStack(Material.BOW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&a&l🏹 کمان آرش کمانگیر - مرز ایران"));
        meta.setLore(MessageUtils.color(Arrays.asList(
                "&7کمان آرش کمانگیر که جانش را برای ایران داد",
                "&7و تیرش را از البرز تا مروارید پرتاب کرد",
                "",
                "&c&lویژگی: &fبرد 10 برابر + قدرت 8",
                "&8&lافسانه: &7آرش مرز ایران و توران را تعیین کرد"
        )));
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 7, true);
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.addEnchant(Enchantment.ARROW_KNOCKBACK, 3, true);
        item.setItemMeta(meta);
        return item;
    }

    // متد کمکی برای لوت تصادفی ایرانی
    public static ItemStack getRandomPersianLoot() {
        int r = (int) (Math.random() * 10);
        switch (r) {
            case 0: return createSekkeHakhamaneshi();
            case 1: return createFarshKermani();
            case 2: return createMorvaridKhalij();
            case 3: return createChaiIrani();
            case 4: return createLohIlami();
            case 5: return createMorghAmin();
            case 6: return createAtashMoghadas();
            case 7: return new ItemStack(Material.GOLD_INGOT, 5);
            case 8: return new ItemStack(Material.DIAMOND, 2);
            default: return new ItemStack(Material.EMERALD, 3);
        }
    }
}
