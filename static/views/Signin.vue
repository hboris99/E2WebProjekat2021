<template>
  <div id="sign-in-main">
    <div id="sign-in-container">
      <form @submit="signin" id="sign-in-form">
        <input v-model="username" type="text" placeholder="Username">
        <input v-model="password" type="text" placeholder="Password">
        <input class="button-primary" type="submit" value="Sign In">
      </form>


    </div>

  </div>
</template>

<script>
module.exports = {
  data() {
    return{
    username: '',
        password: ''
      }
    },
  methods:{
    signin: function (e){
      e.preventDefault();
      let data = {
        username: this.username,
        password: this.password,
      };

      axios.post('/user/login', data).then(r => {
        localStorage.jws = r.data;
        this.$router.push('/');
      })
    }
  }
};
</script>