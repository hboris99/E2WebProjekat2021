<template>
<div>
  <custom-nav-bar>
  </custom-nav-bar>
  <div class="d-flex justify-content-between">
    <form class="row g-3 needs-validation" @submit="addUser" id="sign-in-form" novalidate>
      <div class="form-floating">
        <input class="form-control" placeholder="Username" id="form-name-input" v-model="username" type="text">
        <label for="form-name-input">Username</label>
        <div class="invalid-feedback">
          Please enter your username.
        </div>
      </div>
      <div class="form-floating input-group mb-3">
        <input class="form-control" id="form-pass-input" v-model="password" :type="type" id="password" placeholder="Enter your password" autocomplete="off">
        <div class="input-group-append">
          <button class="btn btn-primary h-100" type="button" @click="showPassword()"><i :class="icon_class" aria-hidden="true"></i></button>
        </div>
        <label for="form-pass-input">Password</label>
        <div class="invalid-feedback">
          Please enter your password.
        </div>
      </div>
      <p class="text-muted">
        Don't have an account? Register <a href="#" class="text-reset">here.</a>
      </p>

      <input class="btn btn-primary" type="submit" value="Sign In">

    </form>
  </div>
</div>
</template>

<script>
module.exports= {
  data() {
    return{

    }
  },
  methods: {
    addUser: function (){

    }
  }
}
</script>

