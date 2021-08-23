<template>
  <div id="sign-in-main">
    <div id="sign-in-container">
      <h1>Sign into Runterra's Food Emporium</h1>
      <p>Enter your credentials</p>
      <div class="d-flex justify-content-between">
      <form class="row g-3 needs-validation" @submit="signin" id="sign-in-form" novalidate>
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


        <input class="btn btn-primary" type="submit" value="Sign In">

      </form>
      </div>
    </div>

  </div>
</template>

<script>

  
module.exports = {
  data() {
    return{
    username: '',
        password: '',
      type:'password',
      btnText: 'Show',
      icon_class: 'fa fa-eye-slash'

      }
    },
  methods:{
    showPassword: function (){
      if(this.type === 'password'){
        this.type='text';
        this.btnText='Hide';
        this.icon_class='fa fa-eye'
      }else{
        this.type='password';
        this.btnText='Show';
        this.icon_class='fa fa-eye-slash'

      }
    },

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
<style scoped>
#sign-in-main{
  display: grid;
  place-items: center;
  height: 100vh;
  background: #4b0502;
}
#sign-in-container{
  display: grid;
  place-items: center;
  padding: 20px;
  border: solid 1px #eee;
  border-radius: 10px;
}
.spacer{
  height: 20px;
}




</style>