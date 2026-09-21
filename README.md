# 🚀 LexiCore: Mobile Text Processing Engine

---
![Java](https://img.shields.io/badge/Java-11%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Data Structures](https://img.shields.io/badge/Data_Structures-DSA-00599C?style=for-the-badge&logo=cplusplus&logoColor=white)
![OOP](https://img.shields.io/badge/Architecture-OOP_Principles-3776AB?style=for-the-badge&logo=java&logoColor=white)
![Console Engine](https://img.shields.io/badge/Platform-Console_Engine-4D4D4D?style=for-the-badge&logo=windows-terminal&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)

###

> A Java console-based application that simulates the background SDK of a smart mobile keyboard, providing on-device text processing features without cloud dependency.

---

## 📌 Overview

LexiCore is an academic project developed for the **Data Structures & Algorithms (ITMB 2307)** course. It simulates how mobile applications (like smart keyboards and chat analytics tools) process text locally under strict hardware constraints (limited RAM, CPU, and battery).

The engine loads text (via direct input or local file), processes it, and provides a set of analytical and intelligent features through an interactive console menu.

---

## ✨ Features

### 🔹 Core Features
- **Text Pre-processing:** Lowercase conversion, punctuation removal, whitespace compression.
- **Analytics Dashboard:** Total word count, unique vocabulary, character frequency map.
- **Positional Search:** Finds exact word positions (Sentence Index + Word Position).
- **Atomic Word Replacement:** Replaces all occurrences with mutation count and execution time (ms).
- **Undo / Redo:** State management using Stack (LIFO).

### 🔹 Intelligent Features (4 Selected)
| Feature | Data Structure Used |
| :--- | :--- |
| Undo/Redo Buffer | `Stack<String>` |
| On-Device Similarity (Jaccard) | `HashSet<String>` |
| Instant Sentiment Analyzer | `HashSet<String>` |
| Mobile Keyword Extraction | `HashSet` + `HashMap<String, Integer>` |

---

## 🧠 Data Structures & Big-O Analysis

| Data Structure | Usage | Time Complexity |
| :--- | :--- | :--- |
| `ArrayList` | Storing words & sentences | O(n) for search/replacement |
| `HashSet` | Unique words, similarity, sentiment | O(1) average lookup |
| `HashMap` | Word/character frequency | O(1) average put/get |
| `Stack` | Undo/Redo | O(1) push/pop |

### ⏱️ Algorithmic Complexity

| Feature | Time Complexity | Space Complexity |
| :--- | :--- | :--- |
| Text Cleaning | O(n) | O(n) |
| Word Replacement | O(n) | O(1) |
| Undo/Redo | O(1) | O(h) |
| Positional Search | O(n × m) | O(k) |
| Jaccard Similarity | O(n + m) | O(n + m) |
| Sentiment Analysis | O(n) | O(1) |
| Keyword Extraction | O(n log n) | O(k) |

---

## 🏗️ System Architecture

The project follows **OOP principles**:
- **Encapsulation:** All data members are `private`, accessed via `public` methods.
- **Modularity:** Logic (`TextEngine`) is separated from UI (`Main`).
```
┌──────────────┐ ┌─────────────────┐
│ Main │───────▶│ TextEngine │
│ (UI Layer) │ Has-A │ (Logic Layer) │
└──────────────┘ └─────────────────┘
```
---

## 🛠️ Technologies Used

- **Language:** Java (JDK 11+)
- **Libraries:** Java Collections Framework (`ArrayList`, `HashSet`, `HashMap`, `Stack`)
- **Concepts:** OOP, Recursion, Big-O Analysis, Exception Handling

---

## 🚀 How to Run

1. Clone the repository:
```
git clone https://github.com/YOUR_USERNAME/LexiCore.git
```

2.Navigate to the project folder:
```
cd LexiCore
```

3. Compile the Java files:
```
javac *.java
```

4. Run the main class:
```
java Main
📂 Project Structure
text
LexiCore/
├── TextEngine.java    # Core logic: data structures, text processing
├── Main.java          # UI: menu, user input, feature invocation
└── README.md
```
---
## 👩‍💻 My Role &amp; Contributions

This was a team project for the Data Structures &amp; Algorithms course.

**My Key Contributions:**

* Designed and implemented the core `TextEngine` class.
* Integrated core data structures (`ArrayList`, `HashSet`, `HashMap`, `Stack`).
* Implemented **Undo/Redo** functionality using Stack (LIFO).
* Developed **Jaccard Similarity** matching using `HashSet`.
* Implemented **Sentiment Analysis** and **Keyword Extraction** modules.
* Conducted full **Big-O complexity analysis** for all feature algorithms.

---

## 📚 What I Learned 
- How to choose the right data structure based on time and space complexity.
- How to apply OOP principles (Encapsulation, Modularity) in real-world software architecture.
- How to handle edge cases and exceptions gracefully.
- How to optimize algorithms for resource-constrained environments (mobile platforms).
 
---

## 📄 License
This project is developed for academic purposes as part of the Software Development curriculum at UCAS.

---
## 🔗 Connect with Me
- 🐙 **GitHub:** [@MalakElyan](https://github.com/MalakElyan) 
- 💼 **LinkedIn:** [Malak Elyan](https://www.linkedin.com/in/malak-elyan) 

---
⭐ If you found this project useful, feel free to star it!
