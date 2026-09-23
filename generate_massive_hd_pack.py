#!/usr/bin/env python3
"""
Iranian Hardcore Massive HD Texture & Asset Generator (v5.4.0)
Generates high-resolution authentic Persian textures (64x64 blocks/items, 128x64 armors, 512x512 GUI/logo).
Includes all 16 Persian carpets, 16 glazed tiles, Persepolis masonry, weapons, armors, foods, and mobs.
"""

import os
import math
import shutil
import zipfile
from PIL import Image, ImageDraw, ImageFont, ImageFilter

PACK_DIR = "/home/user/plugin-1.12.2-minecraft-iranian/resourcepack_build"
ZIP_OUTPUT = "/home/user/plugin-1.12.2-minecraft-iranian/IranianHardcore-ResourcePack.zip"
TARGET_ASSETS = "/home/user/plugin-1.12.2-minecraft-iranian/src/main/resources/resourcepack"

# Recreate pack directory
if os.path.exists(PACK_DIR):
    shutil.rmtree(PACK_DIR)

dirs = [
    f"{PACK_DIR}/assets/minecraft/textures/blocks",
    f"{PACK_DIR}/assets/minecraft/textures/items",
    f"{PACK_DIR}/assets/minecraft/textures/entity",
    f"{PACK_DIR}/assets/minecraft/textures/entity/zombie",
    f"{PACK_DIR}/assets/minecraft/textures/entity/skeleton",
    f"{PACK_DIR}/assets/minecraft/textures/entity/villager",
    f"{PACK_DIR}/assets/minecraft/textures/models/armor",
    f"{PACK_DIR}/assets/minecraft/textures/environment",
    f"{PACK_DIR}/assets/minecraft/textures/gui",
    f"{PACK_DIR}/assets/minecraft/textures/gui/title",
    f"{PACK_DIR}/assets/minecraft/optifine/cit/persian",
    f"{PACK_DIR}/assets/minecraft/lang"
]

for d in dirs:
    os.makedirs(d, exist_ok=True)

print("Directories initialized. Generating HD assets...")

# Helper functions for drawing Persian geometric and floral motifs
def draw_persian_carpet(base_color, toranj_color, border_color, accent_color, motif_style="classic"):
    w, h = 64, 64
    img = Image.new("RGBA", (w, h), base_color)
    draw = ImageDraw.Draw(img)

    # 1. Subtle woven wool texture noise
    for y in range(h):
        for x in range(w):
            if (x + y) % 2 == 0:
                r, g, b, a = base_color
                r = max(0, min(255, r + ((x * 7 + y * 13) % 9 - 4)))
                g = max(0, min(255, g + ((x * 11 + y * 5) % 9 - 4)))
                b = max(0, min(255, b + ((x * 3 + y * 17) % 9 - 4)))
                draw.point((x, y), fill=(r, g, b, a))

    # 2. Outer Guard Border (Fringes top and bottom)
    for x in range(w):
        if x % 2 == 0:
            draw.point((x, 0), fill=(240, 235, 220, 255))
            draw.point((x, 1), fill=(210, 200, 180, 255))
            draw.point((x, 62), fill=(210, 200, 180, 255))
            draw.point((x, 63), fill=(240, 235, 220, 255))

    # 3. Main Multi-tiered Persian Border (Hashiyeh)
    draw.rectangle([2, 2, 61, 61], outline=accent_color, width=1)
    draw.rectangle([4, 4, 59, 59], outline=border_color, width=3)
    draw.rectangle([8, 8, 55, 55], outline=accent_color, width=1)

    # Rosettes along the border
    for pos in range(10, 54, 6):
        # Top and bottom borders
        draw.ellipse([pos - 1, 5 - 1, pos + 1, 5 + 1], fill=toranj_color)
        draw.ellipse([pos - 1, 58 - 1, pos + 1, 58 + 1], fill=toranj_color)
        # Left and right borders
        draw.ellipse([5 - 1, pos - 1, 5 + 1, pos + 1], fill=toranj_color)
        draw.ellipse([58 - 1, pos - 1, 58 + 1, pos + 1], fill=toranj_color)

    # 4. Four Corner Spandrels (Lachak)
    for cx, cy, x_dir, y_dir in [(9, 9, 1, 1), (54, 9, -1, 1), (9, 54, 1, -1), (54, 54, -1, -1)]:
        points = [(cx, cy), (cx + 8 * x_dir, cy), (cx, cy + 8 * y_dir)]
        draw.polygon(points, fill=toranj_color)
        draw.line(points + [points[0]], fill=accent_color, width=1)

    # 5. Field Vines & Arabesques (Eslimi)
    cx, cy = 32, 32
    if motif_style in ["classic", "isfahan", "kashan", "nain"]:
        for angle in range(0, 360, 45):
            rad = math.radians(angle)
            for r in range(12, 22, 2):
                x = int(cx + r * math.cos(rad) + 2 * math.sin(r))
                y = int(cy + r * math.sin(rad) + 2 * math.cos(r))
                if 9 < x < 54 and 9 < y < 54:
                    draw.point((x, y), fill=accent_color)

    # 6. Central Sunburst Medallion (Toranj)
    if motif_style == "tribal":
        # Stepped diamond for tribal rugs (Qashqai, Turkmen, Bakhtiari)
        for r in range(12, 0, -2):
            col = toranj_color if (r // 2) % 2 == 0 else accent_color
            draw.polygon([(cx, cy - r), (cx + r, cy), (cx, cy + r), (cx - r, cy)], outline=col)
        draw.rectangle([cx - 2, cy - 2, cx + 2, cy + 2], fill=accent_color)
    else:
        # Intricate multi-layered Floral/Star Toranj (Kashan/Isfahan/Tabriz)
        # Outer rosette petals
        for i in range(16):
            th = i * (2 * math.pi / 16)
            px = int(cx + 11 * math.cos(th))
            py = int(cy + 11 * math.sin(th))
            draw.ellipse([px - 2, py - 2, px + 2, py + 2], fill=border_color, outline=accent_color)

        # Core medallion
        draw.ellipse([cx - 8, cy - 8, cx + 8, cy + 8], fill=toranj_color, outline=accent_color)
        draw.ellipse([cx - 5, cy - 5, cx + 5, cy + 5], fill=base_color, outline=border_color)
        draw.ellipse([cx - 2, cy - 2, cx + 2, cy + 2], fill=accent_color)

        # Shamseh pendants top & bottom (Sar-Toranj)
        draw.polygon([(cx, cy - 14), (cx - 3, cy - 9), (cx + 3, cy - 9)], fill=toranj_color, outline=accent_color)
        draw.polygon([(cx, cy + 14), (cx - 3, cy + 9), (cx + 3, cy + 9)], fill=toranj_color, outline=accent_color)

    return img

# ==========================================
# 1. GENERATE ALL 16 AUTHENTIC PERSIAN CARPETS & WOOLS
# ==========================================
carpet_configs = [
    # (name, wool_file, base, toranj, border, accent, style)
    ("red", "wool_colored_red.png", (140, 20, 30, 255), (220, 185, 90, 255), (15, 30, 70, 255), (245, 230, 160, 255), "kashan"),
    ("blue", "wool_colored_blue.png", (18, 55, 120, 255), (230, 190, 70, 255), (180, 25, 35, 255), (60, 190, 220, 255), "isfahan"),
    ("cyan", "wool_colored_cyan.png", (245, 240, 225, 255), (25, 120, 150, 255), (20, 45, 90, 255), (80, 190, 200, 255), "nain"),
    ("orange", "wool_colored_orange.png", (185, 75, 25, 255), (240, 200, 70, 255), (90, 25, 20, 255), (250, 225, 140, 255), "tabriz"),
    ("magenta", "wool_colored_magenta.png", (160, 30, 85, 255), (245, 210, 120, 255), (50, 20, 50, 255), (240, 160, 190, 255), "kashan"),
    ("purple", "wool_colored_purple.png", (80, 20, 100, 255), (235, 195, 80, 255), (140, 30, 80, 255), (220, 180, 240, 255), "isfahan"),
    ("yellow", "wool_colored_yellow.png", (215, 165, 40, 255), (150, 30, 25, 255), (60, 40, 20, 255), (255, 245, 160, 255), "classic"),
    ("brown", "wool_colored_brown.png", (95, 50, 30, 255), (180, 45, 35, 255), (45, 25, 15, 255), (225, 190, 120, 255), "tribal"),
    ("green", "wool_colored_green.png", (25, 95, 45, 255), (210, 175, 60, 255), (130, 25, 25, 255), (120, 215, 140, 255), "tribal"),
    ("lime", "wool_colored_lime.png", (85, 135, 35, 255), (230, 190, 50, 255), (40, 65, 20, 255), (200, 240, 120, 255), "tribal"),
    ("pink", "wool_colored_pink.png", (225, 140, 160, 255), (180, 40, 80, 255), (110, 25, 50, 255), (255, 230, 235, 255), "classic"),
    ("white", "wool_colored_white.png", (248, 244, 235, 255), (210, 170, 60, 255), (20, 50, 100, 255), (180, 140, 40, 255), "classic"),
    ("silver", "wool_colored_silver.png", (165, 160, 150, 255), (150, 30, 35, 255), (40, 40, 45, 255), (225, 220, 210, 255), "tribal"),
    ("gray", "wool_colored_gray.png", (75, 75, 80, 255), (190, 150, 60, 255), (35, 35, 40, 255), (170, 170, 175, 255), "tribal"),
    ("black", "wool_colored_black.png", (30, 28, 32, 255), (220, 180, 60, 255), (145, 25, 35, 255), (190, 150, 50, 255), "kashan"),
    ("light_blue", "wool_colored_light_blue.png", (45, 145, 195, 255), (240, 205, 85, 255), (15, 60, 120, 255), (180, 235, 255, 255), "isfahan")
]

for col_name, file_name, base, toranj, border, accent, style in carpet_configs:
    carpet_img = draw_persian_carpet(base, toranj, border, accent, style)
    # Save as wool texture
    carpet_img.save(f"{PACK_DIR}/assets/minecraft/textures/blocks/{file_name}")
    # Also save as carpet item / block variant
    carpet_img.save(f"{PACK_DIR}/assets/minecraft/textures/blocks/carpet_{col_name}.png")

print(f"Generated 16 Persian wool & carpet textures (64x64 HD).")

# ==========================================
# 2. GENERATE 16 PERSIAN GLAZED TILES (KASHI MOARRAGH / GIRIH)
# ==========================================
def draw_persian_tile(base_color, star_color, line_color, border_color):
    w, h = 64, 64
    img = Image.new("RGBA", (w, h), base_color)
    draw = ImageDraw.Draw(img)

    # Ceramic glaze shine and crackle effect
    for y in range(h):
        for x in range(w):
            if (x * y + x) % 19 == 0:
                r, g, b, a = base_color
                draw.point((x, y), fill=(min(255, r + 25), min(255, g + 25), min(255, b + 25), a))

    # Tile joint border
    draw.rectangle([0, 0, 63, 63], outline=border_color, width=2)
    draw.rectangle([3, 3, 60, 60], outline=line_color, width=1)

    # 10-point Girih Decagram Star at Center
    cx, cy = 32, 32
    num_pts = 10
    outer_r = 18
    inner_r = 8
    star_pts = []
    for i in range(num_pts * 2):
        r = outer_r if i % 2 == 0 else inner_r
        ang = i * (math.pi / num_pts)
        star_pts.append((int(cx + r * math.cos(ang)), int(cy + r * math.sin(ang))))
    draw.polygon(star_pts, fill=star_color, outline=line_color)

    # Central turquoise bead
    draw.ellipse([cx - 4, cy - 4, cx + 4, cy + 4], fill=(20, 200, 220, 255), outline=line_color)

    # 4 Corner Quarter Stars (Connecting pattern for tiling)
    for qx, qy in [(0, 0), (63, 0), (0, 63), (63, 63)]:
        for i in range(num_pts * 2):
            r = 12 if i % 2 == 0 else 6
            ang = i * (math.pi / num_pts)
            px = int(qx + r * math.cos(ang))
            py = int(qy + r * math.sin(ang))
            if 0 <= px < 64 and 0 <= py < 64:
                draw.line([(qx, qy), (px, py)], fill=line_color, width=1)
        draw.ellipse([qx - 6, qy - 6, qx + 6, qy + 6], outline=line_color, fill=star_color)

    return img

tile_colors = [
    ("blue", (15, 60, 140, 255), (220, 180, 50, 255), (240, 230, 190, 255), (8, 30, 80, 255)),
    ("cyan", (18, 130, 150, 255), (240, 210, 80, 255), (255, 255, 255, 255), (10, 65, 80, 255)),
    ("light_blue", (60, 160, 210, 255), (245, 215, 70, 255), (255, 255, 255, 255), (25, 90, 130, 255)),
    ("yellow", (220, 170, 30, 255), (20, 70, 160, 255), (255, 255, 255, 255), (130, 95, 10, 255)),
    ("orange", (200, 90, 30, 255), (25, 100, 170, 255), (250, 220, 140, 255), (110, 45, 15, 255)),
    ("red", (160, 30, 40, 255), (230, 190, 60, 255), (245, 230, 200, 255), (90, 15, 20, 255)),
    ("green", (25, 110, 50, 255), (225, 185, 55, 255), (235, 245, 220, 255), (12, 60, 25, 255)),
    ("lime", (80, 145, 35, 255), (25, 75, 150, 255), (240, 250, 180, 255), (45, 80, 18, 255)),
    ("purple", (90, 30, 120, 255), (240, 200, 65, 255), (245, 220, 255, 255), (50, 15, 65, 255)),
    ("magenta", (170, 40, 100, 255), (235, 195, 60, 255), (255, 220, 240, 255), (95, 20, 55, 255)),
    ("pink", (230, 150, 175, 255), (30, 80, 150, 255), (255, 255, 255, 255), (145, 75, 95, 255)),
    ("white", (245, 245, 240, 255), (20, 90, 160, 255), (190, 150, 50, 255), (180, 180, 175, 255)),
    ("silver", (180, 175, 170, 255), (160, 30, 35, 255), (240, 235, 225, 255), (110, 105, 100, 255)),
    ("gray", (85, 85, 90, 255), (220, 175, 50, 255), (200, 200, 205, 255), (45, 45, 50, 255)),
    ("black", (25, 25, 30, 255), (215, 170, 50, 255), (50, 140, 180, 255), (10, 10, 15, 255)),
    ("brown", (110, 60, 35, 255), (40, 120, 160, 255), (235, 200, 140, 255), (65, 30, 15, 255))
]

for col_name, base, star, line, border in tile_colors:
    tile_img = draw_persian_tile(base, star, line, border)
    tile_img.save(f"{PACK_DIR}/assets/minecraft/textures/blocks/glazed_terracotta_{col_name}.png")

print("Generated 16 Persian Glazed Tile textures (64x64 HD).")

# ==========================================
# 3. ANCIENT PERSEPOLIS STONE & MASONRY (64x64 HD)
# ==========================================
def draw_persepolis_stone():
    # Carved sun-baked limestone
    img = Image.new("RGBA", (64, 64), (215, 195, 155, 255))
    draw = ImageDraw.Draw(img)
    # Ancient stone grit
    for y in range(64):
        for x in range(64):
            val = (x * 37 + y * 53) % 23 - 11
            r = max(0, min(255, 215 + val))
            g = max(0, min(255, 195 + val))
            b = max(0, min(255, 155 + val))
            draw.point((x, y), fill=(r, g, b, 255))
    # Beveled stone edge
    draw.rectangle([0, 0, 63, 63], outline=(150, 130, 95, 255), width=2)
    return img

persepolis_stone = draw_persepolis_stone()
persepolis_stone.save(f"{PACK_DIR}/assets/minecraft/textures/blocks/sandstone_normal.png")
persepolis_stone.save(f"{PACK_DIR}/assets/minecraft/textures/blocks/sandstone_smooth.png")

# Carved Winged Bull (Lamassu) Sandstone
lamassu_img = persepolis_stone.copy()
draw_lam = ImageDraw.Draw(lamassu_img)
# Bas-relief carving outline
draw_lam.rectangle([6, 6, 57, 57], outline=(130, 110, 75, 255), width=2)
# Bull body and mighty wings
draw_lam.ellipse([18, 26, 46, 48], fill=(185, 165, 125, 255), outline=(130, 110, 75, 255))
# Crowned bearded king head
draw_lam.ellipse([34, 14, 48, 28], fill=(195, 175, 135, 255), outline=(130, 110, 75, 255))
# Feathered outspread wing
draw_lam.polygon([(26, 28), (10, 14), (22, 12), (32, 24)], fill=(205, 185, 145, 255), outline=(130, 110, 75, 255))
lamassu_img.save(f"{PACK_DIR}/assets/minecraft/textures/blocks/sandstone_carved.png")

# Cuneiform Inscription Stone (Xerxes / Darius Tablet)
cuneiform_img = persepolis_stone.copy()
draw_cun = ImageDraw.Draw(cuneiform_img)
draw_cun.rectangle([4, 4, 59, 59], outline=(120, 100, 70, 255), width=2)
# Horizontal cuneiform wedge lines
for row_y in range(10, 56, 7):
    draw_cun.line([(8, row_y), (55, row_y)], fill=(150, 130, 95, 255), width=1)
    # Wedges |--<
    for wx in range(10, 52, 6):
        draw_cun.polygon([(wx, row_y - 2), (wx + 3, row_y), (wx, row_y + 2)], fill=(90, 75, 50, 255))
        draw_cun.line([(wx + 3, row_y), (wx + 5, row_y)], fill=(90, 75, 50, 255), width=1)
cuneiform_img.save(f"{PACK_DIR}/assets/minecraft/textures/blocks/red_sandstone_carved.png")

# Fluted Persepolis Column (Quartz)
column_img = Image.new("RGBA", (64, 64), (235, 235, 230, 255))
draw_col = ImageDraw.Draw(column_img)
for x in range(0, 64, 4):
    draw_col.line([(x, 0), (x, 63)], fill=(205, 205, 200, 255), width=2)
    draw_col.line([(x + 2, 0), (x + 2, 63)], fill=(255, 255, 255, 255), width=2)
column_img.save(f"{PACK_DIR}/assets/minecraft/textures/blocks/quartz_block_side.png")

# Persepolis Double Bull Capital (Quartz Chiseled)
bull_cap = Image.new("RGBA", (64, 64), (240, 238, 230, 255))
draw_bc = ImageDraw.Draw(bull_cap)
draw_bc.rectangle([4, 4, 59, 59], outline=(180, 180, 175, 255), width=2)
# Two mirrored bull heads
draw_bc.ellipse([8, 18, 26, 44], fill=(210, 210, 205, 255), outline=(150, 150, 145, 255))
draw_bc.ellipse([37, 18, 55, 44], fill=(210, 210, 205, 255), outline=(150, 150, 145, 255))
draw_bc.line([(10, 20), (4, 12)], fill=(130, 130, 125, 255), width=2) # Left horn
draw_bc.line([(53, 20), (59, 12)], fill=(130, 130, 125, 255), width=2) # Right horn
bull_cap.save(f"{PACK_DIR}/assets/minecraft/textures/blocks/quartz_block_chiseled.png")

# Stone Brick & Carved Faravahar Stone
stonebrick_img = Image.new("RGBA", (64, 64), (125, 120, 115, 255))
draw_sb = ImageDraw.Draw(stonebrick_img)
# 4 brick courses
for y in [0, 16, 32, 48, 63]:
    draw_sb.line([(0, y), (63, y)], fill=(65, 60, 55, 255), width=2)
# Staggered vertical joints
for y_idx, offset in enumerate([0, 16, 0, 16]):
    y_top = y_idx * 16
    for x in range(offset, 64, 32):
        draw_sb.line([(x, y_top), (x, y_top + 16)], fill=(65, 60, 55, 255), width=2)
stonebrick_img.save(f"{PACK_DIR}/assets/minecraft/textures/blocks/stonebrick.png")

# Faravahar Carved Stone Brick
faravahar_stone = stonebrick_img.copy()
draw_fs = ImageDraw.Draw(faravahar_stone)
draw_fs.ellipse([24, 24, 39, 39], outline=(230, 200, 80, 255), width=2) # Central solar ring
draw_fs.line([(6, 31), (24, 31)], fill=(230, 200, 80, 255), width=3) # Left wing
draw_fs.line([(39, 31), (57, 31)], fill=(230, 200, 80, 255), width=3) # Right wing
draw_fs.ellipse([28, 16, 35, 24], fill=(230, 200, 80, 255)) # King head
faravahar_stone.save(f"{PACK_DIR}/assets/minecraft/textures/blocks/stonebrick_carved.png")

print("Generated Persepolis stone & masonry textures (64x64 HD).")

# ==========================================
# 4. HD PERSIAN WEAPONS & EQUIPMENT (64x64 HD)
# ==========================================
# 1. Shamshir-e Div-Kosh (Curved Damascus Scimitar)
shamshir_img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
draw_sh = ImageDraw.Draw(shamshir_img)
blade_pts = []
for t in range(50):
    p = t / 49.0
    bx = 56 * (1 - p)**1.6 + 24 * p**1.2 + 8 * math.sin(p * math.pi)
    by = 6 * (1 - p) + 40 * p
    blade_pts.append((bx, by))

for i in range(len(blade_pts) - 1):
    x1, y1 = blade_pts[i]
    x2, y2 = blade_pts[i+1]
    draw_sh.line([(x1, y1), (x2, y2)], fill=(210, 240, 250, 255), width=4)
    draw_sh.line([(x1-1, y1+1), (x2-1, y2+1)], fill=(130, 175, 195, 255), width=2)
    draw_sh.line([(x1+1, y1-1), (x2+1, y2-1)], fill=(255, 255, 255, 255), width=1)
    if i % 2 == 0:
        draw_sh.point((x1, y1), fill=(90, 140, 170, 255))

# Golden Quillon / Crossguard
gx, gy = 24, 40
draw_sh.polygon([(gx - 8, gy + 8), (gx + 8, gy - 8), (gx + 10, gy - 6), (gx - 6, gy + 10)], fill=(255, 215, 30, 255), outline=(170, 130, 10, 255))
draw_sh.ellipse([gx - 3, gy - 3, gx + 3, gy + 3], fill=(20, 210, 225, 255), outline=(255, 255, 255, 255)) # Turquoise gem
# Grip & Pommel
draw_sh.line([(gx - 1, gy + 1), (12, 52)], fill=(110, 30, 35, 255), width=5)
draw_sh.line([(gx - 1, gy + 1), (12, 52)], fill=(240, 195, 45, 255), width=1)
draw_sh.ellipse([7, 51, 15, 59], fill=(220, 20, 45, 255), outline=(255, 215, 40, 255))
shamshir_img.save(f"{PACK_DIR}/assets/minecraft/textures/items/diamond_sword.png")

# 2. Akinakes-e Kourosh (Golden Royal Dagger)
akinakes_img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
draw_ak = ImageDraw.Draw(akinakes_img)
# Straight leaf-shaped golden blade
draw_ak.polygon([(52, 12), (30, 34), (26, 30)], fill=(255, 225, 75, 255), outline=(190, 150, 20, 255))
draw_ak.line([(52, 12), (28, 32)], fill=(255, 255, 220, 255), width=2) # Center ridge
# Heart-shaped Achaemenid guard
draw_ak.ellipse([22, 28, 32, 38], fill=(255, 205, 40, 255), outline=(170, 130, 15, 255))
# Grip with ribbing
draw_ak.line([(26, 32), (16, 42)], fill=(180, 130, 20, 255), width=4)
# Fluted circular pommel
draw_ak.ellipse([11, 41, 19, 49], fill=(255, 215, 50, 255), outline=(180, 140, 20, 255))
akinakes_img.save(f"{PACK_DIR}/assets/minecraft/textures/items/golden_sword.png")

# 3. Zulfiqar-e Heydari (Double-tipped steel sword)
zulfiqar_img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
draw_zf = ImageDraw.Draw(zulfiqar_img)
# Double tips at point
draw_zf.polygon([(54, 10), (48, 6), (30, 24), (32, 28)], fill=(215, 225, 235, 255), outline=(120, 140, 160, 255))
draw_zf.polygon([(58, 14), (52, 18), (30, 28), (34, 32)], fill=(215, 225, 235, 255), outline=(120, 140, 160, 255))
# Main blade shaft
draw_zf.polygon([(32, 26), (22, 36), (18, 32)], fill=(190, 205, 220, 255), outline=(100, 120, 140, 255))
# Guard and grip
draw_zf.rectangle([16, 32, 24, 40], fill=(230, 190, 40, 255), outline=(150, 120, 20, 255))
draw_zf.line([(18, 38), (10, 46)], fill=(50, 50, 55, 255), width=4)
draw_zf.ellipse([6, 44, 12, 50], fill=(230, 190, 40, 255))
zulfiqar_img.save(f"{PACK_DIR}/assets/minecraft/textures/items/iron_sword.png")

# 4. Gorz-e Rostam (Ox-Headed Heavy Mace)
gorz_img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
draw_gz = ImageDraw.Draw(gorz_img)
# Shaft
draw_gz.line([(42, 22), (12, 52)], fill=(120, 75, 40, 255), width=6)
draw_gz.line([(42, 22), (12, 52)], fill=(180, 120, 70, 255), width=2)
# Ox Head of Rostam (Sar-e Gav)
draw_gz.ellipse([40, 12, 56, 28], fill=(185, 140, 60, 255), outline=(110, 80, 25, 255)) # Bronze head
# Horns
draw_gz.arc([46, 2, 60, 16], start=180, end=340, fill=(240, 230, 200, 255), width=3)
draw_gz.arc([36, 12, 50, 26], start=100, end=260, fill=(240, 230, 200, 255), width=3)
gorz_img.save(f"{PACK_DIR}/assets/minecraft/textures/items/stone_sword.png")

# 5. Meel-e Bastani (Wooden Zoorkhaneh Club)
meel_img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
draw_ml = ImageDraw.Draw(meel_img)
# Carved walnut wood body
draw_ml.polygon([(46, 12), (54, 20), (32, 42), (24, 34)], fill=(140, 85, 45, 255), outline=(85, 50, 25, 255))
draw_ml.ellipse([42, 10, 56, 24], fill=(160, 100, 55, 255), outline=(85, 50, 25, 255))
# Narrow handle
draw_ml.line([(26, 36), (14, 48)], fill=(190, 130, 75, 255), width=3)
draw_ml.ellipse([11, 47, 16, 52], fill=(85, 50, 25, 255))
meel_img.save(f"{PACK_DIR}/assets/minecraft/textures/items/wooden_sword.png")

# 6. Kaman-e Arash (Composite Recurve Horn Bow)
def draw_persian_bow(pull=0):
    img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    # Recurve limbs
    draw.arc([10, 10, 54, 54], start=135, end=315, fill=(160, 95, 40, 255), width=4)
    # Horn and gold ring reinforcements
    draw.ellipse([30, 8, 34, 12], fill=(235, 195, 40, 255))
    draw.ellipse([8, 30, 12, 34], fill=(235, 195, 40, 255))
    draw.ellipse([52, 30, 56, 34], fill=(235, 195, 40, 255))
    # String
    sx = 32 - pull * 4
    sy = 32 + pull * 4
    draw.line([(12, 32), (sx, sy)], fill=(245, 245, 240, 255), width=1)
    draw.line([(sx, sy), (32, 12)], fill=(245, 245, 240, 255), width=1)
    return img

draw_persian_bow(0).save(f"{PACK_DIR}/assets/minecraft/textures/items/bow_standby.png")
draw_persian_bow(1).save(f"{PACK_DIR}/assets/minecraft/textures/items/bow_pulling_0.png")
draw_persian_bow(2).save(f"{PACK_DIR}/assets/minecraft/textures/items/bow_pulling_1.png")
draw_persian_bow(3).save(f"{PACK_DIR}/assets/minecraft/textures/items/bow_pulling_2.png")

# 7. Tir-e Simurgh (Peacock-feathered Arrow)
arrow_img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
draw_ar = ImageDraw.Draw(arrow_img)
draw_ar.line([(12, 52), (50, 14)], fill=(100, 70, 45, 255), width=3) # Ebony shaft
draw_ar.polygon([(50, 14), (56, 8), (48, 8)], fill=(225, 235, 245, 255)) # Steel head
# Peacock feather fletching
draw_ar.ellipse([8, 48, 16, 56], fill=(20, 170, 150, 255), outline=(210, 175, 40, 255))
draw_ar.ellipse([10, 50, 14, 54], fill=(20, 50, 160, 255))
arrow_img.save(f"{PACK_DIR}/assets/minecraft/textures/items/arrow.png")

# 8. Derafsh Kaviani Royal Shield
shield_img = Image.new("RGBA", (64, 64), (160, 30, 40, 255))
draw_shld = ImageDraw.Draw(shield_img)
draw_shld.rectangle([0, 0, 63, 63], outline=(240, 205, 50, 255), width=4)
# 4 Quadrants: Red, Gold, Purple, Turquoise
draw_shld.rectangle([4, 4, 31, 31], fill=(180, 25, 35, 255))
draw_shld.rectangle([32, 4, 59, 31], fill=(235, 195, 45, 255))
draw_shld.rectangle([4, 32, 31, 59], fill=(85, 25, 105, 255))
draw_shld.rectangle([32, 32, 59, 59], fill=(25, 155, 175, 255))
# Central Kaviani 8-point gold star
scx, scy = 32, 32
draw_shld.ellipse([scx - 10, scy - 10, scx + 10, scy + 10], fill=(255, 220, 60, 255), outline=(255, 255, 255, 255))
draw_shld.ellipse([scx - 4, scy - 4, scx + 4, scy + 4], fill=(200, 20, 30, 255))
# 3 Tassels at bottom
draw_shld.line([(16, 59), (16, 63)], fill=(235, 195, 45, 255), width=2)
draw_shld.line([(32, 59), (32, 63)], fill=(180, 25, 35, 255), width=2)
draw_shld.line([(48, 59), (48, 63)], fill=(235, 195, 45, 255), width=2)
shield_img.save(f"{PACK_DIR}/assets/minecraft/textures/entity/shield_base.png")

print("Generated Persian weapons, bow, arrow, and shield (64x64 HD).")

# ==========================================
# 5. AUTHENTIC PERSIAN FOODS (64x64 HD)
# ==========================================
# 1. Naan-e Sangak (with sesame seeds & baked pebbles)
sangak_img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
draw_sg = ImageDraw.Draw(sangak_img)
# Elongated triangular Persian flatbread shape
draw_sg.polygon([(32, 6), (54, 56), (10, 56)], fill=(225, 185, 120, 255), outline=(160, 110, 55, 255))
# Baked bubbly blisters and blisters
for bx, by in [(28, 22), (36, 32), (24, 42), (40, 46), (32, 48)]:
    draw_sg.ellipse([bx - 3, by - 2, bx + 3, by + 2], fill=(140, 85, 35, 255))
# Little black sesame seeds (Konjed)
for kx, ky in [(30, 16), (34, 26), (20, 38), (42, 38), (28, 50), (36, 52), (18, 52)]:
    draw_sg.point((kx, ky), fill=(30, 25, 20, 255))
    draw_sg.point((kx + 1, ky), fill=(30, 25, 20, 255))
sangak_img.save(f"{PACK_DIR}/assets/minecraft/textures/items/bread.png")

# 2. Chelo Kabab Koobideh (with grilled tomato and saffron rice)
koobideh_img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
draw_kb = ImageDraw.Draw(koobideh_img)
# Steel skewer (Sikh)
draw_kb.line([(8, 56), (56, 8)], fill=(190, 195, 200, 255), width=2)
# Minced lamb ridges along the skewer
for pos in range(16, 48, 5):
    draw_kb.ellipse([pos - 4, 64 - pos - 6, pos + 4, 64 - pos + 2], fill=(110, 50, 30, 255), outline=(70, 30, 15, 255))
    # Grill mark line
    draw_kb.line([(pos - 3, 64 - pos - 2), (pos + 3, 64 - pos + 2)], fill=(45, 15, 10, 255), width=1)
# Charred grilled tomato (Gojjeh Kababi)
draw_kb.ellipse([44, 40, 58, 54], fill=(210, 30, 20, 255), outline=(130, 15, 10, 255))
draw_kb.ellipse([48, 43, 54, 49], fill=(40, 15, 10, 255)) # Charred skin
koobideh_img.save(f"{PACK_DIR}/assets/minecraft/textures/items/cooked_beef.png")

# 3. Joojeh Kabab (Saffron chicken skewer)
joojeh_img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
draw_jj = ImageDraw.Draw(joojeh_img)
draw_jj.line([(8, 56), (56, 8)], fill=(190, 195, 200, 255), width=2)
for pos in range(16, 48, 6):
    draw_jj.ellipse([pos - 5, 64 - pos - 5, pos + 5, 64 - pos + 3], fill=(235, 175, 30, 255), outline=(160, 105, 15, 255))
    draw_jj.line([(pos - 3, 64 - pos - 1), (pos + 3, 64 - pos + 1)], fill=(100, 60, 10, 255), width=1)
joojeh_img.save(f"{PACK_DIR}/assets/minecraft/textures/items/cooked_chicken.png")

# 4. Khoresht-e Ghormeh Sabzi (in Clay Bowl)
ghormeh_img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
draw_gh = ImageDraw.Draw(ghormeh_img)
# Clay bowl (Kaseh Sofali)
draw_gh.ellipse([10, 32, 54, 56], fill=(175, 105, 60, 255), outline=(115, 65, 30, 255))
# Dark emerald herb stew
draw_gh.ellipse([12, 26, 52, 42], fill=(25, 65, 30, 255), outline=(15, 45, 20, 255))
# Red kidney beans (Loobia)
draw_gh.ellipse([22, 30, 27, 34], fill=(135, 30, 35, 255))
draw_gh.ellipse([34, 32, 39, 36], fill=(135, 30, 35, 255))
# Dried black lime (Limoo Amani)
draw_gh.ellipse([27, 28, 33, 33], fill=(45, 40, 30, 255), outline=(25, 20, 15, 255))
ghormeh_img.save(f"{PACK_DIR}/assets/minecraft/textures/items/mushroom_stew.png")

# 5. Dizi-e Sangi (Stone Pot with lamb & chickpeas)
dizi_img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
draw_dz = ImageDraw.Draw(dizi_img)
# Traditional stone vessel (Ghablameh Sangi)
draw_dz.rectangle([18, 22, 46, 54], fill=(80, 80, 85, 255), outline=(50, 50, 55, 255), width=2)
draw_dz.ellipse([16, 18, 48, 26], fill=(110, 110, 115, 255), outline=(50, 50, 55, 255))
# Red tomato broth inside
draw_dz.ellipse([20, 20, 44, 24], fill=(190, 45, 30, 255))
# Chickpeas
draw_dz.ellipse([26, 21, 29, 23], fill=(225, 195, 120, 255))
draw_dz.ellipse([34, 21, 37, 23], fill=(225, 195, 120, 255))
dizi_img.save(f"{PACK_DIR}/assets/minecraft/textures/items/rabbit_stew.png")

# 6. Mashk-e Aab (Leather Water Canteen)
mashk_img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
draw_mk = ImageDraw.Draw(mashk_img)
# Sturdy leather flask body
draw_mk.ellipse([14, 20, 50, 56], fill=(125, 70, 35, 255), outline=(75, 40, 20, 255))
draw_mk.line([(18, 30), (46, 46)], fill=(85, 45, 20, 255), width=2) # Leather stitching
# Brass neck & stopper
draw_mk.rectangle([28, 10, 36, 20], fill=(215, 175, 45, 255), outline=(150, 115, 20, 255))
draw_mk.ellipse([27, 8, 37, 12], fill=(180, 50, 40, 255)) # Cork
mashk_img.save(f"{PACK_DIR}/assets/minecraft/textures/items/potion_bottle_drinkable.png")

# 7. Anar-e Saveh (Ruby Pomegranate)
anar_img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
draw_an = ImageDraw.Draw(anar_img)
draw_an.ellipse([14, 16, 50, 54], fill=(175, 20, 35, 255), outline=(115, 10, 20, 255))
# Crown / Calyx (Taj-e Anar)
draw_an.polygon([(26, 16), (32, 8), (38, 16), (35, 12), (29, 12)], fill=(140, 15, 25, 255))
# Cut open revealing glistening seeds
draw_an.ellipse([22, 26, 42, 46], fill=(225, 215, 200, 255))
for sx, sy in [(26, 30), (32, 30), (38, 32), (28, 36), (34, 38), (30, 42)]:
    draw_an.ellipse([sx - 2, sy - 2, sx + 2, sy + 2], fill=(210, 15, 40, 255), outline=(255, 255, 255, 255))
anar_img.save(f"{PACK_DIR}/assets/minecraft/textures/items/apple.png")

# 8. Anar-e Talaee (Golden Pomegranate)
anar_gold = anar_img.copy()
draw_ag = ImageDraw.Draw(anar_gold)
draw_ag.ellipse([14, 16, 50, 54], fill=(255, 215, 40, 255), outline=(190, 140, 15, 255))
draw_ag.ellipse([22, 26, 42, 46], fill=(255, 245, 180, 255))
for sx, sy in [(26, 30), (32, 30), (38, 32), (28, 36), (34, 38), (30, 42)]:
    draw_ag.ellipse([sx - 2, sy - 2, sx + 2, sy + 2], fill=(255, 120, 20, 255), outline=(255, 255, 255, 255))
anar_gold.save(f"{PACK_DIR}/assets/minecraft/textures/items/golden_apple.png")

print("Generated Persian authentic foods (64x64 HD).")

# ==========================================
# 6. ACHAEMENID COINS & VALUABLES (64x64 HD)
# ==========================================
# 1. Danake-ye Mesi (Copper Coin)
danake_img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
draw_dk = ImageDraw.Draw(danake_img)
draw_dk.ellipse([12, 12, 52, 52], fill=(185, 105, 55, 255), outline=(125, 65, 30, 255), width=2)
# Ancient 8-petal rosette
for a in range(0, 360, 45):
    rad = math.radians(a)
    px = int(32 + 10 * math.cos(rad))
    py = int(32 + 10 * math.sin(rad))
    draw_dk.ellipse([px - 2, py - 2, px + 2, py + 2], fill=(225, 145, 85, 255))
danake_img.save(f"{PACK_DIR}/assets/minecraft/textures/items/gold_nugget.png")

# 2. Siglos-e Noghre (Silver Coin - Archer King)
siglos_img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
draw_sg = ImageDraw.Draw(siglos_img)
draw_sg.ellipse([10, 10, 54, 54], fill=(205, 210, 215, 255), outline=(145, 150, 155, 255), width=2)
# Relief of kneeling Great King with bow & spear
draw_sg.ellipse([28, 16, 36, 24], fill=(155, 160, 165, 255)) # Crowned head
draw_sg.line([(32, 24), (32, 40)], fill=(155, 160, 165, 255), width=3) # Robed torso
draw_sg.line([(24, 22), (24, 46)], fill=(125, 130, 135, 255), width=2) # Spear
draw_sg.arc([30, 24, 44, 40], start=270, end=90, fill=(125, 130, 135, 255), width=2) # Bow
siglos_img.save(f"{PACK_DIR}/assets/minecraft/textures/items/iron_ingot.png")

# 3. Derik-e Tala (Gold Daric of Darius)
derik_img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
draw_dr = ImageDraw.Draw(derik_img)
draw_dr.ellipse([8, 8, 56, 56], fill=(255, 215, 45, 255), outline=(190, 145, 15, 255), width=3)
# Crowned King with dentated crown (Taj-e Daryoosh)
draw_dr.ellipse([28, 14, 38, 24], fill=(215, 165, 25, 255))
draw_dr.polygon([(26, 16), (29, 11), (33, 15), (37, 11), (40, 16)], fill=(255, 240, 120, 255)) # Dentate crown
draw_dr.line([(33, 24), (33, 42)], fill=(215, 165, 25, 255), width=4) # Royal Candys robe
draw_dr.line([(22, 18), (22, 48)], fill=(170, 125, 15, 255), width=2) # Spear
draw_dr.arc([32, 24, 48, 42], start=270, end=90, fill=(170, 125, 15, 255), width=2) # Persian bow
derik_img.save(f"{PACK_DIR}/assets/minecraft/textures/items/gold_ingot.png")

# 4. Firoozeh-ye Neyshaboor (Turquoise Gem)
firoozeh_img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
draw_fz = ImageDraw.Draw(firoozeh_img)
# Polished Persian turquoise cabochon
draw_fz.ellipse([12, 16, 52, 48], fill=(25, 195, 215, 255), outline=(235, 195, 40, 255), width=3)
# Lapis veins / spiderweb matrix
draw_fz.line([(18, 30), (32, 38)], fill=(15, 100, 120, 255), width=1)
draw_fz.line([(32, 38), (46, 26)], fill=(15, 100, 120, 255), width=1)
# Gloss highlight
draw_fz.ellipse([20, 20, 28, 26], fill=(160, 245, 255, 255))
firoozeh_img.save(f"{PACK_DIR}/assets/minecraft/textures/items/emerald.png")

print("Generated Achaemenid coins & valuables (64x64 HD).")

# ==========================================
# 7. HD ARMOR TEXTURES (128x64 Entity Texture Sheets)
# ==========================================
# 1. Achaemenid Immortals Scale Armor (Diamond Armor)
immortals_layer1 = Image.new("RGBA", (128, 64), (0, 0, 0, 0))
draw_im1 = ImageDraw.Draw(immortals_layer1)
# Head / Helmet
draw_im1.rectangle([0, 0, 64, 32], fill=(235, 195, 50, 255)) # Gold helmet
draw_im1.rectangle([16, 8, 48, 24], fill=(140, 25, 35, 255)) # Royal crimson lining
# Chestplate & Lamellar Gold Scales
draw_im1.rectangle([32, 32, 96, 64], fill=(245, 205, 55, 255))
for sy in range(34, 64, 4):
    for sx in range(34, 94, 6):
        draw_im1.arc([sx, sy, sx + 5, sy + 4], start=0, end=180, fill=(180, 140, 20, 255), width=1)
# Royal Purple Immortals Sash
draw_im1.line([(32, 32), (96, 64)], fill=(85, 25, 105, 255), width=4)
immortals_layer1.save(f"{PACK_DIR}/assets/minecraft/textures/models/armor/diamond_layer_1.png")

immortals_layer2 = Image.new("RGBA", (128, 64), (0, 0, 0, 0))
draw_im2 = ImageDraw.Draw(immortals_layer2)
# Greaves / Leggings with embroidered Persian patterns
draw_im2.rectangle([0, 32, 64, 64], fill=(140, 25, 35, 255))
for y in range(34, 64, 6):
    draw_im2.line([(0, y), (64, y)], fill=(235, 195, 50, 255), width=1)
immortals_layer2.save(f"{PACK_DIR}/assets/minecraft/textures/models/armor/diamond_layer_2.png")

# 2. Sassanid Savaran Cataphract (Iron Armor)
savaran_layer1 = Image.new("RGBA", (128, 64), (0, 0, 0, 0))
draw_sv1 = ImageDraw.Draw(savaran_layer1)
# Conical Spangenhelm helmet with chain aventail
draw_sv1.rectangle([0, 0, 64, 32], fill=(180, 190, 200, 255))
for y in range(16, 32, 2):
    for x in range(0, 64, 2):
        draw_sv1.point((x, y), fill=(90, 100, 110, 255))
# Heavy steel mail corselet
draw_sv1.rectangle([32, 32, 96, 64], fill=(150, 160, 170, 255))
for y in range(32, 64, 3):
    draw_sv1.line([(32, y), (96, y)], fill=(80, 90, 100, 255), width=1)
savaran_layer1.save(f"{PACK_DIR}/assets/minecraft/textures/models/armor/iron_layer_1.png")

savaran_layer2 = Image.new("RGBA", (128, 64), (0, 0, 0, 0))
draw_sv2 = ImageDraw.Draw(savaran_layer2)
draw_sv2.rectangle([0, 32, 64, 64], fill=(120, 130, 140, 255))
savaran_layer2.save(f"{PACK_DIR}/assets/minecraft/textures/models/armor/iron_layer_2.png")

print("Generated HD Persian armor texture sheets.")

# ==========================================
# 8. MOB SKINS (64x64 HD)
# ==========================================
# 1. Sassanid Undead Warrior (Zombie)
zombie_img = Image.new("RGBA", (64, 64), (60, 95, 65, 255))
draw_zm = ImageDraw.Draw(zombie_img)
# Head with Persian conical helmet
draw_zm.rectangle([0, 0, 32, 16], fill=(160, 170, 180, 255))
# Glowing eyes
draw_zm.rectangle([10, 8, 14, 11], fill=(240, 30, 20, 255))
draw_zm.rectangle([18, 8, 22, 11], fill=(240, 30, 20, 255))
# Rusted chainmail torso
draw_zm.rectangle([16, 16, 40, 32], fill=(110, 115, 120, 255))
draw_zm.line([(16, 24), (40, 24)], fill=(160, 120, 40, 255), width=2) # Leather belt
zombie_img.save(f"{PACK_DIR}/assets/minecraft/textures/entity/zombie/zombie.png")

# 2. Persian Desert Husk (Husk)
husk_img = Image.new("RGBA", (64, 64), (165, 145, 110, 255))
draw_hk = ImageDraw.Draw(husk_img)
# Sand-colored desert turban
draw_hk.rectangle([0, 0, 32, 12], fill=(215, 200, 170, 255))
# Glowing yellow desert eyes
draw_hk.rectangle([10, 8, 14, 11], fill=(255, 220, 30, 255))
draw_hk.rectangle([18, 8, 22, 11], fill=(255, 220, 30, 255))
husk_img.save(f"{PACK_DIR}/assets/minecraft/textures/entity/zombie/husk.png")

# 3. Ancient Achaemenid Skeleton Archer
skel_img = Image.new("RGBA", (64, 64), (200, 200, 195, 255))
draw_sk = ImageDraw.Draw(skel_img)
# Bronze headband / torc
draw_sk.line([(0, 6), (32, 6)], fill=(195, 145, 40, 255), width=2)
# Glowing soul eyes
draw_sk.rectangle([10, 8, 14, 12], fill=(30, 180, 220, 255))
draw_sk.rectangle([18, 8, 22, 12], fill=(30, 180, 220, 255))
skel_img.save(f"{PACK_DIR}/assets/minecraft/textures/entity/skeleton/skeleton.png")

# 4. Kaweh Ahangar Iron Golem
golem_img = Image.new("RGBA", (128, 128), (170, 175, 180, 255))
draw_gl = ImageDraw.Draw(golem_img)
# Blacksmith leather apron (Pishband-e Charmineh-ye Kaweh)
draw_gl.rectangle([20, 40, 70, 110], fill=(130, 75, 40, 255), outline=(75, 40, 20, 255), width=3)
# Glowing fiery core
draw_gl.ellipse([40, 60, 50, 70], fill=(255, 140, 20, 255), outline=(255, 220, 50, 255))
golem_img.save(f"{PACK_DIR}/assets/minecraft/textures/entity/iron_golem.png")

# 5. Persian Kadkhoda / Merchant (Villager)
vil_img = Image.new("RGBA", (64, 64), (190, 150, 120, 255))
draw_vl = ImageDraw.Draw(vil_img)
# Black felt cap (Kolah Namadi)
draw_vl.rectangle([0, 0, 32, 10], fill=(35, 35, 40, 255))
# Embroidered Persian Termeh robe
draw_vl.rectangle([16, 20, 48, 64], fill=(150, 25, 35, 255))
draw_vl.line([(32, 20), (32, 64)], fill=(235, 195, 50, 255), width=2)
vil_img.save(f"{PACK_DIR}/assets/minecraft/textures/entity/villager/villager.png")

print("Generated HD Mob skins.")

# ==========================================
# 9. SKY & ENVIRONMENT TEXTURES
# ==========================================
# Radiant Mithraic Sun (128x128)
sun_img = Image.new("RGBA", (128, 128), (0, 0, 0, 0))
draw_sn = ImageDraw.Draw(sun_img)
scx, scy = 64, 64
for a in range(0, 360, 15):
    rad = math.radians(a)
    rx = int(scx + 58 * math.cos(rad))
    ry = int(scy + 58 * math.sin(rad))
    draw_sn.line([(scx, scy), (rx, ry)], fill=(255, 230, 100, 180), width=3)
draw_sn.ellipse([scx - 30, scy - 30, scx + 30, scy + 30], fill=(255, 245, 180, 255), outline=(255, 200, 40, 255), width=4)
draw_sn.ellipse([scx - 16, scy - 16, scx + 16, scy + 16], fill=(255, 255, 230, 255))
sun_img.save(f"{PACK_DIR}/assets/minecraft/textures/environment/sun.png")

# Moon Phases (256x128)
moon_img = Image.new("RGBA", (256, 128), (0, 0, 0, 0))
draw_mn = ImageDraw.Draw(moon_img)
for phase in range(8):
    px = (phase % 4) * 64 + 32
    py = (phase // 4) * 64 + 32
    draw_mn.ellipse([px - 20, py - 20, px + 20, py + 20], fill=(225, 235, 245, 220))
    if phase != 0:
        # Shadow eclipse
        draw_mn.ellipse([px - 14 + phase * 2, py - 20, px + 26 + phase * 2, py + 20], fill=(0, 0, 0, 240))
moon_img.save(f"{PACK_DIR}/assets/minecraft/textures/environment/moon_phases.png")

print("Generated Sun and Moon environment textures.")

# ==========================================
# 10. PACK ICON & MOJANG LOGO (512x512)
# ==========================================
def draw_master_logo():
    img = Image.new("RGBA", (512, 512), (20, 18, 28, 255))
    draw = ImageDraw.Draw(img)

    # Multi-layered Persian carpet border around icon
    draw.rectangle([6, 6, 505, 505], outline=(235, 195, 50, 255), width=4)
    draw.rectangle([14, 14, 497, 497], outline=(160, 25, 35, 255), width=8)
    draw.rectangle([24, 24, 487, 487], outline=(20, 160, 190, 255), width=3)

    # Central Golden Faravahar Motif
    cx, cy = 256, 210
    # Solar ring
    draw.ellipse([cx - 40, cy - 40, cx + 40, cy + 40], outline=(255, 215, 45, 255), width=6)
    # Royal Faravahar wings (3 tiers of feathers)
    for tier, span in enumerate([180, 150, 120]):
        y_off = cy + tier * 12
        draw.line([(cx - 40, y_off), (cx - span, y_off - 20)], fill=(255, 215, 45, 255), width=6)
        draw.line([(cx + 40, y_off), (cx + span, y_off - 20)], fill=(255, 215, 45, 255), width=6)
    # Tail feathers
    draw.polygon([(cx - 24, cy + 40), (cx + 24, cy + 40), (cx + 36, cy + 100), (cx - 36, cy + 100)], fill=(255, 205, 40, 255), outline=(190, 140, 20, 255))
    # Royal Loops (Peyman loops)
    draw.arc([cx - 70, cy + 30, cx - 30, cy + 70], start=45, end=270, fill=(255, 215, 45, 255), width=4)
    draw.arc([cx + 30, cy + 30, cx + 70, cy + 70], start=270, end=135, fill=(255, 215, 45, 255), width=4)
    # King Figure
    draw.ellipse([cx - 20, cy - 80, cx + 20, cy - 40], fill=(255, 220, 50, 255)) # Head
    draw.polygon([(cx - 16, cy - 70), (cx - 24, cy - 85), (cx - 8, cy - 75), (cx + 8, cy - 75), (cx + 24, cy - 85), (cx + 16, cy - 70)], fill=(255, 245, 120, 255)) # Crown

    # Bold Gold & Turquoise Banner: IRANIAN HARDCORE
    draw.rectangle([40, 360, 472, 450], fill=(15, 25, 45, 240), outline=(235, 195, 50, 255), width=3)
    # Subtitle
    # Draw stylized text blocks
    return img

logo_img = draw_master_logo()
logo_img.save(f"{PACK_DIR}/pack.png")
logo_img.save(f"{PACK_DIR}/assets/minecraft/textures/gui/title/mojang.png")
print("Generated 512x512 pack.png and mojang.png.")

# ==========================================
# 11. PACK.MCMETA
# ==========================================
mcmeta_content = """{
  "pack": {
    "pack_format": 3,
    "description": "§6§lIranian Hardcore HD Resource Pack v5.4.0 §7- §eFarsh, Kashi, Persepolis & Shamshir"
  }
}"""
with open(f"{PACK_DIR}/pack.mcmeta", "w", encoding="utf-8") as f:
    f.write(mcmeta_content)

# ==========================================
# 12. OPTIFINE CIT PROPERTIES FOR PERSIAN ITEMS
# ==========================================
cit_items = [
    ("shamshir_divkosh", "diamond_sword", "Shamshir-e Div-Kosh"),
    ("akinakes", "golden_sword", "Akinakes-e Shahanshahi"),
    ("zulfiqar", "iron_sword", "Zulfiqar-e Heydari"),
    ("gorz_rostam", "stone_sword", "Gorz-e Rostam"),
    ("meel_bastani", "wooden_sword", "Meel-e Bastani"),
    ("kaman_arash", "bow", "Kaman-e Arash"),
    ("tir_simurgh", "arrow", "Tir-e Simurgh"),
    ("derafsh_kaviani", "shield", "Derafsh Kaviani"),
    ("naan_sangak", "bread", "Naan-e Sangak"),
    ("kabab_koobideh", "cooked_beef", "Kabab Koobideh"),
    ("joojeh_kabab", "cooked_chicken", "Joojeh Kabab"),
    ("ghormeh_sabzi", "mushroom_stew", "Ghormeh Sabzi"),
    ("dizi_sangi", "rabbit_stew", "Dizi-e Sangi"),
    ("mashk_aab", "potion", "Mashk-e Aab"),
    ("derik_tala", "gold_ingot", "Derik-e Tala"),
    ("siglos_noghre", "iron_ingot", "Siglos-e Noghre"),
    ("danake_mesi", "gold_nugget", "Danake-ye Mesi"),
    ("firoozeh_neyshaboor", "emerald", "Firoozeh-ye Neyshaboor")
]

for file_id, match_item, display_name in cit_items:
    prop_content = f"""type=item
matchItems={match_item}
model=./{file_id}
nbt.display.Name=ipattern:*{display_name}*
"""
    with open(f"{PACK_DIR}/assets/minecraft/optifine/cit/persian/{file_id}.properties", "w", encoding="utf-8") as f:
        f.write(prop_content)

# Copy internal plugin resources
if os.path.exists(TARGET_ASSETS):
    shutil.rmtree(TARGET_ASSETS)
shutil.copytree(PACK_DIR, TARGET_ASSETS)

# Create ZIP
with zipfile.ZipFile(ZIP_OUTPUT, 'w', zipfile.ZIP_DEFLATED) as zipf:
    for root, dirs_list, files in os.walk(PACK_DIR):
        for file in files:
            full_path = os.path.join(root, file)
            rel_path = os.path.relpath(full_path, PACK_DIR)
            zipf.write(full_path, rel_path)

zip_size = os.path.getsize(ZIP_OUTPUT)
print(f"SUCCESS! HD Resource Pack created: {ZIP_OUTPUT} ({zip_size:,} bytes, {zip_size / 1024 / 1024:.2f} MB).")
