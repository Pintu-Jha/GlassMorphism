import React from 'react';
import { Text, View, Button, StyleSheet } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { NativeStackNavigationProp } from '@react-navigation/native-stack';

type RootStackParamList = {
  Home: undefined;
  Second: undefined;
};

type NavigationProp = NativeStackNavigationProp<RootStackParamList, 'Home'>;

import ScreenWrapper from './ScreenWrapper';
const HomeScreen = () => {
  const navigation = useNavigation<NavigationProp>();

  return (
    <ScreenWrapper>
      <View style={styles.content}>
        <Text style={styles.text}>HomeScreen</Text>
        <Button title="Go to Second Screen" onPress={() => navigation.navigate('Second')} />
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

export default HomeScreen;