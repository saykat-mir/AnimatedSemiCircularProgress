# Animated Semi-Circular Progress

A beautiful, animated semi-circular progress indicator built with Jetpack Compose for Android
applications.

## ✨ Features

- **Smooth Animations**: Elegant progress animations with customizable duration
- **Dynamic Labels**: Context-aware labels that highlight based on current progress
- **Gradient Background**: Animated gradient background with mesmerizing effects
- **Interactive Controls**: Clickable buttons to test different progress values
- **Customizable Design**: Easy to modify colors, sizes, and styling
- **Performance Optimized**: Efficient rendering with Compose Canvas

## 🎯 Progress Ranges

The component includes 5 predefined progress ranges with corresponding labels:

| Range | Label | Color |
|-------|-------|-------|
| 0-20% | OFF TRACK | Light Gray |
| 21-40% | IMPROVING | Light Gray |
| 41-60% | GOOD | Light Gray |
| 61-80% | GREAT | Light Gray |
| 81-100% | SUPERB | Black (Active) |

## 🚀 Usage

### Basic Implementation

```kotlin
@Composable
fun MyScreen() {
    AnimatedSemiCircleProgress(progress = 75)
}
```

### With Custom Progress

```kotlin
@Composable
fun MyScreen() {
    var currentProgress by remember { mutableStateOf(0) }
    
    AnimatedSemiCircleProgress(progress = currentProgress)
    
    // Update progress programmatically
    LaunchedEffect(Unit) {
        currentProgress = 85
    }
}
```

## 🎨 Customization

### Colors

- **Progress Arc**: `Color(0xFF5F71FD)` - Blue progress indicator
- **Background Arc**: `Color(0xFFF3F4F6)` - Light gray background
- **End Indicator**: `Color(0xFF33CC66)` - Green end marker
- **Gradient Background**: Multi-color animated gradient

### Dimensions

- **Circle Size**: 350dp outer container
- **Stroke Width**: 28dp arc thickness
- **Center Content**: 280dp inner circle

### Animation

- **Progress Animation**: 3-second smooth transition
- **Background Animation**: 25-second infinite gradient movement

## 🏗️ Component Structure

```
AnimatedSemiCircleProgress
├── Animation Setup
│   ├── Infinite Transition (Gradient)
│   └── Progress Animation
├── State Management
│   ├── Progress Bounds Checking
│   └── Arc Angle Conversion
├── UI Components
│   ├── ProgressCircle
│   │   ├── Canvas (Arc & Labels)
│   │   └── CenterContent
│   └── ProgressControlButtons
└── Helper Functions
    ├── drawProgressLabels()
    ├── drawEndIndicator()
    └── ProgressButton()
```

## 📱 Screenshots

The component features:

- Semi-circular progress arc (280° sweep)
- Rotating labels around the perimeter
- Animated gradient background in the center
- Progress percentage display
- Test buttons for different progress values

## 🛠️ Technical Details

### Dependencies

- Jetpack Compose
- Compose Animation
- Compose Canvas
- Material3

### Key Technologies

- **Canvas API**: Custom drawing for arcs and labels
- **Compose Animation**: Smooth state transitions
- **Gradient Brushes**: Dynamic background effects
- **State Management**: Reactive progress updates

## 🔧 Installation

1. Add the `AnimatedSemiCircleProgress.kt` file to your project
2. Ensure you have the required Compose dependencies
3. Use the component in your Compose UI

```kotlin
implementation "androidx.compose.ui:ui:$compose_version"
implementation "androidx.compose.ui:ui-graphics:$compose_version"
implementation "androidx.compose.animation:animation:$compose_version"
implementation "androidx.compose.material3:material3:$material3_version"
```

## 🎯 Use Cases

- Fitness apps (workout progress)
- Learning platforms (course completion)
- Project management (task completion)
- Gaming (level progress)
- Any application requiring progress visualization

## 🔄 Performance

- Optimized Canvas rendering
- Efficient state management with `remember`
- Smooth 60fps animations
- Minimal recomposition with proper state handling

## 📈 Future Enhancements

- [ ] Customizable progress ranges
- [ ] Different arc styles (rounded, square, etc.)
- [ ] Sound effects on progress milestones
- [ ] Accessibility improvements
- [ ] RTL support
- [ ] Different animation curves

## 🤝 Contributing

Feel free to contribute to this project by:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

**Built with ❤️ using Jetpack Compose**