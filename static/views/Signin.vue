<template>
  <div id="sign-in-main">
    <div id="sign-in-container">
      <h1>Sign into Runterra's Food Emporium</h1>
      <p>Enter your credentials</p>
      <form @submit="signin" id="sign-in-form">
        <label for="form-name-input">Username:</label>
        <input id="form-name-input" v-model="username" type="text" placeholder="Enter your username...">
        <label for="form-pass-input">Password:</label>
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
      btnText: 'Show'
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