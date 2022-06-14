<template>
  <div id="app">
    <Header org="NASDAQ" msg="Financial Dashboard"/>
    <router-view></router-view>
  </div>
</template>

<script>
import Header from './components/Header'

export default {
  name: 'App',
  components: {
    Header
  }
}
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  padding-top: 60px;
}
@import "~vue-material/dist/theme/engine"; // Import the theme engine

@include md-register-theme("default", (
    primary: md-get-palette-color(blue, A200), // The primary color of your application
    accent: md-get-palette-color(red, A200) // The accent or secondary color
));

@import "~vue-material/dist/theme/all"; // Apply the theme
:root {
  --md-theme-default-background-variant: #ffffff;
}
</style>
