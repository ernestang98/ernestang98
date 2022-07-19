import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'

const router = new VueRouter({
  base: '/',
  mode: 'history',
  routes: [
    { path: '/', name: "landing", component: Landing },
    { path: '/search', name: "dashboard", component: Dashboard }
  ]
})

Vue.use(VueRouter)

Vue.config.productionTip = false

import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
Vue.use(BootstrapVue)

import VueMaterial from 'vue-material'
import 'vue-material/dist/vue-material.min.css'
import 'vue-material/dist/theme/default.css'
import Dashboard from "./components/Dashboard";
import Landing from "./components/Landing";

Vue.use(VueMaterial)

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
