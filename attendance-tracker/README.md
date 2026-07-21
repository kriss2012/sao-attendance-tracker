⚔️ SAO Attendance Tracker – 3D Cardinal System Edition

A next-generation Spring Boot Attendance Management System inspired by the futuristic interface design of virtual reality RPGs. The application combines enterprise-grade Spring Boot architecture with an immersive 3D holographic dashboard, animated HUD elements, glowing neon effects, and real-time analytics to create a unique attendance management experience.

---

✨ Features

🌌 3D Cardinal Dashboard

- Fully animated 3D glassmorphism interface
- Floating holographic information panels
- Interactive attendance statistics
- Animated particle background
- Neon blue & golden HUD theme
- Real-time dashboard updates
- Smooth page transitions
- 3D hover animations
- Dynamic lighting effects
- Animated progress rings
- Responsive futuristic UI

---

🏗️ Project Architecture

Layer| Description
"entity/AbstractAuditable.java"| Abstract base entity with audit timestamps
"entity/Student.java"| Student entity
"entity/AttendanceRecord.java"| Attendance entity
"repository/"| Spring Data JPA repositories
"service/"| Business logic interfaces
"service/impl/"| Service implementations
"controller/api/"| REST APIs
"controller/web/"| MVC Controllers
"exception/"| Custom exception handling
"config/"| Demo data and configuration
"templates/"| Thymeleaf UI
"static/css/"| 3D HUD styling
"static/js/"| Dashboard animations
"static/models/"| Optional 3D assets (Three.js)

---

🚀 3D UI Experience

The dashboard includes:

- 3D rotating attendance cards
- Floating holographic windows
- Animated energy borders
- Live attendance graphs
- Circular holographic gauges
- Animated radar scanner
- Particle starfield background
- Interactive glowing buttons
- Floating notification system
- Pulse animations
- Glassmorphism control panels
- Dynamic shadows
- Depth perspective effects
- Neon grid floor
- Animated data streams
- Digital clock
- HUD-style statistics
- Responsive mobile interface

---

🛠 Technology Stack

Backend

- Java 17+
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate
- Maven
- MySQL
- H2 Database

Frontend

- Thymeleaf
- HTML5
- CSS3
- JavaScript (ES6)
- Three.js
- GSAP
- SVG Animations
- CSS Glassmorphism
- CSS 3D Transformations

---

💾 Database

Supports both:

- MySQL (Production)
- H2 (Development)

Simply switch the configuration inside:

src/main/resources/application.properties

---

▶️ Run the Project

Requirements:

- Java 17+
- Maven

mvn spring-boot:run

Visit:

http://localhost:8080

You'll enter the futuristic Link Start Portal, which transitions into the Cardinal Dashboard populated with demo students and attendance records.

---

📊 REST API

Method| Endpoint| Description
GET| "/api/students"| List students
GET| "/api/students/{rollNumber}"| Student details
GET| "/api/attendance/status/{rollNumber}"| Attendance status
GET| "/api/attendance/history/{rollNumber}"| Attendance history
POST| "/api/attendance/mark"| Mark attendance
PUT| "/api/attendance/update"| Update attendance
GET| "/api/attendance/summary"| Dashboard analytics

---

⚡ API Response

{
  "status":404,
  "message":"No student found with roll number 'S999'.",
  "timestamp":"2026-07-21T15:30:12"
}

---

🎮 Future Enhancements

- 3D student avatars
- Classroom visualization
- Face recognition attendance
- QR attendance scanner
- Voice assistant
- AI attendance prediction
- Animated leaderboard
- Faculty analytics dashboard
- Dark/Light HUD modes
- WebSocket live synchronization
- Progressive Web App support
- Docker deployment
- JWT Authentication
- Role-based access control
- Attendance heatmaps
- Export to Excel/PDF
- Email notifications
- Mobile companion application

---

🎨 Design Philosophy

The interface is inspired by the clean, futuristic aesthetic of virtual-reality game menus while remaining entirely original. Every visual element—including holographic panels, glowing HUD components, particle effects, glass surfaces, animated SVG gauges, and 3D transitions—is built using HTML, CSS, SVG, JavaScript, and optional Three.js. No copyrighted artwork, characters, logos, or official assets are included, making the project suitable for portfolios, learning, and deployment.

---

⭐ Highlights

- Enterprise Spring Boot Architecture
- Layered MVC Design
- RESTful APIs
- MySQL & H2 Support
- Original Futuristic HUD Interface
- Interactive 3D Dashboard
- Glassmorphism Design
- Three.js Powered Visual Effects
- Responsive Layout
- Modern Animations
- Clean Code Structure
- Production Ready
- Easy Deployment
