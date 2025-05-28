# Minesweeper Game

![Build Status](https://img.shields.io/github/actions/workflow/status/JLHL20/Java-MineSweeper/main.yml)
![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)

A Java-based Minesweeper clone with a Swing GUI, multiple difficulty modes, custom icons, and right-click flagging.

---

## Table of Contents

1. [Features](#features)
2. [Demo](#demo)
3. [Getting Started](#getting-started)

   * [Prerequisites](#prerequisites)
   * [Installation](#installation)
4. [Usage](#usage)

   * [Running the Game](#running-the-game)
   * [Difficulty Levels](#difficulty-levels)
   * [Images & Assets](#images--assets)
5. [Project Structure](#project-structure)
6. [Contributing](#contributing)
7. [License](#license)

---

## Features

* **Three built-in difficulties**: Easy (10×10, 10 bombs), Medium (16×16, 40 bombs), Hard (30×16, 99 bombs)
* **Optional Super Hard & Impossible modes** (configurable in code or via a custom dropdown)
* **Right-click flagging** with a custom flag icon
* **Customizable tile icons** stored in `/images` (bombs, flags, numbers)
* **Clean MVC-style codebase** split between `Grid.java` (logic) and `MineSweeperGUI.java` (view/controller)

---

## Demo

🎥 [Watch the 8 s Minesweeper demo on Google Drive]([https://drive.google.com/file/d/FILE_ID/view?usp=sharing](https://drive.google.com/file/d/1m1js0fJ8WE323y48bWKhJ0GJ0Mgz_Zim/view?usp=sharing))

> ⚠️ **Images folder warning**
> You **must** clone or download the entire `images/` directory at the root—otherwise these in-README previews and the in-game icons won’t load.

---

## Getting Started

### Prerequisites

* Java 8 or higher
* Eclipse IDE (or any Java-Swing–capable IDE)
* Git (for cloning)

### Installation

```bash
# Clone the repo (including images)
git clone https://github.com/your-username/Minesweeper.git

# Import into Eclipse:
File → Import… → General → Existing Projects into Workspace → Select Minesweeper folder
```

---

## Usage

### Running the Game

```bash
# In Eclipse:
Run → Run As → Java Application → select MineSweeperGUI.java
```

### Difficulty Levels

| Mode       | Board Size | Bombs |
| ---------- | ---------- | ----- |
| Easy       | 10 × 10    | 10    |
| Medium     | 16 × 16    | 40    |
| Hard       | 30 × 16    | 99    |
| Super Hard | 50 × 30    | 300   |
| Impossible | 100 × 50   | 1000  |

> To add or tweak modes, open `MineSweeperGUI.java`, locate the `enum Difficulty { … }` block, and customize rows, columns, and bomb count.

### Images & Assets

* All tile images (numbers, bombs, flags) live in the top-level `images/` folder.
* Make sure `images/` is a sibling to `src/` in your cloned directory; otherwise the GUI code will throw a "file not found."
* You can swap in your own PNGs—just preserve the filenames (`bomb.png`, `flag.png`, `1.png`, …).

---

## Project Structure

```text
Minesweeper/
├── images/              ← all game icons & screenshots
│   ├── bomb.png
│   ├── flag.png
│   ├── 1.png … 8.png
│   └── demo_easy.png
├── src/
│   ├── Grid.java            ← game-logic: board, neighbor counts, flood-fill
│   └── MineSweeperGUI.java  ← Swing UI, menus, event handlers
├── .gitignore
├── README.md
└── LICENSE
```

---

## Contributing

1. Fork this repository
2. Create your feature branch (`git checkout -b feature/AwesomeFeatures`)
3. Commit your changes (`git commit -m 'Add AwesomeFeatures'`)
4. Push to the branch (`git push origin feature/AwesomeFeatures`)
5. Open a Pull Request

---
