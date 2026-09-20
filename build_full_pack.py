#!/usr/bin/env python3
import os, struct, zlib, math, random, zipfile, shutil

def make_png(width, height, rgba):
    raw = bytearray()
    for y in range(height):
        raw.append(0)
        for x in range(width):
            raw.extend(rgba[y][x])
    def chunk(tag, data):
        return struct.pack('>I', len(data)) + tag + data + struct.pack('>I', zlib.crc32(tag + data) & 0xffffffff)
    return b'\x89PNG\r\n\x1a\n' + chunk(b'IHDR', struct.pack('>IIBBBBB', width, height, 8, 6, 0, 0, 0)) + chunk(b'IDAT', zlib.compress(bytes(raw), 9)) + chunk(b'IEND', b'')

def save_png(path, width, height, rgba):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, 'wb') as f:
        f.write(make_png(width, height, rgba))

# -------------------------------------------------------------
# 1. Environment & Weather Textures
# -------------------------------------------------------------
def gen_rain():
    # 64x64 falling rain texture
    w, h = 64, 64
    grid = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    for y in range(h):
        for x in range(w):
            # Dense diagonal streaks (slope -2)
            val1 = (x * 4 + y * 9) % 37
            val2 = (x * 7 + y * 13) % 47
            val3 = (x * 11 + y * 5) % 59
            if val1 == 0:
                grid[y][x] = (175, 225, 255, 210) # bright streak
            elif val1 == 1:
                grid[y][x] = (140, 195, 245, 170)
            elif val2 == 0:
                grid[y][x] = (120, 180, 240, 150)
            elif val3 == 0:
                grid[y][x] = (200, 240, 255, 190)
    return grid

def gen_snow():
    # 64x64 detailed snowflakes
    w, h = 64, 64
    grid = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    centers = [(8, 8), (24, 12), (40, 6), (56, 18), (14, 28), (32, 34), (50, 42), (8, 48), (26, 56), (46, 58), (60, 50)]
    for cx, cy in centers:
        for dx in range(-4, 5):
            for dy in range(-4, 5):
                dist = math.sqrt(dx*dx + dy*dy)
                px = (cx + dx) % w
                py = (cy + dy) % h
                if dist < 1.2:
                    grid[py][px] = (255, 255, 255, 245)
                elif (dx == 0 or dy == 0 or abs(dx) == abs(dy)) and dist <= 3.5:
                    alpha = int(220 - dist * 40)
                    grid[py][px] = (235, 245, 255, max(60, alpha))
    return grid

def gen_sun():
    # 128x128 radiant golden Persian sun disk
    w, h = 128, 128
    grid = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    cx, cy = 64, 64
    for y in range(h):
        for x in range(w):
            dx = x - cx
            dy = y - cy
            dist = math.hypot(dx, dy)
            angle = math.atan2(dy, dx)
            # 16-pointed solar rays of Mithra
            ray = (math.cos(angle * 16) + 1.0) * 0.5
            effective_radius = 28 + ray * 18

            if dist <= 24:
                # Sun core: bright white-gold
                ratio = dist / 24.0
                r = 255
                g = int(255 - ratio * 45) # 255 -> 210
                b = int(210 - ratio * 150) # 210 -> 60
                grid[y][x] = (r, g, b, 255)
            elif dist <= effective_radius:
                # Solar corona & rays
                ratio = (dist - 24) / (effective_radius - 24)
                r = 255
                g = int(210 - ratio * 70)
                b = int(60 - ratio * 40)
                alpha = int(240 * (1.0 - ratio))
                grid[y][x] = (r, g, b, max(0, min(255, alpha)))
            elif dist <= 60:
                # Outer solar halo
                ratio = (dist - effective_radius) / (60 - effective_radius)
                alpha = int(60 * (1.0 - ratio) * ray)
                grid[y][x] = (255, 190, 40, max(0, min(255, alpha)))
    return grid

def gen_moon():
    # 256x128 sheet containing 8 phases (2 rows of 4 phases, each cell 64x64)
    w, h = 256, 128
    grid = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    for phase in range(8):
        col = phase % 4
        row = phase // 4
        ox = col * 64
        oy = row * 64
        mcx = ox + 32
        mcy = oy + 32
        radius = 22

        for y in range(oy, oy + 64):
            for x in range(ox, ox + 64):
                dx = x - mcx
                dy = y - mcy
                dist = math.hypot(dx, dy)
                if dist > radius:
                    continue

                # Lunar crater noise
                noise = math.sin(dx * 0.5) * math.cos(dy * 0.5) * 15
                base_color = int(215 + noise)
                base_color = max(160, min(240, base_color))

                # Determine illuminated part based on phase
                # phase: 0=Full, 1=Waning Gibbous, 2=Last Quarter, 3=Waning Crescent,
                #        4=New Moon, 5=Waxing Crescent, 6=First Quarter, 7=Waxing Gibbous
                lit = False
                nx = dx / radius # -1 to +1

                if phase == 0: # Full Moon
                    lit = True
                elif phase == 1: # Waning Gibbous
                    lit = (nx < 0.6)
                elif phase == 2: # Third Quarter
                    lit = (nx < 0.0)
                elif phase == 3: # Waning Crescent
                    lit = (nx < -0.5)
                elif phase == 4: # New Moon
                    lit = False
                elif phase == 5: # Waxing Crescent
                    lit = (nx > 0.5)
                elif phase == 6: # First Quarter
                    lit = (nx > 0.0)
                elif phase == 7: # Waxing Gibbous
                    lit = (nx > -0.6)

                if lit:
                    grid[y][x] = (base_color, base_color, base_color + 15, 255)
                else:
                    # Dark unlit moon disk
                    grid[y][x] = (25, 28, 38, 200)
    return grid

def gen_clouds():
    w, h = 256, 256
    grid = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    for y in range(h):
        for x in range(w):
            v1 = math.sin(x * 0.05) + math.cos(y * 0.05)
            v2 = math.sin(x * 0.1 + y * 0.08)
            val = (v1 + v2 * 0.5) / 1.5
            if val > 0.2:
                alpha = int(min(220, (val - 0.2) * 260))
                grid[y][x] = (245, 250, 255, alpha)
    return grid

# -------------------------------------------------------------
# 2. Iranian Mob Entity Textures (64x64 Standard Minecraft Biped)
# -------------------------------------------------------------
def create_base_skin(skin_color, eye_color, hair_color, cloth_color, cloth2_color):
    w, h = 64, 64
    grid = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]

    def fill_rect(x1, y1, x2, y2, color):
        for y in range(y1, y2):
            for x in range(x1, x2):
                if 0 <= x < w and 0 <= y < h:
                    grid[y][x] = color

    # Head (8, 0 to 16, 8) Top & Bottom
    fill_rect(8, 0, 16, 8, hair_color)
    fill_rect(16, 0, 24, 8, hair_color)
    # Head Front (8, 8, 16, 16)
    fill_rect(8, 8, 16, 16, skin_color)
    # Eyes
    grid[12][9] = eye_color
    grid[12][10] = eye_color
    grid[12][13] = eye_color
    grid[12][14] = eye_color
    # Mouth / beard
    grid[14][11] = (120, 50, 50, 255)
    grid[14][12] = (120, 50, 50, 255)

    # Head Right & Left & Back
    fill_rect(0, 8, 8, 16, hair_color)
    fill_rect(16, 8, 24, 16, hair_color)
    fill_rect(24, 8, 32, 16, hair_color)

    # Torso (20, 16 to 28, 20) Top/Bottom
    fill_rect(20, 16, 28, 20, cloth_color)
    fill_rect(28, 16, 36, 20, cloth_color)
    # Torso Front (20, 20 to 28, 32)
    fill_rect(20, 20, 28, 32, cloth_color)
    # Torso emblem/chestplate
    fill_rect(22, 22, 26, 28, cloth2_color)

    # Torso Back and Sides
    fill_rect(16, 20, 20, 32, cloth_color)
    fill_rect(28, 20, 32, 32, cloth_color)
    fill_rect(32, 20, 40, 32, cloth_color)

    # Right Arm (44, 20 to 48, 32)
    fill_rect(40, 20, 56, 32, cloth_color)
    fill_rect(44, 28, 48, 32, skin_color) # hand

    # Right Leg (0, 20 to 16, 32)
    fill_rect(0, 20, 16, 32, cloth2_color)

    # In 64x64: Left Arm (32, 48 to 48, 64) and Left Leg (16, 48 to 32, 64)
    fill_rect(32, 48, 48, 64, cloth_color)
    fill_rect(16, 48, 32, 64, cloth2_color)

    return grid

def gen_div_sepid():
    # White Demon of Mazandaran: Snowy pale fur, glowing blood-red eyes, dark horns, sharp fangs
    skin = (235, 238, 245, 255)
    eyes = (255, 30, 30, 255) # Glowing blood red
    fur = (210, 215, 225, 255)
    loincloth = (45, 40, 40, 255)
    horns = (30, 30, 35, 255)
    g = create_base_skin(skin, eyes, fur, loincloth, horns)

    # Add demonic horns on head overlay (40, 8) and (8, 6)
    g[6][9] = horns
    g[5][8] = horns
    g[6][14] = horns
    g[5][15] = horns

    # Sharp white fangs
    g[13][10] = (255, 255, 255, 255)
    g[13][13] = (255, 255, 255, 255)

    return g

def gen_zahhak():
    # Zahhak Mar-Doosh: Cursed Persian King, Bronze/Gold crown, two venomous black snakes on shoulders!
    skin = (175, 140, 115, 255)
    eyes = (220, 40, 40, 255)
    crown = (230, 180, 40, 255) # Gold crown
    armor = (50, 45, 55, 255) # Dark royal armor
    gold = (215, 170, 35, 255)
    g = create_base_skin(skin, eyes, crown, armor, gold)

    # Coiled Shoulder Serpents: Head at shoulders (44, 16) and (36, 48)
    snake_color = (25, 45, 30, 255)
    snake_eye = (140, 255, 50, 255) # Poisonous green snake eyes
    g[18][20] = snake_color
    g[17][20] = snake_eye
    g[18][27] = snake_color
    g[17][27] = snake_eye

    return g

def gen_div_siah():
    # Div Siah: Obsidian Black Demon with glowing amber desert eyes
    skin = (35, 35, 40, 255)
    eyes = (255, 140, 20, 255) # Glowing amber
    hair = (20, 20, 25, 255)
    cloth = (55, 45, 35, 255)
    trim = (180, 80, 25, 255)
    return create_base_skin(skin, eyes, hair, cloth, trim)

def gen_javidan():
    # Sarbaz Javidan (Achaemenid Immortal Guard): Imperial royal purple, golden scale armor
    skin = (210, 175, 140, 255)
    eyes = (50, 40, 30, 255)
    helmet = (220, 175, 45, 255) # Gold helmet
    purple = (105, 30, 95, 255) # Royal Persian Purple
    scale_gold = (240, 195, 50, 255)
    return create_base_skin(skin, eyes, helmet, purple, scale_gold)

def gen_rostam():
    # Rostam Dastan: Leopard-skin Babr-e Bayan coat, dragon iron helmet, Iranian beard
    skin = (215, 180, 145, 255)
    eyes = (40, 80, 120, 255)
    helm = (130, 135, 145, 255)
    leopard_skin = (210, 155, 75, 255) # Spotted pelt
    iron = (90, 95, 105, 255)
    g = create_base_skin(skin, eyes, helm, leopard_skin, iron)

    # Leopard pelt spots on torso
    g[22][22] = (70, 45, 20, 255)
    g[24][25] = (70, 45, 20, 255)
    g[27][23] = (70, 45, 20, 255)
    return g

def gen_kaveh():
    # Kaveh Ahangar (Blacksmith Golem / Guard): Leather apron, bronze iron muscles, Derafsh Kaviani colors
    w, h = 128, 128 # Iron golem texture size
    grid = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    iron_body = (180, 175, 170, 255)
    leather_apron = (120, 75, 45, 255)
    brass_rivet = (220, 180, 50, 255)
    red_banner = (190, 30, 30, 255)

    for y in range(h):
        for x in range(w):
            grid[y][x] = iron_body

    # Apron over chest (30 to 70)
    for y in range(35, 80):
        for x in range(35, 75):
            grid[y][x] = leather_apron

    # Derafsh Kaviani banner colors across apron center
    for y in range(50, 75):
        for x in range(50, 60):
            grid[y][x] = red_banner
            if (x + y) % 3 == 0:
                grid[y][x] = brass_rivet

    return grid

def gen_alamut_assassin():
    # Hassan Sabbah / Alamut Assassin (Wither Skeleton style)
    skin = (40, 42, 48, 255)
    eyes = (0, 220, 255, 255) # Stealth cyan glow
    hood = (25, 27, 32, 255)
    crimson_sash = (175, 25, 35, 255)
    metal = (110, 115, 125, 255)
    return create_base_skin(skin, eyes, hood, hood, crimson_sash)

# -------------------------------------------------------------
# 3. High-Detail 16x16 Persian Item Textures
# -------------------------------------------------------------
def gen_mashk_ab():
    # Realistic leather waterskin with wooden nozzle and brass cord
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    L1 = (170, 105, 55, 255) # Light leather
    L2 = (135, 80, 40, 255)  # Mid leather
    L3 = (95, 55, 25, 255)   # Shadow leather
    W  = (100, 65, 30, 255)  # Wood nozzle
    B  = (225, 185, 45, 255) # Brass cord
    S  = (240, 240, 240, 200)# Specular highlight

    # Nozzle at top
    g[2][7] = W; g[2][8] = W; g[3][7] = W; g[3][8] = W
    # Cord
    g[4][6] = B; g[4][7] = B; g[4][8] = B; g[4][9] = B
    # Body
    for y in range(5, 14):
        for x in range(4, 12):
            g[y][x] = L2
    # Highlights
    g[6][5] = S; g[7][5] = S; g[8][6] = L1; g[9][6] = L1
    # Shadows
    for y in range(5, 14):
        g[y][11] = L3
    for x in range(5, 11):
        g[13][x] = L3
    return g

def gen_canteen():
    # Polished steel canteen with leather shoulder strap
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    M1 = (240, 245, 250, 255) # Highlight steel
    M2 = (190, 200, 210, 255) # Mid steel
    M3 = (130, 140, 150, 255) # Shadow steel
    STRAP = (110, 70, 35, 255)
    GOLD  = (220, 180, 40, 255)

    # Cap
    g[2][7] = GOLD; g[2][8] = GOLD; g[3][7] = M2; g[3][8] = M2
    # Body
    for y in range(4, 13):
        for x in range(4, 12):
            g[y][x] = M2
    # Specular curve
    for y in range(5, 12):
        g[y][5] = M1
    # Shadow
    for y in range(4, 13):
        g[y][11] = M3
    for x in range(5, 11):
        g[12][x] = M3
    # Strap diagonal
    g[4][4] = STRAP; g[8][8] = STRAP; g[12][11] = STRAP
    return g

def gen_sword_kourosh():
    # Golden Akinakes with lion pommel
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    GOLD1 = (255, 240, 120, 255)
    GOLD2 = (230, 185, 30, 255)
    GOLD3 = (170, 125, 15, 255)
    RUBY  = (220, 30, 30, 255)

    # Blade (diagonal from 2,13 to 10,5)
    for i in range(9):
        x = 13 - i
        y = 2 + i
        g[y][x] = GOLD1
        g[y][x-1] = GOLD2
    g[2][13] = (255, 255, 255, 255) # Tip

    # Guard
    g[11][4] = GOLD2; g[10][5] = RUBY; g[9][6] = GOLD2
    # Handle & Pommel
    g[12][3] = GOLD3; g[13][2] = GOLD2; g[14][1] = GOLD1; g[15][1] = GOLD2
    return g

def gen_sword_alamut():
    # Damascus curved steel assassin blade
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    S1 = (220, 230, 240, 255)
    S2 = (140, 150, 165, 255)
    S3 = (75, 80, 95, 255)
    CYAN = (0, 210, 230, 255)

    for i in range(9):
        x = 14 - i
        y = 1 + i
        g[y][x] = S1 if i % 2 == 0 else S2
        if x > 1:
            g[y][x-1] = S3
    # Guard & Hilt
    g[11][5] = CYAN; g[12][4] = S3; g[13][3] = S2; g[14][2] = CYAN
    return g

def gen_sword_babak():
    # Crimson Azerbaijani blade
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    R1 = (255, 120, 120, 255)
    R2 = (210, 35, 35, 255)
    R3 = (130, 15, 15, 255)
    GOLD = (235, 185, 35, 255)

    for i in range(9):
        x = 13 - i
        y = 2 + i
        g[y][x] = R1
        g[y][x-1] = R2
        if x > 2:
            g[y][x-2] = R3
    g[11][4] = GOLD; g[10][5] = GOLD; g[12][3] = (40, 40, 40, 255); g[13][2] = GOLD
    return g

def gen_sword_zulfiqar():
    # Double-pointed silver scimitar
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    P1 = (245, 250, 255, 255)
    P2 = (185, 195, 210, 255)
    P3 = (110, 120, 135, 255)
    EMERALD = (30, 220, 90, 255)

    # Double split tip
    g[1][14] = P1; g[3][13] = P1
    for i in range(2, 9):
        x = 14 - i
        y = 1 + i
        g[y][x] = P1
        g[y][x-1] = P2
        if x > 2:
            g[y][x-2] = P3
    g[11][4] = EMERALD; g[10][5] = P2; g[12][3] = (60, 60, 60, 255); g[13][2] = EMERALD
    return g

def gen_ghormeh_sabzi():
    # Ceramic bowl filled with dark green herbs, kidney beans, and dried lime
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    BOWL = (210, 195, 175, 255)
    HERB = (30, 85, 35, 255)
    HERB_LIGHT = (45, 115, 50, 255)
    BEAN = (150, 35, 40, 255)
    LIME = (75, 60, 25, 255)

    # Stew surface
    for y in range(6, 11):
        for x in range(3, 13):
            g[y][x] = HERB if (x + y) % 2 == 0 else HERB_LIGHT
    # Kidney beans
    g[7][5] = BEAN; g[8][9] = BEAN; g[9][6] = BEAN
    # Limoo Amani (dried lime)
    g[7][8] = LIME; g[8][8] = LIME
    # Bowl rim and body
    for x in range(2, 14):
        g[6][x] = BOWL
    for y in range(7, 13):
        g[y][2] = BOWL; g[y][13] = BOWL
    for x in range(4, 12):
        g[13][x] = BOWL
    return g

def gen_dizi_sangak():
    # Clay pot (dizi) on sangak flatbread
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    POT = (180, 85, 45, 255)
    POT_DARK = (130, 55, 30, 255)
    BROTH = (215, 120, 40, 255)
    BREAD = (220, 175, 110, 255)
    SESAME = (255, 245, 215, 255)

    # Clay Pot
    g[4][7] = POT; g[4][8] = POT
    for y in range(5, 11):
        for x in range(5, 11):
            g[y][x] = POT
    # Rich Broth
    g[5][7] = BROTH; g[5][8] = BROTH
    # Sangak flatbread beneath
    for x in range(2, 14):
        g[12][x] = BREAD; g[13][x] = BREAD
        if x % 3 == 0:
            g[12][x] = SESAME
    return g

def gen_chai_lahijan():
    # Persian tulip tea glass (estekan) with saffron rock candy (nabat)
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    GLASS = (225, 240, 255, 170)
    TEA = (175, 45, 25, 255)     # Deep amber red
    TEA_TOP = (210, 80, 35, 255)
    NABAT = (255, 215, 35, 255)  # Saffron golden sugar crystals
    STICK = (180, 140, 80, 255)

    # Nabat stick emerging diagonally
    g[2][11] = NABAT; g[3][10] = NABAT; g[4][9] = STICK
    # Glass cup
    for y in range(5, 13):
        g[y][5] = GLASS; g[y][10] = GLASS
    # Amber tea inside
    for y in range(6, 12):
        for x in range(6, 10):
            g[y][x] = TEA
    g[6][7] = TEA_TOP; g[6][8] = TEA_TOP
    # Saucer plate
    for x in range(3, 13):
        g[13][x] = (240, 245, 255, 220)
    return g

def gen_derik_tala():
    # Ancient Achaemenid gold Daric coin with Great King
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    G1 = (255, 235, 110, 255)
    G2 = (225, 180, 30, 255)
    G3 = (165, 120, 15, 255)
    RELIEF = (130, 85, 10, 255)

    # Coin circle
    for y in range(2, 14):
        for x in range(2, 14):
            dx = x - 7.5
            dy = y - 7.5
            if dx*dx + dy*dy <= 30:
                g[y][x] = G2
    # Rim highlight
    for y in range(2, 14):
        for x in range(2, 14):
            dx = x - 7.5
            dy = y - 7.5
            dist = math.hypot(dx, dy)
            if 4.8 <= dist <= 5.5:
                g[y][x] = G1 if (dx < 0 and dy < 0) else G3

    # Embossed King running with spear and bow
    g[5][7] = RELIEF; g[6][7] = RELIEF; g[7][7] = RELIEF # King torso
    g[6][6] = RELIEF; g[7][5] = RELIEF # Bow
    g[5][9] = RELIEF; g[4][10] = RELIEF # Spear
    g[9][6] = RELIEF; g[10][5] = RELIEF # Legs
    return g

# -------------------------------------------------------------
# MAIN BUILD PROCESS
# -------------------------------------------------------------
def main():
    base_dir = '/home/user/plugin-1.12.2-minecraft-iranian/resourcepack_build'
    if os.path.exists(base_dir):
        shutil.rmtree(base_dir)
    os.makedirs(base_dir, exist_ok=True)

    # 1. pack.mcmeta
    with open(os.path.join(base_dir, 'pack.mcmeta'), 'w') as f:
        f.write('{\n  "pack": {\n    "pack_format": 3,\n    "description": "Iranian Hardcore v5.2 - Textures, Weather & Mobs"\n  }\n}')

    # 2. pack.png
    save_png(os.path.join(base_dir, 'pack.png'), 16, 16, gen_derik_tala())

    # 3. Environment Weather Textures
    env_dir = os.path.join(base_dir, 'assets/minecraft/textures/environment')
    save_png(os.path.join(env_dir, 'rain.png'), 64, 64, gen_rain())
    save_png(os.path.join(env_dir, 'snow.png'), 64, 64, gen_snow())
    save_png(os.path.join(env_dir, 'sun.png'), 128, 128, gen_sun())
    save_png(os.path.join(env_dir, 'moon_phases.png'), 256, 128, gen_moon())
    save_png(os.path.join(env_dir, 'clouds.png'), 256, 256, gen_clouds())
    print('[+] Environment & Weather textures generated (rain, snow, sun, moon_phases, clouds).')

    # 4. Mob Textures (Vanilla + OptiFine + McPatcher)
    # Vanilla Mobs
    save_png(os.path.join(base_dir, 'assets/minecraft/textures/entity/zombie/zombie.png'), 64, 64, gen_div_sepid())
    save_png(os.path.join(base_dir, 'assets/minecraft/textures/entity/zombie/husk.png'), 64, 64, gen_div_siah())
    save_png(os.path.join(base_dir, 'assets/minecraft/textures/entity/skeleton/wither_skeleton.png'), 64, 64, gen_alamut_assassin())
    save_png(os.path.join(base_dir, 'assets/minecraft/textures/entity/iron_golem.png'), 128, 128, gen_kaveh())

    # OptiFine / McPatcher Random Mob Skins
    mob_targets = ['optifine/mob', 'mcpatcher/mob']
    for m in mob_targets:
        md = os.path.join(base_dir, 'assets/minecraft', m)
        # Zombie skins: 2=Div Sepid, 3=Zahhak, 4=Javidan, 5=Alamut
        save_png(os.path.join(md, 'zombie/zombie.png'), 64, 64, gen_div_sepid())
        save_png(os.path.join(md, 'zombie/zombie2.png'), 64, 64, gen_div_sepid())
        save_png(os.path.join(md, 'zombie/zombie3.png'), 64, 64, gen_zahhak())
        save_png(os.path.join(md, 'zombie/zombie4.png'), 64, 64, gen_javidan())
        save_png(os.path.join(md, 'zombie/zombie5.png'), 64, 64, gen_alamut_assassin())
        with open(os.path.join(md, 'zombie/zombie.properties'), 'w') as f:
            f.write('skins.1=1\nskins.2=2\nname.2=iregex:.*Div Sepid.*\nskins.3=3\nname.3=iregex:.*Zahhak.*\nskins.4=4\nname.4=iregex:.*Javidan.*\nskins.5=5\nname.5=iregex:.*Boss.*|.*Alamut.*\n')

        # Husk skins: 2=Div Siah
        save_png(os.path.join(md, 'husk/husk.png'), 64, 64, gen_div_siah())
        save_png(os.path.join(md, 'husk/husk2.png'), 64, 64, gen_div_siah())
        with open(os.path.join(md, 'husk/husk.properties'), 'w') as f:
            f.write('skins.1=1\nskins.2=2\nname.2=iregex:.*Div Siah.*\n')

        # Skeleton skins: 2=Rostam
        save_png(os.path.join(md, 'skeleton/skeleton2.png'), 64, 64, gen_rostam())
        with open(os.path.join(md, 'skeleton/skeleton.properties'), 'w') as f:
            f.write('skins.1=1\nskins.2=2\nname.2=iregex:.*Rostam.*\n')

        # Wither Skeleton skins: 2=Alamut
        save_png(os.path.join(md, 'wither_skeleton/wither_skeleton2.png'), 64, 64, gen_alamut_assassin())
        with open(os.path.join(md, 'wither_skeleton/wither_skeleton.properties'), 'w') as f:
            f.write('skins.1=1\nskins.2=2\nname.2=iregex:.*Alamut.*|.*Hassan.*|.*Boss.*\n')

        # Golem skins: 2=Kaveh
        save_png(os.path.join(md, 'villager_golem/iron_golem2.png'), 128, 128, gen_kaveh())
        save_png(os.path.join(md, 'villager_golem/villager_golem2.png'), 128, 128, gen_kaveh())
        with open(os.path.join(md, 'villager_golem/iron_golem.properties'), 'w') as f:
            f.write('skins.1=1\nskins.2=2\nname.2=iregex:.*Kaveh.*\n')
        with open(os.path.join(md, 'villager_golem/villager_golem.properties'), 'w') as f:
            f.write('skins.1=1\nskins.2=2\nname.2=iregex:.*Kaveh.*\n')

    print('[+] Custom Iranian Mob skins generated (Div Sepid, Zahhak, Div Siah, Javidan, Rostam, Kaveh, Alamut).')

    # 8. Ghalb-e Sorkh (Crystal Red Heart)
    def gen_ghalb_sorkh():
        w, h = 16, 16
        g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
        RED1 = (255, 60, 80, 255)
        RED2 = (220, 20, 40, 255)
        RED3 = (140, 10, 25, 255)
        GOLD = (255, 220, 50, 255)
        WHITE = (255, 255, 255, 255)

        # Heart shape
        for dy in range(10):
            y = 4 + dy
            half_w = 6 - (dy * 6 // 10)
            for x in range(8 - half_w, 8 + half_w):
                g[y][x] = RED2
        # Top rounded lobes
        for x in range(3, 7): g[3][x] = RED2
        for x in range(9, 13): g[3][x] = RED2
        for x in range(4, 6): g[2][x] = RED1
        for x in range(10, 12): g[2][x] = RED1
        # Specular glint
        g[4][4] = WHITE; g[4][5] = WHITE; g[5][4] = WHITE
        # Gold filigree rim
        g[2][3] = GOLD; g[2][6] = GOLD; g[3][7] = GOLD; g[3][8] = GOLD
        g[2][9] = GOLD; g[2][12] = GOLD; g[13][7] = GOLD; g[14][7] = GOLD
        return g

    # 9. Ghalb-e Firoozeh (Turquoise Heart)
    def gen_ghalb_firoozeh():
        w, h = 16, 16
        g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
        CYAN1 = (80, 245, 240, 255)
        CYAN2 = (20, 195, 205, 255)
        CYAN3 = (10, 120, 145, 255)
        GOLD  = (255, 225, 60, 255)
        WHITE = (255, 255, 255, 255)

        for dy in range(10):
            y = 4 + dy
            half_w = 6 - (dy * 6 // 10)
            for x in range(8 - half_w, 8 + half_w):
                g[y][x] = CYAN2
        for x in range(3, 7): g[3][x] = CYAN2
        for x in range(9, 13): g[3][x] = CYAN2
        for x in range(4, 6): g[2][x] = CYAN1
        for x in range(10, 12): g[2][x] = CYAN1
        g[4][4] = WHITE; g[5][4] = WHITE
        g[2][3] = GOLD; g[3][7] = GOLD; g[3][8] = GOLD; g[2][12] = GOLD
        return g

    # 10. Eksir-e Javidan (Elixir of Immortality)
    def gen_eksir_javidan():
        w, h = 16, 16
        g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
        GLASS = (220, 245, 255, 180)
        ELIXIR = (255, 230, 90, 255) # Glowing celestial gold
        LIGHT  = (255, 255, 210, 255)
        CORK   = (160, 110, 50, 255)

        g[1][7] = CORK; g[1][8] = CORK; g[2][7] = CORK; g[2][8] = CORK
        g[3][6] = GLASS; g[3][9] = GLASS
        for y in range(4, 13):
            g[y][4] = GLASS; g[y][11] = GLASS
            for x in range(5, 11):
                g[y][x] = ELIXIR if (x + y) % 2 == 0 else LIGHT
        g[6][6] = (255, 255, 255, 255); g[7][6] = (255, 255, 255, 255) # glint
        for x in range(5, 11): g[13][x] = GLASS
        return g

    # 11. Tigh-e Div-Kosh (Demon Slayer Scimitar)
    def gen_tigh_div_kosh():
        w, h = 16, 16
        g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
        B1 = (255, 70, 70, 255)
        B2 = (220, 190, 240, 255)
        B3 = (90, 40, 120, 255)
        GOLD = (255, 215, 30, 255)

        for i in range(10):
            x = 14 - i
            y = 1 + i
            g[y][x] = B1 if i % 2 == 0 else B2
            if x > 1: g[y][x-1] = B3
        g[11][4] = GOLD; g[10][5] = GOLD; g[12][3] = (40, 30, 45, 255); g[13][2] = GOLD
        return g

    # 12. Bomb-e Naft-e Siah (Naphtha Firebomb)
    def gen_bomb_naft():
        w, h = 16, 16
        g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
        CLAY = (145, 75, 40, 255)
        PITCH = (30, 28, 35, 255)
        FUSE = (200, 160, 100, 255)
        SPARK = (255, 180, 20, 255)

        g[1][9] = SPARK; g[2][8] = FUSE; g[3][7] = FUSE
        g[4][7] = CLAY; g[4][8] = CLAY
        for y in range(5, 13):
            for x in range(4, 12):
                g[y][x] = PITCH if (x + y) % 3 == 0 else CLAY
        return g

    # 13. Separ-e Derafsh-e Kaviani (Persian Aegis Shield)
    def gen_separ_kaviani():
        w, h = 16, 16
        g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
        GOLD = (255, 215, 40, 255)
        RED  = (220, 25, 35, 255)
        PURPLE = (120, 25, 110, 255)
        CYAN = (30, 215, 210, 255)
        YELLOW = (255, 240, 60, 255)

        for y in range(2, 14):
            half = 5 - (max(0, y - 9) * 4 // 5)
            for x in range(8 - half, 8 + half):
                g[y][x] = GOLD
        # 4 Quadrants of Derafsh Kaviani
        for y in range(4, 8):
            for x in range(5, 8): g[y][x] = RED
            for x in range(8, 11): g[y][x] = YELLOW
        for y in range(8, 11):
            for x in range(5, 8): g[y][x] = PURPLE
            for x in range(8, 11): g[y][x] = CYAN
        # Center Star Gem
        g[7][7] = (255, 255, 255, 255); g[8][8] = (255, 255, 255, 255)
        return g

    # 14. Telesm-e Jam-e Jam (Talisman of Jamshid)
    def gen_jam_e_jam():
        w, h = 16, 16
        g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
        GOLD = (255, 215, 30, 255)
        COSMIC1 = (40, 20, 95, 255)
        COSMIC2 = (20, 160, 220, 255)
        STAR = (255, 255, 255, 255)

        for y in range(2, 14):
            for x in range(2, 14):
                dx = x - 7.5; dy = y - 7.5
                dist = math.hypot(dx, dy)
                if dist <= 5.5:
                    g[y][x] = COSMIC1 if (dx + dy) % 2 == 0 else COSMIC2
                if 4.8 <= dist <= 5.5:
                    g[y][x] = GOLD
        g[6][6] = STAR; g[9][8] = STAR; g[7][9] = STAR
        return g

    # 5. Item Textures Dictionary
    items = {
        'mashk_ab': (gen_mashk_ab(), 'potion', '.*Mashk.*'),
        'canteen': (gen_canteen(), 'potion', '.*Ghomghame.*|.*Canteen.*'),
        'sword_kourosh': (gen_sword_kourosh(), 'diamond_sword', '.*Kourosh.*'),
        'sword_alamut': (gen_sword_alamut(), 'diamond_sword', '.*Alamut.*|.*Hassan.*'),
        'sword_babak': (gen_sword_babak(), 'iron_sword', '.*Babak.*'),
        'sword_zulfiqar': (gen_sword_zulfiqar(), 'diamond_sword', '.*Zolfaghar.*|.*Zulfiqar.*'),
        'ghormeh_sabzi': (gen_ghormeh_sabzi(), 'bread', '.*Ghormeh.*'),
        'dizi_sangak': (gen_dizi_sangak(), 'bread', '.*Dizi.*'),
        'chai_lahijan': (gen_chai_lahijan(), 'potion', '.*Chai.*'),
        'derik_tala': (gen_derik_tala(), 'gold_nugget', '.*Derik.*|.*Sekke.*'),
        'ghalb_sorkh': (gen_ghalb_sorkh(), 'golden_apple', '.*Ghalb.*Sorkh.*'),
        'ghalb_firoozeh': (gen_ghalb_firoozeh(), 'prismarine_crystals', '.*Ghalb.*Firoozeh.*'),
        'eksir_javidan': (gen_eksir_javidan(), 'potion', '.*Eksir.*|.*Hayat.*'),
        'tigh_div_kosh': (gen_tigh_div_kosh(), 'diamond_sword', '.*Div-Kosh.*'),
        'bomb_naft': (gen_bomb_naft(), 'magma_cream', '.*Bomb.*Naft.*'),
        'separ_kaviani': (gen_separ_kaviani(), 'shield', '.*Kaviani.*'),
        'jam_e_jam': (gen_jam_e_jam(), 'clock', '.*Jam-e Jam.*|.*Telesm.*')
    }

    # Save to both texture paths & OptiFine/McPatcher CIT
    for name, (matrix, vanilla_item, regex) in items.items():
        save_png(os.path.join(base_dir, f'assets/minecraft/textures/item/iranian/{name}.png'), 16, 16, matrix)
        save_png(os.path.join(base_dir, f'assets/minecraft/textures/items/iranian/{name}.png'), 16, 16, matrix)
        save_png(os.path.join(base_dir, f'assets/minecraft/optifine/cit/iranian/{name}.png'), 16, 16, matrix)
        save_png(os.path.join(base_dir, f'assets/minecraft/mcpatcher/cit/iranian/{name}.png'), 16, 16, matrix)

        # OptiFine / McPatcher properties
        prop_content = f"type=item\nitems={vanilla_item}\ntexture={name}.png\nnbt.display.Name=iregex:{regex}\n"
        with open(os.path.join(base_dir, f'assets/minecraft/optifine/cit/iranian/{name}.properties'), 'w') as f:
            f.write(prop_content)
        with open(os.path.join(base_dir, f'assets/minecraft/mcpatcher/cit/iranian/{name}.properties'), 'w') as f:
            f.write(prop_content)

        # 1.12.2 item model
        model_json = f'{{\n  "parent": "item/generated",\n  "textures": {{\n    "layer0": "items/iranian/{name}"\n  }}\n}}\n'
        os.makedirs(os.path.join(base_dir, 'assets/minecraft/models/item/iranian'), exist_ok=True)
        with open(os.path.join(base_dir, f'assets/minecraft/models/item/iranian/{name}.json'), 'w') as f:
            f.write(model_json)

    # Vanilla override models for 1.12.2 durability
    models_dir = os.path.join(base_dir, 'assets/minecraft/models/item')
    # diamond_sword.json
    with open(os.path.join(models_dir, 'diamond_sword.json'), 'w') as f:
        f.write('''{
  "parent": "item/handheld",
  "textures": {
    "layer0": "items/diamond_sword"
  },
  "overrides": [
    { "predicate": { "damaged": 0, "damage": 0.0006406149903907751 }, "model": "item/iranian/sword_kourosh" },
    { "predicate": { "damaged": 0, "damage": 0.0012812299807815502 }, "model": "item/iranian/sword_alamut" },
    { "predicate": { "damaged": 0, "damage": 0.0019218449711723255 }, "model": "item/iranian/sword_zulfiqar" }
  ]
}''')
    # iron_sword.json
    with open(os.path.join(models_dir, 'iron_sword.json'), 'w') as f:
        f.write('''{
  "parent": "item/handheld",
  "textures": {
    "layer0": "items/iron_sword"
  },
  "overrides": [
    { "predicate": { "damaged": 0, "damage": 0.003984063745019920 }, "model": "item/iranian/sword_babak" }
  ]
}''')
    # potion.json
    with open(os.path.join(models_dir, 'potion.json'), 'w') as f:
        f.write('''{
  "parent": "item/generated",
  "textures": {
    "layer0": "items/potion_overlay",
    "layer1": "items/potion_bottle_drinkable"
  },
  "overrides": [
    { "predicate": { "custom_model_data": 1 }, "model": "item/iranian/mashk_ab" },
    { "predicate": { "custom_model_data": 2 }, "model": "item/iranian/canteen" },
    { "predicate": { "custom_model_data": 3 }, "model": "item/iranian/chai_lahijan" }
  ]
}''')
    # bread.json
    with open(os.path.join(models_dir, 'bread.json'), 'w') as f:
        f.write('''{
  "parent": "item/generated",
  "textures": {
    "layer0": "items/bread"
  },
  "overrides": [
    { "predicate": { "custom_model_data": 1 }, "model": "item/iranian/ghormeh_sabzi" },
    { "predicate": { "custom_model_data": 2 }, "model": "item/iranian/dizi_sangak" }
  ]
}''')
    # gold_nugget.json
    with open(os.path.join(models_dir, 'gold_nugget.json'), 'w') as f:
        f.write('''{
  "parent": "item/generated",
  "textures": {
    "layer0": "items/gold_nugget"
  },
  "overrides": [
    { "predicate": { "custom_model_data": 1 }, "model": "item/iranian/derik_tala" }
  ]
}''')

    # Copy sounds.json from workspace if exists
    src_sounds = '/home/user/plugin-1.12.2-minecraft-iranian/src/main/resources/sounds.json'
    if os.path.exists(src_sounds):
        shutil.copy(src_sounds, os.path.join(base_dir, 'assets/minecraft/sounds.json'))

    # Zip the resource pack
    zip_path = '/home/user/plugin-1.12.2-minecraft-iranian/IranianHardcore-ResourcePack.zip'
    if os.path.exists(zip_path):
        os.remove(zip_path)
    with zipfile.ZipFile(zip_path, 'w', zipfile.ZIP_DEFLATED) as z:
        for root, dirs, files in os.walk(base_dir):
            for file in files:
                full = os.path.join(root, file)
                rel = os.path.relpath(full, base_dir)
                z.write(full, rel)

    # Also copy into src/main/resources/pack.zip if needed
    shutil.rmtree(base_dir)
    print(f'[SUCCESS] Resource pack built successfully at: {zip_path}')
    print(f'Pack size: {os.path.getsize(zip_path)} bytes')

if __name__ == '__main__':
    main()
