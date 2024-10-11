import { AppRegistry } from 'react-native';
import App from './App.tsx'; // Verifique se o arquivo App.tsx realmente está na raiz
import { name as appName } from './app.json';

AppRegistry.registerComponent(appName, () => App);
