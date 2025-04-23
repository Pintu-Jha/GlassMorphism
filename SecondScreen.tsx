import React from 'react';
import {StyleSheet, Text, View} from 'react-native';

import ScreenWrapper from './ScreenWrapper';
const SecondScreen = () => {
  return (
    <ScreenWrapper>
      <View style={styles.content}>
        <Text style={styles.text}>SecondScreen</Text>
      </View>
    </ScreenWrapper>
  );
};

const styles = StyleSheet.create({
  content: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  text: {
    fontSize: 24,
    color: 'white',
    marginBottom: 20,
  },
});

export default SecondScreen;
