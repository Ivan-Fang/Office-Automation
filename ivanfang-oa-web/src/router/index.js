import Vue from 'vue';
import VueRouter from 'vue-router';
Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'OA審批',
    component: () =>
      import('../views/index.vue'),
  },
  {
    path: '/apply/:processTemplateId',
    name: 'Apply',
    component: () =>
      import('../views/apply.vue'),
  },
  {
    path: '/list/:activeIndex',
    name: '審批列表',
    component: () =>
      import('../views/list.vue'),
  },
  {
    path: '/show/:id/:taskId',
    name: '審批詳情',
    component: () =>
      import('../views/show.vue'),
  },
  {
    path: '/user',
    name: '基本資訊',
    component: () =>
      import('../views/user.vue'),
  },
  // {
  //   path: '/about',
  //   name: '關於我們',
  //   component: () =>
  //     import('../views/about.vue'),
  // },
  {
    path: '/test',
    name: '測試',
    component: () =>
      import('../views/test.vue'),
  }
];

const router = new VueRouter({
  routes,
});

export default router;
