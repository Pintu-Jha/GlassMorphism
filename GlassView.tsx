// GlassView.tsx
import { requireNativeComponent, Platform, View } from 'react-native';

// This will only run once per JS context (helps prevent re-registration)
const GlassView = Platform.OS === 'android' ? requireNativeComponent('GlassView') : View;

export default GlassView;