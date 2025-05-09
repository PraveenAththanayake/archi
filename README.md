# Archi - Furniture Design Software

> Visualize. Design. Decide.

**Archi** is an interactive furniture design desktop application developed as part of the PUSL3122 module (HCI, Computer Graphics, and Visualisation) at the University of Plymouth. It empowers interior designers to create precise 2D layouts and immersive 3D visualizations for customized room planning, bridging the gap between customer imagination and realistic expectations.

---

## 🎥 Demonstration

📺 [Watch our YouTube Demo](https://youtu.be/7zfn2dlQy88)

🔗 [Project Repository](https://github.com/PraveenAththanayake/archi)

---

## 📌 Table of Contents

- [Background](#background)
- [Features](#features)
- [Target Users](#target-users)
- [System Architecture](#system-architecture)
- [UI/UX Design](#uiux-design)
- [Installation](#installation)
- [Usage](#usage)
- [Evaluation](#evaluation)
- [Team](#team)
- [License](#license)

---

## 📖 Background

Customers often struggle to visualize how furniture would look in their real home environments. Traditional catalogs and in-store displays fail to accommodate unique room dimensions, lighting, and color schemes, which can lead to customer indecision, returns, and lost sales.

**Archi** solves this by providing:

- A collaborative tool for designers and customers
- Real-time 2D and 3D room modeling
- High customization and flexibility
- A guided, user-friendly interface for furniture layout planning

---

## 🚀 Features

### 🔐 Authentication System

- Secure login and registration powered by Firebase

### 🏠 Room Specification Module

- Input room size, shape, and color scheme
- Supports real-world dimensional validation

### 🧩 2D Design Editor

- Drag and drop furniture components
- Modify placement, scale, and colors

### 🌀 3D Visualization Engine

- Real-time 3D rendering from 2D plans using OpenGL
- Shading, lighting, and perspective effects for realism

### 🎨 Design Customization

- Color schemes and shading control
- Save/load/edit/delete multiple designs

### 🗂 Design Management

- Create profiles and persist designs across sessions

---

## 👥 Target Users

### 👤 Primary Persona: Chamindu Lasanga

- Interior designer with 8 years' experience
- Goal: Reduce time spent iterating designs with clients

### 👤 Secondary Persona: Mark Anthony

- Sales associate in furniture retail
- Goal: Provide visual aids to improve sales and reduce returns

---

## 🧱 System Architecture

- **Frontend:** Java Swing for GUI
- **Rendering:** OpenGL (2D + 3D visualization)
- **Backend:** Firebase for Authentication and Storage
- **Tools Used:** Java, GL Libraries, Firebase SDK

---

## 🎨 UI/UX Design

Design evolved through usability-focused iterations, ensuring:

- ✅ Learnability
- ✅ Accessibility (Keyboard navigation, contrast, screen reader)
- ✅ Error prevention (validations, undo/redo)
- ✅ Task efficiency (core tasks within 2 clicks)

### 💡 Notable Screens:

- Splash screen
- Sign-up/sign-in
- Room layout page
- 2D editor
- 3D visualizer
- Final render preview
- Contact/About pages

---

## 🛠️ Installation

> Prerequisites: Java JDK 20+, OpenGL bindings, Firebase config

```bash
git clone https://github.com/PraveenAththanayake/archi.git
cd archi
# Open in IDE and run Loading.java
```
