<template>
  <div id="sign-in-main">
    <div id="sign-in-container">
      <h1>Sign into Runterra's Food Emporium</h1>
      <p>Enter your credentials</p>
      <form @submit="signin" id="sign-in-form">
        <label for="form-name-input">Username:</label>
        <b class="error">{{errors.signin}}</b>
        <b class="error">{{errors.username}}</b>

        <input id="form-name-input" v-model="username" type="text" placeholder="Enter your username...">
        <label for="form-pass-input">Password:</label>
        <b class="error">{{errors.password}}</b>

        <input id="form-pass-input" v-model="password" :type="type" id="password" placeholder="Enter your password" autocomplete="off">
        <button type="button" @click="showPassword()">{{btnText}}</button>
        <input class="button-primary" type="submit" value="Sign In">

      </form>

    </div>

  </div>
</template>

<script>
const passwordField = document.querySelector('#password')
module.exports = {
  data() {
    return{
    username: '',
        password: '',
      type:'password',
      btnText: 'Show',
      errors: {
        username: '',
        password: '',
        signin: '',
      },
      }
    },
  methods:{
    showPassword: function (){
      if(this.type === 'password'){
        this.type='text';
        this.btnText='Hide';
      }else{
        this.type='password';
        this.btnText='Show';
      }
    },
    validateInputs: function (){
      this.errors.signin ='';
      let usernameValid = !!this.username;
      let passwordValid = !!this.password;
      this.errors.username = usernameValid ? '' : 'Username required';
      this.errors.password = passwordValid ? '' : 'Password required';
      return usernameValid && passwordValid;
    },
    signin: function (e){
      e.preventDefault();

      if(!this.validateInputs()) {
        return;
      }
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
  height: 100%;
  background: #dbff69;
}
#sign-in-container{
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 20px;
  border: solid 1px #eee ;
  background: #ff6961;
  border-radius: 10px;
  text-align: center;

}

#sign-in-form{
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-bottom: 0;
  min-height: 25vh;
}
</style>