#!/usr/bin/env python3
import os, struct, zlib, math, random, zipfile, shutil

def make_png(width, height, rgba):
    raw = bytearray()
    for y in range(height):
        raw.append(0)
        for x in range(width):
            r, g, b, a = rgba[y][x]
            raw.append(max(0, min(255, int(r))))
            raw.append(max(0, min(255, int(g))))
            raw.append(max(0, min(255, int(b))))
            raw.append(max(0, min(255, int(a))))
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
    w, h = 64, 64
    grid = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    for y in range(h):
        for x in range(w):
            val1 = (x * 4 + y * 9) % 37
            val2 = (x * 7 + y * 13) % 47
            if val1 == 0:
                grid[y][x] = (175, 225, 255, 210)
            elif val2 == 0:
                grid[y][x] = (120, 180, 240, 150)
    return grid

def gen_snow():
    w, h = 64, 64
    grid = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    centers = [(8, 8), (24, 12), (40, 6), (56, 18), (14, 28), (32, 34), (50, 42), (8, 48), (26, 56), (46, 58), (60, 50)]
    for cx, cy in centers:
        for dx in range(-4, 5):
            for dy in range(-4, 5):
                dist = math.hypot(dx, dy)
                if dist < 4:
                    if dx == 0 or dy == 0 or abs(dx) == abs(dy):
                        grid[cy + dy][cx + dx] = (240, 250, 255, 230)
    return grid

def gen_sun():
    w, h = 128, 128
    grid = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    cx, cy = 63.5, 63.5
    for y in range(h):
        for x in range(w):
            dx, dy = x - cx, y - cy
            dist = math.hypot(dx, dy)
            angle = math.atan2(dy, dx)
            # 16 Persian Solar Rays
            ray = math.sin(angle * 16)
            ray_dist = 36 + ray * 20
            if dist <= 24:
                grid[y][x] = (255, 250, 180, 255) # Golden core
            elif dist <= 32:
                grid[y][x] = (255, 215, 50, 255)
            elif dist <= ray_dist:
                alpha = int(255 * (1.0 - (dist - 32) / 28.0))
                grid[y][x] = (255, 140, 20, max(0, min(255, alpha)))
    return grid

def gen_moon():
    w, h = 256, 128
    grid = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    centers = [(32, 32), (96, 32), (160, 32), (224, 32), (32, 96), (96, 96), (160, 96), (224, 96)]
    for i, (mcx, mcy) in enumerate(centers):
        for y in range(mcy - 28, mcy + 29):
            for x in range(mcx - 28, mcx + 29):
                dx, dy = x - mcx, y - mcy
                if dx*dx + dy*dy <= 24*24:
                    val = (dx * 3 + dy * 7 + i * 11) % 19
                    c = 210 + val * 2
                    grid[y][x] = (c, c + 10, min(255, c + 25), 255)
    return grid

def gen_clouds():
    w, h = 256, 256
    grid = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    for y in range(h):
        for x in range(w):
            density = math.sin(x * 0.1) * math.cos(y * 0.1) + math.sin((x + y) * 0.08)
            if density > 0.3:
                a = int(min(220, (density - 0.3) * 260))
                grid[y][x] = (245, 250, 255, a)
    return grid

# -------------------------------------------------------------
# 2. Iranian Mob Entity Skins
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
    grid[12][9] = eye_color; grid[12][10] = eye_color
    grid[12][13] = eye_color; grid[12][14] = eye_color
    # Mouth / beard
    grid[14][11] = (120, 50, 50, 255); grid[14][12] = (120, 50, 50, 255)

    # Head Right & Left & Back
    fill_rect(0, 8, 8, 16, hair_color)
    fill_rect(16, 8, 24, 16, hair_color)
    fill_rect(24, 8, 32, 16, hair_color)

    # Torso
    fill_rect(20, 16, 28, 20, cloth_color)
    fill_rect(28, 16, 36, 20, cloth_color)
    fill_rect(20, 20, 28, 32, cloth_color)
    fill_rect(22, 22, 26, 28, cloth2_color) # Torso emblem
    fill_rect(16, 20, 20, 32, cloth_color)
    fill_rect(28, 20, 32, 32, cloth_color)
    fill_rect(32, 20, 40, 32, cloth_color)

    # Right Arm & Leg
    fill_rect(40, 20, 56, 32, cloth_color)
    fill_rect(44, 28, 48, 32, skin_color)
    fill_rect(0, 20, 16, 32, cloth2_color)

    # Left Arm & Leg (64x64 format)
    fill_rect(32, 48, 48, 64, cloth_color)
    fill_rect(16, 48, 32, 64, cloth2_color)
    return grid

def gen_div_sepid():
    skin = (235, 238, 245, 255)
    eyes = (255, 30, 30, 255) # Glowing blood red
    fur = (210, 215, 225, 255)
    loincloth = (45, 40, 40, 255)
    horns = (30, 30, 35, 255)
    g = create_base_skin(skin, eyes, fur, loincloth, horns)
    g[6][9] = horns; g[5][8] = horns; g[6][14] = horns; g[5][15] = horns
    g[13][10] = (255, 255, 255, 255); g[13][13] = (255, 255, 255, 255)
    return g

def gen_zahhak():
    skin = (175, 140, 115, 255)
    eyes = (220, 40, 40, 255)
    crown = (230, 180, 40, 255)
    armor = (50, 45, 55, 255)
    gold = (215, 170, 35, 255)
    g = create_base_skin(skin, eyes, crown, armor, gold)
    # Serpents on shoulders
    g[18][20] = (25, 45, 30, 255); g[17][20] = (140, 255, 50, 255)
    g[18][27] = (25, 45, 30, 255); g[17][27] = (140, 255, 50, 255)
    return g

def gen_div_siah():
    return create_base_skin((35, 35, 40, 255), (255, 140, 20, 255), (20, 20, 25, 255), (55, 45, 35, 255), (180, 80, 25, 255))

def gen_javidan():
    return create_base_skin((210, 175, 140, 255), (50, 40, 30, 255), (220, 175, 45, 255), (105, 30, 95, 255), (240, 195, 50, 255))

def gen_rostam():
    g = create_base_skin((215, 180, 145, 255), (40, 80, 120, 255), (130, 135, 145, 255), (210, 155, 75, 255), (90, 95, 105, 255))
    g[22][22] = (70, 45, 20, 255); g[24][25] = (70, 45, 20, 255); g[27][23] = (70, 45, 20, 255)
    return g

def gen_kaveh():
    w, h = 128, 128
    grid = [[(180, 175, 170, 255) for _ in range(w)] for _ in range(h)]
    for y in range(35, 80):
        for x in range(35, 75):
            grid[y][x] = (120, 75, 45, 255)
    for y in range(50, 75):
        for x in range(50, 60):
            grid[y][x] = (190, 30, 30, 255) if (x + y) % 3 != 0 else (220, 180, 50, 255)
    return grid

def gen_alamut_assassin():
    return create_base_skin((40, 42, 48, 255), (0, 220, 255, 255), (25, 27, 32, 255), (25, 27, 32, 255), (175, 25, 35, 255))

# -------------------------------------------------------------
# 3. Persian Weapons, Foods, Relics & Blocks
# -------------------------------------------------------------
def gen_persian_scimitar(blade_r, blade_g, blade_b, gold_hilt=True):
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    curve = [(14, 1), (13, 2), (12, 3), (11, 4), (10, 5), (9, 6), (8, 7), (7, 8), (6, 9), (5, 10)]
    for (x, y) in curve:
        g[y][x] = (blade_r, blade_g, blade_b, 255)
        g[y][x-1] = (max(0, blade_r - 40), max(0, blade_g - 40), max(0, blade_b - 40), 255)
        if x < 14 and y > 1:
            g[y-1][x] = (min(255, blade_r + 40), min(255, blade_g + 40), min(255, blade_b + 40), 255)
    H1 = (255, 215, 30, 255) if gold_hilt else (180, 180, 190, 255)
    H2 = (200, 150, 10, 255) if gold_hilt else (120, 120, 130, 255)
    g[11][3] = H1; g[10][4] = H1; g[11][5] = H2; g[9][6] = H2
    g[12][2] = (90, 30, 25, 255); g[13][1] = (90, 30, 25, 255)
    g[14][1] = (220, 20, 40, 255); g[14][0] = H1
    return g

def gen_persian_bow():
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    HORN = (160, 95, 45, 255); GOLD = (255, 215, 30, 255); STR = (230, 235, 240, 220)
    for (x, y) in [(14, 2), (12, 3), (9, 4), (7, 6), (6, 8), (7, 10), (9, 12), (12, 13), (14, 14)]:
        g[y][x] = HORN
    g[2][14] = GOLD; g[14][14] = GOLD
    for y in range(3, 14): g[y][14] = STR
    g[8][6] = (220, 30, 40, 255); g[8][5] = GOLD
    return g

def gen_persian_arrow():
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    g[1][14] = (210, 140, 50, 255); g[2][13] = (210, 140, 50, 255); g[2][14] = (210, 140, 50, 255)
    for i in range(3, 13): g[i][15 - i] = (140, 90, 40, 255)
    g[13][2] = (30, 180, 210, 255); g[14][1] = (30, 180, 210, 255); g[14][2] = (255, 255, 255, 255)
    return g

def gen_sangak_bread():
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    for y in range(4, 13):
        for x in range(3, 13): g[y][x] = (235, 195, 115, 255)
    for x in range(3, 13): g[3][x] = (205, 145, 60, 255); g[13][x] = (205, 145, 60, 255)
    for y in range(4, 13): g[y][2] = (205, 145, 60, 255); g[y][13] = (205, 145, 60, 255)
    for (x, y) in [(5, 5), (8, 6), (11, 5), (6, 8), (9, 9), (7, 11)]:
        g[y][x] = (160, 95, 35, 255)
        if (x + y) % 2 == 0: g[y][x+1] = (40, 35, 30, 255)
    return g

def gen_kabab_koobideh():
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    for i in range(2, 14):
        g[i][i] = (95, 45, 20, 255) if i % 2 == 0 else (135, 70, 30, 255)
        g[i+1][i-1] = (135, 70, 30, 255) if i % 2 == 0 else (40, 25, 20, 255)
    g[1][1] = (210, 215, 220, 255); g[14][14] = (210, 215, 220, 255)
    for ty in range(2, 6):
        for tx in range(10, 14):
            if (tx-11.5)**2 + (ty-3.5)**2 <= 2.5: g[ty][tx] = (220, 30, 20, 255)
    return g

def gen_ghormeh_sabzi():
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    for y in range(4, 10):
        for x in range(3, 13): g[y][x] = (35, 80, 25, 255) if (x+y)%2 == 0 else (20, 50, 15, 255)
    g[5][5] = (145, 25, 30, 255); g[7][9] = (145, 25, 30, 255); g[6][7] = (70, 55, 20, 255)
    for x in range(2, 14): g[4][x] = (195, 140, 85, 255)
    for y in range(9, 13):
        half = 5 - (y - 9)
        for x in range(8 - half, 8 + half): g[y][x] = (165, 110, 60, 255)
    for x in range(5, 11): g[13][x] = (195, 140, 85, 255)
    return g

def gen_dizi_sangak():
    # Persian clay crock (Dizi pot) with garlic, broth, and lamb
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    CLAY = (170, 95, 55, 255); BROTH = (200, 70, 30, 255); MEAT = (110, 45, 25, 255)
    g[3][6] = (130, 65, 35, 255); g[3][9] = (130, 65, 35, 255)
    for x in range(5, 11): g[4][x] = CLAY
    for y in range(5, 12):
        for x in range(4, 12): g[y][x] = CLAY
    for y in range(6, 9):
        for x in range(6, 10): g[y][x] = BROTH
    g[7][7] = MEAT; g[7][8] = (255, 215, 100, 255)
    for x in range(5, 11): g[12][x] = (130, 65, 35, 255)
    return g

def gen_chai_lahijan():
    # Persian crystal tea glass (Estekan) with brewed black tea & saffron rock candy (Nabat)
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    GLASS = (220, 245, 255, 170); TEA = (160, 45, 20, 255); SAFFRON = (255, 215, 30, 255)
    for y in range(4, 13):
        half = 3 if y in (4, 5, 11, 12) else 2
        g[y][8 - half] = GLASS; g[y][8 + half - 1] = GLASS
        for x in range(8 - half + 1, 8 + half - 1): g[y][x] = TEA
    g[3][8] = SAFFRON; g[4][8] = SAFFRON; g[5][8] = SAFFRON
    for x in range(5, 11): g[13][x] = (235, 195, 45, 255)
    return g

def gen_sekkeh_derik():
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    for y in range(2, 14):
        for x in range(2, 14):
            dist = math.hypot(x - 7.5, y - 7.5)
            if dist <= 5.5: g[y][x] = (220, 185, 20, 255)
            if dist <= 4.5: g[y][x] = (255, 230, 50, 255)
            if 4.8 <= dist <= 5.5: g[y][x] = (160, 120, 10, 255)
    g[5][7] = (160, 120, 10, 255); g[6][7] = (160, 120, 10, 255); g[7][7] = (160, 120, 10, 255)
    g[3][5] = (255, 255, 200, 255); g[4][4] = (255, 255, 200, 255)
    return g

def gen_crystal_heart(red=True):
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    C1 = (255, 60, 80, 255) if red else (80, 240, 245, 255)
    C2 = (220, 20, 40, 255) if red else (20, 185, 205, 255)
    GOLD = (255, 220, 50, 255)
    for dy in range(10):
        y = 4 + dy
        half_w = 6 - (dy * 6 // 10)
        for x in range(8 - half_w, 8 + half_w): g[y][x] = C2
    for x in range(3, 7): g[3][x] = C2
    for x in range(9, 13): g[3][x] = C2
    for x in range(4, 6): g[2][x] = C1
    for x in range(10, 12): g[2][x] = C1
    g[4][4] = (255, 255, 255, 255); g[5][4] = (255, 255, 255, 255)
    g[2][3] = GOLD; g[2][6] = GOLD; g[3][7] = GOLD; g[2][9] = GOLD; g[2][12] = GOLD
    return g

def gen_mashk_canteen():
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    g[1][7] = (80, 50, 25, 255); g[2][7] = (80, 50, 25, 255); g[2][8] = (80, 50, 25, 255)
    for y in range(3, 14):
        half = 4 if y in (6, 7, 8, 9, 10) else (3 if y in (4, 5, 11, 12) else 2)
        for x in range(8 - half, 8 + half): g[y][x] = (140, 85, 45, 255)
    for i in range(4, 12): g[i][i-1] = (210, 175, 75, 255)
    return g

def gen_persian_carpet():
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    for y in range(h):
        for x in range(w):
            if x in (0, 15) or y in (0, 15):
                g[y][x] = (245, 240, 225, 255) if (x + y) % 2 == 0 else (225, 185, 45, 255)
            elif x in (1, 14) or y in (1, 14):
                g[y][x] = (15, 25, 60, 255)
            elif x in (2, 13) or y in (2, 13):
                g[y][x] = (225, 185, 45, 255)
            else:
                g[y][x] = (170, 20, 30, 255)
    for y in range(5, 11):
        for x in range(5, 11):
            dist = math.hypot(x - 7.5, y - 7.5)
            if dist <= 3.2: g[y][x] = (15, 25, 60, 255)
            if dist <= 2.2: g[y][x] = (225, 185, 45, 255)
            if dist <= 1.2: g[y][x] = (25, 150, 165, 255)
    g[7][7] = (245, 240, 225, 255); g[8][8] = (245, 240, 225, 255)
    return g

def gen_persian_tile():
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    for y in range(h):
        for x in range(w):
            g[y][x] = (18, 55, 140, 255)
            d1 = abs(x - 7.5) + abs(y - 7.5)
            if d1 == 7 or d1 == 6: g[y][x] = (20, 180, 195, 255)
            elif d1 == 5: g[y][x] = (235, 195, 45, 255)
            elif d1 <= 4: g[y][x] = (20, 180, 195, 255)
            if (x == 7 or x == 8 or y == 7 or y == 8) and d1 <= 3:
                g[y][x] = (245, 250, 255, 255)
    return g

def gen_widgets():
    w, h = 256, 256
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    for y in range(22):
        for x in range(182):
            if y == 0 or y == 21 or x == 0 or x == 181:
                g[y][x] = (215, 175, 40, 255)
            elif x % 20 == 0:
                g[y][x] = (120, 95, 25, 255)
            else:
                g[y][x] = (35, 30, 40, 220)
    for y in range(22, 46):
        for x in range(24):
            if y in (22, 23, 44, 45) or x in (0, 1, 22, 23):
                g[y][x] = (30, 220, 230, 255)
    return g

def gen_eksir_javidan():
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    GLASS = (220, 245, 255, 180); ELIXIR = (255, 230, 90, 255)
    g[1][7] = (160, 110, 50, 255); g[1][8] = (160, 110, 50, 255)
    for y in range(4, 13):
        g[y][4] = GLASS; g[y][11] = GLASS
        for x in range(5, 11):
            g[y][x] = ELIXIR if (x + y) % 2 == 0 else (255, 255, 210, 255)
    g[6][6] = (255, 255, 255, 255); g[7][6] = (255, 255, 255, 255)
    for x in range(5, 11): g[13][x] = GLASS
    return g

def gen_bomb_naft():
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    g[1][9] = (255, 180, 20, 255); g[2][8] = (200, 160, 100, 255); g[3][7] = (200, 160, 100, 255)
    for y in range(5, 13):
        for x in range(4, 12):
            g[y][x] = (30, 28, 35, 255) if (x + y) % 3 == 0 else (145, 75, 40, 255)
    return g

def gen_separ_kaviani():
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    for y in range(2, 14):
        half = 5 - (max(0, y - 9) * 4 // 5)
        for x in range(8 - half, 8 + half): g[y][x] = (255, 215, 40, 255)
    for y in range(4, 8):
        for x in range(5, 8): g[y][x] = (220, 25, 35, 255)
        for x in range(8, 11): g[y][x] = (255, 240, 60, 255)
    for y in range(8, 11):
        for x in range(5, 8): g[y][x] = (120, 25, 110, 255)
        for x in range(8, 11): g[y][x] = (30, 215, 210, 255)
    g[7][7] = (255, 255, 255, 255); g[8][8] = (255, 255, 255, 255)
    return g

def gen_jam_e_jam():
    w, h = 16, 16
    g = [[(0, 0, 0, 0) for _ in range(w)] for _ in range(h)]
    for y in range(2, 14):
        for x in range(2, 14):
            dx = x - 7.5; dy = y - 7.5
            dist = math.hypot(dx, dy)
            if dist <= 5.5:
                g[y][x] = (40, 20, 95, 255) if (dx + dy) % 2 == 0 else (20, 160, 220, 255)
            if 4.8 <= dist <= 5.5:
                g[y][x] = (255, 215, 30, 255)
    g[6][6] = (255, 255, 255, 255); g[9][8] = (255, 255, 255, 255); g[7][9] = (255, 255, 255, 255)
    return g

# -------------------------------------------------------------
# MAIN BUILD SCRIPT
# -------------------------------------------------------------
def main():
    base_dir = '/tmp/iranian_pack_full'
    if os.path.exists(base_dir):
        shutil.rmtree(base_dir)
    os.makedirs(base_dir, exist_ok=True)

    # 1. pack.mcmeta (Format 3 for Minecraft 1.12.2)
    with open(os.path.join(base_dir, 'pack.mcmeta'), 'w') as f:
        f.write('''{
  "pack": {
    "pack_format": 3,
    "description": "Iranian Hardcore v5.3 - 100% Persian Historical Texture & Mob Transformation"
  }
}''')

    # 2. pack.png
    save_png(os.path.join(base_dir, 'pack.png'), 16, 16, gen_sekkeh_derik())

    # 3. Environment & Sky Textures
    env_dir = os.path.join(base_dir, 'assets/minecraft/textures/environment')
    save_png(os.path.join(env_dir, 'sun.png'), 128, 128, gen_sun())
    save_png(os.path.join(env_dir, 'moon_phases.png'), 256, 128, gen_moon())
    save_png(os.path.join(env_dir, 'rain.png'), 64, 64, gen_rain())
    save_png(os.path.join(env_dir, 'snow.png'), 64, 64, gen_snow())
    save_png(os.path.join(env_dir, 'clouds.png'), 256, 256, gen_clouds())

    # 4. Mob Textures (Vanilla Base)
    save_png(os.path.join(base_dir, 'assets/minecraft/textures/entity/zombie/zombie.png'), 64, 64, gen_div_sepid())
    save_png(os.path.join(base_dir, 'assets/minecraft/textures/entity/zombie/husk.png'), 64, 64, gen_div_siah())
    save_png(os.path.join(base_dir, 'assets/minecraft/textures/entity/skeleton/skeleton.png'), 64, 64, gen_rostam())
    save_png(os.path.join(base_dir, 'assets/minecraft/textures/entity/skeleton/wither_skeleton.png'), 64, 64, gen_alamut_assassin())
    save_png(os.path.join(base_dir, 'assets/minecraft/textures/entity/iron_golem.png'), 128, 128, gen_kaveh())

    # Mob Textures (OptiFine / McPatcher CIT)
    for m in ['optifine/mob', 'mcpatcher/mob']:
        md = os.path.join(base_dir, 'assets/minecraft', m)
        save_png(os.path.join(md, 'zombie/zombie.png'), 64, 64, gen_div_sepid())
        save_png(os.path.join(md, 'zombie/zombie2.png'), 64, 64, gen_div_sepid())
        save_png(os.path.join(md, 'zombie/zombie3.png'), 64, 64, gen_zahhak())
        save_png(os.path.join(md, 'zombie/zombie4.png'), 64, 64, gen_javidan())
        save_png(os.path.join(md, 'zombie/zombie5.png'), 64, 64, gen_alamut_assassin())
        with open(os.path.join(md, 'zombie/zombie.properties'), 'w') as f:
            f.write('skins.1=1\nskins.2=2\nname.2=iregex:.*Div Sepid.*\nskins.3=3\nname.3=iregex:.*Zahhak.*\nskins.4=4\nname.4=iregex:.*Javidan.*\nskins.5=5\nname.5=iregex:.*Boss.*|.*Alamut.*\n')

        save_png(os.path.join(md, 'husk/husk.png'), 64, 64, gen_div_siah())
        save_png(os.path.join(md, 'husk/husk2.png'), 64, 64, gen_div_siah())
        with open(os.path.join(md, 'husk/husk.properties'), 'w') as f:
            f.write('skins.1=1\nskins.2=2\nname.2=iregex:.*Div Siah.*\n')

        save_png(os.path.join(md, 'skeleton/skeleton2.png'), 64, 64, gen_rostam())
        with open(os.path.join(md, 'skeleton/skeleton.properties'), 'w') as f:
            f.write('skins.1=1\nskins.2=2\nname.2=iregex:.*Rostam.*\n')

        save_png(os.path.join(md, 'wither_skeleton/wither_skeleton2.png'), 64, 64, gen_alamut_assassin())
        with open(os.path.join(md, 'wither_skeleton/wither_skeleton.properties'), 'w') as f:
            f.write('skins.1=1\nskins.2=2\nname.2=iregex:.*Alamut.*|.*Hassan.*|.*Boss.*\n')

        save_png(os.path.join(md, 'villager_golem/iron_golem2.png'), 128, 128, gen_kaveh())
        save_png(os.path.join(md, 'villager_golem/villager_golem2.png'), 128, 128, gen_kaveh())
        with open(os.path.join(md, 'villager_golem/iron_golem.properties'), 'w') as f:
            f.write('skins.1=1\nskins.2=2\nname.2=iregex:.*Kaveh.*\n')
        with open(os.path.join(md, 'villager_golem/villager_golem.properties'), 'w') as f:
            f.write('skins.1=1\nskins.2=2\nname.2=iregex:.*Kaveh.*\n')

    # 5. Base Vanilla Item Textures
    items_dir = os.path.join(base_dir, 'assets/minecraft/textures/items')
    save_png(os.path.join(items_dir, 'diamond_sword.png'), 16, 16, gen_persian_scimitar(70, 215, 235))
    save_png(os.path.join(items_dir, 'iron_sword.png'), 16, 16, gen_persian_scimitar(220, 225, 230))
    save_png(os.path.join(items_dir, 'golden_sword.png'), 16, 16, gen_persian_scimitar(255, 215, 35))
    save_png(os.path.join(items_dir, 'stone_sword.png'), 16, 16, gen_persian_scimitar(190, 130, 60))
    save_png(os.path.join(items_dir, 'wooden_sword.png'), 16, 16, gen_persian_scimitar(140, 90, 45, gold_hilt=False))

    save_png(os.path.join(items_dir, 'bow_standby.png'), 16, 16, gen_persian_bow())
    save_png(os.path.join(items_dir, 'bow_pulling_0.png'), 16, 16, gen_persian_bow())
    save_png(os.path.join(items_dir, 'bow_pulling_1.png'), 16, 16, gen_persian_bow())
    save_png(os.path.join(items_dir, 'bow_pulling_2.png'), 16, 16, gen_persian_bow())
    save_png(os.path.join(items_dir, 'arrow.png'), 16, 16, gen_persian_arrow())

    save_png(os.path.join(items_dir, 'bread.png'), 16, 16, gen_sangak_bread())
    save_png(os.path.join(items_dir, 'cooked_beef.png'), 16, 16, gen_kabab_koobideh())
    save_png(os.path.join(items_dir, 'mushroom_stew.png'), 16, 16, gen_ghormeh_sabzi())
    save_png(os.path.join(items_dir, 'beetroot_soup.png'), 16, 16, gen_ghormeh_sabzi())
    save_png(os.path.join(items_dir, 'rabbit_stew.png'), 16, 16, gen_ghormeh_sabzi())

    save_png(os.path.join(items_dir, 'gold_nugget.png'), 16, 16, gen_sekkeh_derik())
    save_png(os.path.join(items_dir, 'gold_ingot.png'), 16, 16, gen_sekkeh_derik())
    save_png(os.path.join(items_dir, 'golden_apple.png'), 16, 16, gen_crystal_heart(red=True))
    save_png(os.path.join(items_dir, 'apple_golden.png'), 16, 16, gen_crystal_heart(red=True))
    save_png(os.path.join(items_dir, 'prismarine_crystals.png'), 16, 16, gen_crystal_heart(red=False))
    save_png(os.path.join(items_dir, 'potion_bottle_drinkable.png'), 16, 16, gen_mashk_canteen())
    save_png(os.path.join(items_dir, 'clock.png'), 16, 16, gen_jam_e_jam())

    # 6. Base Vanilla Blocks
    blocks_dir = os.path.join(base_dir, 'assets/minecraft/textures/blocks')
    save_png(os.path.join(blocks_dir, 'wool_colored_red.png'), 16, 16, gen_persian_carpet())
    save_png(os.path.join(blocks_dir, 'wool_colored_cyan.png'), 16, 16, gen_persian_tile())
    save_png(os.path.join(blocks_dir, 'lapis_block.png'), 16, 16, gen_persian_tile())

    # 7. GUI Textures
    save_png(os.path.join(base_dir, 'assets/minecraft/textures/gui/widgets.png'), 256, 256, gen_widgets())

    # 8. Custom Iranian Items & OptiFine CIT
    items = {
        'mashk_ab': (gen_mashk_canteen(), 'potion', '.*Mashk.*'),
        'canteen': (gen_mashk_canteen(), 'potion', '.*Ghomghame.*|.*Canteen.*'),
        'sword_kourosh': (gen_persian_scimitar(70, 215, 235), 'diamond_sword', '.*Kourosh.*'),
        'sword_alamut': (gen_persian_scimitar(190, 40, 60), 'diamond_sword', '.*Alamut.*|.*Hassan.*'),
        'sword_babak': (gen_persian_scimitar(220, 50, 40), 'iron_sword', '.*Babak.*'),
        'sword_zulfiqar': (gen_persian_scimitar(255, 215, 40), 'diamond_sword', '.*Zolfaghar.*|.*Zulfiqar.*'),
        'ghormeh_sabzi': (gen_ghormeh_sabzi(), 'bread', '.*Ghormeh.*'),
        'dizi_sangak': (gen_dizi_sangak(), 'bread', '.*Dizi.*'),
        'chai_lahijan': (gen_chai_lahijan(), 'potion', '.*Chai.*'),
        'derik_tala': (gen_sekkeh_derik(), 'gold_nugget', '.*Derik.*|.*Sekke.*'),
        'ghalb_sorkh': (gen_crystal_heart(red=True), 'golden_apple', '.*Ghalb.*Sorkh.*'),
        'ghalb_firoozeh': (gen_crystal_heart(red=False), 'prismarine_crystals', '.*Ghalb.*Firoozeh.*'),
        'eksir_javidan': (gen_eksir_javidan(), 'potion', '.*Eksir.*|.*Hayat.*'),
        'tigh_div_kosh': (gen_persian_scimitar(255, 50, 60), 'diamond_sword', '.*Div-Kosh.*'),
        'bomb_naft': (gen_bomb_naft(), 'magma_cream', '.*Bomb.*Naft.*'),
        'separ_kaviani': (gen_separ_kaviani(), 'shield', '.*Kaviani.*'),
        'jam_e_jam': (gen_jam_e_jam(), 'clock', '.*Jam-e Jam.*|.*Telesm.*')
    }

    iranian_item_dir = os.path.join(base_dir, 'assets/minecraft/textures/items/iranian')
    models_iranian_dir = os.path.join(base_dir, 'assets/minecraft/models/item/iranian')
    os.makedirs(iranian_item_dir, exist_ok=True)
    os.makedirs(models_iranian_dir, exist_ok=True)

    for name, (matrix, vanilla_item, regex) in items.items():
        save_png(os.path.join(iranian_item_dir, f'{name}.png'), 16, 16, matrix)
        save_png(os.path.join(base_dir, f'assets/minecraft/textures/item/iranian/{name}.png'), 16, 16, matrix)

        for cit_path in ['optifine/cit/iranian', 'mcpatcher/cit/iranian']:
            full_cit = os.path.join(base_dir, 'assets/minecraft', cit_path)
            save_png(os.path.join(full_cit, f'{name}.png'), 16, 16, matrix)
            with open(os.path.join(full_cit, f'{name}.properties'), 'w') as f:
                f.write(f"type=item\nmatchItems={vanilla_item}\nitems={vanilla_item}\ntexture={name}.png\nnbt.display.Name=iregex:{regex}\n")

        with open(os.path.join(models_iranian_dir, f'{name}.json'), 'w') as f:
            f.write(f'{{\n  "parent": "item/generated",\n  "textures": {{\n    "layer0": "items/iranian/{name}"\n  }}\n}}\n')

    # 9. Vanilla item model overrides
    models_dir = os.path.join(base_dir, 'assets/minecraft/models/item')
    os.makedirs(models_dir, exist_ok=True)
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

    # 10. Sounds config
    src_sounds = '/home/user/plugin-1.12.2-minecraft-iranian/src/main/resources/sounds.json'
    if os.path.exists(src_sounds):
        shutil.copy(src_sounds, os.path.join(base_dir, 'assets/minecraft/sounds.json'))

    # 11. Zip into IranianHardcore-ResourcePack.zip
    zip_path = '/home/user/plugin-1.12.2-minecraft-iranian/IranianHardcore-ResourcePack.zip'
    if os.path.exists(zip_path):
        os.remove(zip_path)
    with zipfile.ZipFile(zip_path, 'w', zipfile.ZIP_DEFLATED) as z:
        for root, dirs, files in os.walk(base_dir):
            for file in files:
                full = os.path.join(root, file)
                rel = os.path.relpath(full, base_dir)
                z.write(full, rel)

    os.makedirs('/home/user/plugin-1.12.2-minecraft-iranian/resourcepack', exist_ok=True)
    shutil.copy(zip_path, '/home/user/plugin-1.12.2-minecraft-iranian/resourcepack/IranianHardcore-ResourcePack.zip')
    shutil.rmtree(base_dir)

    print(f'[SUCCESS] Comprehensive Iranian Resource Pack built: {zip_path}')
    print(f'Pack size: {os.path.getsize(zip_path)} bytes')

if __name__ == '__main__':
    main()
