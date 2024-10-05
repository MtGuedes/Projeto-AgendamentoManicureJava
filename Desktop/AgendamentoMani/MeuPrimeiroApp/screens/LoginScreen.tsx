import React from 'react';
import { Text, TextInput, Button, StyleSheet } from 'react-native'; // Removido 'View'
import LinearGradient from 'react-native-linear-gradient';  // Importar o gradiente

// 6018DC
// EEAD92

const LoginScreen = () => {
  return (
    <LinearGradient
      colors={['#EEAD92', '#6018DC']}  // Definir o gradiente
      start={{ x: 0.1, y: 0 }}  // Início do gradiente
      end={{ x: 1, y: 1 }}  // Fim do gradiente
      style={styles.container}
    >
      <Text style={styles.title}>Login</Text>
      <TextInput style={styles.input} placeholder="Username" />
      <TextInput style={styles.input} placeholder="Password" secureTextEntry />
      <Button title="Login" onPress={() => {}} />
    </LinearGradient>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    padding: 16,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 16,
    color: '#000000',  // Cor do texto para contrastar com o fundo
  },
  input: {
    height: 40,
    borderColor: '#000000',
    borderWidth: 1,
    marginBottom: 12,
    paddingLeft: 8,
    backgroundColor: '#000000',  // Manter os inputs com fundo branco
  },
});

export default LoginScreen;
