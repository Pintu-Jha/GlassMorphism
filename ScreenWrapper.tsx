import React from 'react';
import { StyleSheet, View } from 'react-native';
import GlassView from './GlassView';

type Props = {
  children: React.ReactNode;
};

export default function ScreenWrapper({ children }: Props) {
  return (
    <View style={styles.container}>
      <GlassView style={StyleSheet.absoluteFill} />
      {children}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    position: 'relative',
    backgroundColor: '#030521',
  },
});