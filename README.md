# Archi - Advanced Furniture Design & Visualization Software

## 🏆 Project Overview

Archi is a sophisticated desktop application developed for a furniture design company, enabling designers to create accurate visualizations of furniture configurations in customers' homes. This interactive tool bridges the gap between imagination and reality, allowing customers to make informed purchasing decisions based on realistic representations of how furniture will look in their spaces.

**Demo Video:** [Watch on YouTube](https://youtu.be/7zfn2dlQy88)



## 🌟 Key Features

- **User Authentication System** - Secure login and registration via Firebase Authentication
- **Room Specification Module** - Input and validate room dimensions, shapes, and color schemes
- **2D Design Editor** - Intuitive interface for arranging furniture items in a 2D view
- **3D Visualization Engine** - High-performance rendering for realistic furniture visualization
- **Real-time Design Customization** - Dynamic adjustment of colors, scaling, and positioning
- **Design Management System** - Save, retrieve, edit, and delete designs with metadata
- **Responsive UI/UX** - Smooth transitions between 2D and 3D views

## 🔍 Technical Implementation

### Technology Stack

- **Frontend Framework:** Java Swing
- **3D Rendering:** OpenGL
- **Authentication:** Firebase Authentication
- **Storage:** [Database Technology]
- **Design Patterns:** MVC Architecture, Singleton Pattern, Observer Pattern

### System Architecture

```
┌─────────────────────────────────────┐
│           Presentation Layer         │
│  ┌─────────────┐    ┌─────────────┐  │
│  │ 2D Editor   │    │ 3D Viewer   │  │
│  └─────────────┘    └─────────────┘  │
├─────────────────────────────────────┤
│           Application Layer          │
│  ┌─────────────┐    ┌─────────────┐  │
│  │ Room Config │    │ Furniture   │  │
│  │ Manager     │    │ Manager     │  │
│  └─────────────┘    └─────────────┘  │
│  ┌─────────────┐    ┌─────────────┐  │
│  │ Design      │    │ Rendering   │  │
│  │ Manager     │    │ Engine      │  │
│  └─────────────┘    └─────────────┘  │
├─────────────────────────────────────┤
│              Data Layer              │
│  ┌─────────────┐    ┌─────────────┐  │
│  │ User Data   │    │ Design      │  │
│  │ Repository  │    │ Repository  │  │
│  └─────────────┘    └─────────────┘  │
│  ┌─────────────────────────────────┐ │
│  │       Firebase Integration      │ │
│  └─────────────────────────────────┘ │
└─────────────────────────────────────┘
```

## 📊 User-Centered Design Process

### Research & Requirements

Our development approach began with comprehensive user research:
- 5 structured interviews with company designers and management
- 4 contextual inquiry sessions in furniture showrooms
- Competitive analysis of 6 similar applications
- 2 focus group sessions with 6-8 designers
- Online survey with 11 respondents

These research methods identified key user needs:
- 40% of consultation time spent helping customers visualize furniture
- 78% of clients prioritize color matching between furniture and existing rooms
- Scale and proportion understanding is the biggest challenge for customers

### Personas & User Stories

**Primary Person: Chamindu Lasanga**
- 32-year-old professional interior designer with 8 years of experience
- Moderate to high technical proficiency
- Goals: Create realistic visualizations efficiently, improve customer satisfaction

**Secondary Person: Mark Anthony**
- 45-year-old senior sales associate with 15 years in furniture retail
- Basic to moderate technical proficiency
- Goals: Close more sales, provide accurate visualizations to reduce returns

### Design Evolution

Our design process followed a structured progression:
1. **Storyboarding** - Visual narratives capturing user workflows
2. **Low-fidelity Prototyping** - Paper sketches and wireframes
3. **High-fidelity Prototyping** - Interactive mock-ups with visual design elements
4. **Usability Testing** - Iterative testing throughout development

## 🔬 Evaluation & Results

Our comprehensive evaluation strategy included:
- **Expert Evaluation** - 3 UX experts conducted heuristic evaluations
- **Usability Testing** - 12 participants from target user group completed 7 core tasks
- **Performance Testing** - Technical metrics for rendering and response times

Key findings demonstrated:
- High task completion rates across all core functionality
- Strong user satisfaction scores on System Usability Scale (SUS)
- Identified specific areas for improvement in future iterations

## 🚀 Installation & Setup

```bash
# Clone the repository
git clone https://github.com/PraveenAththanayake/archi.git

# Navigate to project directory
cd archi

# Install dependencies
# Ensure you have Java JDK installed

# Run the application
java Loading
```

## 🔮 Future Enhancements

- **Augmented Reality Integration** - Examine designs in customers' actual spaces
- **Inventory Management System Connection** - Real-time product availability
- **Advanced Lighting Simulation** - Time-of-day lighting effects
- **Collaborative Design** - Multi-user design sessions
- **Mobile Companion App** - View designs on the go

## 👥 Team Members

- **W M L R Wijekoon** (10898707)
- **A M P A Aththanayake** (10898417)
- **H G C Lasanga** (10898545)
- **Anthonidura Z Gunathilake** (10899543)
- **Weragoda Weragoda** (10898700)


## 🙏 Acknowledgments

- Dr. Taimur Bakhshi for supervision and guidance
- Plymouth University for academic support

---

© 2025 Archi Team | BSc. (Hons.) in Software Engineering | Plymouth University
